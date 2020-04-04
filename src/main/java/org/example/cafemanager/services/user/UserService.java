package org.example.cafemanager.services.user;

import org.example.cafemanager.dto.user.UserPublicProfile;
import org.example.cafemanager.dto.user.CreateUserRequest;
import org.example.cafemanager.domain.User;
import org.example.cafemanager.domain.enums.Role;
import org.example.cafemanager.dto.user.UpdateUserRequestBody;

import java.util.Collection;

public interface UserService {

    User findByEmail(String email);

    User findByUsernameOrEmail(String username, String email);

    Iterable<User> findAllUsers();

    User create(CreateUserRequest createUserRequest, Role role) throws Exception;

    Collection<UserPublicProfile> getAllWaiters();

    User save(User user);

    User getUserById(Long userId);

    User update(Long userId, UpdateUserRequestBody requestBody);

    void delete(Long userId);
}
