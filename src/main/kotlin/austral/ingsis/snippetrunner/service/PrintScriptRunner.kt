package austral.ingsis.snippetrunner.service

import ast.AstNode
import cli.FileReader
import cli.PrintScriptRunner
import formatter.PrintScriptFormatterBuilder
import interpreter.builder.InterpreterBuilder
import lexer.factory.LexerBuilder
import parser.parserBuilder.PrintScriptParserBuilder
import sca.StaticCodeAnalyzerImpl
import java.io.InputStream

class PrintScriptRunner(private val version: String) {
    fun executeCode(snippet: InputStream): ExecutionOutput {
        val runner = PrintScriptRunner()
        val errors = mutableListOf<String>()
        val outputs = mutableListOf<String>()
        try {
            val output =
                runner.executeCode(
                    FileReader(snippet, version),
                    LexerBuilder().build(version),
                    PrintScriptParserBuilder().build(version),
                    InterpreterBuilder().build(version),
                    mutableMapOf(),
                )
            outputs.addAll(output)
        } catch (e: Exception) {
            errors.add(e.message ?: "An error occurred")
        }
        return ExecutionOutput(outputs, errors)
    }

    fun format(
        snippet: String,
        config: String,
    ): String {
        val formatter = PrintScriptFormatterBuilder().build(version, config)
        val ast = getAST(snippet)
        return formatter.format(ast)
    }

    fun analyze(
        snippet: String,
        config: String,
    ): List<String> {
        val analyzer = StaticCodeAnalyzerImpl(config, version)
        val ast = getAST(snippet)
        return analyzer.analyze(ast)
    }

    private fun getAST(snippet: String): AstNode {
        val parser = PrintScriptParserBuilder().build(version)
        val tokens = LexerBuilder().build(version).lex(snippet)
        return parser.createAST(tokens)
    }
}
