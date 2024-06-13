package austral.ingsis.snippetrunner.controller

import austral.ingsis.snippetrunner.model.dto.ExecutionOutputDTO
import austral.ingsis.snippetrunner.model.dto.RunnerDTO
import austral.ingsis.snippetrunner.service.FormatterOutput
import austral.ingsis.snippetrunner.service.LintingOutput
import austral.ingsis.snippetrunner.service.PrintScriptRunner
import austral.ingsis.snippetrunner.service.SnippetService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class RunnerController(private val snippetService: SnippetService) {
    @GetMapping
    fun hello(): String {
        return "Snippet-runner is working!"
    }

    @CrossOrigin
    @PostMapping("/execute")
    fun execute(
        @RequestHeader("Authorization") token: String,
        @RequestBody dto: RunnerDTO,
    ): ResponseEntity<ExecutionOutputDTO> {
        try {
            val snippet = snippetService.fetchSnippet(dto.snippetId, token) ?: return ResponseEntity.notFound().build()
            val output = PrintScriptRunner(dto.version).executeCode(snippet.content.byteInputStream(), dto.inputs)
            return ResponseEntity.ok().body(ExecutionOutputDTO(output.outputs, output.errors))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest().build()
        }
    }

    @CrossOrigin
    @PostMapping("/format")
    fun format(
        @RequestHeader("Authorization") token: String,
        @RequestBody dto: RunnerDTO,
    ): ResponseEntity<FormatterOutput> {
        try {
            val snippet = snippetService.fetchSnippet(dto.snippetId, token) ?: return ResponseEntity.notFound().build()
            return ResponseEntity.ok().body(PrintScriptRunner(dto.version).format(snippet.content))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest().build()
        }
    }

    @CrossOrigin
    @PostMapping("/analyze")
    fun analyze(
        @RequestHeader("Authorization") token: String,
        @RequestBody dto: RunnerDTO,
    ): ResponseEntity<LintingOutput> {
        try {
            val snippet = snippetService.fetchSnippet(dto.snippetId, token) ?: return ResponseEntity.notFound().build()
            return ResponseEntity.ok().body(PrintScriptRunner(dto.version).analyze(snippet.content))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest().build()
        }
    }
}
