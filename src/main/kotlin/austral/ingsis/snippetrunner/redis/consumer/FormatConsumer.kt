package austral.ingsis.snippetrunner.redis.consumer

import austral.ingsis.snippetrunner.controller.RunnerController
import austral.ingsis.snippetrunner.model.dto.FormatDto
import org.austral.ingsis.redis.RedisStreamConsumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class FormatConsumer @Autowired constructor(
    redis: ReactiveRedisTemplate<String, String>,
    @Value("\${redis.stream.request_formater_key}") streamKey: String,
    @Value("\${redis.groups.format}") groupId: String,
    private val runnerController: RunnerController,
) : RedisStreamConsumer<FormaterRequestEvent>(streamKey, groupId, redis){

    init {
        subscription()
    }
    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, FormaterRequestEvent>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
            .targetType(FormaterRequestEvent::class.java) // Set type to de-serialize record
            .build();
    }

    override fun onMessage(record: ObjectRecord<String, FormaterRequestEvent>) {
        runnerController.format(FormatDto(record.value.snippetId.toString(), record.value.snippetContent, record.value.formaterRules))
    }
}

data class FormaterRequestEvent(
    val snippetId: Long,
    val snippetContent: String,
    val formaterRules: Map<String, Any>,
)