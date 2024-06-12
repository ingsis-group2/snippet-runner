package austral.ingsis.snippetrunner.service

import austral.ingsis.snippetrunner.model.dto.SnippetDTO
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class SnippetService {
    private val baseUrl = "https://localhost:8080"
    private val client = WebClient.create(baseUrl)

    private val snippet1 = SnippetDTO(1, "name", 1, "println(  'Hello World!');")
    private val snippet2 = SnippetDTO(2, "name", 1, "let a = 1; let b = 2; let c = a + b; println(c);")
    private val snippet3 = SnippetDTO(3, "name", 1, "let snake_Case_Variable: string = 'Hello'; println(snake_Case_Variable + 1);")
    private val snippet4 = SnippetDTO(4, "name", 1, "let camelCaseVariable: string = 'Hello'; println(camelCaseVariable);")
    private val snippet5 = SnippetDTO(5, "name", 1, "const value = 1; println(  value + 2  );")
    private val snippet6 = SnippetDTO(6, "name", 1, "let a: number = readInput(\"Enter a number: \"); println('Number is: ' + a);")
    private val snippetStore =
        mutableMapOf(
            1 to snippet1,
            2 to snippet2,
            3 to snippet3,
            4 to snippet4,
            5 to snippet5,
            6 to snippet6,
        )

    fun fetchSnippet(snippetId: Int): SnippetDTO? {
        return getSnippet(snippetId)
    }

    private fun getSnippet(snippetId: Int): SnippetDTO? {
        return snippetStore[snippetId]
    }
}
