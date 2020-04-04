package org.example.cafemanager.dto.order;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ProductInOrderReq implements Serializable {
    @Min(value = 1, message = "must be equal or greater than 1")
    @Max(value = 50, message = "must be equal or less than 50")
    private Integer amount = 1;

    @NotNull
    private Long productId;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
