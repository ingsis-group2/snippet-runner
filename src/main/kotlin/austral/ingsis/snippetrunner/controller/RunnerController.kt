package austral.ingsis.snippetrunner.controller

import austral.ingsis.snippetrunner.factory.SnippetRunnerFactory
import austral.ingsis.snippetrunner.model.dto.ExecuteDTO
import austral.ingsis.snippetrunner.model.dto.ExecutionOutputDTO
import austral.ingsis.snippetrunner.model.dto.FormatDTO
import austral.ingsis.snippetrunner.model.dto.FormatterOutputDTO
import austral.ingsis.snippetrunner.model.dto.LintDTO
import austral.ingsis.snippetrunner.model.dto.LintingOutputDTO
import austral.ingsis.snippetrunner.model.dto.TestCaseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RunnerController(private val snippetRunnerFactory: SnippetRunnerFactory) {
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
            val runner = snippetRunnerFactory.getRunner(dto.language, dto.version)
            val output = runner.executeCode(dto.content.byteInputStream(), dto.inputs)
            return ResponseEntity.ok().body(ExecutionOutputDTO(output.outputs, output.errors))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest().build()
        }
    }

    @CrossOrigin
    @PostMapping("/format")
    fun format(
        @RequestBody dto: FormatDTO,
    ): ResponseEntity<FormatterOutputDTO> {
        try {
            val runner = snippetRunnerFactory.getRunner(dto.language, dto.version)
            return ResponseEntity.ok().body(runner.format(dto.content, dto.formatRules))
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest().build()
        }
    }

    @CrossOrigin
    @PostMapping("/lint")
    fun analyze(
        @RequestBody dto: LintDTO,
    ): ResponseEntity<LintingOutputDTO> {
        try {
            val runner = snippetRunnerFactory.getRunner(dto.language, dto.version)
            return ResponseEntity.ok().body(runner.analyze(dto.content, dto.lintRules))
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
            val runner = snippetRunnerFactory.getRunner(dto.language, dto.version)
            val testResult = runner.test(dto.content, dto.inputs, dto.envs, dto.expectedOutput)
            return ResponseEntity.ok().body(testResult)
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest().build()
        }
    }
}
