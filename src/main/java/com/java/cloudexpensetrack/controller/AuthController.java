package com.java.cloudexpensetrack.controller;

import com.java.cloudexpensetrack.dto.AuthRequest;
import com.java.cloudexpensetrack.dto.AuthResponse;
import com.java.cloudexpensetrack.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        String token = authService.register(req.getEmail(), req.getPassword(), req.getFullName());
        return ResponseEntity.ok(new AuthResponse(token, Long.parseLong(System.getenv().getOrDefault("JWT_EXP_MS", "3600000"))));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        String token = authService.login(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(new AuthResponse(token, Long.parseLong(System.getenv().getOrDefault("JWT_EXP_MS", "3600000"))));
    }

    // RegisterRequest inner static class (so we keep file count small)
    public static class RegisterRequest {
        @jakarta.validation.constraints.Email
        @jakarta.validation.constraints.NotBlank
        private String email;
        @jakarta.validation.constraints.NotBlank
        private String password;
        private String fullName;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
    }
}
