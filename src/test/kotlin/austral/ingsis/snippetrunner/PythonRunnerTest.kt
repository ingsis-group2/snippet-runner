package austral.ingsis.snippetrunner

import austral.ingsis.snippetrunner.service.PythonRunner
import org.junit.jupiter.api.Test

class PythonRunnerTest {
    private val runner = PythonRunner()

    @Test
    fun `execute simple print`() {
        val code = "print('Hello World!')"
        val result = runner.executeCode(code.byteInputStream(), emptyList())
        assert(result.outputs == listOf("Hello World!"))
    }

    @Test
    fun `execute operation with error`() {
        val code = "print(1/0)"
        val result = runner.executeCode(code.byteInputStream(), emptyList())
        assert(result.errors.isNotEmpty())
    }

    @Test
    fun `execute operation with result`() {
        val code = "result = 1 + 1\nprint(result)"
        val result = runner.executeCode(code.byteInputStream(), emptyList())
        println(result)
        assert(result.outputs == listOf("2"))
    }
}
