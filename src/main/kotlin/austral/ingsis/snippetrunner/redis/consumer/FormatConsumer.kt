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
        @Value("\${spring.data.redis.stream.request_formater_key}") streamKey: String,
        @Value("\${spring.data.redis.groups.format}") groupId: String,
        private val formatResultProducer: FormatResultProducer,
    ) : RedisStreamConsumer<FormaterRequest>(streamKey, groupId, redis) {
        private val runner: PrintScriptRunner = PrintScriptRunner("1.1")

        init {
            subscription()
        }

        override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, FormaterRequest>> =
            StreamReceiver.StreamReceiverOptions
                .builder()
                .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
                .targetType(FormaterRequest::class.java) // Set type to de-serialize record
                .build()

        override fun onMessage(record: ObjectRecord<String, FormaterRequest>) {
            println("------------------------------------------------------")
            println("message received on formater stream: ${record.value}")
            println("snippet id: ${record.value.snippetId}")
            println("writer id: ${record.value.writerId}")
            println("snippet content: ${record.value.snippetContent}")
            println("formater rules: ${record.value.formaterRules}")
            try {
                val response = runner.format(record.value.snippetContent, record.value.formaterRules)
                if (response.errors.isNotEmpty()) {
                    throw Exception(response.errors.joinToString("\n"))
                }
                runBlocking {
                    formatResultProducer.publishFormatRequest(
                        FormatResult(record.value.snippetId, record.value.writerId, response.formattedCode),
                    )
                    println("------------------------------------------------------")
                }
            } catch (e: Exception) {
                println(e.message)
                println("------------------------------------------------------")
            }
        }
    }

data class FormaterRequest(
    val snippetId: Long,
    val writerId: String,
    val snippetContent: String,
    val formaterRules: Map<String, Any>,
)
