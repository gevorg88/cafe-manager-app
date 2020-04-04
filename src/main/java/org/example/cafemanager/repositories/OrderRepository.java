package org.example.cafemanager.repositories;

import org.example.cafemanager.domain.CafeTable;
import org.example.cafemanager.domain.Order;
import org.example.cafemanager.domain.User;
import org.example.cafemanager.domain.enums.Status;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Order getByIdAndCafeTable_User(Long id, User user);

    Order findOrderByStatusAndCafeTable(Status open, CafeTable cafeTable);
}
