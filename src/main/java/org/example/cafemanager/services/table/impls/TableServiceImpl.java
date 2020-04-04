package org.example.cafemanager.services.table.impls;

import org.example.cafemanager.domain.CafeTable;
import org.example.cafemanager.domain.Order;
import org.example.cafemanager.domain.User;
import org.example.cafemanager.domain.enums.Status;
import org.example.cafemanager.dto.table.*;
import org.example.cafemanager.repositories.CafeTableRepository;
import org.example.cafemanager.services.exceptions.InstanceNotFoundException;
import org.example.cafemanager.services.exceptions.MustBeUniqueException;
import org.example.cafemanager.services.table.TableService;
import org.example.cafemanager.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;

@Service
public class TableServiceImpl implements TableService {

    private final CafeTableRepository tableRepo;

    private final UserService userService;

    @Autowired
    public TableServiceImpl(CafeTableRepository cafeTableRepository, UserService userService) {
        tableRepo = cafeTableRepository;
        this.userService = userService;
    }

    @Override
    public Collection<SimpleTableProps> getAllTables() {
        return tableRepo.findAllBy();
    }

    @Override
    public CafeTable create(@NotNull TableCreate createDto) {
        Assert.notNull(createDto, "TableCreate can not be null");
        Assert.notNull(createDto.getName(), "Table Name is not provided");

        OnlyTableProps existingTable = tableRepo.findOneByName(createDto.getName());
        if (null != existingTable) {
            throw new MustBeUniqueException("name");
        }

        CafeTable table = new CafeTable();
        table.setName(createDto.getName());
        tableRepo.save(table);

        return table;
    }

    @Override
    @Transactional
    public String assignUser(Long tableId, Long userId) {
        CafeTable table = tableRepo.findCafeTableById(tableId);
        if (null == table) {
            throw new InstanceNotFoundException("table");
        }
        if (-1 == userId) {
            table.setUser(null);
            tableRepo.save(table);
            return CafeTable.DETACHED;
        }

        User user = userService.getUserById(userId);
        if (null == user) {
            throw new InstanceNotFoundException("user");
        }

        table.setUser(user);
        user.addTable(table);
        tableRepo.save(table);
        return CafeTable.ATTACHED;
    }

    @Override
    @Transactional
    public void delete(Long tableId) {
        CafeTable table = tableRepo.findCafeTableById(tableId);
        if (null == table) {
            throw new InstanceNotFoundException("table");
        }
        User user = table.getUser();
        if (null != user) {
            table.getUser().removeTable(table);
        }

        for (Order order : table.getOrders()) {
            order.setProductsInOrders(new HashSet<>());
        }

        table.setOrders(new HashSet<>());
        tableRepo.delete(table);
    }

    @Override
    public CafeTable update(Long id, TableCreateRequestBody requestBody) {
        Assert.notNull(requestBody, "TableCreateRequestBody can not be null");
        Assert.notNull(requestBody.getName(), "Table Name is not provided");

        CafeTable table = tableRepo.findCafeTableById(id);
        if (null == table) {
            throw new InstanceNotFoundException("table");
        }

        CafeTable existingTable = tableRepo.findCafeTableByNameAndIdIsNot(requestBody.getName(), id);
        if (null != existingTable) {
            throw new MustBeUniqueException("name");
        }

        if (!table.getName().equals(requestBody.getName())) {
            table.setName(requestBody.getName());
            tableRepo.save(table);
        }

        return table;
    }

    @Override
    public Collection<TableWithOpenOrdersCount> getUserAssignedTablesWithOpenStatus(Long userId) {
        return tableRepo.userTablesWithOrdersForStatus(userId, Status.OPEN);
    }

    @Override
    public CafeTable getUserAssignedTable(Long tableId, User user) {
        CafeTable table = tableRepo.findCafeTableByIdAndUser(tableId, user);
        if (null == table) {
            throw new InstanceNotFoundException("table");
        }
        return table;
    }
}
