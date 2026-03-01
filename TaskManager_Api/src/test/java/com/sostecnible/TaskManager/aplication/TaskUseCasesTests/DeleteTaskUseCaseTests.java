package com.sostecnible.TaskManager.aplication.TaskUseCasesTests;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.DeleteTaskUseCase;
import com.sostecnible.TaskManager.domain.exceptions.BusinessException;
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
    void testExecute_existingTaskAndCorrectUser_shouldSetInactiveAndSave() {
        Long taskId = 1L;
        Long userId = 100L;
        
        Task task = new Task();
        task.setIdTask(taskId);
        task.setUserId(userId); 
        task.setIsActive(1);

        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        deleteTaskUseCase.execute(taskId, userId);

        assertEquals(0, task.getIsActive());
        verify(repository, times(1)).save(task);
    }

    @Test
    void testExecute_wrongUser_shouldThrowBusinessException() {
        Long taskId = 1L;
        Long ownerId = 100L;
        Long attackerId = 999L;
        
        Task task = new Task();
        task.setIdTask(taskId);
        task.setUserId(ownerId);
        task.setIsActive(1);

        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        assertThrows(BusinessException.class, () -> {
            deleteTaskUseCase.execute(taskId, attackerId);
        });
        
        verify(repository, never()).save(any(Task.class));
    }

    @Test
    void testExecute_nonExistingTask_shouldNotCallSave() {
        Long taskId = 2L;
        Long userId = 100L;
        when(repository.findById(taskId)).thenReturn(Optional.empty());

        deleteTaskUseCase.execute(taskId, userId);

        verify(repository, never()).save(any());
    }
}