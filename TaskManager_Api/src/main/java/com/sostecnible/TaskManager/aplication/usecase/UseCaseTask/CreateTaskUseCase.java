package com.sostecnible.TaskManager.aplication.usecase.UseCaseTask;

import org.springframework.stereotype.Service;

import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.domain.repository.TaskRepository;

@Service
public class CreateTaskUseCase {
    private final TaskRepository repository;

    public CreateTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    public Task execute(Task task) {
        return repository.save(task);
    }
}