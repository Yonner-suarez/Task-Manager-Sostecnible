package com.sostecnible.TaskManager.domain.repository;

import com.sostecnible.TaskManager.domain.model.User;

public interface UserRepository {
    User save(User task);
    User findByUserNameOrEmail(String userName, String email);
}