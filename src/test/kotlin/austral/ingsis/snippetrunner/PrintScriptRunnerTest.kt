package austral.ingsis.snippetrunner

import austral.ingsis.snippetrunner.service.PrintScriptRunner
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PrintScriptRunnerTest {
    val runner10 = PrintScriptRunner("1.0")
    val runner11 = PrintScriptRunner("1.1")

    @Test
    fun `execute simple print`() {
        val code = "println('Hello World!');"
        val result = runner10.executeCode(code.byteInputStream(), emptyList())
        assertEquals(listOf("Hello World!"), result.outputs)
    }

    @Test
    fun `format readInput and print`() {
        val code = "let a: number = readInput('aaa'  ); println(a);"
        val rules =
            mapOf(
                "colonBefore" to false,
                "colonAfter" to true,
                "assignationBefore" to true,
                "assignationAfter" to true,
                "printJump" to 1,
            )
        val result = runner11.format(code, rules)
        assertEquals(
            "let a: number = readInput(\"aaa\");\n" +
                "\n" +
                "println(a);\n",
            result.formattedCode,
        )
    }

    @Test
    fun `analyze simple print with violation`() {
        val code = "println('Hello' + 'World!');"
        val rules = mapOf("enablePrintExpressions" to false)
        val result = runner10.analyze(code, rules)
        assertTrue(result.reportList.isNotEmpty())
    }

    @Test
    fun `analyze simple print without violation`() {
        val code = "println('Hello' + 'World!');"
        val rules = mapOf("enablePrintExpressions" to true)
        val result = runner10.analyze(code, rules)
        assertTrue(result.reportList.isEmpty())
    }
}
