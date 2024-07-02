package austral.ingsis.snippetrunner.service

import austral.ingsis.snippetrunner.model.dto.ExecutionOutputDTO
import austral.ingsis.snippetrunner.model.dto.FormatterOutputDTO
import austral.ingsis.snippetrunner.model.dto.LintingOutputDTO
import org.python.util.PythonInterpreter
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets

class PythonRunner : SnippetRunner {
    override fun executeCode(
        snippet: InputStream,
        inputs: List<String>,
    ): ExecutionOutputDTO {
        val interpreter = PythonInterpreter()
        val script = snippet.readBytes().toString(StandardCharsets.UTF_8)

        val outputStream = ByteArrayOutputStream()
        val errorStream = ByteArrayOutputStream()
        val outPrintStream = PrintStream(outputStream)
        val errPrintStream = PrintStream(errorStream)

        interpreter.setOut(outPrintStream)
        interpreter.setErr(errPrintStream)

        try {
            interpreter.exec(script)
            outPrintStream.flush()
            errPrintStream.flush()

            val outputString = outputStream.toString(StandardCharsets.UTF_8.name()).trim()
            val errorString = errorStream.toString(StandardCharsets.UTF_8.name()).trim()

            val outputs = if (outputString.isNotEmpty()) outputString.split("\n") else emptyList()
            val errors = if (errorString.isNotEmpty()) errorString.split("\n") else emptyList()

            return ExecutionOutputDTO(outputs, errors)
        } catch (e: Exception) {
            val errors = listOf(e.message ?: "An error occurred")
            return ExecutionOutputDTO(listOf(), errors)
        } finally {
            outPrintStream.close()
            errPrintStream.close()
        }
    }

    override fun format(
        snippet: String,
        rules: Map<String, Any>,
    ): FormatterOutputDTO {
        // Placeholder implementation for formatting
        return FormatterOutputDTO(snippet, listOf())
    }

    override fun analyze(
        snippet: String,
        rules: Map<String, Any>,
    ): LintingOutputDTO {
        // Placeholder implementation for analyzing
        return LintingOutputDTO(listOf(), listOf("Not implemented"))
    }

    override fun test(
        snippet: String,
        inputs: List<String>,
        envs: Map<String, Any>,
        expectedOutput: List<String>,
    ): Boolean {
        // Placeholder implementation for testing
        return false
    }
}
