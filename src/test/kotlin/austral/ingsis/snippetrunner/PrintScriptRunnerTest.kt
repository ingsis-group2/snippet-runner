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

    @Test
    fun `execute simple test case`() {
        val code = "let a: number = readInput('Enter a number'); println('Number: ' + a);"
        val inputs = listOf("5")
        val expected = listOf("Enter a number", "5", "Number: 5")
        val result =
            runner11.test(
                code,
                inputs,
                emptyMap(),
                expected,
            )
        assertTrue(result)
    }

    @Test
    fun `execute test case with multiple inputs`() {
        val code =
            "let a: number = readInput('Enter a number'); " +
                "let b: number = readInput('Enter another number'); " +
                "println('Numbers are: ' + a + ' and ' + b);"
        val inputs = listOf("5", "3")
        val expected = listOf("Enter a number", "5", "Enter another number", "3", "Numbers are: 5 and 3")
        val result =
            runner11.test(
                code,
                inputs,
                emptyMap(),
                expected,
            )
        assertTrue(result)
    }

    @Test
    fun `execute test case with env`() {
        val code = "let a: string = readEnv('ENV_VAR'); println(a);"
        val inputs = emptyList<String>()
        val envs = mapOf("ENV_VAR" to "Hello World!")
        val expected = listOf("Hello World!")
        val result = runner11.test(code, inputs, envs, expected)
        assertTrue(result)
    }

    @Test
    fun `failed execution`() {
        val code = "print('Hello World!')"
        val result = runner10.executeCode(code.byteInputStream(), emptyList())
        assertTrue(result.errors.isNotEmpty())
        assertTrue(result.outputs.isEmpty())
    }

    @Test
    fun `failed format`() {
        val code = "let a: number = readInput('aaa'   println(a)"
        val rules =
            mapOf(
                "colonBefore" to false,
                "colonAfter" to true,
                "assignationBefore" to true,
                "assignationAfter" to true,
                "printJump" to 1,
            )
        val result = runner11.format(code, rules)
        assertTrue(result.errors.isNotEmpty())
        assertTrue(result.formattedCode.isEmpty())
    }
}
