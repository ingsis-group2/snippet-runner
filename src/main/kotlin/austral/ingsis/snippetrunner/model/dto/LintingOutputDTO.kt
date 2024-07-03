package austral.ingsis.snippetrunner.model.dto

data class LintingOutputDTO(
    val reportList: List<String>,
    val errors: List<String>,
)
