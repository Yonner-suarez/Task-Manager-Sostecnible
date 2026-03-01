package com.sostecnible.TaskManager.aplication.usecase.UseCaseUser;

import org.springframework.stereotype.Service;

import com.sostecnible.TaskManager.domain.exceptions.BusinessException;
import com.sostecnible.TaskManager.domain.model.User;
import com.sostecnible.TaskManager.domain.ports.out.PasswordHasher;
import com.sostecnible.TaskManager.domain.ports.out.TokenService;
import com.sostecnible.TaskManager.domain.repository.UserRepository;
import com.sostecnible.TaskManager.infraestructure.controller.request.LoginRequest;

@Service
public class CreateUserUseCase {
    private final UserRepository repository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;

    public CreateUserUseCase(UserRepository repository, PasswordHasher passwordHasher, TokenService tokenService) {
      this.repository = repository;
      this.passwordHasher = passwordHasher;
      this.tokenService = tokenService;
    }

    public User execute(User user) {
      //hasehar paswword para DB
      String hashed = passwordHasher.hash(user.getPassword());
      user.setPassword(hashed);
      return  repository.save(user);
    }
    
    public String login(LoginRequest request) {
      User user = repository.findByUserNameOrEmail(request.username(), request.email());
      if (!passwordHasher.check(request.password(), user.getPassword())) {
          throw new BusinessException("Credenciales inválidas: contraseña incorrecta");
      }
      return tokenService.generateToken(user);
    }
}