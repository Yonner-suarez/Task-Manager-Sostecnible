package com.sostecnible.taskmanager.infrastructure.controller;

import com.sostecnible.taskmanager.application.usecase.*;
import com.sostecnible.taskmanager.domain.model.Task;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
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
    public Task create(@RequestBody Task task) {
        return createTask.execute(task);
    }

    @GetMapping
    public List<Task> getAll() {
        return getTasks.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Task> getById(@PathVariable Long id) {
        return getTasks.getById(id);
    }

    @PutMapping("/{id}")
    public Optional<Task> update(@PathVariable Long id, @RequestBody Task task) {
        return updateTask.execute(id, task);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        deleteTask.execute(id);
    }
}