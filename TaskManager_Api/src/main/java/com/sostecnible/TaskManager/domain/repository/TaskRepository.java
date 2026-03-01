package com.sostecnible.TaskManager.domain.repository;

import java.util.List;
import java.util.Optional;

import com.sostecnible.TaskManager.domain.model.Task;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findAll();
    void delete(Long id);
}