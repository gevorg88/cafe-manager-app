package org.example.cafemanager.repositories;

import org.example.cafemanager.EntitiesBuilder;
import org.example.cafemanager.domain.Order;
import org.example.cafemanager.domain.enums.Status;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import javax.persistence.PersistenceException;

public class OrderRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test(expected = PersistenceException.class)
    public void persistOrderWithoutStatus() {
        Order order = EntitiesBuilder.createOrder();
        order.setStatus(null);
        entityManager.persist(order.getCafeTable());
        entityManager.persist(order);
        entityManager.flush();
        entityManager.clear();
    }

    @Test(expected = PersistenceException.class)
    public void persistOrderWithoutTable() {
        Order order = EntitiesBuilder.createOrder();
        order.setCafeTable(null);
        entityManager.persist(order);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void findOrderByStatusAndCafeTable() {
        Order order = EntitiesBuilder.createOrder();

        entityManager.persist(order.getCafeTable());
        entityManager.persist(order);
        entityManager.flush();
        entityManager.clear();

        Assert.assertNotNull(orderRepository.findOrderByStatusAndCafeTable(Status.OPEN, order.getCafeTable()));
    }

    @Test
    public void getByIdAndCafeTable_User() {
        Order order = EntitiesBuilder.createOrder();
        order.getCafeTable().setUser(EntitiesBuilder.createUser());

        entityManager.persist(order.getCafeTable().getUser());
        entityManager.persist(order.getCafeTable());
        entityManager.persist(order);
        entityManager.flush();

        Assert.assertNotNull(orderRepository.getByIdAndCafeTable_User(order.getId(), order.getCafeTable().getUser()));

        entityManager.clear();

    }
}
