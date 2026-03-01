package com.sostecnible.TaskManager.dominio;

import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.infraestructure.persistence.TaskRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional 
class TaskRepositoryTests {

    @Autowired
    private TaskRepositoryImpl repository;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle("Tarea Test");
        task.setDescription("Descripción Test");
        task.setPriority(Task.Priority.MEDIA);
        task.setStatus("PENDIENTE");
        task.setIsActive(1);
        
        task.setFechaVencimiento(LocalDate.now().plusDays(7));
        
        task.setCreatedAt(LocalDate.now());
        
    }

    @Test
    void testSaveAndFindById() {
        Task savedTask = repository.save(task);
        assertNotNull(savedTask.getIdTask(), "El ID no debería ser nulo tras guardar");

        Optional<Task> foundTask = repository.findById(savedTask.getIdTask());
        assertTrue(foundTask.isPresent());
        assertEquals("Tarea Test", foundTask.get().getTitle());
    }

    @Test
    void testFindAll() {
        repository.save(task);
        List<Task> tasks = repository.findAll();
        assertFalse(tasks.isEmpty(), "La lista de tareas no debería estar vacía");
    }

    @Test
    void testUpdate() {
        Task saved = repository.save(task);
        saved.setTitle("Tarea Actualizada");

        Task updated = repository.save(saved);
        assertEquals("Tarea Actualizada", updated.getTitle());
    }

    @Test
    void testDelete() {
        Task saved = repository.save(task);
        Long id = saved.getIdTask();

        repository.delete(id);
        Optional<Task> deleted = repository.findById(id);
        assertFalse(deleted.isPresent(), "La tarea debería haber sido eliminada");
    }
}