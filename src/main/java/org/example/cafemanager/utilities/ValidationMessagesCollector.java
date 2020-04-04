package org.example.cafemanager.utilities;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;

import java.util.stream.Collectors;

public class ValidationMessagesCollector {
    public static String collectErrorMessages(Errors errors) {
        return errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
    }
}
