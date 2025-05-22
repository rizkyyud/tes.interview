package task.management.test.interview.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import task.management.test.interview.model.dto.TaskRequest;
import task.management.test.interview.model.dto.TaskResponse;
import task.management.test.interview.model.entity.Task;
import task.management.test.interview.service.TaskService;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "API endpoints for managing tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(
        summary = "Create new task",
        description = "Create a task and assign it to a user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Task created successfully",
                         content = @Content(schema = @Schema(implementation = Task.class)))
        }
    )
    @PostMapping
    public ResponseEntity<TaskResponse<Task>> createTask(
            @RequestBody TaskRequest request,
            @Parameter(hidden = true) Principal principal) {
        Task task = taskService.createTask(principal.getName(), request);
        return ResponseEntity.ok(TaskResponse.success("Task created", task));
    }

    @Operation(
        summary = "Mark task as completed",
        description = "Mark an existing task as completed by ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Task marked as completed")
        }
    )
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse<Object>> completeTask(
            @PathVariable Long id,
            @Parameter(hidden = true) Principal principal) throws AccessDeniedException {
        taskService.completeTask(principal.getName(), id);
        return ResponseEntity.ok(TaskResponse.success("Task marked as completed", null));
    }

    @Operation(
        summary = "Get all tasks created by the user",
        description = "Retrieve list of tasks where current user is the creator"
    )
    @GetMapping
    public ResponseEntity<TaskResponse<List<Task>>> getTasks(@Parameter(hidden = true) Principal principal) {
        List<Task> tasks = taskService.getTasks(principal.getName());
        return ResponseEntity.ok(TaskResponse.success("Fetched task list", tasks));
    }

    @Operation(
        summary = "Get all incomplete tasks",
        description = "Retrieve list of tasks that are not completed"
    )
    @GetMapping("/incomplete")
    public ResponseEntity<TaskResponse<List<Task>>> getIncompleteTasks(@Parameter(hidden = true) Principal principal) {
        List<Task> tasks = taskService.getIncompleteTasks(principal.getName(), false);
        return ResponseEntity.ok(TaskResponse.success("List of incomplete tasks", tasks));
    }

    @Operation(
        summary = "Sum all even numbers",
        description = "Submit a list of integers and get the sum of all even numbers"
    )
    @PostMapping("/sum-evens")
    public ResponseEntity<TaskResponse<Integer>> sumEvenNumbers(@RequestBody List<Integer> input) {
        int result = taskService.sumOfEvenNumbers(input);
        return ResponseEntity.ok(TaskResponse.success("Sum of even numbers", result));
    }
}
