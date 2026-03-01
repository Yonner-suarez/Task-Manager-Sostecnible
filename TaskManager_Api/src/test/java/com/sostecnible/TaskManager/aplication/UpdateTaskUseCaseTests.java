package com.sostecnible.TaskManager.aplication;

import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.UpdateTaskUseCase;
import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.infraestructure.persistence.Task.TaskRepositoryImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateTaskUseCaseTests {

    @Mock
    private TaskRepositoryImpl repository;

    @InjectMocks
    private UpdateTaskUseCase updateTaskUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute_existingTask_shouldUpdateAndReturnTask() {
        // Arrange
        Long id = 1L;
        Task existingTask = new Task();
        existingTask.setIdTask(id);
        existingTask.setTitle("Original");

        Task updatedTask = new Task();
        updatedTask.setTitle("Actualizada");

        when(repository.findById(id)).thenReturn(Optional.of(existingTask));
        when(repository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<Task> result = updateTaskUseCase.execute(id, updatedTask);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getIdTask());
        assertEquals("Actualizada", result.get().getTitle());

        verify(repository, times(1)).save(updatedTask);
    }

    @Test
    void testExecute_nonExistingTask_shouldReturnEmpty() {
        // Arrange
        Long id = 2L;
        Task updatedTask = new Task();
        updatedTask.setTitle("Nueva");

        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Task> result = updateTaskUseCase.execute(id, updatedTask);

        // Assert
        assertFalse(result.isPresent());
        verify(repository, never()).save(any(Task.class));
    }
}