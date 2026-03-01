package com.sostecnible.TaskManager.aplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.CreateTaskUseCase;
import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreateTaskUseCaseTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private CreateTaskUseCase createTaskUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa @Mock y @InjectMocks
    }

    @Test
    void testExecute_shouldSaveTaskAndReturnIt() {
        // Arrange: crear tarea de prueba
        Task task = new Task();
        task.setTitle("Prueba");
        task.setDescription("Descripción de prueba");

        Task savedTask = new Task();
        savedTask.setIdTask(1L);
        savedTask.setTitle(task.getTitle());
        savedTask.setDescription(task.getDescription());

        when(repository.save(task)).thenReturn(savedTask);

        Task result = createTaskUseCase.execute(task);

        assertEquals(savedTask.getIdTask(), result.getIdTask());
        assertEquals(savedTask.getTitle(), result.getTitle());
        assertEquals(savedTask.getDescription(), result.getDescription());

        verify(repository, times(1)).save(task);
    }
}