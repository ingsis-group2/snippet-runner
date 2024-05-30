package austral.ingsis.snippetrunner.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class SnippetService(private val webClient: WebClient.Builder) {
    private val baseUrl = "https://localhost:8080"

    fun fetchSnippet(snippetId: Int): Mono<String> {
        return webClient.build()
            .get()
            .uri("$baseUrl/snippets/$snippetId")
            .retrieve()
            .bodyToMono(String::class.java)
    }
}
