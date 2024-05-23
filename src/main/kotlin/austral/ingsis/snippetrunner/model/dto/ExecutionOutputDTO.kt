package austral.ingsis.snippetrunner.model.dto

data class ExecutionOutputDTO(
    val outputs: List<String>,
    val errors: List<String>,
)
