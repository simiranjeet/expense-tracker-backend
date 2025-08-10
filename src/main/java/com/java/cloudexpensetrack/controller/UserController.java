package com.java.cloudexpensetrack.controller;

import com.java.cloudexpensetrack.model.User;
import com.java.cloudexpensetrack.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping("/me")
    public ResponseEntity<User> me(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return userService.findById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateMe(@RequestBody User incoming, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return userService.findById(userId).map(u -> {
            u.setFullName(incoming.getFullName() != null ? incoming.getFullName() : u.getFullName());
            u.setEmail(incoming.getEmail() != null ? incoming.getEmail() : u.getEmail());
            User saved = userService.save(u);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
