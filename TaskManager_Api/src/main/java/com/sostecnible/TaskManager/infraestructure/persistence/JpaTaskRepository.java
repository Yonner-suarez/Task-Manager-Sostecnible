package com.sostecnible.taskmanager.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTaskRepository extends JpaRepository<TaskEntity, Long> { }