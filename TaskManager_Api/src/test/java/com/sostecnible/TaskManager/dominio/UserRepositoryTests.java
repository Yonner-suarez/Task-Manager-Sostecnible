package com.sostecnible.TaskManager.dominio;

import com.sostecnible.TaskManager.infraestructure.persistence.User.UserEntity;
import com.sostecnible.TaskManager.domain.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testSaveUser_shouldReturnPersistedUser() {
        com.sostecnible.TaskManager.domain.model.User userDomain = new com.sostecnible.TaskManager.domain.model.User();
        userDomain.setUserName("tester_" + System.currentTimeMillis());
        userDomain.setEmail("test@sostecnible.com");
        userDomain.setPassword("password123");

        com.sostecnible.TaskManager.domain.model.User saved = userRepository.save(userDomain);

        assertNotNull(saved);
        assertEquals(userDomain.getUserName(), saved.getUserName());
    }

    @Test
    void testFindByUserNameOrEmail() {
        UserEntity entity = new UserEntity();
        entity.setUserName("findme");
        entity.setEmail("find@me.com");
        entity.setPassword("123");
        entityManager.persist(entity);
        entityManager.flush();

        com.sostecnible.TaskManager.domain.model.User found = userRepository.findByUserNameOrEmail("findme", null);

        assertNotNull(found);
        assertEquals("find@me.com", found.getEmail());
    }
}