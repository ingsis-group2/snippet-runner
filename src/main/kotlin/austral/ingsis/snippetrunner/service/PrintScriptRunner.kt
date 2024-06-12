package austral.ingsis.snippetrunner.service

import cli.FileReader
import cli.PrintScriptRunner
import formatter.PrintScriptFormatterBuilder
import interpreter.builder.InterpreterBuilder
import interpreter.input.InputProvider
import parser.parserBuilder.PrintScriptParserBuilder
import sca.StaticCodeAnalyzerImpl
import java.io.InputStream

class PrintScriptRunner(private val version: String) {
    fun executeCode(
        snippet: InputStream,
        inputs: List<String>,
    ): ExecutionOutput {
        val runner = PrintScriptRunner()
        val inputProvider = InputProvider(inputs)
        val errors = mutableListOf<String>()
        val outputs = mutableListOf<String>()
        try {
            val output =
                runner.executeCode(
                    FileReader(snippet, version),
                    PrintScriptParserBuilder().build(version),
                    InterpreterBuilder().build(version),
                    mutableMapOf(),
                    inputProvider,
                )
            outputs.addAll(output)
        } catch (e: Exception) {
            errors.add(e.message ?: "An error occurred")
        }
        return ExecutionOutput(outputs, errors)
    }

    fun format(snippet: String): String {
        val config = "src/main/resources/formatterConfig.yaml"
        val runner = PrintScriptRunner()
        return try {
            runner.formatCode(
                FileReader(snippet.byteInputStream(), version),
                PrintScriptParserBuilder().build(version),
                PrintScriptFormatterBuilder().build(version, config),
            )
        } catch (e: Exception) {
            e.message ?: "An error occurred"
        }
    }

    fun analyze(snippet: String): List<String> {
        val config = "src/main/resources/linterConfig.yaml"
        val runner = PrintScriptRunner()
        return try {
            runner.analyzeCode(
                FileReader(snippet.byteInputStream(), version),
                PrintScriptParserBuilder().build(version),
                StaticCodeAnalyzerImpl(config, version),
            )
        } catch (e: Exception) {
            listOf(e.message ?: "An error occurred")
        }
    }
}
