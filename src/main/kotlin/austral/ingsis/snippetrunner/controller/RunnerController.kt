package austral.ingsis.snippetrunner.controller

import austral.ingsis.snippetrunner.model.dto.ExecutionOutputDTO
import austral.ingsis.snippetrunner.model.dto.RunnerDTO
import austral.ingsis.snippetrunner.service.PrintScriptRunner
import austral.ingsis.snippetrunner.service.SnippetService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RunnerController(private val snippetService: SnippetService) {
    @GetMapping
    fun hello(): String {
        return "Hello World"
    }

    @PostMapping("/execute")
    fun execute(
        @RequestBody dto: RunnerDTO,
    ): ResponseEntity<ExecutionOutputDTO> {
        try {
            val snippet = snippetService.fetchSnippet(dto.snippetId).block() ?: return ResponseEntity.notFound().build()
            val output = PrintScriptRunner(dto.version).executeCode(snippet.byteInputStream())
            return ResponseEntity.ok().body(ExecutionOutputDTO(output.outputs, output.errors))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/format")
    fun format(
        @RequestBody dto: RunnerDTO,
    ): ResponseEntity<String> {
        try {
            val snippet = snippetService.fetchSnippet(dto.snippetId).block() ?: return ResponseEntity.notFound().build()
            return ResponseEntity.ok().body(PrintScriptRunner(dto.version).format(snippet, ""))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/analyze")
    fun analyze(
        @RequestBody dto: RunnerDTO,
    ): ResponseEntity<List<String>> {
        try {
            val snippet = snippetService.fetchSnippet(dto.snippetId).block() ?: return ResponseEntity.notFound().build()
            return ResponseEntity.ok().body(PrintScriptRunner(dto.version).analyze(snippet, ""))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }
}
