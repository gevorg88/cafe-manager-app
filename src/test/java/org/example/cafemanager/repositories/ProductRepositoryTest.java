package org.example.cafemanager.repositories;

import org.example.cafemanager.EntitiesBuilder;
import org.example.cafemanager.Util;
import org.example.cafemanager.domain.Product;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

public class ProductRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test(expected = ConstraintViolationException.class)
    public void persistProductWithoutName() {
        Product product = new Product();
        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();
    }

    @Test(expected = PersistenceException.class)
    public void persistOrderWithDupName() {
        String name = Util.randomString(6);

        Product product1 = EntitiesBuilder.createProduct(name);
        Product product2 = EntitiesBuilder.createProduct(name);

        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void findAllBy() {
        Product product = EntitiesBuilder.createProduct();

        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();

        Assert.assertEquals(productRepository.findAllBy().size(), 1);
    }

    @Test
    public void findProductByName() {
        String name = Util.randomString(6);
        Product product = EntitiesBuilder.createProduct(name);

        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();

        Assert.assertNotNull(productRepository.findProductByName(name));
    }

    @Test
    public void findProductById() {
        Product product = EntitiesBuilder.createProduct();

        entityManager.persist(product);
        entityManager.flush();

        Assert.assertNotNull(productRepository.findProductById(product.getId()));

        entityManager.clear();
    }

    @Test
    public void findProductByNameAndIdIsNot() {
        String name = Util.randomString(6);
        Product product = EntitiesBuilder.createProduct(name);

        entityManager.persist(product);
        entityManager.flush();
        entityManager.clear();

        Assert.assertNotNull(productRepository.findProductByNameAndIdIsNot(name, product.getId() + 1));
    }
}
