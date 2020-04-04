package org.example.cafemanager.services.exceptions;

import javax.validation.constraints.NotNull;

public class InstanceNotFoundException extends RuntimeException {
    public InstanceNotFoundException() {
        this("Instance");
    }

    public InstanceNotFoundException(@NotNull String instanceName) {
        super(String.format("The requested %s was not found", instanceName));
    }
}
