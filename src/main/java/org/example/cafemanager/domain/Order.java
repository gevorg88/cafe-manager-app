package org.example.cafemanager.domain;

import org.example.cafemanager.domain.enums.Status;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private CafeTable cafeTable;

    @ManyToMany
    @JoinTable(name = "products_in_order", joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private Set<ProductsInOrder> productsInOrders = new HashSet<>();

    public Order() {
        this.status = Status.OPEN;
    }

    public Set<ProductsInOrder> getProductsInOrders() {
        return productsInOrders;
    }

    public void setProductsInOrders(Set<ProductsInOrder> productsInOrders) {
        this.productsInOrders = productsInOrders;
    }

    public void addProductsInOrders(ProductsInOrder productsInOrder) {
        this.productsInOrders.add(productsInOrder);
    }

    public boolean removeProductInOrder(ProductsInOrder pio) {
        return productsInOrders.remove(pio);
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CafeTable getCafeTable() {
        return cafeTable;
    }

    public void setCafeTable(CafeTable cafeTable) {
        this.cafeTable = cafeTable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Order order = (Order) o;
        return getId().equals(order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
