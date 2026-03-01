package com.sostecnible.TaskManager.infraestructure.controller;

import org.springframework.web.bind.annotation.*;

import com.sostecnible.TaskManager.aplication.usecase.CreateTaskUseCase;
import com.sostecnible.TaskManager.aplication.usecase.DeleteTaskUseCase;
import com.sostecnible.TaskManager.aplication.usecase.GetTasksUseCase;
import com.sostecnible.TaskManager.aplication.usecase.UpdateTaskUseCase;
import com.sostecnible.TaskManager.domain.model.Task;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:5173") 
public class TaskController {

    private final CreateTaskUseCase createTask;
    private final UpdateTaskUseCase updateTask;
    private final DeleteTaskUseCase deleteTask;
    private final GetTasksUseCase getTasks;

    public TaskController(CreateTaskUseCase createTask, UpdateTaskUseCase updateTask,
                          DeleteTaskUseCase deleteTask, GetTasksUseCase getTasks) {
        this.createTask = createTask;
        this.updateTask = updateTask;
        this.deleteTask = deleteTask;
        this.getTasks = getTasks;
    }

    @PostMapping
    public Task create(@Valid @RequestBody Task task) {
      return createTask.execute(task);
    }
    
    @GetMapping("/{id}")
    public Optional<Task> getById(@PathVariable Long id) {
      return getTasks.getById(id);
    }
    
    @GetMapping
      public List<Task> getAll(
              @RequestParam(required = false) String priority,
              @RequestParam(required = false) String status,
              @RequestParam(required = false) String search,
              @RequestParam(required = false) String sortBy
      ) {
        return getTasks.getByFilter(priority, status, search, sortBy);
      }

    @PutMapping("/{id}")
    public Optional<Task> update(@Valid @PathVariable Long id,@Valid @RequestBody Task task) {
        return updateTask.execute(id, task);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deleteTask.execute(id);
    }
}