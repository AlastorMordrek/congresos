package com.tecn.tijuana.congresos.boletos.boleto;

import com.tecn.tijuana.congresos.boletos.boleto.dto.BoletoInscribirseDto;
import com.tecn.tijuana.congresos.boletos.boleto.dto.RegistroBoletoDto;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.security.ExpresionSeguridad;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Clase controladora principal de la entidad.
 * */
@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/boletos/boleto")
@Validated
public class BoletoController {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final BoletoService bolSvc;


  /**
   * CONSTRUCTOR principal de esta clase/controller, usado principalmente por
   * Spring para el funcionamiento de la app.
   *
   * @param bolSvc
   * Objeto de la capa de servicio de la entidad de BOLETO.
   */
  public BoletoController (
    BoletoService bolSvc
  ) {
    this.bolSvc = bolSvc;
  }



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite a un ALUMNO inscribirse a CONGRESO, generando su BOLETO.
   *
   * @param dto
   * Objeto con los datos del BOLETO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El nuevo registro creado.
   */
  @PostMapping("inscribirse")

  @Operation(
    summary = "Inscribirse a congreso",
    description = "Permite a un alumno inscribirse a un congreso, " +
      "generando su boleto.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Datos para la inscripcion",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = BoletoInscribirseDto.class),
        examples = @ExampleObject(
          name = "Inscribirse",
          description = "Ejemplo para inscribirse a un congreso",
          value = """
{
  "congresoId": 1
}
"""
        )
      )
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "201",
      description = "Inscripcion exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto generado",
          value = """
{
  "id"                  : 1,
  "folio"               : "A1B2C3",
  "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
  
  "fechaCreacion"       : "2025-10-11T01:56:11",
  "creadorId"           : 1,
  
  "congresoId"          : 1,
  "congresoNombre"      : "Congreso de Tecnologia",
  "congresoFechaInicio" : "2025-10-11T10:00:00",
  "congresoFechaFin"    : "2025-10-11T18:00:00",
  "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
  
  "alumnoId"            : 2,
  "alumnoNoControl"     : "12345678",
  "alumnoNombre"        : "Juan Perez Garcia",
  
  "excedente"           : false,
  "pagado"              : false,
  "usuarioEditoPagado"  : null,
  
  "cancelado"           : false,
  "usado"               : false,
  "asistencias"         : 0
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Error de validacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Parametros incorrectos",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for object='boletoInscribirseDto'",
  "instance": "/api/v1/boletos/boleto/inscribirse",
  "timestamp": "2025-10-13T04:15:16",
  "campos": {
    "boletoInscribirseDto.congresoId": "No puede ser nulo"
  }
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/inscribirse",
  "timestamp": "2025-10-13T04:16:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/inscribirse",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize("hasRole('ALUMNO')")

  public ResponseEntity<Boleto> inscribirse (

    @RequestBody
    BoletoInscribirseDto dto,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.inscribirse(actor, dto),
      HttpStatus.CREATED);
  }



  /**
   * Permite al personal autorizado inscribir un ALUMNO a un CONGRESO, generando
   * su BOLETO.
   *
   * @param dto
   * Objeto con los datos del BOLETO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El nuevo registro creado.
   */
  @PostMapping("inscribir")

  @Operation(
    summary = "Inscribir alumno a congreso",
    description = "Permite al personal autorizado inscribir un alumno a un " +
      "congreso, generando su boleto.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Datos para la inscripcion del alumno",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = RegistroBoletoDto.class),
        examples = {
          @ExampleObject(
            name = "Inscribir usando ID",
            description = "Ejemplo para inscribir un alumno a un congreso" +
              " usando su ID",
            value = """
              {
                "congresoId": 1,
                "alumnoId": 2
              }
              """
          ),
          @ExampleObject(
            name = "Inscribir usando #Control",
            description = "Ejemplo para inscribir un alumno a un congreso" +
              " usando su #Control",
            value = """
              {
                "congresoId": 1,
                "alumnoNoControl": "12345678"
              }
              """
          ),
          @ExampleObject(
            name =
              "Inscribir usando #Control como excedente",
            description = "Ejemplo para inscribir un alumno a un congreso" +
              " usando su #Control cuando el evento ya tiene cupo lleno," +
              " marcando su boleto como excedente",
            value = """
              {
                "congresoId": 1,
                "alumnoNoControl": "12345678",
                "registrarComoExcedente": true
              }
              """
          )
        }
      )
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "201",
      description = "Inscripcion exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto generado",
          value = """
{
  "id"                  : 1,
  "folio"               : "A1B2C3",
  "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
  
  "fechaCreacion"       : "2025-10-11T01:56:11",
  "creadorId"           : 1,
  
  "congresoId"          : 1,
  "congresoNombre"      : "Congreso de Tecnologia",
  "congresoFechaInicio" : "2025-10-11T10:00:00",
  "congresoFechaFin"    : "2025-10-11T18:00:00",
  "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
  
  "alumnoId"            : 2,
  "alumnoNoControl"     : "12345678",
  "alumnoNombre"        : "Juan Perez Garcia",
  
  "excedente"           : false,
  "pagado"              : false,
  "usuarioEditoPagado"  : null,
  
  "cancelado"           : false,
  "usado"               : false,
  "asistencias"         : 0
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Error de validacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Parametros incorrectos",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for object='registroBoletoDto'",
  "instance": "/api/v1/boletos/boleto/inscribir",
  "timestamp": "2025-10-13T04:15:16",
  "campos": {
    "registroBoletoDto.alumnoNoControl": "debe tener 8 caracteres"
  }
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/inscribir",
  "timestamp": "2025-10-13T04:16:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/inscribir",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.INSCRIBIR_ALUMNOS)

  public ResponseEntity<Boleto> inscribir (

    @RequestBody
    RegistroBoletoDto dto,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.inscribir(actor, dto),
      HttpStatus.CREATED);
  }



  /**
   * Permite a un ALUMNO cancelar un BOLETO propio.
   *
   * @param id
   * ID del registro a CANCELAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro cancelado.
   */
  @PatchMapping("cancelar/mio/{id}")

  @Operation(
    summary = "Cancelar boleto propio",
    description = "Permite a un alumno cancelar un boleto de su propiedad.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Cancelacion exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto cancelado",
          value = """
{
  "id"                  : 1,
  "folio"               : "A1B2C3",
  "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
  
  "fechaCreacion"       : "2025-10-11T01:56:11",
  "creadorId"           : 1,
  
  "congresoId"          : 1,
  "congresoNombre"      : "Congreso de Tecnologia",
  "congresoFechaInicio" : "2025-10-11T10:00:00",
  "congresoFechaFin"    : "2025-10-11T18:00:00",
  "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
  
  "alumnoId"            : 2,
  "alumnoNoControl"     : "12345678",
  "alumnoNombre"        : "Juan Perez Garcia",
  
  "excedente"           : false,
  "pagado"              : false,
  "usuarioEditoPagado"  : null,
  
  "cancelado"           : true,
  "usado"               : false,
  "asistencias"         : 0
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/cancelar/mio/1",
  "timestamp": "2025-10-13T04:16:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "No encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Boleto no existe o no pertenece al alumno",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el boleto con ID 1",
  "instance": "/api/v1/boletos/boleto/cancelar/mio/1",
  "timestamp": "2025-10-13T04:27:17"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/cancelar/mio/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize("hasRole('ALUMNO')")

  public ResponseEntity<Boleto> cancelarMio (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.cancelarMio(actor, id),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado CANCELAR un BOLETO.
   *
   * @param id
   * ID del registro a CANCELAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro CANCELADO.
   */
  @PatchMapping("cancelar/{id}")

  @Operation(
    summary = "Cancelar boleto",
    description = "Permite al personal autorizado cancelar un boleto.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Cancelacion exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto cancelado",
          value = """
{
  "id"                  : 1,
  "folio"               : "A1B2C3",
  "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
  
  "fechaCreacion"       : "2025-10-11T01:56:11",
  "creadorId"           : 1,
  
  "congresoId"          : 1,
  "congresoNombre"      : "Congreso de Tecnologia",
  "congresoFechaInicio" : "2025-10-11T10:00:00",
  "congresoFechaFin"    : "2025-10-11T18:00:00",
  "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
  
  "alumnoId"            : 2,
  "alumnoNoControl"     : "12345678",
  "alumnoNombre"        : "Juan Perez Garcia",
  
  "excedente"           : false,
  "pagado"              : false,
  "usuarioEditoPagado"  : null,
  
  "cancelado"           : true,
  "usado"               : false,
  "asistencias"         : 0
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/cancelar/1",
  "timestamp": "2025-10-13T04:16:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "No encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Boleto no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el boleto con ID 1",
  "instance": "/api/v1/boletos/boleto/cancelar/1",
  "timestamp": "2025-10-13T04:27:17"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/cancelar/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.GESTIONAR_BOLETOS)

  public ResponseEntity<Boleto> cancelar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.cancelado(actor, id, Boleto.CANCELADO),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado RESTAURAR un BOLETO.
   *
   * @param id
   * ID del registro a RESTAURAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro RESTAURADO.
   */
  @PatchMapping("restaurar/{id}")

  @Operation(
    summary = "Restaurar boleto",
    description = "Permite al personal autorizado restaurar un boleto.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Restauracion exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto restaurado",
          value = """
{
  "id"                  : 1,
  "folio"               : "A1B2C3",
  "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
  
  "fechaCreacion"       : "2025-10-11T01:56:11",
  "creadorId"           : 1,
  
  "congresoId"          : 1,
  "congresoNombre"      : "Congreso de Tecnologia",
  "congresoFechaInicio" : "2025-10-11T10:00:00",
  "congresoFechaFin"    : "2025-10-11T18:00:00",
  "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
  
  "alumnoId"            : 2,
  "alumnoNoControl"     : "12345678",
  "alumnoNombre"        : "Juan Perez Garcia",
  
  "excedente"           : false,
  "pagado"              : false,
  "usuarioEditoPagado"  : null,
  
  "cancelado"           : false,
  "usado"               : false,
  "asistencias"         : 0
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/restaurar/1",
  "timestamp": "2025-10-13T05:15:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "No encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Boleto no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el boleto con ID 1",
  "instance": "/api/v1/boletos/boleto/restaurar/1",
  "timestamp": "2025-10-13T05:16:17"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/restaurar/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.GESTIONAR_BOLETOS)

  public ResponseEntity<Boleto> restaurar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.cancelado(actor, id, Boleto.RESTAURADO),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado marcar un BOLETO como PAGADO.
   *
   * @param id
   * ID del registro.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro actualizado.
   */
  @PatchMapping("pagado/{id}")

  @Operation(
    summary = "Marcar boleto como PAGADO",
    description =
      "Permite al personal autorizado marcar un boleto como PAGADO.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Edicion exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto editado",
          value = """
{
  "id"                  : 1,
  "folio"               : "A1B2C3",
  "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
  
  "fechaCreacion"       : "2025-10-11T01:56:11",
  "creadorId"           : 1,
  
  "congresoId"          : 1,
  "congresoNombre"      : "Congreso de Tecnologia",
  "congresoFechaInicio" : "2025-10-11T10:00:00",
  "congresoFechaFin"    : "2025-10-11T18:00:00",
  "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
 
  "alumnoId"            : 2,
  "alumnoNoControl"     : "12345678",
  "alumnoNombre"        : "Juan Perez Garcia",
  
  "excedente"           : false,
  "pagado"              : true,
  "usuarioEditoPagado"  : 123,
  
  "cancelado"           : true,
  "usado"               : false,
  "asistencias"         : 0
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/cancelar/1",
  "timestamp": "2025-10-13T04:16:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "No encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Boleto no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el boleto con ID 1",
  "instance": "/api/v1/boletos/boleto/cancelar/1",
  "timestamp": "2025-10-13T04:27:17"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/cancelar/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.GESTIONAR_BOLETOS)

  public ResponseEntity<Boleto> pagado (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.pagado(actor, id, Boleto.PAGADO),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado marcar un BOLETO como NO_PAGADO.
   *
   * @param id
   * ID del registro.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro actualizado.
   */
  @PatchMapping("noPagado/{id}")

  @Operation(
    summary = "Marcar boleto como NO_PAGADO",
    description =
      "Permite al personal autorizado marcar un boleto como NO_PAGADO.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Edicion exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto editado",
          value = """
{
  "id"                  : 1,
  "folio"               : "A1B2C3",
  "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
  
  "fechaCreacion"       : "2025-10-11T01:56:11",
  "creadorId"           : 1,
  
  "congresoId"          : 1,
  "congresoNombre"      : "Congreso de Tecnologia",
  "congresoFechaInicio" : "2025-10-11T10:00:00",
  "congresoFechaFin"    : "2025-10-11T18:00:00",
  "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
  
  "alumnoId"            : 2,
  "alumnoNoControl"     : "12345678",
  "alumnoNombre"        : "Juan Perez Garcia",
  
  "excedente"           : false,
  "pagado"              : false,
  "usuarioEditoPagado"  : 123,
  
  "cancelado"           : false,
  "usado"               : false,
  "asistencias"         : 0
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/restaurar/1",
  "timestamp": "2025-10-13T05:15:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "No encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Boleto no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el boleto con ID 1",
  "instance": "/api/v1/boletos/boleto/restaurar/1",
  "timestamp": "2025-10-13T05:16:17"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/restaurar/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.GESTIONAR_BOLETOS)

  public ResponseEntity<Boleto> noPagado (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.pagado(actor, id, Boleto.NO_PAGADO),
      HttpStatus.OK);
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Consulta los BOLETOS indiscriminadamente usando una busqueda de texto.
   *
   * @param txt {@code [""]}
   * Texto de busqueda.
   *
   * @param page {@code [0]}
   * Numero de pagina.
   *
   * @param pageSize {@code [10]}
   * Tamano de pagina.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("buscar")

  @Operation(
    summary = "Buscar boletos",
    description = "Permite al personal autorizado buscar boletos usando " +
      "filtro de texto.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Busqueda exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boletos encontrados",
          value = """
[
  {
    "id"                  : 1,
    "folio"               : "A1B2C3",
    "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
    
    "fechaCreacion"       : "2025-10-11T01:56:11",
    "creadorId"           : 1,
    
    "congresoId"          : 1,
    "congresoNombre"      : "Congreso de Tecnologia",
    "congresoFechaInicio" : "2025-10-11T10:00:00",
    "congresoFechaFin"    : "2025-10-11T18:00:00",
    "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
    
    "alumnoId"            : 2,
    "alumnoNoControl"     : "12345678",
    "alumnoNombre"        : "Juan Perez Garcia",
  
    "excedente"           : false,
    "pagado"              : false,
    "usuarioEditoPagado"  : null,
    
    "cancelado"           : false,
    "usado"               : false,
    "asistencias"         : 0
  }
]
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Parametros invalidos",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Validacion fallida",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for parameters",
  "instance": "/api/v1/boletos/boleto/buscar",
  "timestamp": "2025-10-13T06:25:10",
  "campos": {
    "txt": "size must be between 1 and 30",
    "page": "must be greater than or equal to 0",
    "pageSize": "must be less than or equal to 100"
  }
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/buscar",
  "timestamp": "2025-10-13T06:26:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/buscar",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_BOLETOS_AJENOS)

  public ResponseEntity<List<Boleto>> buscar (

    @RequestParam(name = "txt", required = false, defaultValue = "")
    @Size(min = 1, max = 30)
    String txt,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      bolSvc.buscar(txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Permite a un ALUMNO consultar sus boletos.
   *
   * @param page {@code [0]}
   * Numero de pagina.
   *
   * @param pageSize {@code [10]}
   * Tamano de pagina.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("mios")

  @Operation(
    summary = "Consultar boletos propios",
    description = "Obtiene los boletos del usuario autenticado.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boletos del usuario",
          value = """
[
  {
    "id"                  : 1,
    "folio"               : "A1B2C3",
    "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
    
    "fechaCreacion"       : "2025-10-11T01:56:11",
    "creadorId"           : 1,
    
    "congresoId"          : 1,
    "congresoNombre"      : "Congreso de Tecnologia",
    "congresoFechaInicio" : "2025-10-11T10:00:00",
    "congresoFechaFin"    : "2025-10-11T18:00:00",
    "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
    
    "alumnoId"            : 2,
    "alumnoNoControl"     : "12345678",
    "alumnoNombre"        : "Juan Perez Garcia",
  
    "excedente"           : false,
    "pagado"              : false,
    "usuarioEditoPagado"  : null,
    
    "cancelado"           : false,
    "usado"               : false,
    "asistencias"         : 0
  }
]
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Parametros invalidos",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Validacion fallida",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for parameters",
  "instance": "/api/v1/boletos/boleto/mios",
  "timestamp": "2025-10-13T03:45:10",
  "campos": {
    "page": "must be greater than or equal to 0",
    "pageSize": "must be less than or equal to 100"
  }
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/mios",
  "timestamp": "2025-10-13T03:46:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/mios",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize("hasRole('ALUMNO')")

  public ResponseEntity<List<Boleto>> mios (

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.qMios(actor, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Permite a un ALUMNO consultar sus boletos usando una busqueda de texto.
   *
   * @param txt {@code [""]}
   * Texto de busqueda.
   *
   * @param page {@code [0]}
   * Numero de pagina.
   *
   * @param pageSize {@code [10]}
   * Tamano de pagina.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("buscar/mios")

  @Operation(
    summary = "Buscar mis boletos",
    description = "Permite a un alumno buscar sus boletos usando filtro " +
      "de texto.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Busqueda exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boletos encontrados",
          value = """
[
  {
    "id"                  : 1,
    "folio"               : "A1B2C3",
    "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
    
    "fechaCreacion"       : "2025-10-11T01:56:11",
    "creadorId"           : 1,
    
    "congresoId"          : 1,
    "congresoNombre"      : "Congreso de Tecnologia",
    "congresoFechaInicio" : "2025-10-11T10:00:00",
    "congresoFechaFin"    : "2025-10-11T18:00:00",
    "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
    
    "alumnoId"            : 2,
    "alumnoNoControl"     : "12345678",
    "alumnoNombre"        : "Juan Perez Garcia",
  
    "excedente"           : false,
    "pagado"              : false,
    "usuarioEditoPagado"  : null,
    
    "cancelado"           : false,
    "usado"               : false,
    "asistencias"         : 0
  }
]
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Parametros invalidos",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Validacion fallida",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for parameters",
  "instance": "/api/v1/boletos/boleto/buscar/mios",
  "timestamp": "2025-10-13T06:25:10",
  "campos": {
    "txt": "size must be between 1 and 30",
    "page": "must be greater than or equal to 0",
    "pageSize": "must be less than or equal to 100"
  }
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/buscar/mios",
  "timestamp": "2025-10-13T06:26:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/buscar/mios",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize("hasRole('ALUMNO')")

  public ResponseEntity<List<Boleto>> buscarMios (

    @RequestParam(name = "txt", required = false, defaultValue = "")
    @Size(min = 1, max = 30)
    String txt,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.buscarMios(actor, txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta BOLETO publicamente usando su Folio Largo.
   *
   * @param folioLargo
   * Folio Largo del BOLETO.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping("publico/boleto/{folioLargo}")

  @Operation(
    summary = "Consultar boleto por folio largo",
    description = "Consulta un boleto publicamente usando su folio largo.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto encontrado",
          value = """
{
  "id"                  : 1,
  "folio"               : "A1B2C3",
  "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
  
  "fechaCreacion"       : "2025-10-11T01:56:11",
  "creadorId"           : 1,
  
  "congresoId"          : 1,
  "congresoNombre"      : "Congreso de Tecnologia",
  "congresoFechaInicio" : "2025-10-11T10:00:00",
  "congresoFechaFin"    : "2025-10-11T18:00:00",
  "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
  
  "alumnoId"            : 2,
  "alumnoNoControl"     : "12345678",
  "alumnoNombre"        : "Juan Perez Garcia",
  
  "excedente"           : false,
  "pagado"              : false,
  "usuarioEditoPagado"  : null,
  
  "cancelado"           : false,
  "usado"               : false,
  "asistencias"         : 0
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "No encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Boleto no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el boleto con folio largo A1B2C3D4E5F6G7H8I9J0",
  "instance": "/api/v1/boletos/boleto/publico/boleto/A1B2C3D4E5F6G7H8I9J0",
  "timestamp": "2025-10-13T04:27:17"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/publico/boleto/A1B2C3D4E5F6G7H8I9J0",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<Boleto> qFolioLargo (

    @PathVariable("folioLargo")
    String folioLargo
  ) {
    return new ResponseEntity<>(
      bolSvc.afirmarFolioLargo(folioLargo),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado consultar BOLETOS via Folio.
   *
   * @param folio
   * Folio del BOLETO.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping("folio/{folio}")

  @Operation(
    summary = "Consultar boleto por folio",
    description = "Permite al personal autorizado consultar un boleto " +
      "usando su folio.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto encontrado",
          value = """
{
  "id"                  : 1,
  "folio"               : "A1B2C3",
  "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
  
  "fechaCreacion"       : "2025-10-11T01:56:11",
  "creadorId"           : 1,
  
  "congresoId"          : 1,
  "congresoNombre"      : "Congreso de Tecnologia",
  "congresoFechaInicio" : "2025-10-11T10:00:00",
  "congresoFechaFin"    : "2025-10-11T18:00:00",
  "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
  
  "alumnoId"            : 2,
  "alumnoNoControl"     : "12345678",
  "alumnoNombre"        : "Juan Perez Garcia",
  
  "excedente"           : false,
  "pagado"              : false,
  "usuarioEditoPagado"  : null,
  
  "cancelado"           : false,
  "usado"               : false,
  "asistencias"         : 0
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/folio/A1B2C3",
  "timestamp": "2025-10-13T06:36:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "No encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Boleto no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el boleto con folio A1B2C3",
  "instance": "/api/v1/boletos/boleto/folio/A1B2C3",
  "timestamp": "2025-10-13T04:27:17"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/folio/A1B2C3",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_BOLETOS_AJENOS)

  public ResponseEntity<Boleto> qFolio (

    @PathVariable("folio")
    String folio
  ) {
    return new ResponseEntity<>(
      bolSvc.afirmarFolio(folio),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado consultar BOLETOS via ID.
   *
   * @param id
   * ID del BOLETO.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping("{id}")

  @Operation(
    summary = "Consultar boleto por ID",
    description = "Permite al personal autorizado consultar un boleto " +
      "usando su ID.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto encontrado",
          value = """
{
  "id"                  : 1,
  "folio"               : "A1B2C3",
  "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
  
  "fechaCreacion"       : "2025-10-11T01:56:11",
  "creadorId"           : 1,
  
  "congresoId"          : 1,
  "congresoNombre"      : "Congreso de Tecnologia",
  "congresoFechaInicio" : "2025-10-11T10:00:00",
  "congresoFechaFin"    : "2025-10-11T18:00:00",
  "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
  
  "alumnoId"            : 2,
  "alumnoNoControl"     : "12345678",
  "alumnoNombre"        : "Juan Perez Garcia",
  
  "excedente"           : false,
  "pagado"              : false,
  "usuarioEditoPagado"  : null,
  
  "cancelado"           : false,
  "usado"               : false,
  "asistencias"         : 0
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/1",
  "timestamp": "2025-10-13T06:36:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "No encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Boleto no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el boleto con folio A1B2C3",
  "instance": "/api/v1/boletos/boleto/1",
  "timestamp": "2025-10-13T04:27:17"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_BOLETOS_AJENOS)

  public ResponseEntity<Boleto> qId (

    @PathVariable("id")
    Long id
  ) {
    return new ResponseEntity<>(
      bolSvc.afirmar(id),
      HttpStatus.OK);
  }



  /**
   * Consulta los BOLETOS de un CONGRESO indiscriminadamente.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param page {@code [0]}
   * Numero de pagina.
   *
   * @param pageSize {@code [10]}
   * Tamano de pagina.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("congreso/{congresoId}")

  @Operation(
    summary = "Consultar boletos por congreso",
    description = "Permite al personal autorizado consultar los boletos " +
      "de un congreso especifico.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boletos encontrados",
          value = """
[
  {
    "id"                  : 1,
    "folio"               : "A1B2C3",
    "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
    
    "fechaCreacion"       : "2025-10-11T01:56:11",
    "creadorId"           : 1,
    
    "congresoId"          : 1,
    "congresoNombre"      : "Congreso de Tecnologia",
    "congresoFechaInicio" : "2025-10-11T10:00:00",
    "congresoFechaFin"    : "2025-10-11T18:00:00",
    "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
    
    "alumnoId"            : 2,
    "alumnoNoControl"     : "12345678",
    "alumnoNombre"        : "Juan Perez Garcia",
  
    "excedente"           : false,
    "pagado"              : false,
    "usuarioEditoPagado"  : null,
    
    "cancelado"           : false,
    "usado"               : false,
    "asistencias"         : 0
  }
]
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Parametros invalidos",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Validacion fallida",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for parameters",
  "instance": "/api/v1/boletos/boleto/congreso/1",
  "timestamp": "2025-10-13T06:15:10",
  "campos": {
    "page": "must be greater than or equal to 0",
    "pageSize": "must be less than or equal to 100"
  }
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/congreso/1",
  "timestamp": "2025-10-13T06:16:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/congreso/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_BOLETOS_AJENOS)

  public ResponseEntity<List<Boleto>> qIdCongreso (

    @PathVariable("congresoId")
    Long congresoId,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      bolSvc.qIdCongreso(congresoId, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta los BOLETOS de un CONGRESO segun el estatus del BOLETO
   * especificado.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param page {@code [0]}
   * Numero de pagina.
   *
   * @param pageSize {@code [10]}
   * Tamano de pagina.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("congreso/{congresoId}/cancelados")

  @Operation(
    summary = "Consultar boletos cancelados de congreso",
    description = "Permite al personal autorizado consultar los boletos " +
      "cancelados de un congreso especifico.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boletos encontrados",
          value = """
[
  {
    "id"                  : 3,
    "folio"               : "G7H8I9",
    "folioLargo"          : "U1V2W3X4Y5Z6A7B8C9D0",
    
    "fechaCreacion"       : "2025-10-11T03:45:00",
    "creadorId"           : 1,
    
    "congresoId"          : 1,
    "congresoNombre"      : "Congreso de Tecnologia",
    "congresoFechaInicio" : "2025-10-11T10:00:00",
    "congresoFechaFin"    : "2025-10-11T18:00:00",
    "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
    
    "alumnoId"            : 4,
    "alumnoNoControl"     : "11112222",
    "alumnoNombre"        : "Carlos Rodriguez Martinez",
  
    "excedente"           : false,
    "pagado"              : false,
    "usuarioEditoPagado"  : null,
    
    "cancelado"           : true,
    "usado"               : false,
    "asistencias"         : 0
  }
]
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Parametros invalidos",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Validacion fallida",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for parameters",
  "instance": "/api/v1/boletos/boleto/congreso/1/cancelados",
  "timestamp": "2025-10-13T06:15:10",
  "campos": {
    "page": "must be greater than or equal to 0",
    "pageSize": "must be less than or equal to 100"
  }
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/congreso/1/cancelados",
  "timestamp": "2025-10-13T06:16:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/congreso/1/cancelados",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_BOLETOS_AJENOS)

  public ResponseEntity<List<Boleto>> qIdCongresoCancelados (

    @PathVariable("congresoId")
    Long congresoId,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      bolSvc.qIdCongresoCancelado(
        congresoId, Boleto.CANCELADO, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta los BOLETOS de un CONGRESO segun el estatus del BOLETO
   * especificado.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param page {@code [0]}
   * Numero de pagina.
   *
   * @param pageSize {@code [10]}
   * Tamano de pagina.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("congreso/{congresoId}/no-cancelados")

  @Operation(
    summary = "Consultar boletos no cancelados de congreso",
    description = "Permite al personal autorizado consultar los boletos " +
      "no cancelados de un congreso especifico.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boletos encontrados",
          value = """
[
  {
    "id"                  : 1,
    "folio"               : "A1B2C3",
    "folioLargo"          : "A1B2C3D4E5F6G7H8I9J0",
    
    "fechaCreacion"       : "2025-10-11T01:56:11",
    "creadorId"           : 1,
    
    "congresoId"          : 1,
    "congresoNombre"      : "Congreso de Tecnologia",
    "congresoFechaInicio" : "2025-10-11T10:00:00",
    "congresoFechaFin"    : "2025-10-11T18:00:00",
    "congresoDireccion"   : "Av. Universidad 123, Tijuana, B.C.",
    
    "alumnoId"            : 2,
    "alumnoNoControl"     : "12345678",
    "alumnoNombre"        : "Juan Perez Garcia",
  
    "excedente"           : false,
    "pagado"              : false,
    "usuarioEditoPagado"  : null,
    
    "cancelado"           : false,
    "usado"               : false,
    "asistencias"         : 0
  }
]
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Parametros invalidos",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Validacion fallida",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for parameters",
  "instance": "/api/v1/boletos/boleto/congreso/1/no-cancelados",
  "timestamp": "2025-10-13T06:15:10",
  "campos": {
    "page": "must be greater than or equal to 0",
    "pageSize": "must be less than or equal to 100"
  }
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/boletos/boleto/congreso/1/no-cancelados",
  "timestamp": "2025-10-13T06:16:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/boletos/boleto/congreso/1/no-cancelados",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_BOLETOS_AJENOS)

  public ResponseEntity<List<Boleto>> qIdCongresoNoCancelados (

    @PathVariable("congresoId")
    Long congresoId,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      bolSvc.qIdCongresoCancelado(
        congresoId, Boleto.NO_CANCELADO, page, pageSize),
      HttpStatus.OK);
  }
}
