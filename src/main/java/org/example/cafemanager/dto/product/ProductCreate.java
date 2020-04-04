package org.example.cafemanager.dto.product;

public class ProductCreate {
    private String name;

    public ProductCreate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
