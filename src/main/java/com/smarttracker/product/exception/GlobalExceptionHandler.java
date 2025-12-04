package com.smarttracker.product.exception;

import com.smarttracker.product.dto.ApiResponseDTO;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ApiResponseDTO<Map<String, String>> response = ApiResponseDTO.error(
                "Validation failed", 
                "VALIDATION_ERROR"
        ).toBuilder()
                .data(errors)
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        
        ApiResponseDTO<String> response = ApiResponseDTO.error(
                ex.getMessage(), 
                "CONSTRAINT_VIOLATION"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleDuplicateResourceException(
            DuplicateResourceException ex) {
        
        ApiResponseDTO<String> response = ApiResponseDTO.error(
                ex.getMessage(), 
                ex.getErrorCode()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleBadCredentialsException(
            BadCredentialsException ex) {
        
        ApiResponseDTO<String> response = ApiResponseDTO.error(
                "Invalid credentials", 
                "INVALID_CREDENTIALS"
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        
        ApiResponseDTO<String> response = ApiResponseDTO.error(
                ex.getMessage(), 
                "ILLEGAL_ARGUMENT"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<String>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        
        ApiResponseDTO<String> response = ApiResponseDTO.error(
                "An unexpected error occurred. Please try again later.", 
                "INTERNAL_SERVER_ERROR"
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
     public ResponseEntity<ApiResponseDTO<String>> handleAuthenticationFailedException(
        AuthenticationFailedException ex) {

     ApiResponseDTO<String> response = ApiResponseDTO.error(
        ex.getMessage(), 
        ex.getErrorCode()
 );
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
}
}