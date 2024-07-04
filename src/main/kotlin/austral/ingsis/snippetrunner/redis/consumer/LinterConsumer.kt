package austral.ingsis.snippetrunner.redis.consumer

import austral.ingsis.snippetrunner.redis.producer.LintRequestProducer
import austral.ingsis.snippetrunner.redis.producer.LintResult
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
class LinterConsumer
    @Autowired
    constructor(
        redis: RedisTemplate<String, String>,
        @Value("\${spring.data.redis.stream.request_linter_key}") streamKey: String,
        @Value("\${spring.data.redis.groups.lint}") groupId: String,
        private val lintResultProducer: LintRequestProducer,
    ) : RedisStreamConsumer<LintRequestEvent>(streamKey, groupId, redis) {
        private val runner: PrintScriptRunner = PrintScriptRunner("1.1")

        init {
            subscription()
        }

        override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LintRequestEvent>> =
            StreamReceiver.StreamReceiverOptions
                .builder()
                .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
                .targetType(LintRequestEvent::class.java) // Set type to de-serialize record
                .build()

        override fun onMessage(record: ObjectRecord<String, LintRequestEvent>) {
            println("Received record: ${record}")
            println("Record value: ${record.value}")
            try {
                val response = runner.analyze(record.value.snippetContent, record.value.lintRules)
                runBlocking {
                    lintResultProducer.publishLintRequest(LintResult(record.value.snippetId, response.reportList, response.errors))
                }
            } catch (e: Exception) {
                runBlocking {
                    lintResultProducer.publishLintRequest(
                        LintResult(record.value.snippetId, listOf(), listOf(e.message ?: "Unknown error")),
                    )
                }
            }
        }
    }

data class LintRequestEvent(
    val snippetId: Long,
    val snippetContent: String,
    val lintRules: Map<String, Any>,
)
