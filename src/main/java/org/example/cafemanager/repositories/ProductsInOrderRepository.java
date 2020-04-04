package org.example.cafemanager.repositories;

import org.example.cafemanager.domain.ProductsInOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsInOrderRepository extends CrudRepository<ProductsInOrder, Long> {
}
