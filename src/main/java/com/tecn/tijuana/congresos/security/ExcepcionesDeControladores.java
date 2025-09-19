package com.tecn.tijuana.congresos.security;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExcepcionesDeControladores {


  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<String> handleResponseStatusException (
    ResponseStatusException ex
  ) {
    return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
  }



  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolation (
    ConstraintViolationException ex
  ) {
    // Aux.
    Map<String, String> errores = new HashMap<>();

    // Recorrer los errores de validacion y construir la respuesta.
    for (ConstraintViolation<?> violacion : ex.getConstraintViolations()) {
      String campo = violacion.getPropertyPath().toString();
      String mensaje = violacion.getMessage();
      errores.put(campo, mensaje);
    }

    // Retornar la respuesta de error con todas las validaciones.
    return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
  }
}
