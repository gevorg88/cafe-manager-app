package org.example.cafemanager.repositories;

import org.example.cafemanager.domain.Product;
import org.example.cafemanager.dto.product.SimpleProductProps;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Collection;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Collection<SimpleProductProps> findAllBy();

    SimpleProductProps findProductByName(String name);

    Product findProductById(Long id);

    Product findProductByNameAndIdIsNot(String name, Long id);
}
