package com.sostecnible.TaskManager.infraestructure.persistence;

import com.sostecnible.TaskManager.domain.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskRepositoryImplUnitTest {

    private JpaTaskRepository jpaRepository;
    private TaskRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(JpaTaskRepository.class);
        repository = new TaskRepositoryImpl(jpaRepository);
    }

    @Test
    void testSave() {
        // Preparar el Task de dominio
        Task task = new Task();
        task.setTitle("Test Task");
        task.setPriority(Task.Priority.MEDIA);
        task.setStatus("PENDIENTE");
        task.setIsActive(1);

        TaskEntity savedEntity = new TaskEntity();
        savedEntity.setIdTask(1L);
        savedEntity.setTitle("Test Task");
        savedEntity.setPriority(TaskEntity.Priority.MEDIA);
        savedEntity.setStatus("PENDIENTE");
        savedEntity.setIsActive(1);

        // Configurar el mock
        when(jpaRepository.save(any(TaskEntity.class))).thenReturn(savedEntity);

        // Ejecutar
        Task result = repository.save(task);

        // Verificaciones
        assertNotNull(result.getIdTask());
        assertEquals("Test Task", result.getTitle());
        assertEquals(Task.Priority.MEDIA, result.getPriority());
        assertEquals("PENDIENTE", result.getStatus());
        assertEquals(1,result.getIsActive());

        verify(jpaRepository, times(1)).save(any(TaskEntity.class));
    }

    @Test
    void testFindById() {
        TaskEntity entity = new TaskEntity();
        entity.setIdTask(1L);
        entity.setTitle("Test Task");
        entity.setPriority(TaskEntity.Priority.MEDIA);
        entity.setStatus("PENDIENTE");
        entity.setIsActive(1);

        when(jpaRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Task> result = repository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getIdTask());
        assertEquals("Test Task", result.get().getTitle());
    }

    @Test
    void testFindAll() {
        TaskEntity e1 = new TaskEntity();
        e1.setIdTask(1L);
        e1.setTitle("Task 1");
        e1.setPriority(TaskEntity.Priority.ALTA);

        TaskEntity e2 = new TaskEntity();
        e2.setIdTask(2L);
        e2.setTitle("Task 2");
        e2.setPriority(TaskEntity.Priority.MEDIA);

        when(jpaRepository.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<Task> result = repository.findAll();

        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());
    }

    @Test
    void testDelete() {
        
        when(jpaRepository.existsById(1L)).thenReturn(true);
        
        doNothing().when(jpaRepository).deleteById(1L);

        repository.delete(1L);

        verify(jpaRepository, times(1)).existsById(1L);
        verify(jpaRepository, times(1)).deleteById(1L);
    }
}