package com.basiliqo.cvbuilder.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class FileProcessingException extends RuntimeException {

    public FileProcessingException(String message) {
        super(message);
    }
}
