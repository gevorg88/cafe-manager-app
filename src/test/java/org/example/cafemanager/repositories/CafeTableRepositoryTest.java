package org.example.cafemanager.repositories;

import org.example.cafemanager.EntitiesBuilder;
import org.example.cafemanager.Util;
import org.example.cafemanager.domain.CafeTable;
import org.example.cafemanager.domain.User;
import org.example.cafemanager.domain.enums.Status;
import org.example.cafemanager.dto.table.OnlyTableProps;
import org.example.cafemanager.dto.table.TableWithOpenOrdersCount;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

public class CafeTableRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private CafeTableRepository cafeTableRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test(expected = ConstraintViolationException.class)
    public void persistTableWithoutName() {
        CafeTable table = new CafeTable();
        entityManager.persist(table);
        entityManager.flush();

        entityManager.clear();
    }

    @Test(expected = ConstraintViolationException.class)
    public void persistTableWithVeryLongName() {
        CafeTable table = EntitiesBuilder.createCafeTable(Util.randomString(256));
        entityManager.persist(table);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void findAllBy() {
        CafeTable table1 = EntitiesBuilder.createCafeTable();
        CafeTable table2 = EntitiesBuilder.createCafeTable();
        entityManager.persist(table1);
        entityManager.persist(table2);
        entityManager.flush();

        Assert.assertEquals(cafeTableRepository.findAllBy().size(), 2);

        entityManager.clear();
    }

    @Test
    public void findOneByName() {
        String name = Util.randomString(5);
        CafeTable table = EntitiesBuilder.createCafeTable(name);
        entityManager.persist(table);
        entityManager.flush();

        OnlyTableProps found = cafeTableRepository.findOneByName(name);
        Assert.assertEquals(found.getName(), name);

        entityManager.clear();
    }

    @Test
    public void findCafeTableById() {
        CafeTable table = EntitiesBuilder.createCafeTable();
        entityManager.persist(table);
        entityManager.flush();

        CafeTable table1 = cafeTableRepository.findCafeTableById(table.getId());
        Assert.assertEquals(table, table1);

        entityManager.clear();
    }

    @Test
    public void findCafeTableByNameAndIdIsNot() {
        String name = Util.randomString(6);
        CafeTable table = EntitiesBuilder.createCafeTable(name);
        entityManager.persist(table);
        entityManager.flush();

        CafeTable table1 = cafeTableRepository.findCafeTableByNameAndIdIsNot(name, table.getId() + 1);
        Assert.assertEquals(table, table1);

        entityManager.clear();
    }

    @Test
    public void getAllByUserIds() {
        User user = EntitiesBuilder.createUser();
        CafeTable table = EntitiesBuilder.createCafeTable(user);

        entityManager.persist(user);
        entityManager.persist(table);
        entityManager.flush();

        Assert.assertEquals(cafeTableRepository.getAllByUserIdIs(user.getId()).size(), 1);

        entityManager.clear();
    }

    @Test
    public void userTablesWithoutOrders() {
        User user = EntitiesBuilder.createUser();
        CafeTable table = EntitiesBuilder.createCafeTable(user);

        entityManager.persist(table);
        entityManager.persist(user);
        entityManager.flush();

        Optional<TableWithOpenOrdersCount> t =
                cafeTableRepository.userTablesWithOrdersForStatus(user.getId(), Status.OPEN).stream().findFirst();
        Assert.assertTrue(t.isPresent());
        int count = t.get().getOrderCount();

        Assert.assertEquals(count, 0);

        entityManager.clear();
    }

    @Test
    public void findCafeTableByIdAndUser() {
        User user = EntitiesBuilder.createUser();
        CafeTable table = EntitiesBuilder.createCafeTable(user);

        entityManager.persist(table);
        entityManager.persist(user);
        entityManager.flush();

        Assert.assertEquals(table, cafeTableRepository.findCafeTableByIdAndUser(table.getId(), user));

        entityManager.clear();
    }
}
