package org.example.cafemanager.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "products_in_order")
public class ProductsInOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "order_id", nullable = false)
    @ManyToOne
    private Order order;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne
    private Product product;

    @Column(name = "amount", nullable = false)
    private Integer amount = 1;

    public ProductsInOrder() {

    }

    public ProductsInOrder(Integer amount, Product product, Order order) {
        this.product = product;
        this.amount = amount;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductsInOrder that = (ProductsInOrder) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ProductsInOrder{" + "id=" + id + ", amount=" + amount + ", order=" + order + ", product=" + product
                + '}';
    }
}
