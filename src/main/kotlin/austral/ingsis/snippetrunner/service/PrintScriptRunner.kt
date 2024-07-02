package austral.ingsis.snippetrunner.service

import austral.ingsis.snippetrunner.model.dto.ExecutionOutputDTO
import austral.ingsis.snippetrunner.model.dto.FormatterOutputDTO
import austral.ingsis.snippetrunner.model.dto.LintingOutputDTO
import cli.FileReader
import cli.PrintScriptRunner
import formatter.PrintScriptFormatterBuilder
import interpreter.builder.InterpreterBuilder
import interpreter.input.InputProvider
import interpreter.variable.Variable
import parser.parserBuilder.PrintScriptParserBuilder
import sca.StaticCodeAnalyzerImpl
import token.TokenType
import java.io.File
import java.io.InputStream
import java.io.PrintWriter

class PrintScriptRunner(private val version: String) : SnippetRunner {
    override fun executeCode(
        snippet: InputStream,
        inputs: List<String>,
    ): ExecutionOutputDTO {
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
        return ExecutionOutputDTO(outputs, errors)
    }

    override fun format(
        snippet: String,
        rules: Map<String, Any>,
    ): FormatterOutputDTO {
        val configFilePath = generateFileFromRules(rules)
        val runner = PrintScriptRunner()
        return try {
            val output =
                runner.formatCode(
                    FileReader(snippet.byteInputStream(), version),
                    PrintScriptParserBuilder().build(version),
                    PrintScriptFormatterBuilder().build(version, configFilePath),
                )
            FormatterOutputDTO(output.formattedCode, output.errors)
        } catch (e: Exception) {
            FormatterOutputDTO("", listOf(e.message ?: "An error occurred"))
        } finally {
            // Clean up: delete the temporary config file
            File(configFilePath).delete()
        }
    }

    override fun analyze(
        snippet: String,
        rules: Map<String, Any>,
    ): LintingOutputDTO {
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
        return LintingOutputDTO(reportList, errorList)
    }

    override fun test(
        snippet: String,
        inputs: List<String>,
        envs: Map<String, Any>,
        expectedOutput: List<String>,
    ): Boolean {
        val runner = PrintScriptRunner()
        val inputProvider = InputProvider(inputs)
        val errors = mutableListOf<String>()
        val outputs = mutableListOf<String>()
        val symbolTable = mutableMapOf<Variable, Any>()
        envs.forEach { (key, value) ->
            symbolTable[Variable(key, TokenType.STRINGTYPE, TokenType.CONST)] = value
        }
        try {
            val output =
                runner.executeCode(
                    FileReader(snippet.byteInputStream(), version),
                    PrintScriptParserBuilder().build(version),
                    InterpreterBuilder().build(version),
                    symbolTable,
                    inputProvider,
                )
            outputs.addAll(output.outputs)
            errors.addAll(output.errors)
        } catch (e: Exception) {
            errors.add(e.message ?: "An error occurred")
        }
        return outputs == expectedOutput
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
