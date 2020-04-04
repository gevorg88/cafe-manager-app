package org.example.cafemanager.dto.order;

import org.example.cafemanager.domain.User;

import java.util.List;

public class OrderDetails {
    private Long tableId;
    private List<ProductInOrderReq> prodsInOrder;
    private User user;

    public OrderDetails(Long tableId, List<ProductInOrderReq> prodsInOrder, User user) {
        this.prodsInOrder = prodsInOrder;
        this.tableId = tableId;
        this.user = user;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public List<ProductInOrderReq> getProdsInOrder() {
        return prodsInOrder;
    }

    public void setProdsInOrder(List<ProductInOrderReq> prodsInOrder) {
        this.prodsInOrder = prodsInOrder;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
