package org.example.cafemanager.services.user.impls;

import org.example.cafemanager.domain.CafeTable;
import org.example.cafemanager.domain.Order;
import org.example.cafemanager.dto.user.UserPublicProfile;
import org.example.cafemanager.dto.user.CreateUserRequest;
import org.example.cafemanager.domain.User;
import org.example.cafemanager.domain.enums.Role;
import org.example.cafemanager.dto.user.UpdateUserRequestBody;
import org.example.cafemanager.repositories.OrderRepository;
import org.example.cafemanager.repositories.UserRepository;
import org.example.cafemanager.services.communication.NotificationService;
import org.example.cafemanager.services.exceptions.InstanceNotFoundException;
import org.example.cafemanager.services.exceptions.MustBeUniqueException;
import org.example.cafemanager.services.user.UserService;
import org.example.cafemanager.utilities.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    private final OrderRepository orderRepository;

    private final NotificationService notificationService;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepo,
            OrderRepository orderRepository,
            NotificationService notificationService) {
        this.userRepo = userRepo;
        this.orderRepository = orderRepository;
        this.notificationService = notificationService;
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    @Override
    @Cacheable("findUserByEmailAndEmail")
    public User findByUsernameOrEmail(String username, String email) {
        return userRepo.findUserByUsernameOrEmail(username, email);
    }

    @Override
    public Iterable<User> findAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User create(@NotNull CreateUserRequest createUserRequest, @NotNull Role role) {
        Assert.notNull(createUserRequest, "CreateUserRequest can not be null");
        Assert.notNull(role, "Role can not be null");
        Assert.notNull(createUserRequest.getEmail(), "Email not provided");
        Assert.notNull(createUserRequest.getUsername(), "Username not provided");

        if (findByUsernameOrEmail(createUserRequest.getUsername(), createUserRequest.getEmail()) != null) {
            throw new MustBeUniqueException("username or email");
        }

        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setEmail(createUserRequest.getEmail());

        user.setPassword(SecurityUtility.passwordEncoder().encode(createUserRequest.getPassword()));

        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setRole(role);
        save(user);

        notificationService.notify(createUserRequest);
        return user;
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public Collection<UserPublicProfile> getAllWaiters() {
        return userRepo.findAllByRole(Role.WAITER);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepo.findUserById(userId);
    }

    @Override
    public User update(Long userId, @NotNull UpdateUserRequestBody requestBody) {
        Assert.notNull(requestBody, "UpdateUserRequestBody can not be null");

        User user = userRepo.findUserById(userId);
        if (null == user) {
            throw new InstanceNotFoundException("user");
        }

        if (!user.getFirstName().equals(requestBody.getFirstName())) {
            user.setFirstName(requestBody.getFirstName());
        }

        if (!user.getLastName().equals(requestBody.getLastName())) {
            user.setLastName(requestBody.getLastName());
        }
        userRepo.save(user);

        return user;
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        User user = userRepo.findUserById(userId);

        if (null == user) {
            throw new InstanceNotFoundException("user");
        }

        Set<CafeTable> assignedTables = user.getTables();
        Set<Order> orders = new HashSet<>();
        for (CafeTable table : assignedTables) {
            orders.addAll(table.getOrders());
        }

        if (orders.size() > 0) {
            for (Order order : orders) {
                order.setProductsInOrders(new HashSet<>());
            }
            orderRepository.deleteAll(orders);
        }

        for (CafeTable table : assignedTables) {
            table.setUser(null);
            table.setOrders(new HashSet<>());
        }
        userRepo.delete(user);
    }
}
