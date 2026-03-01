package com.sostecnible.TaskManager.infraestructure.controller.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sostecnible.TaskManager.domain.exceptions.BusinessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejar excepciones de negocio
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            null, 
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 2. manejo de errores validacion
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Error de validación en los campos",
            errors,
            System.currentTimeMillis()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 3. Manejar errores inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
      ex.printStackTrace();

      ErrorResponse error = new ErrorResponse(
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          "Ocurrió un error interno en el servidor",
          null, 
          System.currentTimeMillis());
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // 4. Manejar cuando (JSON vacío)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleReadableException(HttpMessageNotReadableException ex) {
      ErrorResponse error = new ErrorResponse(
          HttpStatus.BAD_REQUEST.value(),
          "El cuerpo de la petición (JSON) es obligatorio o está mal formado",
          null, 
          System.currentTimeMillis());
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //5. Msnejo de error dato en Base de datos
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String mensaje = "Error de integridad de datos: Verifique que todos los campos obligatorios estén presentes y no estén duplicados.";

        ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            mensaje,
            null, 
            System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}