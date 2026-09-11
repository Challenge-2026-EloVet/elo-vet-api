package com.br.elovetapi.responsible.validation;

import com.br.elovetapi.responsible.exceptions.ResponsibleNotFoundException;
import com.br.elovetapi.responsible.exceptions.ResponsiblePetNotFoundException;
import com.br.elovetapi.responsible.exceptions.ResponsibleValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ResponsibleValidationHandler {

    public record ErrorResponse(String mensagem, Integer codigoStatus) {
    }

    public record ValidationErrorResponse(String field, String message) {
        public ValidationErrorResponse(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorResponse>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<ValidationErrorResponse> errors = exception.getFieldErrors().stream()
                .map(ValidationErrorResponse::new)
                .toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ResponsibleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResponsibleNotFound(ResponsibleNotFoundException exception) {
        log.warn("Business logic error: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(ResponsiblePetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResponsiblePetNotFound(ResponsiblePetNotFoundException exception) {
        log.warn("Business logic error: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(ResponsibleValidationException.class)
    public ResponseEntity<ErrorResponse> handleResponsibleValidation(ResponsibleValidationException exception) {
        log.warn("Domain validation error: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
}
