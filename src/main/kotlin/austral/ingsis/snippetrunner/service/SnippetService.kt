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

        val mockSnippet1 = SnippetDTO(1, "Mock Snippet 1", 1, "println(\"Hello, world!\");")
        val mockSnippet2 =
            SnippetDTO(2, "Mock Snippet 2", 1, "let x: number = 5;\nlet y: number = 10;\nprintln(x + y);")
        return when (snippetId) {
            1 -> mockSnippet1
            2 -> mockSnippet2
            else -> null
        }
    }
}
