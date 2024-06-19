package austral.ingsis.snippetrunner.controller

import austral.ingsis.snippetrunner.model.dto.ExecuteDTO
import austral.ingsis.snippetrunner.model.dto.ExecutionOutputDTO
import austral.ingsis.snippetrunner.model.dto.FormatDto
import austral.ingsis.snippetrunner.model.dto.LintDto
import austral.ingsis.snippetrunner.service.FormatterOutput
import austral.ingsis.snippetrunner.service.LintingOutput
import austral.ingsis.snippetrunner.service.PrintScriptRunner
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RunnerController() {
    @GetMapping
    fun hello(): String {
        return "Snippet-runner is working!"
    }

    @CrossOrigin
    @PostMapping("/execute")
    fun execute(
        @RequestBody dto: ExecuteDTO,
    ): ResponseEntity<ExecutionOutputDTO> {
        try {
            val output = PrintScriptRunner(dto.version).executeCode(dto.content.byteInputStream(), dto.inputs)
            return ResponseEntity.ok().body(ExecutionOutputDTO(output.outputs, output.errors))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest().build()
        }
    }

    @CrossOrigin
    @PostMapping("/format")
    fun format(
        @RequestBody dto: FormatDto,
    ): ResponseEntity<FormatterOutput> {
        try {
            return ResponseEntity.ok().body(PrintScriptRunner(dto.version).format(dto.content, dto.formatRules))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest().build()
        }
    }

    @CrossOrigin
    @PostMapping("/lint")
    fun analyze(
        @RequestBody dto: LintDto,
    ): ResponseEntity<LintingOutput> {
        try {
            return ResponseEntity.ok().body(PrintScriptRunner(dto.version).analyze(dto.content, dto.lintRules))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest().build()
        }
    }
}
