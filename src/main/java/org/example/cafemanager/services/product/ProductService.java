package org.example.cafemanager.services.product;

import org.example.cafemanager.dto.product.ProductCreate;
import org.example.cafemanager.domain.Product;
import org.example.cafemanager.dto.product.CreateProductRequest;
import org.example.cafemanager.dto.product.SimpleProductProps;

import java.util.Collection;

public interface ProductService {

    Collection<SimpleProductProps> getAllProducts();

    Product create(ProductCreate createDto);

    Product update(Long id, CreateProductRequest requestBody);

    Product findOneById(Long productId);

    void delete(Long productId);
}
