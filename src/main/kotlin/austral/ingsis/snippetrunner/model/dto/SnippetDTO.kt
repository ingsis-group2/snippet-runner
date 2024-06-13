package austral.ingsis.snippetrunner.model.dto

import java.time.LocalDateTime

data class SnippetDTO(
    val id: Long,
    val writer: String,
    val name: String,
    val language: String,
    val extension: String,
    val readers: List<String>,
    val content: String,
    val creationDate: LocalDateTime,
    val updateDate: LocalDateTime?,
)
