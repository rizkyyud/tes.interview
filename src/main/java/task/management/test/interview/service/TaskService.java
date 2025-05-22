package task.management.test.interview.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import task.management.test.interview.exception.ResourceNotFoundException;
import task.management.test.interview.model.dto.TaskRequest;
import task.management.test.interview.model.entity.Task;
import task.management.test.interview.repository.TaskRepository;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(String creatorUsername, TaskRequest req) {
        Task task = new Task();
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setAssignedTo(req.getAssignedTo());
        task.setCompleted(false);
        task.setCreatedAt(Instant.now());
        task.setCreatedBy(creatorUsername);
        return taskRepository.save(task);
    }

    public List<Task> getIncompleteTasks(String username, Boolean complete) {
        return taskRepository.findTaskWithStatus(username,complete);
    }

    public void completeTask(String username, Long id) throws AccessDeniedException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (!task.getCreatedBy().equals(username)) {
            throw new AccessDeniedException("You are not allowed to modify this task");
        }

        taskRepository.markCompleted(id);
    }

    public List<Task> getTasks(String username) {
        return taskRepository.findByCreatedBy(username);
    }

    public int sumOfEvenNumbers(List<Integer> numbers) {
        return numbers.stream()
                .filter(n -> n % 2 == 0)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
