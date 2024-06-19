package austral.ingsis.snippetrunner

import austral.ingsis.snippetrunner.service.PrintScriptRunner
import org.junit.jupiter.api.Assertions.assertEquals
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
        val result = runner11.format(code)
        assertEquals(
            "let a:number=readInput(\"aaa\");\n" +
                "\n" +
                "println(a);\n",
            result.formattedCode,
        )
    }
}
