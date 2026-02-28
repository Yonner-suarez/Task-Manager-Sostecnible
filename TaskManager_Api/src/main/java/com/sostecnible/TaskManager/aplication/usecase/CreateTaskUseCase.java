package com.sostecnible.taskmanager.application.usecase;

import com.sostecnible.taskmanager.domain.model.Task;
import com.sostecnible.taskmanager.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;

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