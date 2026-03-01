package com.sostecnible.TaskManager.aplication.usecase;

import org.springframework.stereotype.Service;

import com.sostecnible.TaskManager.domain.repository.TaskRepository;

@Service
public class DeleteTaskUseCase {
    private final TaskRepository repository;

    public DeleteTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id) {
        repository.findById(id).ifPresent(task -> {
            task.setIsActive(0);  
            repository.save(task);
        });
    }
}