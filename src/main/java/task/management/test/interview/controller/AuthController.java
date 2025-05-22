package task.management.test.interview.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import task.management.test.interview.model.dto.TaskResponse;
import task.management.test.interview.model.dto.AuthRequest;
import task.management.test.interview.security.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user login and token generation")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "Login to get JWT token",
            description = "Authenticate user and return a JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login success, token returned"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<TaskResponse<Map<String, String>>> login(@RequestBody AuthRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String token = jwtUtil.generateToken(request.getUsername());
        return ResponseEntity.ok(TaskResponse.success("Login successful", Map.of("token", token)));
    }
}
