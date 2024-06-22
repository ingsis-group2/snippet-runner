package austral.ingsis.snippetrunner.service

import cli.FileReader
import cli.PrintScriptRunner
import formatter.PrintScriptFormatterBuilder
import interpreter.builder.InterpreterBuilder
import interpreter.input.InputProvider
import parser.parserBuilder.PrintScriptParserBuilder
import sca.StaticCodeAnalyzerImpl
import java.io.File
import java.io.InputStream
import java.io.PrintWriter

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
            outputs.addAll(output.outputs)
            errors.addAll(output.errors)
        } catch (e: Exception) {
            errors.add(e.message ?: "An error occurred")
        }
        return ExecutionOutput(outputs, errors)
    }

    fun format(
        snippet: String,
        rules: Map<String, Any>,
    ): FormatterOutput {
        val configFilePath = generateFileFromRules(rules)
        val runner = PrintScriptRunner()
        return try {
            val output =
                runner.formatCode(
                    FileReader(snippet.byteInputStream(), version),
                    PrintScriptParserBuilder().build(version),
                    PrintScriptFormatterBuilder().build(version, configFilePath),
                )
            FormatterOutput(output.formattedCode, output.errors)
        } catch (e: Exception) {
            FormatterOutput("", listOf(e.message ?: "An error occurred"))
        } finally {
            // Clean up: delete the temporary config file
            File(configFilePath).delete()
        }
    }

    fun analyze(
        snippet: String,
        rules: Map<String, Any>,
    ): LintingOutput {
        val configFilePath = generateFileFromRules(rules)
        val runner = PrintScriptRunner()
        val reportList = mutableListOf<String>()
        val errorList = mutableListOf<String>()
        try {
            val output =
                runner.analyzeCode(
                    FileReader(snippet.byteInputStream(), version),
                    PrintScriptParserBuilder().build(version),
                    StaticCodeAnalyzerImpl(configFilePath, version),
                )
            reportList.addAll(output.reportList)
            errorList.addAll(output.errors)
        } catch (e: Exception) {
            errorList.add(e.message ?: "An error occurred")
        } finally {
            // Clean up: delete the temporary config file
            File(configFilePath).delete()
        }
        return LintingOutput(reportList, errorList)
    }

    private fun generateFileFromRules(rules: Map<String, Any>): String {
        val configFilePath = "tempFormatterConfig.yaml"
        PrintWriter(configFilePath).use { writer ->
            rules.forEach { (key, value) ->
                writer.println("$key: $value")
            }
        }
        return configFilePath
    }
}
