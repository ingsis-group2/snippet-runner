package austral.ingsis.snippetrunner.service

import austral.ingsis.snippetrunner.model.dto.ExecutionOutputDTO
import austral.ingsis.snippetrunner.model.dto.FormatterOutputDTO
import austral.ingsis.snippetrunner.model.dto.LintingOutputDTO
import java.io.InputStream

interface SnippetRunner {
    fun executeCode(
        snippet: InputStream,
        inputs: List<String>,
    ): ExecutionOutputDTO

    fun format(
        snippet: String,
        rules: Map<String, Any>,
    ): FormatterOutputDTO

    fun analyze(
        snippet: String,
        rules: Map<String, Any>,
    ): LintingOutputDTO

    fun test(
        snippet: String,
        inputs: List<String>,
        envs: Map<String, Any>,
        expectedOutput: List<String>,
    ): Boolean
}
