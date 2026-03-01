package com.sostecnible.TaskManager.aplication.usecase;

import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.infraestructure.persistence.TaskRepositoryImpl;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UpdateTaskUseCase {
    private final TaskRepositoryImpl repository;

    public UpdateTaskUseCase(TaskRepositoryImpl repository) {
        this.repository = repository;
    }

    public Optional<Task> execute(Long id, Task updatedTask) {
        return repository.findById(id).map(task -> {
            updatedTask.setIdTask(id);
            return repository.save(updatedTask);
        });
    }
} 