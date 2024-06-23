package austral.ingsis.snippetrunner.controller

import austral.ingsis.snippetrunner.model.dto.*
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
    ): ResponseEntity<FormatterOutputDTO> {
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
    ): ResponseEntity<LintingOutputDTO> {
        try {
            return ResponseEntity.ok().body(PrintScriptRunner(dto.version).analyze(dto.content, dto.lintRules))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest().build()
        }
    }

    @CrossOrigin
    @PostMapping("/test")
    fun executeTestCase(
        @RequestBody dto: TestCaseDTO,
    ): ResponseEntity<Boolean> {
        try {
            val testResult = PrintScriptRunner(dto.version).test(dto.content, dto.inputs, dto.envs, dto.expectedOutput)
            return ResponseEntity.ok().body(testResult)
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest().build()
        }
    }
}
