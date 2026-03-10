package com.demo.oms.repository;

import com.demo.oms.model.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> usersByUsername = new HashMap<>();
    private final Map<String, User> usersByEmail = new HashMap<>();

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        if (user.getUsername() != null) {
            usersByUsername.put(user.getUsername(), user);
        }
        if (user.getEmail() != null) {
            usersByEmail.put(user.getEmail(), user);
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return usersByUsername.get(username);
    }

    @Override
    public User findByEmail(String email) {
        return usersByEmail.get(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return usersByUsername.containsKey(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usersByEmail.containsKey(email);
    }
}
