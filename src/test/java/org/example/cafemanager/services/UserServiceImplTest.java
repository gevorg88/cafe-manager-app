package org.example.cafemanager.services;

import org.example.cafemanager.EntitiesBuilder;
import org.example.cafemanager.Util;
import org.example.cafemanager.domain.User;
import org.example.cafemanager.domain.enums.Role;
import org.example.cafemanager.dto.user.CreateUserRequest;
import org.example.cafemanager.dto.user.UpdateUserRequestBody;
import org.example.cafemanager.dto.user.UserPublicProfile;
import org.example.cafemanager.repositories.UserRepository;
import org.example.cafemanager.services.communication.impls.EmailService;
import org.example.cafemanager.services.exceptions.InstanceNotFoundException;
import org.example.cafemanager.services.exceptions.MustBeUniqueException;
import org.example.cafemanager.services.user.impls.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService notificationService;

    @Test
    public void findByEmail() {
        User u = EntitiesBuilder.createUser();

        Mockito.when(userRepository.findUserByEmail(EntitiesBuilder.email)).thenReturn(u);
        Assert.assertEquals(userService.findByEmail(EntitiesBuilder.email), u);
    }

    @Test
    public void findByUsernameOrEmail() {
        String username = Util.randomString(6);
        User u = EntitiesBuilder.createUser(username);

        Mockito.when(userRepository.findUserByUsernameOrEmail(username, EntitiesBuilder.email)).thenReturn(u);
        Assert.assertEquals(userService.findByUsernameOrEmail(username, EntitiesBuilder.email), u);
    }

    @Test
    public void findAllUsers() {
        List<User> usersSet = Arrays.asList(EntitiesBuilder.createUser(), EntitiesBuilder.createUser());

        Mockito.when(userRepository.findAll()).thenReturn(usersSet);
        int size = 0;
        Iterator<User> usersIterator = userService.findAllUsers().iterator();
        for (; usersIterator.hasNext(); usersIterator.next()) {
            size++;
        }

        Assert.assertEquals(usersSet.size(), size);
    }

    @Test
    public void create() {
        CreateUserRequest uq = EntitiesBuilder.createCreateUserRequest();

        User u = EntitiesBuilder.createUser();
        u.setUsername(uq.getUsername());
        User user = userService.create(uq, Role.WAITER);

        Assert.assertNotNull(user);
        Assert.assertEquals(user.getUsername(), uq.getUsername());
        Mockito.verify(notificationService, Mockito.times(1)).notify(uq);
    }

    @Test
    public void createWithNullableRequest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> userService.create(null, Role.WAITER));
    }

    @Test
    public void createWithNullableStatus() {
        Assert.assertThrows(IllegalArgumentException.class,
                () -> userService.create(EntitiesBuilder.createCreateUserRequest(), null));
    }

    @Test
    public void createWithNullableEmail() {
        CreateUserRequest uq = EntitiesBuilder.createCreateUserRequest();
        uq.setEmail(null);
        Assert.assertThrows(IllegalArgumentException.class, () -> userService.create(uq, Role.WAITER));
    }

    @Test
    public void createWithNullableUsername() {
        CreateUserRequest uq = EntitiesBuilder.createCreateUserRequest();
        uq.setUsername(null);
        Assert.assertThrows(IllegalArgumentException.class, () -> userService.create(uq, Role.WAITER));
    }

    @Test
    public void createWithDuplicateUsername() {
        User u = EntitiesBuilder.createUser();
        CreateUserRequest uq = EntitiesBuilder.createCreateUserRequest();
        uq.setEmail(u.getEmail() + "asd");
        Mockito.when(userRepository.findUserByUsernameOrEmail(uq.getUsername(), uq.getEmail())).thenReturn(u);
        Assert.assertThrows(MustBeUniqueException.class, () -> userService.create(uq, Role.WAITER));
    }

    @Test
    public void createWithDuplicateEmail() {
        User u = EntitiesBuilder.createUser();
        CreateUserRequest uq = EntitiesBuilder.createCreateUserRequest();
        uq.setUsername(u.getUsername() + "asd");
        Mockito.when(userRepository.findUserByUsernameOrEmail(uq.getUsername(), uq.getEmail())).thenReturn(u);
        Assert.assertThrows(MustBeUniqueException.class, () -> userService.create(uq, Role.WAITER));
    }

    @Test
    public void save() {
        User u = EntitiesBuilder.createUser();
        Mockito.when(userRepository.save(u)).thenReturn(u);
        Assert.assertEquals(userService.save(u), u);
    }

    @Test
    public void getAllWaiters() {
        List<UserPublicProfile> users =
                Arrays.asList(EntitiesBuilder.createUserPublicProfile(1L), EntitiesBuilder.createUserPublicProfile(2L));
        Mockito.when(userRepository.findAllByRole(Role.WAITER)).thenReturn(users);
        Assert.assertEquals(userService.getAllWaiters().size(), users.size());
    }

    @Test
    public void getUserById() {
        Long id = 1L;
        User user = EntitiesBuilder.createUser();
        user.setId(id);
        Mockito.when(userRepository.findUserById(id)).thenReturn(user);
        Assert.assertEquals(user, userService.getUserById(id));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateWithNullUpdateUserRequestBody() {
        User u = EntitiesBuilder.createUser();
        Mockito.when(userService.update(1L, null)).thenReturn(u);
    }

    @Test
    public void updateWithNotFoundException() {
        Assert.assertThrows(InstanceNotFoundException.class, () -> {
            userService.update(2L, new UpdateUserRequestBody());
        });
    }

    @Test
    public void update() {
        UpdateUserRequestBody u = EntitiesBuilder.createUpdateUserRequestBody();
        User user = EntitiesBuilder.createUser();
        user.setFirstName(u.getFirstName());
        user.setLastName(u.getLastName());
        user.setId(1L);
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);
        User updatedUser = userService.update(user.getId(), u);
        Assert.assertEquals(u.getFirstName(), updatedUser.getFirstName());
        Assert.assertEquals(u.getLastName(), updatedUser.getLastName());
    }

    @Test
    public void deleteWithNotFoundException() {
        Mockito.when(userRepository.findUserById(1L)).thenReturn(null);
        Assert.assertThrows(InstanceNotFoundException.class, () -> {
            userService.delete(1L);
        });
    }
}
