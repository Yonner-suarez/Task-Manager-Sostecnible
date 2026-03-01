package com.sostecnible.TaskManager.infraestructure.persistence;

import org.springframework.stereotype.Repository;

import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.domain.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final JpaTaskRepository jpaRepository;

    public TaskRepositoryImpl(JpaTaskRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = new TaskEntity(
                task.getIdTask(),
                task.getTitle(),
                task.getDescription(),
                TaskEntity.Priority.valueOf(task.getPriority().name()),
                task.getCreatedAt(),
                task.getDueDate(),
                task.getStatus(),
                task.getIsActive(),
                task.getFechaVencimiento()
        );
        TaskEntity saved = jpaRepository.save(entity);
        return new Task(
                saved.getIdTask(),
                saved.getTitle(),
                saved.getDescription(),
                com.sostecnible.TaskManager.domain.model.Task.Priority.valueOf(saved.getPriority().name()),
                saved.getCreatedAt(),
                saved.getDueDate(),
                saved.getStatus(),
                saved.getIsActive(),
                saved.getFechaVencimiento()
        );
    }

    @Override
    public Optional<Task> findById(Long id) {
        return jpaRepository.findById(id)
                .map(e -> new Task(
                        e.getIdTask(),
                        e.getTitle(),
                        e.getDescription(),
                        com.sostecnible.TaskManager.domain.model.Task.Priority.valueOf(e.getPriority().name()),
                        e.getCreatedAt(),
                        e.getDueDate(),
                        e.getStatus(),
                        e.getIsActive(),
                        e.getFechaVencimiento()
                ));
    }

    @Override
    public List<Task> findAll() {
        return jpaRepository.findAll().stream()
                .map(e -> new Task(
                        e.getIdTask(),
                        e.getTitle(),
                        e.getDescription(),
                        com.sostecnible.TaskManager.domain.model.Task.Priority.valueOf(e.getPriority().name()),
                        e.getCreatedAt(),
                        e.getDueDate(),
                        e.getStatus(),
                        e.getIsActive(),
                        e.getFechaVencimiento()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }
}