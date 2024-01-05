package com.basiliqo.cvbuilder.exception;

import com.basiliqo.cvbuilder.dto.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileProcessingException.class)
    public ExceptionDetails handleFileProcessingException(FileProcessingException ex) {
        return new ExceptionDetails(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }

    @ExceptionHandler(FileSavingException.class)
    public ExceptionDetails handleFileSavingException(FileSavingException ex) {
        return new ExceptionDetails(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(TemplateNotFoundException.class)
    public ExceptionDetails handleTemplateNotFoundException(TemplateNotFoundException ex) {
        return new ExceptionDetails(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UnsupportedDocumentTypeException.class)
    public ExceptionDetails handleUnsupportedDocumentTypeException(UnsupportedDocumentTypeException ex) {
        return new ExceptionDetails(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }

    @ExceptionHandler(UnsupportedFileFormatException.class)
    public ExceptionDetails handleUnsupportedFileFormatException(UnsupportedFileFormatException ex) {
        return new ExceptionDetails(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
    }

    @ExceptionHandler(WrongNameException.class)
    public ExceptionDetails handleWrongNameException(WrongNameException ex) {
        return new ExceptionDetails(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
