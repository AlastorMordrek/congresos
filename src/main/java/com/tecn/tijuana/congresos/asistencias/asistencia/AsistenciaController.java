package com.tecn.tijuana.congresos.asistencias.asistencia;

import com.tecn.tijuana.congresos.asistencias.asistencia.dto.TransicionConferenciaDto;
import com.tecn.tijuana.congresos.boletos.boleto.Boleto;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.security.ExpresionSeguridad;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
import java.util.Map;

/**
 * Clase controladora principal de la entidad.
 * */
@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/asistencias/asistencia")
@Validated
public class AsistenciaController {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final AsistenciaService astSvc;



  /**
   * CONSTRUCTOR principal de esta clase/controller, usado principalmente por
   * Spring para el funcionamiento de la app.
   *
   * @param astSvc
   * Objeto de la capa de servicio de la entidad de ASISTENCIA.
   */
  public AsistenciaController (
    AsistenciaService astSvc
  ) {
    this.astSvc = astSvc;
  }



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a un
   * CONGRESO usando su BOLETO.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param boletoFolio
   * Folio del BOLETO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El BOLETO marcado como usado.
   */
  @PostMapping("asistirCongreso/{congresoId}/" +
    "boletoFolio/{boletoFolio}")

  @Operation(
    summary = "Registrar entrada a congreso con folio",
    description = "Permite al personal autorizado registrar la entrada " +
      "de un alumno a un congreso usando el folio de su boleto.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Entrada registrada exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto marcado como usado",
          value = """
{
  "id": 1,
  "folio": "A1B2C3",
  "folioLargo": "A1B2C3D4E5F6G7H8I9J0",
  
  "fechaCreacion": "2025-10-11T01:56:11",
  "creadorId": 1,
  
  "congresoId": 1,
  "congresoNombre": "Congreso de Tecnologia",
  "congresoFechaInicio": "2025-10-11T10:00:00",
  "congresoFechaFin": "2025-10-11T18:00:00",
  "congresoDireccion": "Av. Universidad 123, Tijuana, B.C.",
  
  "alumnoId": 2,
  "alumnoNoControl": "12345678",
  "alumnoNombre": "Juan Perez Garcia",
  
  "cancelado": false,
  "usado": true,
  "asistencias": 0
}
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
  "instance":
   "/api/v1/asistencias/asistencia/asistirCongreso/1/boletoFolio/ABC",
  "timestamp": "2025-10-13T06:35:10",
  "campos": {
    "boletoFolio": "debe tener 6 caracteres"
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
  "instance":
   "/api/v1/asistencias/asistencia/asistirCongreso/1/boletoFolio/A1B2C3",
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
        examples = {
          @ExampleObject(
            name = "Error",
            description = "Boleto no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el boleto con folio A1B2C3",
  "instance":
   "/api/v1/asistencias/asistencia/asistirCongreso/1/boletoFolio/A1B2C3",
  "timestamp": "2025-10-13T04:27:17"
}
"""
          ),
          @ExampleObject(
            name = "Error",
            description = "Congreso no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro congreso con ID 1",
  "instance":
   "/api/v1/asistencias/asistencia/asistirCongreso/1/boletoFolio/A1B2C3",
  "timestamp": "2025-10-13T04:27:17"
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
  "instance":
   "/api/v1/asistencias/asistencia/asistirCongreso/1/boletoFolio/A1B2C3",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Boleto> asistirCongresoConFolio (

    @PathVariable
    Long congresoId,

    @PathVariable
    String boletoFolio,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.asistirCongresoConBoleto(actor, congresoId, boletoFolio),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a un
   * CONGRESO usando su Numero de Control.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param noControlAlumno
   * Numero de Control del ALUMNO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El BOLETO marcado como usado.
   */
  @PostMapping("asistirCongreso/{congresoId}/" +
    "noControlAlumno/{noControlAlumno}")

  @Operation(
    summary = "Registrar entrada a congreso con numero de control",
    description = "Permite al personal autorizado registrar la entrada " +
      "de un alumno a un congreso usando su numero de control.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Entrada registrada exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Boleto.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Boleto marcado como usado",
          value = """
{
  "id": 1,
  "folio": "A1B2C3",
  "folioLargo": "A1B2C3D4E5F6G7H8I9J0",
  
  "fechaCreacion": "2025-10-11T01:56:11",
  "creadorId": 1,
  
  "congresoId": 1,
  "congresoNombre": "Congreso de Tecnologia",
  "congresoFechaInicio": "2025-10-11T10:00:00",
  "congresoFechaFin": "2025-10-11T18:00:00",
  "congresoDireccion": "Av. Universidad 123, Tijuana, B.C.",
  
  "alumnoId": 2,
  "alumnoNoControl": "12345678",
  "alumnoNombre": "Juan Perez Garcia",
  
  "cancelado": false,
  "usado": true,
  "asistencias": 0
}
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
  "instance":
   "/api/v1/asistencias/asistencia/asistirCongreso/1/noControlAlumno/123",
  "timestamp": "2025-10-13T06:35:10",
  "campos": {
    "noControlAlumno": "debe tener 8 caracteres"
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
  "instance":
   "/api/v1/asistencias/asistencia/asistirCongreso/1/noControlAlumno/12345678",
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
        examples = {
          @ExampleObject(
            name = "Error",
            description = "Boleto no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro boleto activo para el alumno 12345678",
  "instance":
   "/api/v1/asistencias/asistencia/asistirCongreso/1/noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:27:17"
}
"""
          ),
          @ExampleObject(
            name = "Error",
            description = "Congreso no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro congreso con ID 1",
  "instance":
   "/api/v1/asistencias/asistencia/asistirCongreso/1/noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:27:17"
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
  "instance":
   "/api/v1/asistencias/asistencia/asistirCongreso/1/noControlAlumno/12345678",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Boleto> asistirCongresoConNoControl (

    @PathVariable
    Long congresoId,

    @PathVariable
    String noControlAlumno,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.asistirCongresoConNoControl(
        actor, congresoId, noControlAlumno),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a una
   * CONFERENCIA usando su BOLETO.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param boletoFolio
   * Folio del BOLETO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * La ASISTENCIA registrada/actualizada.
   */
  @PostMapping("asistirConferencia/{conferenciaId}/" +
    "boletoFolio/{boletoFolio}")

  @Operation(
    summary = "Registrar entrada a conferencia con folio",
    description = "Permite al personal autorizado registrar la entrada " +
      "de un alumno a una conferencia usando el folio de su boleto.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Entrada registrada exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Asistencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Asistencia registrada",
          value = """
{
  "id": 1,
  
  "fechaCreacion": "2025-10-11T10:05:00",
  "creadorId": 3,
  "creadorNombre": "Personal de Seguridad",
  
  "boletoId": 1,
  "boletoFolio": "A1B2C3",
  "boletoFolioLargo": "A1B2C3D4E5F6G7H8I9J0",
  
  "congresoId": 1,
  "congresoNombre": "Congreso de Tecnologia",
  "conferenciaId": 5,
  "conferenciaNombre": "Inteligencia Artificial Aplicada",
  
  "alumnoId": 2,
  "alumnoNoControl": "12345678",
  "alumnoNombre": "Juan Perez Garcia",
  
  "fechaUltimaEntrada": "2025-10-11T10:05:00",
  "tiempoAsistido": 0
}
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
  "instance":
   "/api/v1/asistencias/asistencia/asistirConferencia/5/boletoFolio/ABC",
  "timestamp": "2025-10-13T06:35:10",
  "campos": {
    "boletoFolio": "debe tener 6 caracteres"
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
  "instance":
   "/api/v1/asistencias/asistencia/asistirConferencia/5/boletoFolio/A1B2C3",
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
        examples = {
          @ExampleObject(
            name = "Boleto no encontrado",
            description = "El boleto no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el boleto con folio A1B2C3",
  "instance":
   "/api/v1/asistencias/asistencia/asistirConferencia/5/boletoFolio/A1B2C3",
  "timestamp": "2025-10-13T04:27:17"
}
"""
          ),
          @ExampleObject(
            name = "Congreso no encontrado",
            description = "El congreso de la conferencia no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el congreso de la conferencia 5",
  "instance":
   "/api/v1/asistencias/asistencia/asistirConferencia/5/boletoFolio/A1B2C3",
  "timestamp": "2025-10-13T04:28:18"
}
"""
          ),
          @ExampleObject(
            name = "Conferencia no encontrada",
            description = "La conferencia no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro la conferencia con ID 5",
  "instance":
   "/api/v1/asistencias/asistencia/asistirConferencia/5/boletoFolio/A1B2C3",
  "timestamp": "2025-10-13T04:29:19"
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
  "instance":
   "/api/v1/asistencias/asistencia/asistirConferencia/5/boletoFolio/A1B2C3",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Asistencia> asistirConferenciaConFolio (

    @PathVariable
    Long conferenciaId,

    @PathVariable
    String boletoFolio,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.asistirConferenciaConBoleto(
        actor, conferenciaId, boletoFolio),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a una
   * CONFERENCIA usando su Numero de Control.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param noControlAlumno
   * Numero de Control del ALUMNO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * La ASISTENCIA registrada/actualizada.
   */
  @PostMapping("asistirConferencia/{conferenciaId}/" +
    "noControlAlumno/{noControlAlumno}")

  @Operation(
    summary = "Registrar entrada a conferencia con numero de control",
    description = "Permite al personal autorizado registrar la entrada " +
      "de un alumno a una conferencia usando su numero de control.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Entrada registrada exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Asistencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Asistencia registrada",
          value = """
{
  "id": 2,
  
  "fechaCreacion": "2025-10-11T10:10:00",
  "creadorId": 3,
  "creadorNombre": "Personal de Seguridad",
  
  "boletoId": 1,
  "boletoFolio": "A1B2C3",
  "boletoFolioLargo": "A1B2C3D4E5F6G7H8I9J0",
  
  "congresoId": 1,
  "congresoNombre": "Congreso de Tecnologia",
  "conferenciaId": 5,
  "conferenciaNombre": "Inteligencia Artificial Aplicada",
  
  "alumnoId": 2,
  "alumnoNoControl": "12345678",
  "alumnoNombre": "Juan Perez Garcia",
  
  "fechaUltimaEntrada": "2025-10-11T10:10:00",
  "tiempoAsistido": 0
}
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
  "instance":
   "/api/v1/asistencias/asistencia/asistirConferencia/5/noControlAlumno/123",
  "timestamp": "2025-10-13T06:35:10",
  "campos": {
    "noControlAlumno": "debe tener 8 caracteres"
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
  "instance": "/api/v1/asistencias/asistencia/asistirConferencia/5/"+
    "noControlAlumno/12345678",
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
        examples = {
          @ExampleObject(
            name = "Alumno no encontrado",
            description = "El alumno no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el alumno con numero de control 12345678",
  "instance": "/api/v1/asistencias/asistencia/asistirConferencia/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:27:17"
}
"""
          ),
          @ExampleObject(
            name = "Boleto no encontrado",
            description = "El alumno no tiene boleto activo",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro boleto activo para el alumno 12345678",
  "instance": "/api/v1/asistencias/asistencia/asistirConferencia/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:28:18"
}
"""
          ),
          @ExampleObject(
            name = "Congreso no encontrado",
            description = "El congreso de la conferencia no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el congreso de la conferencia 5",
  "instance": "/api/v1/asistencias/asistencia/asistirConferencia/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:29:19"
}
"""
          ),
          @ExampleObject(
            name = "Conferencia no encontrada",
            description = "La conferencia no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro la conferencia con ID 5",
  "instance": "/api/v1/asistencias/asistencia/asistirConferencia/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:30:20"
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
  "instance": "/api/v1/asistencias/asistencia/asistirConferencia/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Asistencia> asistirConferenciaConNoControl (

    @PathVariable
    Long conferenciaId,

    @PathVariable
    String noControlAlumno,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.asistirConferenciaConNoControl(
        actor, conferenciaId, noControlAlumno),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado transicionar masivamente a los ALUMNOS de
   * una CONFERENCIA de origen a otra CONFERENCIA de destino, usando una lista
   * blanca o negra de Folios de BOLETO.
   *
   * @param body
   * Cuerpo de la peticion: conferenciaAnteriorId, conferenciaPosteriorId,
   * folios, listaBlanca.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * <p>{
   * <p>  "conferencia_anterior"  : CONFERENCIA de origen actualizada,
   * <p>  "conferencia_posterior" : CONFERENCIA de destino actualizada,
   * <p>  "asistencia_anterior"   : Lista de ASISTENCIAS de origen procesadas,
   * <p>  "asistencia_posterior"  : Lista de ASISTENCIAS de destino procesadas,
   * <p>  "omitidos"              : Lista de {folioBoleto, razon} no procesados.
   * <p>}
   */
  @PostMapping("transicionarConferencia/conFolios")

  @Operation(
    summary = "Transicion masiva de alumnos entre conferencias con folios",
    description = "Registra la salida masiva de alumnos de una conferencia " +
      "de origen y su entrada a una conferencia de destino, usando una lista " +
      "blanca o negra de folios de boleto. " +
      "Lista blanca con folios: solo los alumnos cuyos folios aparezcan en " +
      "la lista son procesados. " +
      "Lista blanca vacia: solo los alumnos actualmente presentes en la " +
      "conferencia de origen (con entrada activa) son procesados. " +
      "Lista negra con folios: son procesados todos excepto los que aparezcan " +
      "en la lista. " +
      "Lista negra vacia: todos los alumnos con asistencia registrada en la " +
      "conferencia de origen son procesados. " +
      "Si un alumno no tenia entrada activa en la conferencia de origen, " +
      "no se le puede marcar su salida pero de todas formas se intenta " +
      "registrarle su entrada a la conferencia de destino. " +
      "Los folios que no se pudieron procesar (alumno bloqueado, boleto " +
      "cancelado, etc.) se reportan en 'omitidos', sin interrumpir el " +
      "proceso para los demas. " +
      "Toda la operacion esta envuelta en una transaccion de base de datos: " +
      "los errores estructurales (congreso o conferencia invalidos, sin " +
      "autorizacion) revierten todos los cambios ya realizados."
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Transicion procesada exitosamente",
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          name = "Exito",
          description = "Resultado de la transicion masiva",
          value = """
{
  "conferencia_anterior": {
    "id": 5,
    "fechaCreacion": "2025-10-11T01:56:11",
    "creadorId": 1,
    "congresoId": 1,

    "nombre": "Inteligencia Artificial Aplicada",
    "resumen": "Aplicaciones practicas de IA",
    "descripcion": "Conferencia sobre implementacion de IA en la industria...",

    "sala": "Aula Magna",

    "fechaInicio": "2025-10-11T10:00:00",
    "fechaFin": "2025-10-11T11:00:00",

    "publicada": true,
    "cancelada": false,

    "cupo": 100,
    "inscritos": 50,
    "asistencias": 48,

    "staffCantidad": 3,
    "staffRequerimientos": "Proyector, sonido, asistencia",

    "conferencistaNombre": "Dr. Juan Perez",
    "conferencistaEmail": "juan.perez@universidad.edu",
    "conferencistaTelPref": "52",
    "conferencistaTelSuf": "6641234567",
    "conferencistaSemblanza": "Experto en IA con 10 anos de experiencia..."
  },
  "conferencia_posterior": {
    "id": 6,
    "fechaCreacion": "2025-10-11T01:56:11",
    "creadorId": 1,
    "congresoId": 1,

    "nombre": "Machine Learning en la Practica",
    "resumen": "Casos de uso reales de ML",
    "descripcion": "Taller practico de implementacion de modelos de ML...",

    "sala": "Lab de Computo 3",

    "fechaInicio": "2025-10-11T11:00:00",
    "fechaFin": "2025-10-11T13:00:00",

    "publicada": true,
    "cancelada": false,

    "cupo": 100,
    "inscritos": 50,
    "asistencias": 2,

    "staffCantidad": 2,
    "staffRequerimientos": "Proyector, asistencia",

    "conferencistaNombre": "Dra. Maria Lopez",
    "conferencistaEmail": "maria.lopez@universidad.edu",
    "conferencistaTelPref": "52",
    "conferencistaTelSuf": "6649876543",
    "conferencistaSemblanza": "Investigadora en ML con enfoque en NLP..."
  },
  "asistencia_anterior": [
    {
      "id": 10,

      "fechaCreacion": "2025-10-11T10:05:00",
      "creadorId": 3,
      "creadorNombre": "Personal de Seguridad",

      "boletoId": 1,
      "boletoFolio": "A1B2C3",
      "boletoFolioLargo": "A1B2C3D4E5F6G7H8I9J0",

      "congresoId": 1,
      "congresoNombre": "Congreso de Tecnologia",
      "conferenciaId": 5,
      "conferenciaNombre": "Inteligencia Artificial Aplicada",

      "alumnoId": 2,
      "alumnoNoControl": "12345678",
      "alumnoNombre": "Juan Perez Garcia",

      "fechaUltimaEntrada": null,
      "tiempoAsistido": 3600
    }
  ],
  "asistencia_posterior": [
    {
      "id": 11,

      "fechaCreacion": "2025-10-11T11:00:05",
      "creadorId": 3,
      "creadorNombre": "Personal de Seguridad",

      "boletoId": 1,
      "boletoFolio": "A1B2C3",
      "boletoFolioLargo": "A1B2C3D4E5F6G7H8I9J0",

      "congresoId": 1,
      "congresoNombre": "Congreso de Tecnologia",
      "conferenciaId": 6,
      "conferenciaNombre": "Machine Learning en la Practica",

      "alumnoId": 2,
      "alumnoNoControl": "12345678",
      "alumnoNombre": "Juan Perez Garcia",

      "fechaUltimaEntrada": "2025-10-11T11:00:05",
      "tiempoAsistido": 0
    }
  ],
  "omitidos": [
    {
      "folioBoleto": "X9Y8Z7",
      "razon": "El alumno esta bloqueado"
    }
  ]
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Parametros invalidos o conferencias de distintos congresos",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Las conferencias no comparten congreso",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Las conferencias no pertenecen al mismo congreso",
  "instance":
   "/api/v1/asistencias/asistencia/transicionarConferencia/conFolios",
  "timestamp": "2025-10-13T06:35:10"
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
  "instance":
   "/api/v1/asistencias/asistencia/transicionarConferencia/conFolios",
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
        examples = {
          @ExampleObject(
            name = "Conferencia de origen no encontrada",
            description = "La conferencia de origen no existe.",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro la conferencia con ID 5",
  "instance":
   "/api/v1/asistencias/asistencia/transicionarConferencia/conFolios",
  "timestamp": "2025-10-13T04:27:17"
}
"""
          ),
          @ExampleObject(
            name = "Conferencia de destino no encontrada",
            description = "La conferencia de destino no existe.",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro la conferencia con ID 6",
  "instance":
   "/api/v1/asistencias/asistencia/transicionarConferencia/conFolios",
  "timestamp": "2025-10-13T04:27:17"
}
"""
          )
        }
      )
    ),
    @ApiResponse(
      responseCode = "412",
      description = "Precondicion fallida",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = {
          @ExampleObject(
            name = "Congreso no en curso",
            description = "El congreso no esta activo",
            value = """
{
  "type": "about:blank",
  "title": "Precondition Failed",
  "status": 412,
  "detail": "El congreso no esta en curso",
  "instance":
   "/api/v1/asistencias/asistencia/transicionarConferencia/conFolios",
  "timestamp": "2025-10-13T04:28:18"
}
"""
          ),
          @ExampleObject(
            name = "Conferencia de destino no publicada",
            description = "La conferencia de destino no esta publicada",
            value = """
{
  "type": "about:blank",
  "title": "Precondition Failed",
  "status": 412,
  "detail": "La conferencia de destino no esta publicada",
  "instance":
   "/api/v1/asistencias/asistencia/transicionarConferencia/conFolios",
  "timestamp": "2025-10-13T04:29:19"
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
  "instance":
   "/api/v1/asistencias/asistencia/transicionarConferencia/conFolios",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Map<String, Object>> transicionarConferenciaConFolios (

    @RequestBody @Valid
    TransicionConferenciaDto body,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.transicionarConferenciaConFolios(
        actor,
        body.getConferenciaAnteriorId(),
        body.getConferenciaPosteriorId(),
        body.getFolios(),
        body.getListaBlanca()),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado registrar la SALIDA de un ALUMNO a una
   * CONFERENCIA usando su BOLETO.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param boletoFolio
   * Folio del BOLETO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * La ASISTENCIA registrada/actualizada.
   */
  @PostMapping("salirDeConferencia/{conferenciaId}/" +
    "boletoFolio/{boletoFolio}")

  @Operation(
    summary = "Registrar salida de conferencia con folio",
    description = "Permite al personal autorizado registrar la salida " +
      "de un alumno de una conferencia usando el folio de su boleto.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Salida registrada exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Asistencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Asistencia actualizada",
          value = """
{
  "id": 1,
  
  "fechaCreacion": "2025-10-11T10:05:00",
  "creadorId": 3,
  "creadorNombre": "Personal de Seguridad",
  
  "boletoId": 1,
  "boletoFolio": "A1B2C3",
  "boletoFolioLargo": "A1B2C3D4E5F6G7H8I9J0",
  
  "congresoId": 1,
  "congresoNombre": "Congreso de Tecnologia",
  "conferenciaId": 5,
  "conferenciaNombre": "Inteligencia Artificial Aplicada",
  
  "alumnoId": 2,
  "alumnoNoControl": "12345678",
  "alumnoNombre": "Juan Perez Garcia",
  
  "fechaUltimaEntrada": null,
  "tiempoAsistido": 1800
}
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
  "instance":
   "/api/v1/asistencias/asistencia/salirDeConferencia/5/boletoFolio/ABC",
  "timestamp": "2025-10-13T06:35:10",
  "campos": {
    "boletoFolio": "debe tener 6 caracteres"
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
  "instance":
   "/api/v1/asistencias/asistencia/salirDeConferencia/5/boletoFolio/A1B2C3",
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
        examples = {
          @ExampleObject(
            name = "Asistencia no encontrada",
            description = "No hay registro de entrada",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro registro de entrada para el boleto A1B2C3 en la"+
    " conferencia 5",
  "instance":
   "/api/v1/asistencias/asistencia/salirDeConferencia/5/boletoFolio/A1B2C3",
  "timestamp": "2025-10-13T04:27:17"
}
"""
          ),
          @ExampleObject(
            name = "Boleto no encontrado",
            description = "El boleto no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el boleto con folio A1B2C3",
  "instance":
   "/api/v1/asistencias/asistencia/salirDeConferencia/5/boletoFolio/A1B2C3",
  "timestamp": "2025-10-13T04:28:18"
}
"""
          ),
          @ExampleObject(
            name = "Conferencia no encontrada",
            description = "La conferencia no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro la conferencia con ID 5",
  "instance":
   "/api/v1/asistencias/asistencia/salirDeConferencia/5/boletoFolio/A1B2C3",
  "timestamp": "2025-10-13T04:29:19"
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
  "instance":
   "/api/v1/asistencias/asistencia/salirDeConferencia/5/boletoFolio/A1B2C3",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Asistencia> salirDeConferenciaConFolio (

    @PathVariable
    Long conferenciaId,

    @PathVariable
    String boletoFolio,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.salirDeConferenciaConBoleto(
        actor, conferenciaId, boletoFolio),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado registrar la SALIDA de un ALUMNO a una
   * CONFERENCIA usando su Numero de Control.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param noControlAlumno
   * Numero de Control del ALUMNO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * La ASISTENCIA registrada/actualizada.
   */
  @PostMapping("salirDeConferencia/{conferenciaId}/" +
    "noControlAlumno/{noControlAlumno}")

  @Operation(
    summary = "Registrar salida de conferencia con numero de control",
    description = "Permite al personal autorizado registrar la salida " +
      "de un alumno de una conferencia usando su numero de control.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Salida registrada exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Asistencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Asistencia actualizada",
          value = """
{
  "id": 2,
  
  "fechaCreacion": "2025-10-11T10:10:00",
  "creadorId": 3,
  "creadorNombre": "Personal de Seguridad",
  
  "boletoId": 1,
  "boletoFolio": "A1B2C3",
  "boletoFolioLargo": "A1B2C3D4E5F6G7H8I9J0",
  
  "congresoId": 1,
  "congresoNombre": "Congreso de Tecnologia",
  "conferenciaId": 5,
  "conferenciaNombre": "Inteligencia Artificial Aplicada",
  
  "alumnoId": 2,
  "alumnoNoControl": "12345678",
  "alumnoNombre": "Juan Perez Garcia",
  
  "fechaUltimaEntrada": null,
  "tiempoAsistido": 2700
}
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
  "instance":
   "/api/v1/asistencias/asistencia/salirDeConferencia/5/noControlAlumno/123",
  "timestamp": "2025-10-13T06:35:10",
  "campos": {
    "noControlAlumno": "debe tener 8 caracteres"
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
  "instance": "/api/v1/asistencias/asistencia/salirDeConferencia/5/"+
    "noControlAlumno/12345678",
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
        examples = {
          @ExampleObject(
            name = "Asistencia no encontrada",
            description = "No hay registro de entrada",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro registro de entrada para el alumno 12345678 en"+
    " la conferencia 5",
  "instance": "/api/v1/asistencias/asistencia/salirDeConferencia/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:27:17"
}
"""
          ),
          @ExampleObject(
            name = "Alumno no encontrado",
            description = "El alumno no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el alumno con numero de control 12345678",
  "instance": "/api/v1/asistencias/asistencia/salirDeConferencia/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:28:18"
}
"""
          ),
          @ExampleObject(
            name = "Boleto no encontrado",
            description = "El alumno no tiene boleto activo",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro boleto activo para el alumno 12345678",
  "instance": "/api/v1/asistencias/asistencia/salirDeConferencia/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:29:19"
}
"""
          ),
          @ExampleObject(
            name = "Conferencia no encontrada",
            description = "La conferencia no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro la conferencia con ID 5",
  "instance": "/api/v1/asistencias/asistencia/salirDeConferencia/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:30:20"
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
  "instance": "/api/v1/asistencias/asistencia/salirDeConferencia/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Asistencia> salirDeConferenciaConNoControl (

    @PathVariable
    Long conferenciaId,

    @PathVariable
    String noControlAlumno,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.salirDeConferenciaConNoControl(
        actor, conferenciaId, noControlAlumno),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado indicar que un ALUMNO asistio a una
   * CONFERENCIA completa.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param noControlAlumno
   * Numero de Control del ALUMNO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * La ASISTENCIA registrada/actualizada.
   */
  @PostMapping("asistioConferenciaCompleta/{conferenciaId}/" +
    "noControlAlumno/{noControlAlumno}")

  @Operation(
    summary = "Registrar asistencia completa a conferencia",
    description = "Permite al personal autorizado indicar que un alumno " +
      "asistio a una conferencia completa usando su numero de control.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Asistencia completa registrada exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Asistencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Asistencia completa registrada",
          value = """
{
  "id": 3,
  
  "fechaCreacion": "2025-10-11T12:05:00",
  "creadorId": 3,
  "creadorNombre": "Personal de Seguridad",
  
  "boletoId": 1,
  "boletoFolio": "A1B2C3",
  "boletoFolioLargo": "A1B2C3D4E5F6G7H8I9J0",
  
  "congresoId": 1,
  "congresoNombre": "Congreso de Tecnologia",
  "conferenciaId": 5,
  "conferenciaNombre": "Inteligencia Artificial Aplicada",
  
  "alumnoId": 2,
  "alumnoNoControl": "12345678",
  "alumnoNombre": "Juan Perez Garcia",
  
  "fechaUltimaEntrada": null,
  "tiempoAsistido": 7200
}
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
  "instance": "/api/v1/asistencias/asistencia/asistioConferenciaCompleta/5/"+
    "noControlAlumno/123",
  "timestamp": "2025-10-13T06:35:10",
  "campos": {
    "noControlAlumno": "debe tener 8 caracteres"
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
  "instance": "/api/v1/asistencias/asistencia/asistioConferenciaCompleta/5/"+
    "noControlAlumno/12345678",
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
        examples = {
          @ExampleObject(
            name = "Alumno no encontrado",
            description = "El alumno no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el alumno con numero de control 12345678",
  "instance": "/api/v1/asistencias/asistencia/asistioConferenciaCompleta/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:27:17"
}
"""
          ),
          @ExampleObject(
            name = "Boleto no encontrado",
            description = "El alumno no tiene boleto activo",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro boleto activo para el alumno 12345678",
  "instance": "/api/v1/asistencias/asistencia/asistioConferenciaCompleta/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:28:18"
}
"""
          ),
          @ExampleObject(
            name = "Conferencia no encontrada",
            description = "La conferencia no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro la conferencia con ID 5",
  "instance": "/api/v1/asistencias/asistencia/asistioConferenciaCompleta/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T04:29:19"
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
  "instance": "/api/v1/asistencias/asistencia/asistioConferenciaCompleta/5/"+
    "noControlAlumno/12345678",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Asistencia> asistioConferenciaCompletaConNoControl (

    @PathVariable
    Long conferenciaId,

    @PathVariable
    String noControlAlumno,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.alumnoAsistioConferenciaCompletaConNoControl(
        actor, conferenciaId, noControlAlumno),
      HttpStatus.OK);
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Consulta las asistencias de un ALUMNO via el Folio Largo de su BOLETO.
   * Retorna tambien el BOLETO y el CONGRESO correspondientes.
   *
   * @param boletoFolioLargo
   * Folio Largo del BOLETO.
   *
   * @return
   * <p>{
   * <p>  "boleto"     : BOLETO,
   * <p>  "congreso"   : CONGRESO,
   * <p>  "asistencia" : ASISTENCIAS,
   * <p>}
   */
  @GetMapping("publico/boleto/{boletoFolioLargo}")

  @Operation(
    summary = "Consultar asistencias por folio largo de boleto",
    description = "Consulta las Asistencias de un Alumno via el folio " +
      "largo de su Boleto, incluyendo la informacion del Boleto y del " +
      "Congreso correspondiente. Este endpoint es de acceso publico.",
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
        examples = @ExampleObject(
          name = "Exito",
          description = "Congreso, boleto y asistencias encontradas",
          value = """
{
  "congreso": {
    "id": 1,
    "fechaCreacion": "2025-10-11T01:56:11",
    "creadorId": 0,
    "organizadorId": 0,
    
    "nombre": "Di no a las drogas?",
    "resumen": "Maybe?",
    "descripcion": "O no se, como ustedes vean :/",
    "direccion": "Calafornix",
    
    "fechaInicio": "2025-10-11T13:00:00",
    "fechaFin": "2025-10-11T15:00:00",
    
    "inscripcionesFechaInicio": "2025-10-01T00:00:00",
    "inscripcionesFechaFin": "2025-10-10T00:00:00",
    
    "gratuito": true,
    
    "publicado": false,
    "cancelado": false,
    
    "cupo": 300,
    "inscritos": 0,
    "asistencias": 0,
    
    "staffCantidad": 20,
    "staffRequerimientos": "Soporte, seguridad, sonido, etc...",
    
    "alumnoAcreditacionAsistenciasRequeridas": 5,
    "alumnoAcreditacionTiempoAsistidoRequerido": 7200
  },
  "boleto": {
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
  },
  "asistencia": [
    {
      "id": 1,
    
      "fechaCreacion"      : "2025-10-11T10:05:00",
      "creadorId"          : 3,
      "creadorNombre"      : "Personal de Seguridad",
      
      "boletoId"           : 1,
      "boletoFolio"        : "A1B2C3",
      "boletoFolioLargo"   : "A1B2C3D4E5F6G7H8I9J0",
      
      "congresoId"         : 1,
      "congresoNombre"     : "Congreso de Tecnologia",
      "conferenciaId"      : 5,
      "conferenciaNombre"  : "Inteligencia Artificial Aplicada",
      
      "alumnoId"           : 2,
      "alumnoNoControl"    : "12345678",
      "alumnoNombre"       : "Juan Perez Garcia",
      
      "fechaUltimaEntrada" : "2025-10-11T10:05:00",
      "tiempoAsistido"     : 7200
    }
  ]
}
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
  "instance": "/api/v1/asistencias/asistencia/publico/boleto/ABC",
  "timestamp": "2025-10-13T06:35:10",
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
  "instance":
   "/api/v1/asistencias/asistencia/publico/boleto/A1B2C3D4E5F6G7H8I9J0",
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
  "instance":
   "/api/v1/asistencias/asistencia/publico/boleto/A1B2C3D4E5F6G7H8I9J0",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<Map<String, Object>> qBoletoFolioLargo (

    @PathVariable("boletoFolioLargo")
    String boletoFolioLargo,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      astSvc.qBoletoFolioLargo(boletoFolioLargo, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta la lista de asistencias de una CONFERENCIA.
   *
   * @param conferenciaId
   * ID de la ASISTENCIA.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("conferencia/{conferenciaId}")

  @Operation(
    summary = "Consultar asistencias por conferencia",
    description = "Permite al personal autorizado consultar la lista de " +
      "asistencias de una conferencia especifica. " +
      "Acepta el filtro opcional minTiempoAsistido (segundos, 0-144000) " +
      "para retornar unicamente registros con tiempoAsistido >= al valor" +
      " indicado. Acepta el filtro opcional presente (true/false): " +
      "true = solo registros con fechaUltimaEntrada != null; " +
      "false = solo registros con fechaUltimaEntrada == null; " +
      "omitido = sin filtro. " +
      "Acepta el filtro opcional txt (1-30 caracteres) para buscar en " +
      "alumnoNombre, alumnoNoControl, creadorNombre, boletoFolio y" +
      " boletoFolioLargo.",
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
        schema = @Schema(implementation = Asistencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Asistencias encontradas",
          value = """
[
  {
    "id": 1,
    
    "fechaCreacion"      : "2025-10-11T10:05:00",
    "creadorId"          : 3,
    "creadorNombre"      : "Personal de Seguridad",
    
    "boletoId"           : 1,
    "boletoFolio"        : "A1B2C3",
    "boletoFolioLargo"   : "A1B2C3D4E5F6G7H8I9J0",
    
    "congresoId"         : 1,
    "congresoNombre"     : "Congreso de Tecnologia",
    "conferenciaId"      : 5,
    "conferenciaNombre"  : "Inteligencia Artificial Aplicada",
    
    "alumnoId"           : 2,
    "alumnoNoControl"    : "12345678",
    "alumnoNombre"       : "Juan Perez Garcia",
    
    "fechaUltimaEntrada" : "2025-10-11T10:05:00",
    "tiempoAsistido"     : 7200
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
  "instance": "/api/v1/asistencias/asistencia/conferencia/5",
  "timestamp": "2025-10-13T06:35:10",
  "campos": {
    "page": "must be greater than or equal to 0",
    "pageSize": "must be less than or equal to 100",
    "minTiempoAsistido": "must be less than or equal to 144000",
    "presente": "must be true or false or absent from the request"
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
  "instance": "/api/v1/asistencias/asistencia/conferencia/5",
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
          description = "Conferencia no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro la conferencia con ID 5",
  "instance": "/api/v1/asistencias/asistencia/conferencia/5",
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
  "instance": "/api/v1/asistencias/asistencia/conferencia/5",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_ASISTENCIAS_AJENAS)

  public ResponseEntity<List<Asistencia>> qConferenciaId (

    @PathVariable("conferenciaId")
    Long conferenciaId,

    @RequestParam(name = "minTiempoAsistido", required = false)
    @Min(0) @Max(144000)
    Long minTiempoAsistido,

    @RequestParam(name = "presente", required = false)
    Boolean presente,

    @RequestParam(name = "txt", required = false)
    @Size(min = 3, max = 30)
    String txt,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      astSvc.qConferenciaId(
        conferenciaId, minTiempoAsistido, presente, txt, page, pageSize),
      HttpStatus.OK);
  }
}
