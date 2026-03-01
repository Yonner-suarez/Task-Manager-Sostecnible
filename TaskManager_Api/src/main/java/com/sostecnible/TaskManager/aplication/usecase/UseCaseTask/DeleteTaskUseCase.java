package com.sostecnible.TaskManager.aplication.usecase.UseCaseTask;

import org.springframework.stereotype.Service;

import com.sostecnible.TaskManager.domain.exceptions.BusinessException;
import com.sostecnible.TaskManager.domain.repository.TaskRepository;

@Service
public class DeleteTaskUseCase {
    private final TaskRepository repository;

    public DeleteTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id, Long userId) {
        repository.findById(id).ifPresent(task -> {
            if (task.getUserId() != null && task.getUserId().equals(userId)) {
                task.setIsActive(0);
                repository.save(task);
            } else {
                throw new BusinessException("No tienes permiso para eliminar esta tarea.");
            }
        });
    }
}