package austral.ingsis.snippetrunner.redis.producer

import kotlinx.coroutines.reactor.awaitSingle
import org.austral.ingsis.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component

@Component
class FormatResultProducer
    @Autowired
    constructor(
        @Value("\${redis.stream.request_format_result_key}") streamKey: String,
        redis: ReactiveRedisTemplate<String, String>,
    ) : RedisStreamProducer(streamKey, redis) {
        suspend fun publishFormatRequest(event: FormatResult) {
            println("publishing on lint stream: $event")
            emit(event).awaitSingle()
        }
    }

data class FormatResult(
    val snippetId: Long,
    val formattedSnippet: String,
)
