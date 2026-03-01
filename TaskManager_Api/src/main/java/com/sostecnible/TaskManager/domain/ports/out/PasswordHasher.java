package com.sostecnible.TaskManager.domain.ports.out;

public interface PasswordHasher {
    String hash(String rawPassword);
    boolean check(String rawPassword, String encodedPassword);
}