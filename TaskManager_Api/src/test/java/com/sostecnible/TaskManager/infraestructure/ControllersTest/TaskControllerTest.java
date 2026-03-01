package com.sostecnible.TaskManager.infraestructure.ControllersTest;

import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.CreateTaskUseCase;
import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.DeleteTaskUseCase;
import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.GetTasksUseCase;
import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.UpdateTaskUseCase;
import com.sostecnible.TaskManager.infraestructure.controller.TaskController;

import tools.jackson.databind.ObjectMapper;

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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateTaskUseCase createTaskUseCase;

    @Mock
    private UpdateTaskUseCase updateTaskUseCase;

    @Mock
    private DeleteTaskUseCase deleteTaskUseCase;

    @Mock
    private GetTasksUseCase getTasksUseCase;

    @InjectMocks
    private TaskController taskController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        task = new Task();
        task.setIdTask(1L);
        task.setTitle("Tarea Test");
        task.setDescription("Descripción Test");
    }

    @Test
    void testCreateTask() throws Exception {
        when(createTaskUseCase.execute(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTask").value(1))
                .andExpect(jsonPath("$.title").value("Tarea Test"));

        verify(createTaskUseCase, times(1)).execute(any(Task.class));
    }

    @Test
    void testGetById() throws Exception {
        when(getTasksUseCase.getById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTask").value(1))
                .andExpect(jsonPath("$.title").value("Tarea Test"));

        verify(getTasksUseCase, times(1)).getById(1L);
    }

    @Test
    void testGetAll() throws Exception {
        when(getTasksUseCase.getByFilter(null, null, null, null)).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTask").value(1))
                .andExpect(jsonPath("$[0].title").value("Tarea Test"));

        verify(getTasksUseCase, times(1)).getByFilter(null, null, null, null);
    }

    @Test
    void testUpdateTask() throws Exception {
        when(updateTaskUseCase.execute(eq(1L), any(Task.class))).thenReturn(Optional.of(task));

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTask").value(1))
                .andExpect(jsonPath("$.title").value("Tarea Test"));

        verify(updateTaskUseCase, times(1)).execute(eq(1L), any(Task.class));
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(deleteTaskUseCase).execute(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());

        verify(deleteTaskUseCase, times(1)).execute(1L);
    }
}