package austral.ingsis.snippetrunner.model.dto

//
data class ExecuteDTO(
    val content: String,
    val version: String,
    val inputs: List<String>,
)

data class FormatDto(
    val content: String,
    val version: String,
    val formatRules: Map<String, Any>,
)

data class LintDto(
    val content: String,
    val version: String,
    val lintRules: Map<String, Any>,
)
