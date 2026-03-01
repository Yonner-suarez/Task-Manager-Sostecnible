package com.sostecnible.TaskManager.aplication.TaskUseCasesTests;
import com.sostecnible.TaskManager.aplication.usecase.UseCaseTask.GetTasksUseCase;
import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.domain.model.Task.Priority;
import com.sostecnible.TaskManager.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetTaskUseCaseTests {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private GetTasksUseCase getTasksUseCase;

    private Task task1, task2, task3;
    private final Long OWNER_ID = 100L;
    private final Long STRANGER_ID = 999L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        task1 = new Task();
        task1.setIdTask(1L);
        task1.setUserId(OWNER_ID);
        task1.setTitle("Tarea Alta");
        task1.setPriority(Priority.ALTA);
        task1.setStatus("PENDIENTE");
        task1.setIsActive(1);

        task2 = new Task();
        task2.setIdTask(2L);
        task2.setUserId(OWNER_ID);
        task2.setTitle("Tarea Media");
        task2.setPriority(Priority.MEDIA);
        task2.setStatus("EN PROGRESO");
        task2.setIsActive(0);

        task3 = new Task();
        task3.setIdTask(3L);
        task3.setUserId(STRANGER_ID);
        task3.setTitle("Tarea Ajena");
        task3.setPriority(Priority.BAJA);
        task3.setStatus("COMPLETADA");
        task3.setIsActive(1);

        when(repository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));
    }

    @Test
    void testGetByFilter_security_onlyReturnsOwnerTasks() {
        List<Task> result = getTasksUseCase.getByFilter(OWNER_ID, null, null, null, null);

        assertEquals(1, result.size());
        assertEquals(OWNER_ID, result.get(0).getUserId());
        assertEquals(task1.getIdTask(), result.get(0).getIdTask());
    }

    @Test
    void testGetByFilter_priorityFilter_matchingUserAndPriority() {
        List<Task> result = getTasksUseCase.getByFilter(OWNER_ID, "ALTA", null, null, null);
        assertEquals(1, result.size());
        assertEquals(task1.getIdTask(), result.get(0).getIdTask());
    }

    @Test
    void testGetByFilter_searchFilter_noResultsForDifferentUser() {
        List<Task> result = getTasksUseCase.getByFilter(OWNER_ID, null, null, "Ajena", null);
        
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetByFilter_onlyActiveTasksForUser() {
        task3.setUserId(OWNER_ID);
        task3.setIsActive(0);

        List<Task> result = getTasksUseCase.getByFilter(OWNER_ID, null, null, null, null);
        
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(t -> t.getIsActive() == 1 && t.getUserId().equals(OWNER_ID)));
    }

    @Test
    void testGetByFilter_sortByCreatedAt_logic() {
        task1.setIsActive(1); task1.setUserId(OWNER_ID);
        task2.setIsActive(1); task2.setUserId(OWNER_ID);
        task3.setIsActive(1); task3.setUserId(OWNER_ID);

        task1.setCreatedAt(LocalDate.of(2026, 2, 25));
        task2.setCreatedAt(LocalDate.of(2026, 2, 26));
        task3.setCreatedAt(LocalDate.of(2026, 2, 27));

        when(repository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));

        List<Task> resultRecent = getTasksUseCase.getByFilter(OWNER_ID, null, null, null, "soonest");
        assertEquals(task3.getIdTask(), resultRecent.get(0).getIdTask());

        List<Task> resultOldest = getTasksUseCase.getByFilter(OWNER_ID, null, null, null, "latest");
        assertEquals(task1.getIdTask(), resultOldest.get(0).getIdTask());
    }
}