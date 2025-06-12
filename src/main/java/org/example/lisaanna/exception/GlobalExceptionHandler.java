package org.example.lisaanna.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * En global handler för alla skräddarsydda Exceptions som finns i kodbasen.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @param exception är en instans av Exceptionklassen där ett custom exception definieras
     * @return ett ResponseEntity returneras om Exceptionet kastas
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

/*
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (existing, replacement) -> existing)); // Om flera fel på samma fält
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }*/
}
