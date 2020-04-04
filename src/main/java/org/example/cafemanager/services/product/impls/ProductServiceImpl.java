package org.example.cafemanager.services.product.impls;

import org.example.cafemanager.dto.product.ProductCreate;
import org.example.cafemanager.domain.Product;
import org.example.cafemanager.dto.product.CreateProductRequest;
import org.example.cafemanager.dto.product.SimpleProductProps;
import org.example.cafemanager.repositories.ProductRepository;
import org.example.cafemanager.services.exceptions.InstanceNotFoundException;
import org.example.cafemanager.services.exceptions.MustBeUniqueException;
import org.example.cafemanager.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(@NotNull ProductCreate productCreate) {
        Assert.notNull(productCreate, "ProductCreate can not be null");
        Assert.notNull(productCreate.getName(), "Product name is not provided");

        SimpleProductProps existingProd = productRepository.findProductByName(productCreate.getName());
        if (null != existingProd) {
            throw new MustBeUniqueException("product");
        }
        Product product = new Product();
        product.setName(productCreate.getName());
        productRepository.save(product);
        return product;
    }

    @Override
    public Collection<SimpleProductProps> getAllProducts() {
        return productRepository.findAllBy();
    }

    @Override
    public Product update(Long productId, @NotNull CreateProductRequest requestBody) {
        Assert.notNull(requestBody, "CreateProductRequest can not be null");
        Assert.notNull(requestBody.getName(), "Product name is not provided");

        Product product = productRepository.findProductById(productId);
        if (null == product) {
            throw new InstanceNotFoundException("product");
        }

        Product existingProduct = productRepository.findProductByNameAndIdIsNot(requestBody.getName(), productId);
        if (null != existingProduct) {
            throw new MustBeUniqueException("name");
        }

        if (!product.getName().equals(requestBody.getName())) {
            product.setName(requestBody.getName());
            productRepository.save(product);
        }

        return product;
    }

    @Override
    public Product findOneById(Long productId) {
        return productRepository.findProductById(productId);
    }

    @Override
    @Transactional
    public void delete(Long productId) {
        Product product = findOneById(productId);

        if (null == product) {
            throw new InstanceNotFoundException("product");
        }

        product.setProductsInOrders(new HashSet<>());
        productRepository.delete(product);
    }
}
