package org.example.cafemanager.domain;

import org.example.cafemanager.domain.enums.Status;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "cafe_tables", indexes = { @Index(name = "idx_cafe_table_name", columnList = "name"), })
public class CafeTable {
    public static final String ATTACHED = "attached";

    public static final String DETACHED = "detached";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Table Name required")
    @Length(max = 32, message = "Table name is very long")
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "cafeTable", cascade = CascadeType.REMOVE)
    private Set<Order> orders;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public boolean removeOrder(Order order) {
        return this.orders.remove(order);
    }

    public boolean hasOpenTables() {
        return this.orders.stream().anyMatch((order -> order.getStatus().equals(Status.OPEN)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CafeTable cafeTable = (CafeTable) o;
        return getId().equals(cafeTable.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
