package austral.ingsis.snippetrunner.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RunnerController() {
    @GetMapping
    fun hello(): String {
        return "Hello World"
    }

    @PostMapping("/execute")
    fun execute(): String {
        return "Executing"
    }

    @PostMapping("/format")
    fun format(): String {
        return "Formatting"
    }

    @PostMapping("/analyze")
    fun analyze(): String {
        return "Analyzing"
    }

    @PostMapping("/validate")
    fun validate(): String {
        return "Validating"
    }
}
