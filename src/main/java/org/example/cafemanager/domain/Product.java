package org.example.cafemanager.domain;

import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "products", indexes = { @Index(name = "idx_product_name", columnList = "name"), })
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 3, max = 255, message = "Product name should be at least 3 symbols")
    @NotBlank(message = "Product Name required")
    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "products")
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private Set<ProductsInOrder> productsInOrders = new HashSet<>();

    public Set<ProductsInOrder> getProductsInOrders() {
        return productsInOrders;
    }

    public void setProductsInOrders(Set<ProductsInOrder> productsInOrders) {
        this.productsInOrders = productsInOrders;
    }

    public void addProductsInOrders(ProductsInOrder productsInOrder) {
        this.productsInOrders.add(productsInOrder);
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Product product = (Product) o;
        return getId().equals(product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
