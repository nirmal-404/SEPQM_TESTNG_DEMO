package com.demo.oms.service;

import com.demo.oms.exception.AuthenticationFailedException;
import com.demo.oms.exception.UserAlreadyExistsException;
import com.demo.oms.external.EmailService;
import com.demo.oms.model.User;
import com.demo.oms.repository.UserRepository;

import java.util.Objects;
import java.util.UUID;

public class AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
        this.emailService = Objects.requireNonNull(emailService, "emailService must not be null");
    }

    public User register(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        if (isBlank(user.getUsername()) || isBlank(user.getEmail()) || isBlank(user.getPassword())) {
            throw new IllegalArgumentException("username, email and password are required");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + user.getEmail());
        }

        if (isBlank(user.getUserId())) {
            user.setUserId("USR-" + UUID.randomUUID());
        }

        User saved = userRepository.save(user);
        emailService.sendWelcomeEmail(saved);
        return saved;
    }

    public User login(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            throw new AuthenticationFailedException("Username/password must be provided");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AuthenticationFailedException("User not found: " + username);
        }

        if (!password.equals(user.getPassword())) {
            throw new AuthenticationFailedException("Invalid credentials");
        }

        return user;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
