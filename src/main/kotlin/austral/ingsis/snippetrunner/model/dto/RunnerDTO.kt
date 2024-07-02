package austral.ingsis.snippetrunner.model.dto

data class ExecuteDTO(
    val content: String,
    val version: String,
    val inputs: List<String>,
    val language: String,
)

data class FormatDTO(
    val content: String,
    val version: String,
    val formatRules: Map<String, Any>,
    val language: String,
)

data class LintDTO(
    val content: String,
    val version: String,
    val lintRules: Map<String, Any>,
    val language: String,
)

data class TestCaseDTO(
    val content: String,
    val version: String,
    val inputs: List<String>,
    val envs: Map<String, Any>,
    val expectedOutput: List<String>,
    val language: String,
)
