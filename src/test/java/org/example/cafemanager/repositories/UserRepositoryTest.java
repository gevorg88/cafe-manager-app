package org.example.cafemanager.repositories;

import org.example.cafemanager.EntitiesBuilder;
import org.example.cafemanager.Util;
import org.example.cafemanager.domain.User;
import org.example.cafemanager.domain.enums.Role;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

public class UserRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test(expected = ConstraintViolationException.class)
    public void persistUserWithoutPassword() {
        User u = EntitiesBuilder.createUser();
        u.setPassword(null);
        entityManager.persist(u);
        entityManager.flush();

        entityManager.clear();
    }

    @Test(expected = ConstraintViolationException.class)
    public void persistUserWithoutEmail() {
        User u = EntitiesBuilder.createUser();
        u.setEmail(null);
        entityManager.persist(u);
        entityManager.flush();

        entityManager.clear();
    }

    @Test(expected = ConstraintViolationException.class)
    public void persistUserWithoutUsername() {
        User u = EntitiesBuilder.createUser();
        u.setUsername(null);
        entityManager.persist(u);
        entityManager.flush();

        entityManager.clear();
    }

    @Test(expected = PersistenceException.class)
    public void persistUserWithDupEmail() {
        User u1 = EntitiesBuilder.createUser();
        entityManager.persist(u1);
        entityManager.flush();

        User u2 = EntitiesBuilder.createUser();
        entityManager.persist(u2);
        entityManager.flush();

        entityManager.clear();
    }

    @Test(expected = PersistenceException.class)
    public void persistUserWithDupUsername() {
        String username = Util.randomString(6);
        User u1 = EntitiesBuilder.createUser(username);
        entityManager.persist(u1);
        entityManager.flush();

        User u2 = EntitiesBuilder.createUser(username);
        u2.setEmail("r" + EntitiesBuilder.email);
        entityManager.persist(u2);
        entityManager.flush();

        entityManager.clear();
    }

    @Test
    public void findByUsername() {
        String username = Util.randomString(6);
        User u = EntitiesBuilder.createUser(username);
        entityManager.persist(u);
        entityManager.flush();

        User found = userRepository.findUserByUsernameOrEmail(username, "email");
        Assert.assertEquals(found.getUsername(), username);

        entityManager.clear();
    }

    @Test
    public void findByEmail() {
        User u = EntitiesBuilder.createUser();
        entityManager.persist(u);
        entityManager.flush();

        User found = userRepository.findUserByEmail(EntitiesBuilder.email);
        Assert.assertEquals(found.getEmail(), EntitiesBuilder.email);

        entityManager.clear();
    }

    @Test
    public void findAllByRole() {
        User u = EntitiesBuilder.createUser();
        entityManager.persist(u);
        entityManager.flush();

        userRepository.findAllByRole(Role.WAITER);
        Assert.assertEquals(userRepository.findAllByRole(Role.WAITER).size(), 1);

        entityManager.clear();
    }

    @Test
    public void findById() {
        User u = EntitiesBuilder.createUser();
        entityManager.persist(u);
        entityManager.flush();

        User user = userRepository.findUserById(u.getId());
        Assert.assertEquals(user, u);

        entityManager.clear();
    }
}
