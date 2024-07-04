package austral.ingsis.snippetrunner.redis.producer

import org.austral.ingsis.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class FormatResultProducer
    @Autowired
    constructor(
        @Value("\${stream.request_format_result_key}") streamKey: String,
        redis: RedisTemplate<String, String>,
    ) : RedisStreamProducer(streamKey, redis) {
        suspend fun publishFormatRequest(event: FormatResult) {
            println("publishing on format result stream: $event")
            emit(event)
            println("published on lint stream: $event")
        }
    }

data class FormatResult(
    val snippetId: Long,
    val userId: String,
    val formattedSnippet: String,
)
