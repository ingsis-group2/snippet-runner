package austral.ingsis.snippetrunner.redis.consumer

import austral.ingsis.snippetrunner.redis.producer.FormatResult
import austral.ingsis.snippetrunner.redis.producer.FormatResultProducer
import austral.ingsis.snippetrunner.service.PrintScriptRunner
import kotlinx.coroutines.runBlocking
import org.austral.ingsis.redis.RedisStreamConsumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import java.time.Duration

@Component
@Profile("!test")
class FormatConsumer
    @Autowired
    constructor(
        redis: RedisTemplate<String, String>,
        @Value("\${redis.stream.request_formater_key}") streamKey: String,
        @Value("\${redis.groups.format}") groupId: String,
        private val formatResultProducer: FormatResultProducer,
    ) : RedisStreamConsumer<FormaterRequestEvent>(streamKey, groupId, redis) {
        private val runner: PrintScriptRunner = PrintScriptRunner("1.1")

        init {
            subscription()
        }

        override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, FormaterRequestEvent>> =
            StreamReceiver.StreamReceiverOptions
                .builder()
                .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
                .targetType(FormaterRequestEvent::class.java) // Set type to de-serialize record
                .build()

        override fun onMessage(record: ObjectRecord<String, FormaterRequestEvent>) {
            try {
                val response = runner.format(record.value.snippetContent, record.value.formaterRules)
                if (response.errors.isNotEmpty()) {
                    throw Exception(response.errors.joinToString("\n"))
                }
                runBlocking {
                    formatResultProducer.publishFormatRequest(FormatResult(record.value.snippetId, response.formattedCode))
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

data class FormaterRequestEvent(
    val snippetId: Long,
    val snippetContent: String,
    val formaterRules: Map<String, Any>,
)
