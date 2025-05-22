package task.management.test.interview.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "TaskRequest", description = "Request body for creating a task")
public class TaskRequest {

    @Schema(description = "Title of the task", example = "Update API docs", required = true)
    private String title;

    @Schema(description = "Description of the task", example = "Make sure all endpoints are documented", required = false)
    private String description;

    @Schema(description = "Username assigned to this task", example = "budi", required = true)
    private String assignedTo;
}
