package com.sostecnible.TaskManager.domain.ports.out;

import com.sostecnible.TaskManager.domain.model.User;

public interface TokenService {
    String generateToken(User user);

    boolean validateToken(String token);

    Long extractUserId(String token);
}
