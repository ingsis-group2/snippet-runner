package austral.ingsis.snippetrunner.redis.producer

import austral.ingsis.snippetrunner.controller.RunnerController
import austral.ingsis.snippetrunner.model.dto.LintDto
import org.austral.ingsis.redis.RedisStreamConsumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class LinterConsumer @Autowired constructor(
    redis: ReactiveRedisTemplate<String, String>,
    @Value("\${redis.stream.request_linter_key}") streamKey: String,
    @Value("\${redis.groups.lint}") groupId: String,
    private val runnerController: RunnerController,
    controller: RunnerController
) : RedisStreamConsumer<LintRequestEvent>(streamKey, groupId, redis){

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LintRequestEvent>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
            .targetType(LintRequestEvent::class.java) // Set type to de-serialize record
            .build();
    }

    override fun onMessage(record: ObjectRecord<String, LintRequestEvent>) {
        runnerController.analyze(LintDto(record.value.snippetId.toString(), record.value.snippetContent, record.value.lintRules))
    }
}

data class LintRequestEvent(
    val snippetId: Long,
    val version: String,
    val snippetContent: String,
    val lintRules: Map<String, Any>,
)