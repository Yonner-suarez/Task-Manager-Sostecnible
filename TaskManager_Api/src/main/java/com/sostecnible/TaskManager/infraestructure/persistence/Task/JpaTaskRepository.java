package com.sostecnible.TaskManager.infraestructure.persistence.Task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTaskRepository extends JpaRepository<TaskEntity, Long> { }