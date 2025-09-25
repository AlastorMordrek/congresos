package com.tecn.tijuana.congresos.security;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


/**
 * Clase controladora global de excepciones para todos los controladores REST.
 * <p>
 * Proporciona manejo centralizado de excepciones para toda la aplicacion,
 * transformando excepciones tecnicas en respuestas HTTP estandarizadas con
 * formato Problem Details (RFC 7807).
 * </p>
 *
 * @author Alastor Mordrek (para Tecnologico Nacional de Mexico campus Tijuana)
 * @version 1.0
 * @since 2025
 */
@ControllerAdvice
public class ExcepcionesDeControladores {


  /**
   * Maneja excepciones de tipo {@link ResponseStatusException} transformandolas
   * en respuestas HTTP estandarizadas con formato Problem Details.
   * <p>
   * Este metodo captura excepciones que contienen explicitamente un codigo de
   * estado HTTP y un mensaje de razon, convirtiendolas en una respuesta
   * estructurada que incluye timestamp y detalles del error.
   * </p>
   *
   * @param ex
   * Excepcion de tipo {@link ResponseStatusException} que contiene el codigo
   * de estado HTTP y el mensaje de error.
   *
   * @return
   * Respuesta HTTP con formato Problem Details que incluye:
   * <ul>
   *   <li>Codigo de estado HTTP de la excepcion</li>
   *   <li>Titulo y detalle del error</li>
   *   <li>Timestamp del momento en que ocurrio el error</li>
   * </ul>
   */
  @ExceptionHandler(ResponseStatusException.class)

  public ResponseEntity<ProblemDetail> handleResponseStatusException (
    ResponseStatusException ex
  ) {
    ProblemDetail pd = ProblemDetail.forStatus(ex.getStatusCode());

    pd.setTitle(ex.getReason());
    pd.setDetail(ex.getReason());

    pd.setProperty("timestamp", Instant.now());

    return new ResponseEntity<>(pd, ex.getStatusCode());
  }



  /**
   * Maneja excepciones de violacion de constraints de validacion
   * transformandolas en respuestas HTTP estandarizadas con formato Problem
   * Details.
   * <p>
   * Este metodo captura excepciones generadas cuando falla la validacion de
   * parametros de entrada, proporcionando detalles especificos sobre cada
   * campo que no cumple con las restricciones definidas.
   * </p>
   *
   * @param ex
   * Excepcion de tipo {@link ConstraintViolationException} que contiene todas
   * las violaciones de constraints detectadas durante la validacion.
   *
   * @return
   * Respuesta HTTP con formato Problem Details que incluye:
   * <ul>
   *   <li>Codigo de estado HTTP 400 (Bad Request)</li>
   *   <li>Titulo y detalle general del error</li>
   *   <li>Timestamp del momento en que ocurrio el error</li>
   *   <li>
   *     Mapa detallado de campos con errores y sus mensajes correspondientes
   *   </li>
   * </ul>
   */
  @ExceptionHandler(ConstraintViolationException.class)

  public ResponseEntity<Map<String, String>> handleConstraintViolation (
    ConstraintViolationException ex
  ) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

    pd.setTitle("Peticion malformada");
    pd.setDetail("Se rompieron algunas restricciones en la peticion");

    // Aux.
    Map<String, String> errores = new HashMap<>();

    // Recorrer los errores de validacion y construir la respuesta.
    for (ConstraintViolation<?> violacion : ex.getConstraintViolations()) {
      String campo = violacion.getPropertyPath().toString();
      String mensaje = violacion.getMessage();
      errores.put(campo, mensaje);
    }

    pd.setProperty("timestamp", Instant.now());
    pd.setProperty("campos", errores);

    // Retornar la respuesta de error con todas las validaciones.
    return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
  }


  /**
   * Maneja excepciones de tipo {@link AccessDeniedException} transformandolas
   * en respuestas HTTP estandarizadas con formato Problem Details.
   * <p>
   * Este metodo captura excepciones que contienen explicitamente un codigo de
   * estado HTTP y un mensaje de razon, convirtiendolas en una respuesta
   * estructurada que incluye timestamp y detalles del error.
   * </p>
   *
   * @param ex
   * Excepcion de tipo {@link AccessDeniedException} que contiene el codigo
   * de estado HTTP y el mensaje de error.
   *
   * @return
   * Respuesta HTTP con formato Problem Details que incluye:
   * <ul>
   *   <li>Codigo de estado HTTP de la excepcion</li>
   *   <li>Titulo y detalle del error</li>
   *   <li>Timestamp del momento en que ocurrio el error</li>
   * </ul>
   */
  @ExceptionHandler(AccessDeniedException.class)

  public ResponseEntity<ProblemDetail> handleAccessDeniedException (
    AccessDeniedException ex
  ) {
    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

    pd.setTitle("Acceso denegado");
    pd.setDetail("No tiene permiso de realizar esa operacion");

    pd.setProperty("timestamp", Instant.now());

    return new ResponseEntity<>(pd, HttpStatus.UNAUTHORIZED);
  }



  /**
   * Maneja cualquier excepcion no capturada por los manejadores especificos,
   * transformandola en una respuesta HTTP estandarizada con formato Problem
   * Details.
   * <p>
   * Este metodo actua como capturador global de excepciones, asegurando que
   * ningun error quede sin una respuesta estructurada. Incluye informacion
   * basica sobre el error sin exponer detalles sensibles de implementacion.
   * </p>
   *
   * @param ex
   * Excepcion de cualquier tipo no manejada por los manejadores especificos.
   *
   * @return
   * Respuesta HTTP con formato Problem Details que incluye:
   * <ul>
   *   <li>Codigo de estado HTTP 500 (Internal Server Error)</li>
   *   <li>Titulo y detalle generico del error</li>
   *   <li>Timestamp del momento en que ocurrio el error</li>
   *   <li>Tipo de excepcion generada</li>
   * </ul>
   */
  @ExceptionHandler(Exception.class)

  public ResponseEntity<ProblemDetail> handleAllExceptions (
    Exception ex
  )
    throws URISyntaxException {

    ProblemDetail pd =
      ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

    pd.setType(new URI("/probs/error-no-controlado"));
    pd.setTitle("Internal Server Error");
    pd.setDetail("An unexpected error occurred");

    pd.setProperty("timestamp", Instant.now());
    pd.setProperty("exceptionType", ex.getClass().getSimpleName());

    return new ResponseEntity<>(pd, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
