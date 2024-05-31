package austral.ingsis.snippetrunner.service

import austral.ingsis.snippetrunner.model.dto.SnippetDTO
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class SnippetService {
    private val baseUrl = "https://localhost:8080"
    private val client = WebClient.create(baseUrl)

    fun fetchSnippet(snippetId: Int): SnippetDTO? {
//        val snippet =
//            client.get()
//                .uri("/snippets/$snippetId")
//                .retrieve()
//                .bodyToMono(SnippetDTO::class.java)
//                .block()
//        return snippet
        return SnippetDTO(1, "Mock Snippet", 1, "println(\"Hello, World!\");")
    }
}
