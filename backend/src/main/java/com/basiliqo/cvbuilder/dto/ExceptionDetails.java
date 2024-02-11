package com.basiliqo.cvbuilder.dto;

import org.springframework.http.HttpStatus;

/**
 * Details about exception errors
 *
 * @param code   error HTTP-status code
 * @param reason reason of error
 * @param detail detailed error message
 */
public record ExceptionDetails(int code,
                               String reason,
                               String detail) {

    public ExceptionDetails(HttpStatus httpStatus, String detail) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), detail);
    }
}
