package com.basiliqo.cvbuilder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WrongNameException extends RuntimeException {

    public WrongNameException(String message) {
        super(message);
    }
}
