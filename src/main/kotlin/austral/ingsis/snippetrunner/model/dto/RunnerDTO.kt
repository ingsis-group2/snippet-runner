package austral.ingsis.snippetrunner.model.dto

data class RunnerDTO(
    val snippetId: Int,
    val language: String,
    val version: String,
    val inputs: List<String>,
)
