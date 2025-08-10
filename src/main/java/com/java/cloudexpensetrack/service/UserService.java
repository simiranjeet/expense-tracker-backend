package com.java.cloudexpensetrack.service;

import com.java.cloudexpensetrack.model.User;
import com.java.cloudexpensetrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public Optional<User> findById(String id) { return userRepository.findById(id); }

    public User save(User user) { return userRepository.save(user); }
}
