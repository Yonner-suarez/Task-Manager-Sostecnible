package com.sostecnible.TaskManager.aplication.UserUseCasesTest;

import com.sostecnible.TaskManager.aplication.usecase.UseCaseUser.CreateUserUseCase;
import com.sostecnible.TaskManager.domain.exceptions.BusinessException;
import com.sostecnible.TaskManager.domain.model.User;
import com.sostecnible.TaskManager.domain.ports.out.PasswordHasher;
import com.sostecnible.TaskManager.domain.ports.out.TokenService;
import com.sostecnible.TaskManager.domain.repository.UserRepository;
import com.sostecnible.TaskManager.infraestructure.controller.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserUseCaseTests {

    @Mock private UserRepository repository;
    @Mock private PasswordHasher passwordHasher;
    @Mock private TokenService tokenService;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        user = new User();
        user.setUserName("tester");
        user.setEmail("test@sostecnible.com");
        user.setPassword("plain_password");
    }

    @Test
    void testExecute_ShouldHashPasswordAndSaveUser() {
        String hashedPass = "hashed_12345";
        when(passwordHasher.hash("plain_password")).thenReturn(hashedPass);
        when(repository.save(any(User.class))).thenReturn(user);

        User result = createUserUseCase.execute(user);

        assertEquals(hashedPass, result.getPassword());
        verify(passwordHasher).hash("plain_password");
        verify(repository).save(user);
    }

    @Test
    void testLogin_Success_ShouldReturnToken() {
        LoginRequest request = new LoginRequest("tester", "test@sostecnible.com", "plain_password");
        user.setPassword("hashed_password_in_db");

        when(repository.findByUserNameOrEmail(request.username(), request.email())).thenReturn(user);
        when(passwordHasher.check("plain_password", "hashed_password_in_db")).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("jwt_token_xyz");

        String token = createUserUseCase.login(request);

        assertNotNull(token);
        assertEquals("jwt_token_xyz", token);
    }

    @Test
    void testLogin_WrongPassword_ShouldThrowException() {
        LoginRequest request = new LoginRequest("tester", null, "wrong_password");
        user.setPassword("hashed_password_in_db");

        when(repository.findByUserNameOrEmail(any(), any())).thenReturn(user);
        when(passwordHasher.check(eq("wrong_password"), anyString())).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            createUserUseCase.login(request);
        });

        assertEquals("Credenciales inválidas: contraseña incorrecta", exception.getMessage());
        verify(tokenService, never()).generateToken(any());
    }
}