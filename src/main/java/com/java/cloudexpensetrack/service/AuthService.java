package com.java.cloudexpensetrack.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.java.cloudexpensetrack.repository.UserRepository;
import com.java.cloudexpensetrack.model.User;
import com.java.cloudexpensetrack.util.JwtUtil;
import java.util.Collections;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    public String register(String email, String rawPassword, String fullName) {
        if (userRepo.existsByEmail(email)) throw new RuntimeException("Email exists");
        User u = new User();
        u.setEmail(email);
        u.setPasswordHash(passwordEncoder.encode(rawPassword));
        u.setFullName(fullName);
        u.setRoles(Collections.singletonList("USER"));
        userRepo.save(u);
        return jwtUtil.generateToken(u.getId(), u.getEmail());
    }

    public String login(String email, String rawPassword) {
        User u = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Bad credentials"));
        if (!passwordEncoder.matches(rawPassword, u.getPasswordHash())) throw new RuntimeException("Bad credentials");
        return jwtUtil.generateToken(u.getId(), u.getEmail());
    }
}

