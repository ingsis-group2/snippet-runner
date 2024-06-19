package austral.ingsis.snippetrunner.service

import austral.ingsis.snippetrunner.model.dto.SnippetDTO
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime

@Service
class SnippetService {
    private val baseUrl = "http://snippet-operations:8081"
    private val client = WebClient.create(baseUrl)

    private val snippet1 =
        SnippetDTO(1, "name", "un snippet", "PrintScript", "ps", emptyList(), "println(  'Hello World!');", LocalDateTime.now(), null)
    private val snippet2 =
        SnippetDTO(
            2,
            "name",
            "un snippet",
            "PrintScript",
            "ps",
            emptyList(),
            "let a: number = 1; let b: number = 2; let c: number = a + b; println(c);",
            LocalDateTime.now(),
            null,
        )
    private val snippet3 =
        SnippetDTO(
            3,
            "name",
            "un snippet",
            "PrintScript",
            "ps",
            emptyList(),
            "let snake_Case_Variable: string = 'Hello'; println(snake_Case_Variable + 1);",
            LocalDateTime.now(),
            null,
        )
    private val snippet4 =
        SnippetDTO(
            4,
            "name",
            "un snippet",
            "PrintScript",
            "ps",
            emptyList(),
            "let camelCaseVariable: string = 'Hello'; println(camelCaseVariable);",
            LocalDateTime.now(),
            null,
        )
    private val snippet5 =
        SnippetDTO(
            5,
            "name",
            "un snippet",
            "PrintScript",
            "ps",
            emptyList(),
            "const value: number = 1; println(  value + 2  );",
            LocalDateTime.now(),
            null,
        )
    private val snippet6 =
        SnippetDTO(
            6,
            "name",
            "un snippet",
            "PrintScript",
            "ps",
            emptyList(),
            "let a: number = readInput(\"Enter a number: \"); println('Number is: ' + a);",
            LocalDateTime.now(),
            null,
        )
    private val snippetStore =
        mutableMapOf(
            1 to snippet1,
            2 to snippet2,
            3 to snippet3,
            4 to snippet4,
            5 to snippet5,
            6 to snippet6,
        )

    fun fetchSnippet(
        snippetId: Int,
        token: String,
    ): SnippetDTO? {
//        val url = "$baseUrl/snippet/$snippetId"
//        return client.get()
//            .uri(url)
//            .header("Authorization", token)
//            .retrieve()
//            .bodyToMono(SnippetDTO::class.java)
//            .block()
        return getSnippet(snippetId)
    }

    fun fetchLinterRules(token: String): List<String> {
        return emptyList()
    }

    fun fetchFormatterRules(token: String): String {
        return ""
    }

    private fun getSnippet(snippetId: Int): SnippetDTO? {
        return snippetStore[snippetId]
    }
}
