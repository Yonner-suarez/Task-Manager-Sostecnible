package com.sostecnible.TaskManager.infraestructure.ControllersTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sostecnible.TaskManager.aplication.usecase.UseCaseUser.CreateUserUseCase;
import com.sostecnible.TaskManager.domain.model.User;
import com.sostecnible.TaskManager.infraestructure.controller.UserController;
import com.sostecnible.TaskManager.infraestructure.controller.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private User user;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user = new User();
        user.setUserName("tester");
        user.setEmail("test@sostecnible.com");
        user.setPassword("secret123");

        loginRequest = new LoginRequest("tester", "test@sostecnible.com", "secret123");
    }

   @Test
    void testCreateUser() throws Exception {
        when(createUserUseCase.execute(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Usuario creado exitosamente"))
                .andExpect(jsonPath("$.status").value(201));

        verify(createUserUseCase, times(1)).execute(any(User.class));
    }

    @Test
    void testLogin() throws Exception {
        String dummyToken = "jwt.token.mocked";
        when(createUserUseCase.login(any(LoginRequest.class))).thenReturn(dummyToken);

        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(dummyToken))
                .andExpect(jsonPath("$.message").value("Usuario creado exitosamente")); 

        verify(createUserUseCase, times(1)).login(any(LoginRequest.class));
    }
}