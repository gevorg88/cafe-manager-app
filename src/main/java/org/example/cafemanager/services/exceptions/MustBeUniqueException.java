package org.example.cafemanager.services.exceptions;

import javax.validation.constraints.NotNull;

public class MustBeUniqueException extends RuntimeException {
    public MustBeUniqueException() {
        this("property");
    }

    public MustBeUniqueException(@NotNull String propName) {
        super(String.format("Raw with this %s is already exist", propName));
    }
}
