package austral.ingsis.snippetrunner.model.dto

//
data class RunnerDTO(
    val snippetId: Int,
    val version: String,
    val inputs: List<String>,
)
