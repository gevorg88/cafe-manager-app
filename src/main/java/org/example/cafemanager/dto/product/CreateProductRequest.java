package org.example.cafemanager.dto.product;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class CreateProductRequest {
    @NotBlank(message = "Product name is required")
    @Length(max = 32, min = 3, message = "Product name must contain at from 3 to 32 symbols")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
