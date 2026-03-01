package com.sostecnible.TaskManager.infraestructure.persistence.User;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.sostecnible.TaskManager.domain.exceptions.BusinessException;
import com.sostecnible.TaskManager.domain.model.User;
import com.sostecnible.TaskManager.domain.repository.UserRepository;

@Repository
public class UserRespositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public UserRespositoryImpl(JpaUserRepository _jpaUserRepository) {
        this.jpaUserRepository = _jpaUserRepository;
    }

    @Override
    public User save(User user) {
        try {
            UserEntity entity = new UserEntity(
              user.getIdUser(),
              user.getUserName(),
              user.getEmail(),
              user.getPassword(),
              user.getCreatedAt(),
              user.getUpdatedAt(),
              user.getIsActive()                 
            );
            
            UserEntity saved = jpaUserRepository.save(entity);
            
            return mapToDomain(saved);
        } catch (DataAccessException e) {            
            throw new BusinessException("Error al guardar el usuario en la base de datos");
        }
    }

    @Override
    public User findByUserNameOrEmail(String userName, String email) {
        try {
            return jpaUserRepository.findByUserNameOrEmail(userName, email)
                    .map(this::mapToDomain)
                    .orElseThrow(() -> new BusinessException("Usuario no encontrado con las credenciales proporcionadas"));
        } catch (DataAccessException e) {
            throw new BusinessException("Error de conexión al buscar usuario por nombre o email");
        }
    }
    
    //Evita mapeo en func internas
    private User mapToDomain(UserEntity entity) {
      return new User(
        entity.getIdUser(),
        entity.getUserName(),
        entity.getEmail(),
        entity.getPassword(),
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.getIsActive()
        );
    }
}