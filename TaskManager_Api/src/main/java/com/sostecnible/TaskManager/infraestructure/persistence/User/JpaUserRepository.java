package com.sostecnible.TaskManager.infraestructure.persistence.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUserNameOrEmail(String userName, String email);
 }
