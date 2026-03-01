package com.sostecnible.TaskManager.infraestructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.CreateTaskUseCase;
import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.DeleteTaskUseCase;
import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.GetTasksUseCase;
import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.UpdateTaskUseCase;
import com.sostecnible.TaskManager.domain.exceptions.BusinessException;
import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.domain.ports.out.TokenService;
import com.sostecnible.TaskManager.infraestructure.controller.response.ApiResponse;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final CreateTaskUseCase createTask;
    private final UpdateTaskUseCase updateTask;
    private final DeleteTaskUseCase deleteTask;
    private final GetTasksUseCase getTasks;
    private final TokenService tokenService;

    public TaskController(CreateTaskUseCase createTask, UpdateTaskUseCase updateTask,
                          DeleteTaskUseCase deleteTask, GetTasksUseCase getTasks, TokenService tokenService) {
        this.createTask = createTask;
        this.updateTask = updateTask;
        this.deleteTask = deleteTask;
        this.getTasks = getTasks;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Task>> create(
        @RequestHeader("Authorization") String authHeader, 
        @Valid @RequestBody Task task
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException("Token de autorización no proporcionado");
        }

        String token = authHeader.substring(7); 

        if (!tokenService.validateToken(token)) {
            throw new BusinessException("Token inválido o expirado");
        }

        Long userId = tokenService.extractUserId(token);
        task.setUserId(userId); 

        Task taskCreated = createTask.execute(task);

        ApiResponse<Task> response = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            "Tarea creada exitosamente",
            taskCreated,
            System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> getById(@PathVariable Long id) {
        return getTasks.getById(id)
            .map(task -> {
                ApiResponse<Task> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Tarea encontrada",
                    task,
                    System.currentTimeMillis()
                );
                return new ResponseEntity<>(response, HttpStatus.OK);
            })
            .orElseGet(() -> {
                ApiResponse<Task> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "Tarea no encontrada",
                    null,
                    System.currentTimeMillis()
                );
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            });
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Task>>> getAll(
        @RequestHeader("Authorization") String authHeader, 
        @RequestParam(required = false) String priority,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String sortBy
    ) {
        String token = authHeader.substring(7);
        Long userId = tokenService.extractUserId(token);

        List<Task> tasks = getTasks.getByFilter(userId, priority, status, search, sortBy);

        ApiResponse<List<Task>> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            "Lista de tareas obtenida",
            tasks,
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> update(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id, 
        @Valid @RequestBody Task task
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException("Token de autorización no proporcionado");
        }

        String token = authHeader.substring(7);
        if (!tokenService.validateToken(token)) {
            throw new BusinessException("Token inválido o expirado");
        }

        Long userId = tokenService.extractUserId(token);
        
        task.setUserId(userId);

        return updateTask.execute(id, task)
            .map(updatedTask -> {
                ApiResponse<Task> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Tarea actualizada exitosamente",
                    updatedTask,
                    System.currentTimeMillis()
                );
                return new ResponseEntity<>(response, HttpStatus.OK);
            })
            .orElseGet(() -> {
                ApiResponse<Task> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "No se pudo actualizar: Tarea no encontrada o no pertenece al usuario",
                    null,
                    System.currentTimeMillis()
                );
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long id
    ) {
        String token = authHeader.substring(7);
        Long userId = tokenService.extractUserId(token);

        deleteTask.execute(id, userId);

        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            "Tarea eliminada exitosamente",
            null,
            System.currentTimeMillis()
        );
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}