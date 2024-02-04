package com.basiliqo.cvbuilder.exception;

import com.basiliqo.cvbuilder.dto.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(FileProcessingException.class)
    public ExceptionDetails handleFileProcessingException(FileProcessingException ex) {
        return new ExceptionDetails(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileSavingException.class)
    public ExceptionDetails handleFileSavingException(FileSavingException ex) {
        return new ExceptionDetails(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ExceptionDetails handleTemplateNotFoundException(EntityNotFoundException ex) {
        return new ExceptionDetails(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(UnsupportedDocumentTypeException.class)
    public ExceptionDetails handleUnsupportedDocumentTypeException(UnsupportedDocumentTypeException ex) {
        return new ExceptionDetails(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(UnsupportedFileFormatException.class)
    public ExceptionDetails handleUnsupportedFileFormatException(UnsupportedFileFormatException ex) {
        return new ExceptionDetails(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WrongNameException.class)
    public ExceptionDetails handleWrongNameException(WrongNameException ex) {
        return new ExceptionDetails(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
