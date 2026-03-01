package com.sostecnible.TaskManager.infraestructure.ControllersTest;

import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.domain.ports.out.TokenService;
import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.*;
import com.sostecnible.TaskManager.infraestructure.controller.TaskController;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock private CreateTaskUseCase createTaskUseCase;
    @Mock private UpdateTaskUseCase updateTaskUseCase;
    @Mock private DeleteTaskUseCase deleteTaskUseCase;
    @Mock private GetTasksUseCase getTasksUseCase;
    @Mock private TokenService tokenService;

    @InjectMocks
    private TaskController taskController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private final String DUMMY_TOKEN = "Bearer token.test.123";
    private final Long USER_ID = 100L;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        task = new Task();
        task.setIdTask(1L);
        task.setUserId(USER_ID);
        task.setTitle("Tarea Test");
        task.setDescription("Descripción de prueba");
        task.setStatus("PENDIENTE");
        task.setPriority(Task.Priority.MEDIA);
        task.setIsActive(1);

        when(tokenService.extractUserId(anyString())).thenReturn(USER_ID);
        
        try {
            if (tokenService.getClass().getMethod("validateToken", String.class) != null) {
                when(tokenService.validateToken(anyString())).thenReturn(true);
            }
        } catch (NoSuchMethodException ignored) {}
    }

    @Test
    void testGetAll() throws Exception {
        when(getTasksUseCase.getByFilter(eq(USER_ID), any(), any(), any(), any()))
                .thenReturn(List.of(task));

        mockMvc.perform(get("/tasks")
                .header("Authorization", DUMMY_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].idTask").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Tarea Test"));

        verify(getTasksUseCase).getByFilter(eq(USER_ID), any(), any(), any(), any());
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(deleteTaskUseCase).execute(1L, USER_ID);

        mockMvc.perform(delete("/tasks/1")
                .header("Authorization", DUMMY_TOKEN))
                .andExpect(status().isOk());

        verify(deleteTaskUseCase, times(1)).execute(1L, USER_ID);
    }

    @Test
    void testCreateTask() throws Exception {
        when(createTaskUseCase.execute(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/tasks")
                .header("Authorization", DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated()) // <-- Cambia isOk() por isCreated()
                .andExpect(jsonPath("$.data.title").value("Tarea Test"));

        verify(createTaskUseCase).execute(any(Task.class));
    }

    @Test
    void testUpdateTask() throws Exception {
        when(updateTaskUseCase.execute(eq(1L), any(Task.class)))
                .thenReturn(Optional.of(task));

        mockMvc.perform(put("/tasks/1")
                .header("Authorization", DUMMY_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.idTask").value(1));

        verify(updateTaskUseCase).execute(eq(1L), any(Task.class));
    }
}