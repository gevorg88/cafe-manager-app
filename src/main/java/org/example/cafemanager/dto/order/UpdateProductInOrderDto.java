package org.example.cafemanager.dto.order;

import org.example.cafemanager.domain.User;

public class UpdateProductInOrderDto {
    private Long pioId;
    private Long orderId;
    private User user;
    private Integer amount;

    public UpdateProductInOrderDto(Long pioId, Long orderId, User user, Integer amount) {
        this.pioId = pioId;
        this.orderId = orderId;
        this.user = user;
        this.amount = amount;
    }

    public Long getPioId() {
        return pioId;
    }

    public void setPioId(Long pioId) {
        this.pioId = pioId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
