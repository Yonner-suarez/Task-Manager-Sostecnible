package com.sostecnible.TaskManager.dominio;

import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.infraestructure.persistence.Task.TaskRepositoryImpl;
import com.sostecnible.TaskManager.infraestructure.persistence.User.UserEntity;
import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskRepositoryTests {

    @Autowired
    private TaskRepositoryImpl repository;

    @Autowired
    private EntityManager entityManager;

    private Task task;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setUserName("testuser_" + System.currentTimeMillis());
        userEntity.setEmail("test@sostecnible.com");
        userEntity.setPassword("hashed_password_123");

        entityManager.persist(userEntity);
        entityManager.flush(); 

        task = new Task();
        task.setUserId(userEntity.getIdUser()); 
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
        assertNotNull(savedTask.getIdTask());

        Optional<Task> foundTask = repository.findById(savedTask.getIdTask());
        assertTrue(foundTask.isPresent());
        assertEquals("Tarea Test", foundTask.get().getTitle());
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
        assertFalse(deleted.isPresent());
    }
}