package soda_roja.backend.exception;

import jakarta.persistence.EntityNotFoundException;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	/**
	 * Spring busca el handler más específico para la excepción lanzada:
	 * Es muy importante por este motivo el orden en que definimos las excepciones en el GlobalExceptionHandler, ya que Spring las evalúa en orden de especificidad:
 •  Handlers específicos se ejecutan antes (más prioritarios)
	•  handleGenericException es la red de seguridad final

Spring maneja toda esta lógica de búsqueda y resolución de handlers automáticamente.
 Tú solo necesitas definirlos con las anotaciones @ExceptionHandler en la clase @RestControllerAdvice.*/
	
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//Captura errores de validación de argumentos. Por ejemplo: sería un statudo 400 Bad Request cuando el 
//cliente envía datos que no cumplen con las restricciones de validación definidas en el DTO o entidad.
 //El mensaje de error se extrae del primer error de validación encontrado y se devuelve al cliente en un 
    //formato consistente utilizando la clase ApiError.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Datos invalidos");
        
        logger.warn("Validation error: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(message));
    }
// Captura errores de validación de restricciones a nivel de base de datos.
// Ejemplo: se intenta cargar un usuario con un mail que ya esta registrado. Status code 400 bad request también
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("Datos invalidos");
        
        logger.warn("Constraint violation: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(message));
    }
    
//Captura error de recurso no encontrado
    //Ejemplo se busca un camion con una id que no existe. Status code 404 not found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
        logger.warn("Entity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("Recurso no encontrado"));
    }
// Captura cualquier excepcion imprevista que no haya sido manejada por los controladores.
    //Status code 500 internal server error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("Error interno del servidor"));
    }
}
