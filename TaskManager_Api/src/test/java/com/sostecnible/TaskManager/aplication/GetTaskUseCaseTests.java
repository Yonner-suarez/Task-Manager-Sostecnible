package com.sostecnible.TaskManager.aplication;

import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.GetTasksUseCase;
import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.domain.model.Task.Priority;
import com.sostecnible.TaskManager.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetTaskUseCaseTests {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private GetTasksUseCase getTasksUseCase;

    private Task task1, task2, task3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        task1 = new Task();
        task1.setIdTask(1L);
        task1.setTitle("Tarea Alta");
        task1.setPriority(Priority.ALTA);
        task1.setStatus("PENDIENTE");
        task1.setIsActive(1);

        task2 = new Task();
        task2.setIdTask(2L);
        task2.setTitle("Tarea Media");
        task2.setPriority(Priority.MEDIA);
        task2.setStatus("EN PROGRESO");
        task2.setIsActive(0);

        task3 = new Task();
        task3.setIdTask(3L);
        task3.setTitle("Tarea Baja");
        task3.setPriority(Priority.BAJA);
        task3.setStatus("COMPLETADA");
        task3.setIsActive(1);

        when(repository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));
    }

    @Test
    void testGetById_existingActiveTask_returnsTask() {
        when(repository.findById(1L)).thenReturn(Optional.of(task1));
        Optional<Task> result = getTasksUseCase.getById(1L);
        assertTrue(result.isPresent());
        assertEquals(task1.getIdTask(), result.get().getIdTask());
    }

    @Test
    void testGetById_inactiveTask_returnsEmpty() {
        when(repository.findById(2L)).thenReturn(Optional.of(task2));
        Optional<Task> result = getTasksUseCase.getById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetById_nonExistingTask_returnsEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        Optional<Task> result = getTasksUseCase.getById(99L);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetByFilter_priorityFilter_returnsOnlyMatchingPriority() {
        List<Task> result = getTasksUseCase.getByFilter("ALTA", null, null, null);
        assertEquals(1, result.size());
        assertEquals(task1.getIdTask(), result.get(0).getIdTask());
    }

    @Test
    void testGetByFilter_statusFilter_returnsOnlyMatchingStatus() {
        List<Task> result = getTasksUseCase.getByFilter(null, "COMPLETADA", null, null);
        assertEquals(1, result.size());
        assertEquals(task3.getIdTask(), result.get(0).getIdTask());
    }

    @Test
    void testGetByFilter_searchFilter_returnsOnlyMatchingTitle() {
        List<Task> result = getTasksUseCase.getByFilter(null, null, "baja", null);
        assertEquals(1, result.size());
        assertEquals(task3.getIdTask(), result.get(0).getIdTask());
    }

    @Test
    void testGetByFilter_onlyActiveTasks() {
        List<Task> result = getTasksUseCase.getByFilter(null, null, null, null);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(task -> task.getIsActive() == 1));
    }

    @Test
void testGetByFilter_sortByCreatedAt_soonestAndLatest() {
    // Asegúrate de que todas las tareas estén activas
    task1.setIsActive(1);
    task2.setIsActive(1);
    task3.setIsActive(1);

    task1.setCreatedAt(java.time.LocalDate.of(2026, 2, 25));
    task2.setCreatedAt(java.time.LocalDate.of(2026, 2, 26));
    task3.setCreatedAt(java.time.LocalDate.of(2026, 2, 27));

    when(repository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));

    List<Task> resultRecent = getTasksUseCase.getByFilter(null, null, null, "soonest");
    assertEquals(3, resultRecent.size());
    assertEquals(task3.getIdTask(), resultRecent.get(0).getIdTask());
    assertEquals(task2.getIdTask(), resultRecent.get(1).getIdTask());
    assertEquals(task1.getIdTask(), resultRecent.get(2).getIdTask());

    List<Task> resultOldest = getTasksUseCase.getByFilter(null, null, null, "latest");
    assertEquals(3, resultOldest.size());
    assertEquals(task1.getIdTask(), resultOldest.get(0).getIdTask());
    assertEquals(task2.getIdTask(), resultOldest.get(1).getIdTask());
    assertEquals(task3.getIdTask(), resultOldest.get(2).getIdTask());
}
}