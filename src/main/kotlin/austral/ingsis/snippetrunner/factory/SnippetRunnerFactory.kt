package austral.ingsis.snippetrunner.factory

import austral.ingsis.snippetrunner.service.PrintScriptRunner
import austral.ingsis.snippetrunner.service.PythonRunner
import austral.ingsis.snippetrunner.service.SnippetRunner
import org.springframework.stereotype.Component

@Component
class SnippetRunnerFactory {
    fun getRunner(
        language: String,
        version: String,
    ): SnippetRunner {
        return when (language.lowercase()) {
            "printscript" -> PrintScriptRunner(version)
            "python" -> PythonRunner()
            else -> throw IllegalArgumentException("Unsupported language: $language")
        }
    }
}
