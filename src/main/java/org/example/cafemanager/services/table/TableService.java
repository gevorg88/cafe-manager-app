package org.example.cafemanager.services.table;

import org.example.cafemanager.domain.CafeTable;
import org.example.cafemanager.domain.User;
import org.example.cafemanager.dto.table.*;

import java.util.Collection;

public interface TableService {
    Collection<SimpleTableProps> getAllTables();

    CafeTable create(TableCreate createDto);

    String assignUser(Long tableId, Long userId);

    void delete(Long tableId);

    CafeTable update(Long id, TableCreateRequestBody requestBody);

    Collection<TableWithOpenOrdersCount> getUserAssignedTablesWithOpenStatus(Long userId);

    CafeTable getUserAssignedTable(Long tableId, User user);
}
