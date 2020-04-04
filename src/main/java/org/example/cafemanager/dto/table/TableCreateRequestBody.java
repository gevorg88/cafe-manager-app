package org.example.cafemanager.dto.table;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

public class TableCreateRequestBody {
    @NotBlank(message = "Table name is required")
    @Length(max = 32, min = 6, message = "Table name must contain at from 6 to 32 symbols")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
