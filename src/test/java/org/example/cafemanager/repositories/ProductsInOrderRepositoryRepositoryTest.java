package org.example.cafemanager.repositories;

import org.example.cafemanager.EntitiesBuilder;
import org.example.cafemanager.domain.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;

public class ProductsInOrderRepositoryRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private ProductsInOrderRepository productsInOrderRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test(expected = PersistenceException.class)
    public void persistProductsInOrderWithoutProduct() {
        ProductsInOrder pio = EntitiesBuilder.createProductInOrder();
        pio.setProduct(null);
        entityManager.persist(pio.getOrder().getCafeTable());
        entityManager.persist(pio.getOrder());
        entityManager.persist(pio);
        entityManager.flush();

        entityManager.clear();
    }

    @Test(expected = PersistenceException.class)
    public void persistProductsInOrderWithoutOrder() {
        ProductsInOrder pio = EntitiesBuilder.createProductInOrder();
        pio.setOrder(null);

        entityManager.persist(pio.getProduct());
        entityManager.persist(pio);
        entityManager.flush();

        entityManager.clear();
    }

    @Test
    public void productsInOrderIsCreated() {
        ProductsInOrder pio = EntitiesBuilder.createProductInOrder();

        entityManager.persist(pio.getOrder().getCafeTable());
        entityManager.persist(pio.getOrder());
        entityManager.persist(pio.getProduct());
        entityManager.persist(pio);
        entityManager.flush();
        entityManager.clear();

        Assert.assertNotNull(productsInOrderRepository.findById(pio.getId()));
    }
}
