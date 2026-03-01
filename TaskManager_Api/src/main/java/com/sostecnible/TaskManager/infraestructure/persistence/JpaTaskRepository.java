package com.sostecnible.TaskManager.infraestructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTaskRepository extends JpaRepository<TaskEntity, Long> { }