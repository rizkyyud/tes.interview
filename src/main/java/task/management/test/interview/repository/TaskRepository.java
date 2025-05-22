package task.management.test.interview.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import task.management.test.interview.model.entity.Task;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${task.table.name}")
    private String taskTable;

    private final RowMapper<Task> rowMapper = (rs, rowNum) -> {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setCompleted(rs.getBoolean("completed"));
        task.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        Timestamp completedTs = rs.getTimestamp("completed_at");
        if (completedTs != null)
            task.setCompletedAt(completedTs.toInstant());
        task.setCreatedBy(rs.getString("created_by"));
        task.setAssignedTo(rs.getString("assigned_to"));
        return task;
    };

    public Task save(Task task) {
        String sql = "INSERT INTO " + taskTable
                + " (title, description, completed, created_at, completed_at, created_by, assigned_to) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                Timestamp.from(task.getCreatedAt()),
                task.getCompletedAt() != null ? Timestamp.from(task.getCompletedAt()) : null,
                task.getCreatedBy(),
                task.getAssignedTo());
        Long id = jdbcTemplate.queryForObject("SELECT MAX(id) FROM " + taskTable, Long.class);
        task.setId(id);
        return task;
    }

    public List<Task> findByCreatedBy(String username) {
        String sql = "SELECT * FROM " + taskTable + " WHERE createdBy = ?";
        return jdbcTemplate.query(sql, rowMapper, username);
    }

    public List<Task> findTaskWithStatus(String username, boolean completed) {
        String sql = "SELECT * FROM " + taskTable + " WHERE createdBy = ? and completed = ?";
        return jdbcTemplate.query(sql, rowMapper,username, completed);
    }

    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM " + taskTable + " WHERE id = ?";
        List<Task> result = jdbcTemplate.query(sql, rowMapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public void markCompleted(Long id) {
        String sql = "UPDATE " + taskTable + " SET completed = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
