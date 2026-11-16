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
import org.springframework.web.multipart.MultipartFile;

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
  "asistencias"         : 0,
  "tiempoAsistido"      : 0
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
  "asistencias"         : 0,
  "tiempoAsistido"      : 0
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
  "asistencias"         : 0,
  "tiempoAsistido"      : 0
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
  "asistencias"         : 0,
  "tiempoAsistido"      : 0
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
  "asistencias"         : 0,
  "tiempoAsistido"      : 0
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
  "asistencias"         : 0,
  "tiempoAsistido"      : 0
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
  "asistencias"         : 0,
  "tiempoAsistido"      : 0
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



  /**
   * Permite al ORGANIZADOR ACREDITAR a un ALUMNO a travez de su BOLETO.
   *
   * @param id
   * ID del registro.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro ACREDITADO.
   */
  @PatchMapping("acreditar/{id}")

  @Operation(
    summary = "Acreditar boleto",
    description = "Permite al organizador acreditar un boleto cuyo alumno " +
      "ya cumplio con los requerimientos de asistencia del congreso.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Acreditacion exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto acreditado",
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
  "usuarioEditoPagado"  : null,
  
  "cancelado"                          : false,
  "usado"                              : true,
  "asistencias"                        : 3,
  "tiempoAsistido"                     : 10800,
  "cumplioRequerimientosDeAsistencia"  : true,
  "acreditado"                         : true
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
          description = "Sin permisos o requerimientos de asistencia no cumplidos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "El alumno no ha cumplido con los requerimientos de asistencia",
  "instance": "/api/v1/boletos/boleto/acreditar/1",
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
  "instance": "/api/v1/boletos/boleto/acreditar/1",
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
  "instance": "/api/v1/boletos/boleto/acreditar/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.ACREDITAR_ALUMNOS)

  public ResponseEntity<Boleto> acreditar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.acreditar(actor, id),
      HttpStatus.OK);
  }



  /**
   * Quita la acreditacion de un BOLETO y elimina su constancia.
   *
   * @param id
   * ID del BOLETO.
   *
   * @param actor
   * USUARIO responsable de la peticion.
   *
   * @return
   * El BOLETO desacreditado.
   */
  @PatchMapping("desacreditar/{id}")

  @Operation(
    summary = "Desacreditar boleto",
    description = "Quita la acreditacion de un boleto y elimina la constancia" +
      " de acreditacion asociada.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Boleto desacreditado",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class)
      )
    ),
    @ApiResponse(
      responseCode = "403",
      description = "Sin autorizacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Boleto no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class)
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.ACREDITAR_ALUMNOS)

  public ResponseEntity<Boleto> desacreditar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.desacreditar(actor, id),
      HttpStatus.OK);
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Consulta los BOLETOS indiscriminadamente usando una busqueda de texto
   * opcional y otros filtros opcionales.
   *
   * @param txt {@code [""]}
   * Texto de busqueda.
   *
   * @param congresoId {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
   *
   * @param codigoCarrera {@code [null]}
   * Filtro opcional por clave oficial de carrera del ALUMNO. Si no se
   * especifica, no filtra.
   *
   * @param excedente {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
   *
   * @param pagado {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
   *
   * @param cancelado {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
   *
   * @param usado {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
   *
   * @param cumplioRequerimientosDeAsistencia {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
   *
   * @param acreditado {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
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
      "filtro de texto y filtros opcionales de congresoId, codigoCarrera," +
      " excedente, pagado, cancelado, usado," +
      " cumplioRequerimientosDeAsistencia y acreditado.",
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
    "asistencias"         : 0,
    "tiempoAsistido"      : 0
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
    @Size(max = 30)
    String txt,

    // Filtros opcionales de estado. null = no filtrar por ese campo.
    @RequestParam(name = "congresoId", required = false)
    Long congresoId,

    @RequestParam(name = "codigoCarrera", required = false)
    @Size(max = 13)
    String codigoCarrera,

    @RequestParam(name = "excedente", required = false)
    Boolean excedente,

    @RequestParam(name = "pagado", required = false)
    Boolean pagado,

    @RequestParam(name = "cancelado", required = false)
    Boolean cancelado,

    @RequestParam(name = "usado", required = false)
    Boolean usado,

    @RequestParam(name = "cumplioRequerimientosDeAsistencia", required = false)
    Boolean cumplioRequerimientosDeAsistencia,

    @RequestParam(name = "acreditado", required = false)
    Boolean acreditado,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {



    return new ResponseEntity<>(
      bolSvc.qFiltrado(
        txt, null, codigoCarrera, congresoId, excedente, pagado, cancelado,
        usado, cumplioRequerimientosDeAsistencia, acreditado,
        page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Permite a un ALUMNO consultar sus BOLETOS, con busqueda de texto opcional
   * y filtros opcionales de excedente, pagado, cancelado, usado,
   * cumplioRequerimientosDeAsistencia y acreditado.
   *
   * @param txt {@code [""]}
   * Texto de busqueda.
   *
   * @param excedente {@code [null]}
   * Filtro opcional por excedente. Si no se especifica, no filtra.
   *
   * @param pagado {@code [null]}
   * Filtro opcional por pagado. Si no se especifica, no filtra.
   *
   * @param cancelado {@code [null]}
   * Filtro opcional por cancelado. Si no se especifica, no filtra.
   *
   * @param usado {@code [null]}
   * Filtro opcional por usado. Si no se especifica, no filtra.
   *
   * @param cumplioRequerimientosDeAsistencia {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
   *
   * @param acreditado {@code [null]}
   * Filtro opcional por acreditado. Si no se especifica, no filtra.
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
    description = "Obtiene los boletos del usuario autenticado, con busqueda " +
      "de texto y filtros opcionales de excedente, pagado, cancelado, usado, " +
      "cumplioRequerimientosDeAsistencia y acreditado.",
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
    "asistencias"         : 0,
    "tiempoAsistido"      : 0
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

    @RequestParam(name = "txt", required = false, defaultValue = "")
    @Size(max = 30)
    String txt,

    @RequestParam(name = "excedente", required = false)
    Boolean excedente,

    @RequestParam(name = "pagado", required = false)
    Boolean pagado,

    @RequestParam(name = "cancelado", required = false)
    Boolean cancelado,

    @RequestParam(name = "usado", required = false)
    Boolean usado,

    @RequestParam(name = "cumplioRequerimientosDeAsistencia", required = false)
    Boolean cumplioRequerimientosDeAsistencia,

    @RequestParam(name = "acreditado", required = false)
    Boolean acreditado,

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
      bolSvc.qFiltrado(
        txt, actor.getId(), null, null,
        excedente, pagado, cancelado, usado,
        cumplioRequerimientosDeAsistencia, acreditado,
        page, pageSize),
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
    "asistencias"         : 0,
    "tiempoAsistido"      : 0
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
    @Size(max = 30)
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
  "asistencias"         : 0,
  "tiempoAsistido"      : 0
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
  "asistencias"         : 0,
  "tiempoAsistido"      : 0
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
  "asistencias"         : 0,
  "tiempoAsistido"      : 0
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

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_BOLETO)

  public ResponseEntity<Boleto> qId (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.afirmarIdAlumno(actor, id),
      HttpStatus.OK);
  }



  /**
   * Consulta los BOLETOS de un CONGRESO indiscriminadamente, con busqueda de
   * texto opcional y otros filtros opcionales.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param txt {@code [""]}
   * Texto de busqueda.
   *
   * @param excedente {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
   *
   * @param pagado {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
   *
   * @param cancelado {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
   *
   * @param usado {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
   *
   * @param cumplioRequerimientosDeAsistencia {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
   *
   * @param acreditado {@code [null]}
   * Filtro opcional. Si no se especifica, no filtra.
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
      "de un congreso especifico, con busqueda de texto y filtros opcionales " +
      "de excedente, pagado, cancelado, usado, " +
      "cumplioRequerimientosDeAsistencia y acreditado.",
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
    "asistencias"         : 0,
    "tiempoAsistido"      : 0
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

    @RequestParam(name = "txt", required = false, defaultValue = "")
    @Size(max = 30)
    String txt,

    @PathVariable("congresoId")
    Long congresoId,

    @RequestParam(name = "excedente", required = false)
    Boolean excedente,

    @RequestParam(name = "pagado", required = false)
    Boolean pagado,

    @RequestParam(name = "cancelado", required = false)
    Boolean cancelado,

    @RequestParam(name = "usado", required = false)
    Boolean usado,

    @RequestParam(name = "cumplioRequerimientosDeAsistencia", required = false)
    Boolean cumplioRequerimientosDeAsistencia,

    @RequestParam(name = "acreditado", required = false)
    Boolean acreditado,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      bolSvc.qFiltrado(
        txt, null, null, congresoId, excedente, pagado, cancelado, usado,
        cumplioRequerimientosDeAsistencia, acreditado,
        page, pageSize),
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
    "asistencias"         : 0,
    "tiempoAsistido"      : 0
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
    "asistencias"         : 0,
    "tiempoAsistido"      : 0
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



  /**
   * Permite editar la evidencia de pago de un BOLETO.
   *
   * @param id
   * ID del BOLETO.
   *
   * @param slot
   * Nombre del slot a editar.
   *
   * @param img {@code [null]}
   * Posible archivo a poner en el slot, {@code null} si se desea limpiarlo.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   */
  @PatchMapping("editar/{id}/media/{slot}")

  @Operation(
    summary = "Editar evidencia de pago",
    description = "Permite editar la evidencia de pago de un boleto." +
      " Slots disponibles: mediaPago1 y mediaPago2.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Contenido del archivo de imagen",
      required = true,
      content = @Content(
        mediaType = "multipart/form-data",
        examples = {
          @ExampleObject(
            name = "Nueva imagen",
            description = "Ejemplo de peticion para poner una nueva imagen" +
              " en el slot",
            value = """
{
  "img": <CONTENIDO DEL ARCHIVO>
}
"""
          ),
          @ExampleObject(
            name = "Eliminar imagen",
            description = "Si se omite el archivo se limpia el slot",
            value = """
{
}
"""
          )
        }
      )
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Evidencia actualizada",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Slot invalido o imagen invalida",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Slot no valido",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Slot no valido.",
  "instance": "/api/v1/boletos/boleto/editar/1/media/invalido",
  "timestamp": "2025-10-13T04:05:10"
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
          implementation = org.springframework.http.ProblemDetail.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Boleto no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El boleto no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Boleto con ID: 1 no encontrado",
  "instance": "/api/v1/boletos/boleto/editar/1/media/mediaPago1",
  "timestamp": "2025-10-13T04:07:17"
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
          implementation = org.springframework.http.ProblemDetail.class)
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_MEDIA_BOLETO)

  public ResponseEntity<Boleto> editarMedia (

    @PathVariable
    Long id,

    @PathVariable
    String slot,

    @RequestPart(required = false)
    MultipartFile img,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.editarMedia(actor, id, slot, img),
      HttpStatus.OK);
  }



  /**
   * Consulta la evidencia de pago de un BOLETO.
   *
   * @param id
   * ID del BOLETO.
   *
   * @param slot
   * Slot de evidencia de pago.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * La imagen solicitada.
   */
  @GetMapping("{id}/media/{slot}")

  @Operation(
    summary = "Consultar evidencia de pago",
    description = "Obtiene la evidencia de pago de un boleto." +
      " El ALUMNO solo puede consultar la de sus propios boletos.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Imagen obtenida",
      content = @Content(
        mediaType = "image/jpeg",
        schema = @Schema(type = "string", format = "binary")
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Slot invalido",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Slot no valido",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Slot no valido.",
  "instance": "/api/v1/boletos/boleto/1/media/invalido",
  "timestamp": "2025-10-13T04:05:10"
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
          implementation = org.springframework.http.ProblemDetail.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "No encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = {
          @ExampleObject(
            name = "Boleto no encontrado",
            description = "El boleto no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Boleto con ID: 1 no encontrado",
  "instance": "/api/v1/boletos/boleto/1/media/mediaPago1",
  "timestamp": "2025-10-13T04:07:17"
}
"""
          ),
          @ExampleObject(
            name = "Sin imagen",
            description = "No hay imagen en el slot",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Evidencia de pago del boleto con ID: 1 no encontrada.",
  "instance": "/api/v1/boletos/boleto/1/media/mediaPago1",
  "timestamp": "2025-10-13T04:08:18"
}
"""
          )
        }
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class)
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_MEDIA_BOLETO)

  public ResponseEntity<byte[]> qIdMedia (

    @PathVariable("id")
    Long id,

    @PathVariable("slot")
    String slot,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return bolSvc.afirmarMedia(actor, id, slot);
  }



  /**
   * Acepta la evidencia presentada por el ALUMNO y la copia al slot oficial.
   *
   * @param id
   * ID del BOLETO.
   *
   * @param slot
   * Slot oficial que recibe la copia: mediaPago1 o mediaPago2.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El BOLETO actualizado.
   */
  @PatchMapping("aceptar/{id}/media/{slot}")

  @Operation(
    summary = "Aceptar evidencia del alumno",
    description = "Copia la evidencia presentada por el ALUMNO al slot " +
      "oficial indicado. Slots: mediaPago1 y mediaPago2.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Evidencia aceptada",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class)
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Slot invalido",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Slot no valido",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Slot no valido.",
  "instance": "/api/v1/boletos/boleto/aceptar/1/media/invalido",
  "timestamp": "2025-10-13T04:05:10"
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
          implementation = org.springframework.http.ProblemDetail.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Boleto o evidencia no encontrados",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Sin evidencia",
          description = "El ALUMNO no ha presentado evidencia",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "El ALUMNO no ha presentado evidencia para el slot mediaPago1.",
  "instance": "/api/v1/boletos/boleto/aceptar/1/media/mediaPago1",
  "timestamp": "2025-10-13T04:08:18"
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
          implementation = org.springframework.http.ProblemDetail.class)
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.GESTIONAR_BOLETOS)

  public ResponseEntity<Boleto> aceptarEvidenciaAlumno (

    @PathVariable
    Long id,

    @PathVariable
    String slot,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.aceptarEvidenciaAlumno(actor, id, slot),
      HttpStatus.OK);
  }
}
