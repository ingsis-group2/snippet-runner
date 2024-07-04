package austral.ingsis.snippetrunner.redis.consumer

import austral.ingsis.snippetrunner.redis.producer.LintResultProducer
import austral.ingsis.snippetrunner.service.PrintScriptRunner
import com.example.redisevents.LintRequest
import com.example.redisevents.LintResult
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
        @Value("\${stream.request_linter_key}") streamKey: String,
        @Value("\${groups.lint}") groupId: String,
        private val lintResultProducer: LintResultProducer,
    ) : RedisStreamConsumer<LintRequest>(streamKey, groupId, redis) {
        private val runner: PrintScriptRunner = PrintScriptRunner("1.1")

        init {
            subscription()
        }

        override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LintRequest>> =
            StreamReceiver.StreamReceiverOptions
                .builder()
                .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
                .targetType(LintRequest::class.java) // Set type to de-serialize record
                .build()

        override fun onMessage(record: ObjectRecord<String, LintRequest>) {
            println("------------------------------------------------------")
            println("message received on linter stream: ${record.value}")
            println("snippet id: ${record.value.snippetId}")
            println("snippet content: ${record.value.snippetContent}")
            println("lint rules: ${record.value.lintRules}")
            try {
                val response = runner.analyze(record.value.snippetContent, record.value.lintRules)
                runBlocking {
                    lintResultProducer.publishLintRequest(LintResult(record.value.snippetId, response.reportList, response.errors))
                }
                println("------------------------------------------------------")
            } catch (e: Exception) {
                runBlocking {
                    lintResultProducer.publishLintRequest(
                        LintResult(record.value.snippetId, listOf(), listOf(e.message ?: "Unknown error")),
                    )
                    println("------------------------------------------------------")
                }
            }
        }
    }
