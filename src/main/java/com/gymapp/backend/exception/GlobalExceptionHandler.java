package com.gymapp.backend.exception;


import com.gymapp.backend.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de recurso no encontrado (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorDTO error = ErrorDTO.builder()
                .message(ex.getMessage())
                .error("Resource Not Found")
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 2. Manejo de validaciones fallidas (@Valid) - Error 400
    // Cuando el usuario manda un JSON con datos inválidos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Extraemos cada campo que falló y su mensaje de error
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 3. Manejo Genérico (Para todo lo demás) - Error 500
    // Esto evita que se filtre información sensible del stack trace
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception ex) {
        ErrorDTO error = ErrorDTO.builder()
                .message("Ocurrió un error inesperado en el servidor")
                .error("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
