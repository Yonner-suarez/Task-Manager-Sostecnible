package com.sostecnible.TaskManager.infraestructure.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException; 
import com.sostecnible.TaskManager.domain.exceptions.BusinessException;
import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.domain.repository.TaskRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final JpaTaskRepository jpaRepository;

    public TaskRepositoryImpl(JpaTaskRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Task save(Task task) {
        try {
            TaskEntity entity = new TaskEntity(
                    task.getIdTask(),
                    task.getTitle(),
                    task.getDescription(),
                    TaskEntity.Priority.valueOf(task.getPriority().name()),
                    task.getCreatedAt(),
                    task.getStatus(),
                    task.getIsActive(),
                    task.getFechaVencimiento()
            );
            
            TaskEntity saved = jpaRepository.save(entity);
            
            return mapToDomain(saved);
        } catch (DataAccessException e) {            
            throw new BusinessException("Error al guardar la tarea en la base de datos: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Error en los datos de la tarea (Prioridad inválida): " + e.getMessage());
        }
    }

    @Override
    public Optional<Task> findById(Long id) {
        try {
            return jpaRepository.findById(id).map(this::mapToDomain);
        } catch (DataAccessException e) {
            throw new BusinessException("Error al buscar la tarea con ID: " + id);
        }
    }

    @Override
    public List<Task> findAll() {
        try {
            return jpaRepository.findAll().stream()
                    .map(this::mapToDomain)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new BusinessException("Error al obtener la lista de tareas");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            if (!jpaRepository.existsById(id)) {
                throw new BusinessException("No se puede eliminar: La tarea no existe.");
            }
            jpaRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new BusinessException("Error técnico al intentar eliminar la tarea.");
        }
    }
    //Evita mapeo en func internas
    private Task mapToDomain(TaskEntity entity) {
    return new Task(
            entity.getIdTask(),
            entity.getTitle(),
            entity.getDescription(),
            com.sostecnible.TaskManager.domain.model.Task.Priority.valueOf(entity.getPriority().name()),
            entity.getCreatedAt(),
            entity.getStatus(),
            entity.getIsActive(),
            entity.getFechaVencimiento()
        );
    }
}