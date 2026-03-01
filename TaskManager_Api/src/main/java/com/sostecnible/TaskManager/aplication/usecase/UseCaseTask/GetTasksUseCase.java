package com.sostecnible.TaskManager.aplication.usecase.UseCaseTask;

import org.springframework.stereotype.Service;

import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.domain.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetTasksUseCase {
    private final TaskRepository repository;

    public GetTasksUseCase(TaskRepository repository) {
      this.repository = repository;
    }

    public Optional<Task> getById(Long id) {
      return repository.findById(id)
          .filter(task -> task.getIsActive() != null && task.getIsActive() == 1);
    }

    public List<Task> getByFilter(Long userId, String priority, String status, String search, String sortBy) {
      List<Task> tasks = repository.findAll().stream()
          .filter(task -> task.getUserId() != null && task.getUserId().equals(userId))
          
          .filter(task -> task.getIsActive() != null && task.getIsActive() == 1)
          
          .filter(task -> priority == null || priority.isBlank() || 
                  task.getPriority().name().equalsIgnoreCase(priority))
          
          .filter(task -> status == null || status.isBlank() || 
                  task.getStatus().equalsIgnoreCase(status))
          
          .filter(task -> search == null || search.isBlank() || 
                  task.getTitle().toLowerCase().contains(search.toLowerCase()))
          .collect(Collectors.toList());

      sortTask(tasks, sortBy);

      return tasks;
    }

    private void sortTask(List<Task> tasks, String sortBy) {
        if (sortBy == null || sortBy.isBlank())
            return;

        switch (sortBy) {
            case "soonest": 
                tasks.sort((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));
                break;
            case "latest": 
                tasks.sort((t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()));
                break;
            default:
                // No hacer nada
                break;
        }
    }
}