package austral.ingsis.snippetrunner

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
                "content": "println('Hello World!');",
                "version": "1.0",
                "inputs": []
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
                "content": "println('Hello World!');",
                "version": "1.0",
                "formatRules": {
                    "colonBefore": false,
                    "colonAfter": true,
                    "assignationBefore": true,
                    "assignationAfter": true,
                    "printJump": 1
                 }
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/format")
                .contentType("application/json")
                .content(dto),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.formattedCode").value("\nprintln(\"Hello World!\");\n"))
    }

    @Test
    fun `analyze snippet 1 should return an empty list`() {
        val dto =
            """
            {
                "content": "println('Hello World!' + a);",
                "version": "1.0",
                "lintRules": {
                    "enablePrintExpressions": true,
                    "caseConvention": "SNAKE_CASE"
                }
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/lint")
                .contentType("application/json")
                .content(dto),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotExist())
    }

    @Test
    fun `execute valid test case should return true`() {
        val dto =
            """
            {
                "content": "let a: number = readInput('Enter a number'); println('Number: ' + a);",
                "version": "1.1",
                "inputs": ["5"],
                "envs": {},
                "expectedOutput": ["Enter a number", "5", "Number: 5"]
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/test")
                .contentType("application/json")
                .content(dto),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("true"))
    }

    @Test
    fun `bad test request should return 400`() {
        val dto =
            """
            {
                "content": "let a: number = readInput('Enter a number'); println('Number: ' + a);",
                "version": "1.1",
            }
            """.trimIndent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/test")
                .contentType("application/json")
                .content(dto),
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}
