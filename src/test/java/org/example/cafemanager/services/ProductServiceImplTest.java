package org.example.cafemanager.services;

import org.example.cafemanager.EntitiesBuilder;
import org.example.cafemanager.domain.Product;
import org.example.cafemanager.dto.product.CreateProductRequest;
import org.example.cafemanager.dto.product.ProductCreate;
import org.example.cafemanager.dto.product.SimpleProductProps;
import org.example.cafemanager.repositories.ProductRepository;
import org.example.cafemanager.services.exceptions.InstanceNotFoundException;
import org.example.cafemanager.services.exceptions.MustBeUniqueException;
import org.example.cafemanager.services.product.impls.ProductServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void getAllProducts() {
        List<SimpleProductProps> tables =
                Arrays.asList(EntitiesBuilder.createSimpleProductProps(), EntitiesBuilder.createSimpleProductProps());
        Mockito.when(productRepository.findAllBy()).thenReturn(tables);
        Assert.assertEquals(productService.getAllProducts().size(), tables.size());
    }

    @Test
    public void createWithNullableRequest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> productService.create(null));
    }

    @Test
    public void createWithNullableProductName() {
        Assert.assertThrows(IllegalArgumentException.class, () -> productService.create(new ProductCreate(null)));
    }

    @Test
    public void createWitDuplicateProductName() {
        SimpleProductProps productProps = EntitiesBuilder.createSimpleProductProps();
        ProductCreate createReq = new ProductCreate(productProps.getName());
        Mockito.when(productRepository.findProductByName(createReq.getName())).thenReturn(productProps);
        Assert.assertThrows(MustBeUniqueException.class, () -> productService.create(createReq));
    }

    @Test
    public void create() {
        Product product = EntitiesBuilder.createProduct();
        ProductCreate createReq = new ProductCreate(product.getName());
        Assert.assertEquals(product.getName(), productService.create(createReq).getName());
    }

    @Test
    public void updateWithNullableRequest() {
        Assert.assertThrows(IllegalArgumentException.class, () -> productService.update(1L, null));
    }

    @Test
    public void updateWithNullableProductName() {
        Assert.assertThrows(IllegalArgumentException.class,
                () -> productService.update(1L, new CreateProductRequest()));
    }

    @Test
    public void updateWithNotFoundProduct() {
        Mockito.when(productRepository.findProductById(1L)).thenReturn(null);
        Assert.assertThrows(InstanceNotFoundException.class,
                () -> productService.update(1L, EntitiesBuilder.createProductCreateRequestBody()));
    }

    @Test
    public void updateWitDuplicateProductName() {
        CreateProductRequest pr = EntitiesBuilder.createProductCreateRequestBody();
        Product p = EntitiesBuilder.createProduct();
        p.setName(pr.getName());
        Mockito.when(productRepository.findProductById(p.getId())).thenReturn(p);
        Mockito.when(productRepository.findProductByNameAndIdIsNot(pr.getName(), p.getId())).thenReturn(p);
        Assert.assertThrows(MustBeUniqueException.class, () -> productService.update(p.getId(), pr));
    }

    @Test
    public void update() {
        CreateProductRequest pr = EntitiesBuilder.createProductCreateRequestBody();
        Product p = EntitiesBuilder.createProduct();
        p.setName(pr.getName());
        Mockito.when(productRepository.findProductById(p.getId())).thenReturn(p);
        Assert.assertEquals(pr.getName(), productService.update(p.getId(), pr).getName());
    }

    @Test
    public void deleteNotFound() {
        Product p = EntitiesBuilder.createProduct();
        Mockito.when(productRepository.findProductById(p.getId())).thenReturn(null);
        Assert.assertThrows(InstanceNotFoundException.class, () -> productService.delete(p.getId()));
    }
}
