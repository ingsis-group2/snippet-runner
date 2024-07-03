package austral.ingsis.snippetrunner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@AutoConfigureMockMvc
class RunnerControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

//    @Test
//    fun `hello should return a message`() {
//        mockMvc.perform(MockMvcRequestBuilders.get("/"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.content().string("Snippet-runner is working!"))
//    }
//
//    // PrintScript
//
//    @Test
//    fun `execute PrintScript snippet should return Hello World`() {
//        val dto =
//            """
//            {
//                "content": "println('Hello World!');",
//                "version": "1.0",
//                "inputs": [],
//                "language": "PrintScript"
//            }
//            """.trimIndent()
//
//        mockMvc.perform(
//            MockMvcRequestBuilders.post("/execute")
//                .contentType("application/json")
//                .content(dto),
//        )
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.outputs[0]").value("Hello World!"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isEmpty)
//    }
//
//    @Test
//    fun `format PrintScript snippet should return the formatted code`() {
//        val dto =
//            """
//            {
//                "content": "println('Hello World!');",
//                "version": "1.0",
//                "formatRules": {
//                    "colonBefore": false,
//                    "colonAfter": true,
//                    "assignationBefore": true,
//                    "assignationAfter": true,
//                    "printJump": 1
//                 },
//                "language": "PrintScript"
//            }
//            """.trimIndent()
//
//        mockMvc.perform(
//            MockMvcRequestBuilders.post("/format")
//                .contentType("application/json")
//                .content(dto),
//        )
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.formattedCode").value("\nprintln(\"Hello World!\");\n"))
//    }
//
//    @Test
//    fun `analyze PrintScript snippet should return an empty list`() {
//        val dto =
//            """
//            {
//                "content": "println('Hello World!' + a);",
//                "version": "1.0",
//                "lintRules": {
//                    "enablePrintExpressions": true,
//                    "caseConvention": "SNAKE_CASE"
//                },
//                "language": "PrintScript"
//            }
//            """.trimIndent()
//
//        mockMvc.perform(
//            MockMvcRequestBuilders.post("/lint")
//                .contentType("application/json")
//                .content(dto),
//        )
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotExist())
//    }
//
//    @Test
//    fun `execute PrintScript valid test case should return true`() {
//        val dto =
//            """
//            {
//                "content": "let a: number = readInput('Enter a number'); println('Number: ' + a);",
//                "version": "1.1",
//                "inputs": ["5"],
//                "envs": {},
//                "expectedOutput": ["Enter a number", "5", "Number: 5"],
//                "language": "PrintScript"
//            }
//            """.trimIndent()
//
//        mockMvc.perform(
//            MockMvcRequestBuilders.post("/test")
//                .contentType("application/json")
//                .content(dto),
//        )
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.content().string("true"))
//    }
//
//    @Test
//    fun `bad PrintScript request should return 400`() {
//        val dto =
//            """
//            {
//                "content": "let a: number = readInput('Enter a number'); println('Number: ' + a);",
//                "version": "1.1"
//            }
//            """.trimIndent()
//
//        mockMvc.perform(
//            MockMvcRequestBuilders.post("/test")
//                .contentType("application/json")
//                .content(dto),
//        )
//            .andExpect(MockMvcResultMatchers.status().isBadRequest)
//    }
//
//    // Python
//
//    @Test
//    fun `execute Python operation should return result`() {
//        val dto =
//            """
//            {
//                "content": "print(1 + 2)",
//                "version": "3.8",
//                "inputs": [],
//                "language": "Python"
//            }
//            """.trimIndent()
//
//        mockMvc.perform(
//            MockMvcRequestBuilders.post("/execute")
//                .contentType("application/json")
//                .content(dto),
//        )
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.outputs[0]").value("3"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isEmpty)
//    }
//
//    @Test
//    fun `bad Python request should return 400`() {
//        val dto =
//            """
//            {
//                "content": "a = input('Enter a number'); result = 'Number: ' + a",
//                "version": "3.8"
//            }
//            """.trimIndent()
//
//        mockMvc.perform(
//            MockMvcRequestBuilders.post("/execute")
//                .contentType("application/json")
//                .content(dto),
//        )
//            .andExpect(MockMvcResultMatchers.status().isBadRequest)
//    }
}
