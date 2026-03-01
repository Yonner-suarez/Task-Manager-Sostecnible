package com.sostecnible.TaskManager.infraestructure.persistence;

import com.sostecnible.TaskManager.domain.model.User;
import com.sostecnible.TaskManager.infraestructure.persistence.User.JpaUserRepository;
import com.sostecnible.TaskManager.infraestructure.persistence.User.UserEntity;
import com.sostecnible.TaskManager.infraestructure.persistence.User.UserRespositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserRepositoryImplUnitTest {

    private JpaUserRepository jpaUserRepository;
    private UserRespositoryImpl repository;

    @BeforeEach
    void setUp() {
        jpaUserRepository = mock(JpaUserRepository.class);
        repository = new UserRespositoryImpl(jpaUserRepository);
    }

    @Test
    void testSave() {
        User user = new User();
        user.setUserName("jsmith");
        user.setEmail("john@example.com");
        user.setPassword("hashed_pass");
        user.setIsActive(true);

        UserEntity savedEntity = new UserEntity();
        savedEntity.setIdUser(1L);
        savedEntity.setUserName("jsmith");
        savedEntity.setEmail("john@example.com");
        savedEntity.setPassword("hashed_pass");
        savedEntity.setCreatedAt(LocalDateTime.now());
        savedEntity.setUpdatedAt(LocalDateTime.now());
        savedEntity.setIsActive(true);

        when(jpaUserRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

        User result = repository.save(user);

        //Verificaciones
        assertNotNull(result);
        assertEquals(1L, result.getIdUser());
        assertEquals("jsmith", result.getUserName());
        assertEquals("john@example.com", result.getEmail());
        assertTrue(result.getIsActive());

        verify(jpaUserRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testFindByUserNameOrEmail_Success() {
        UserEntity entity = new UserEntity();
        entity.setIdUser(1L);
        entity.setUserName("jsmith");
        entity.setEmail("john@example.com");
        entity.setIsActive(true);

        when(jpaUserRepository.findByUserNameOrEmail("jsmith", null))
                .thenReturn(Optional.of(entity));

        User result = repository.findByUserNameOrEmail("jsmith", null);

        assertNotNull(result);
        assertEquals("jsmith", result.getUserName());
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void testFindByUserNameOrEmail_NotFound_ShouldThrowException() {
        when(jpaUserRepository.findByUserNameOrEmail(anyString(), any()))
                .thenReturn(Optional.empty());

        assertThrows(com.sostecnible.TaskManager.domain.exceptions.BusinessException.class, () -> {
            repository.findByUserNameOrEmail("nonexistent", "nonexistent@mail.com");
        });
    }
}