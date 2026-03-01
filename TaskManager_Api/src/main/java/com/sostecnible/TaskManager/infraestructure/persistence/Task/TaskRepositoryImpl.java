package com.sostecnible.TaskManager.infraestructure.persistence.Task;

import org.springframework.stereotype.Repository;
import org.springframework.dao.DataAccessException; 
import com.sostecnible.TaskManager.domain.exceptions.BusinessException;
import com.sostecnible.TaskManager.domain.model.Task;
import com.sostecnible.TaskManager.domain.repository.TaskRepository;
import com.sostecnible.TaskManager.infraestructure.persistence.User.UserEntity;

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
            TaskEntity entity = new TaskEntity();
        
            // Mapeo manual de campos
            entity.setIdTask(task.getIdTask());
            entity.setTitle(task.getTitle());
            entity.setDescription(task.getDescription());
            entity.setPriority(TaskEntity.Priority.valueOf(task.getPriority().name()));
            entity.setCreatedAt(task.getCreatedAt());
            entity.setStatus(task.getStatus());
            entity.setIsActive(task.getIsActive());
            entity.setFechaVencimiento(task.getFechaVencimiento());

            if (task.getUserId() != null) {
                UserEntity userProxy = new UserEntity();
                userProxy.setIdUser(task.getUserId());
                entity.setUser(userProxy); 
            }
            
            TaskEntity saved = jpaRepository.save(entity);
            
            return mapToDomain(saved);
        } catch (DataAccessException e) {
            throw new BusinessException("Error al guardar la tarea en la base de datos");
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Error en los datos de la tarea (Prioridad inválida)");
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
                entity.getFechaVencimiento(),
                entity.getUser() != null ? entity.getUser().getIdUser() : null
        );
    }
}