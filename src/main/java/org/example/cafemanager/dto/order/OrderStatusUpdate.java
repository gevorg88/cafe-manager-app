package org.example.cafemanager.dto.order;

import org.example.cafemanager.domain.enums.Status;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class OrderStatusUpdate {
    @Enumerated(EnumType.STRING)
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
