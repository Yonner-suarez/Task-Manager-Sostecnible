package com.sostecnible.TaskManager.aplication.usecase;

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

  public List<Task> getByFilter(String priority, String status, String search) {
    return repository.findAll().stream()
        .filter(task -> task.getIsActive() != null && task.getIsActive() == 1)
        .filter(task -> priority == null || priority.isBlank() || task.getPriority().name().equalsIgnoreCase(priority))
        .filter(task -> status == null || status.isBlank() || task.getStatus().equalsIgnoreCase(status))
        .filter(
            task -> search == null || search.isBlank() || task.getTitle().toLowerCase().contains(search.toLowerCase()))
        .collect(Collectors.toList());
  }
}