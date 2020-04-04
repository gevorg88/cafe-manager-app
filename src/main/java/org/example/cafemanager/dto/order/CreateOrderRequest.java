package org.example.cafemanager.dto.order;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderRequest {
    private List<ProductInOrderReq> products = new ArrayList<>();

    public List<ProductInOrderReq> getProducts() {
        return products;
    }

    public void setProduct(List<ProductInOrderReq> products) {
        this.products = products;
    }
}
