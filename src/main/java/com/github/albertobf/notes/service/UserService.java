package com.github.albertobf.notes.service;

import com.github.albertobf.notes.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    User saveUser(User user);
}
