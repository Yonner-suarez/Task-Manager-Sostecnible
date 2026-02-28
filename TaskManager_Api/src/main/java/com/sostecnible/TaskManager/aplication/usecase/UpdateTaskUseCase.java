package com.sostecnible.taskmanager.application.usecase;

import com.sostecnible.taskmanager.domain.model.Task;
import com.sostecnible.taskmanager.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UpdateTaskUseCase {
    private final TaskRepository repository;

    public UpdateTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    public Optional<Task> execute(Long id, Task updatedTask) {
        return repository.findById(id).map(task -> {
            updatedTask.setIdTask(id);
            return repository.save(updatedTask);
        });
    }
} 