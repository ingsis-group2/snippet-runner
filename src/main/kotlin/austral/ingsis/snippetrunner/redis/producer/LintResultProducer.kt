package austral.ingsis.snippetrunner.redis.producer

import kotlinx.coroutines.reactor.awaitSingle
import org.austral.ingsis.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component

@Component
class LintRequestProducer
@Autowired
constructor(
    @Value("\${redis.stream.request_linter_result_key}") streamKey: String,
    redis: ReactiveRedisTemplate<String, String>,
) : RedisStreamProducer(streamKey, redis) {
    suspend fun publishLintRequest(event: LintResult) {
        println("publishing on lint stream: $event")
        emit(event).awaitSingle()
    }
}

data class LintResult(
    val snippetId: Long,
    val reportList: List<String>,
    val errorList: List<String>,
)