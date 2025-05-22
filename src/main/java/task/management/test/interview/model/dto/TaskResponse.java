package task.management.test.interview.model.dto;

import java.time.Instant;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse<T> {
    private String status;
    private String message;
    private T data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Jakarta")
    private String timestamp;

    public static <T> TaskResponse<T> success(String message, T data) {
        return new TaskResponse<>("success", message, data, LocalDateTime.now().toString());
    }

    public static <T> TaskResponse<T> error(String message) {
        return new TaskResponse<>("error", message, null, LocalDateTime.now().toString());
    }
}
