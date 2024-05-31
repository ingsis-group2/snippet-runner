package austral.ingsis.snippetrunner.model.dto

data class SnippetDTO(
    val id: Int,
    val name: String,
    val ownerId: Int,
    val code: String,
)
