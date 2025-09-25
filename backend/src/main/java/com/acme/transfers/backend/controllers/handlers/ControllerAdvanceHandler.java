package com.acme.transfers.backend.controllers.handlers;

import com.acme.transfers.backend.dto.CustomError;
import com.acme.transfers.backend.dto.ValidationError;
import com.acme.transfers.backend.services.Exceptions.InvalidTransferDateException;
import com.acme.transfers.backend.services.Exceptions.TransferNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ControllerAdvanceHandler {

    @ExceptionHandler(TransferNotFoundException.class)
    public ResponseEntity<CustomError> transferNotFound(TransferNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidTransferDateException.class)
    public ResponseEntity<CustomError> invalidTransferDate(InvalidTransferDateException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity <CustomError> methodArgumentNotValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError(Instant.now(), status.value(),"Invalid data", request.getRequestURI());
        for (FieldError f : e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);
    }
}
