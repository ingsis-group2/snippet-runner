package austral.ingsis.snippetrunner

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class RunnerControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapper()

    @Test
    fun `hello should return a message`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("Snippet-runner is working!"))
    }

    @Test
    fun `execute snippet 1 should return Hello World`() {
        val dto =
            """
            {
                "snippetId": 1,
                "language": "PrintScript",
                "version": "1.0"
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/execute")
                .contentType("application/json")
                .content(dto),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.outputs[0]").value("Hello World!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isEmpty)
    }

    @Test
    fun `format snippet 1 should return the formatted code`() {
        val dto =
            """
            {
                "snippetId": 1,
                "language": "PrintScript",
                "version": "1.0"
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/format")
                .contentType("application/json")
                .content(dto),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("\nprintln(\"Hello World!\");\n"))
    }

    @Test
    fun `analyze snippet 1 should return an empty list`() {
        val dto =
            """
            {
                "snippetId": 1,
                "language": "PrintScript",
                "version": "1.0"
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/analyze")
                .contentType("application/json")
                .content(dto),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotExist())
    }

    @Test
    fun `analyze snippet 3 should return 2 errors`() {
        val dto =
            """
            {
                "snippetId": 3,
                "language": "PrintScript",
                "version": "1.0"
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/analyze")
                .contentType("application/json")
                .content(dto),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            // check that the length of the list is 2
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
    }
}
