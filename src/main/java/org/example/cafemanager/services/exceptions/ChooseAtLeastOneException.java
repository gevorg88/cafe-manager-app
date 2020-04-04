package org.example.cafemanager.services.exceptions;

import javax.validation.constraints.NotNull;

public class ChooseAtLeastOneException extends RuntimeException {
    public ChooseAtLeastOneException() {
        this("Instance");
    }

    public ChooseAtLeastOneException(@NotNull String instanceName) {
        super(String.format("You should choose at least one valid %s", instanceName));
    }
}
