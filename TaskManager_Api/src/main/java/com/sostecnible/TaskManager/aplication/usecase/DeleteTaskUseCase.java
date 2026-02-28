package com.sostecnible.taskmanager.application.usecase;

import com.sostecnible.taskmanager.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteTaskUseCase {
    private final TaskRepository repository;

    public DeleteTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id) {
        repository.delete(id);
    }
}