package com.basiliqo.cvbuilder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Template not found")
public class TemplateNotFoundException extends RuntimeException {

    public TemplateNotFoundException(String message) {
        super(message);
    }
}
