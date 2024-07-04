package austral.ingsis.snippetrunner.redis.producer

import org.austral.ingsis.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class LintRequestProducer
    @Autowired
    constructor(
        @Value("\${spring.data.redis.stream.request_linter_result_key}") streamKey: String,
        redis: RedisTemplate<String, String>,
    ) : RedisStreamProducer(streamKey, redis) {
        suspend fun publishLintRequest(event: LintResult) {
            println("publishing on lint stream: $event")
            emit(event)
            println("published on lint stream: $event")
        }
    }

data class LintResult(
    val snippetId: Long,
    val reportList: List<String>,
    val errorList: List<String>,
)
