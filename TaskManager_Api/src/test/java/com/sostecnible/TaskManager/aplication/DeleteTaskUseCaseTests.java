package com.sostecnible.TaskManager.aplication;

import static org.mockito.Mockito.*;

import com.sostecnible.TaskManager.aplication.usecase.DeleteTaskUseCase;
import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class DeleteTaskUseCaseTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private DeleteTaskUseCase deleteTaskUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute_existingTask_shouldSetInactiveAndSave() {
        // Arrange
        Task task = new Task();
        task.setIdTask(1L);
        task.setIsActive(1); // inicialmente activa

        when(repository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        deleteTaskUseCase.execute(1L);

        // Assert
        assert(task.getIsActive() == 0); // tarea marcada como inactiva
        verify(repository, times(1)).save(task); // repository.save llamado
    }

    @Test
    void testExecute_nonExistingTask_shouldNotCallSave() {
        // Arrange
        when(repository.findById(2L)).thenReturn(Optional.empty());

        // Act
        deleteTaskUseCase.execute(2L);

        // Assert
        verify(repository, never()).save(any()); // nunca se llama save
    }
}