package com.sostecnible.TaskManager.infraestructure.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sostecnible.TaskManager.aplication.usecase.UseCaseUser.CreateUserUseCase;
import com.sostecnible.TaskManager.domain.model.User;
import com.sostecnible.TaskManager.infraestructure.controller.request.LoginRequest;
import com.sostecnible.TaskManager.infraestructure.controller.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final CreateUserUseCase createUser;
    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUser = createUserUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@Valid @RequestBody User user) {
        createUser.execute(user);

        ApiResponse<Void> response = new ApiResponse<>(
            HttpStatus.CREATED.value(),
            "Usuario creado exitosamente",
            null,
            System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> getById(@Valid @RequestBody LoginRequest request) {
      String token = createUser.login(request);

      ApiResponse<String> response = new ApiResponse<>(
        HttpStatus.CREATED.value(),
        "Usuario creado exitosamente",
        token,
        System.currentTimeMillis()
    );
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}