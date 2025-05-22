package task.management.test.interview.repository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import task.management.test.interview.model.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
    private static final Map<String, User> users = new HashMap<>();

    static {
        users.put("user", new User(1L, "user", new BCryptPasswordEncoder().encode("password")));
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
}
