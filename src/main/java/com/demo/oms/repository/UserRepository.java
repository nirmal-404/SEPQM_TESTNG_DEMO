package com.demo.oms.repository;

import com.demo.oms.model.User;

public interface UserRepository {

    User save(User user);

    User findByUsername(String username);

    User findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
