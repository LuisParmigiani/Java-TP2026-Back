package soda_roja.backend.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import soda_roja.backend.dtoResponse.ErrorResponseDTO;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errores.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponseDTO response = ErrorResponseDTO.builder()
            .mensaje("Error de validación")
            .errores(errores)
            .codigo(HttpStatus.BAD_REQUEST.value())
            .build();

        logger.warn("Validation error: {}", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
            errores.put(violation.getPropertyPath().toString(), violation.getMessage())
        );

        ErrorResponseDTO response = ErrorResponseDTO.builder()
            .mensaje("Error de validación en base de datos")
            .errores(errores)
            .codigo(HttpStatus.BAD_REQUEST.value())
            .build();

        logger.warn("Constraint violation: {}", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, String> errores = new HashMap<>();
        errores.put("recurso", ex.getMessage());

        ErrorResponseDTO response = ErrorResponseDTO.builder()
            .mensaje("Recurso no encontrado")
            .errores(errores)
            .codigo(HttpStatus.NOT_FOUND.value())
            .build();

        logger.warn("Entity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFound(NoResourceFoundException ex) {
        // treat missing static resources (like favicon.ico) as not an error
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        Map<String, String> errores = new HashMap<>();
        errores.put("error", "Error interno del servidor");

        ErrorResponseDTO response = ErrorResponseDTO.builder()
            .mensaje("Error interno")
            .errores(errores)
            .codigo(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .build();

        logger.error("Unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
