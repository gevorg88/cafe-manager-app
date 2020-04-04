package org.example.cafemanager.repositories;

import org.example.cafemanager.domain.User;
import org.example.cafemanager.domain.enums.Role;
import org.example.cafemanager.dto.user.UserPublicProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository(value = "userRepo")
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByEmail(String email);

    User findUserByUsernameOrEmail(String username, String email);

    Collection<UserPublicProfile> findAllByRole(Role role);

    User findUserById(Long id);
}
