package task.management.test.interview.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import task.management.test.interview.model.dto.TaskResponse;
import task.management.test.interview.service.ExternalService;

@RestController
@RequestMapping("/api/external")
@RequiredArgsConstructor
@Tag(name = "External Service", description = "Test call to external service with retry and fallback")
public class ExternalController {

    private final ExternalService externalService;

    @Operation(
            summary = "Call an external API",
            description = "Calls an external URL and retries on failure. Returns fallback if all retries fail.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Response from external service or fallback"),
                    @ApiResponse(responseCode = "503", description = "External service unavailable", content = @Content)
            }
    )
    @GetMapping("/fetch")
    public ResponseEntity<TaskResponse<String>> fetchExternalData(
            @Parameter(description = "The full URL of the external service to call", example = "https://jsonplaceholder.typicode.com/todos/1")
            @RequestParam String url) {

        String result = externalService.fetchData(url);
        return ResponseEntity.ok(TaskResponse.success("External service result", result));
    }
}

