package com.tecn.tijuana.congresos.eventos.congreso;

import com.tecn.tijuana.congresos.eventos.congreso.dto.RegistroCongresoDto;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/eventos/congreso")
@Validated
public class CongresoController {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final CongresoService conSvc;


  /**
   * CONSTRUCTOR principal de esta clase/controller, usado principalmente por
   * Spring para el funcionamiento de la app.
   *
   * @param conSvc
   * Objeto de la capa de servicio de la entidad de CONGRESO.
   */
  public CongresoController (
    CongresoService conSvc
  ) {
    this.conSvc = conSvc;
  }



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite a un ORGANIZADOR registrar un nuevo CONGRESO.
   *
   * @param dto
   * Objeto con los datos del CONGRESO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El nuevo registro creado.
   */
  @PostMapping("registrar")

  @Operation(
    summary = "Registrar congreso",
    description = "Permite a un organizador registrar un congreso.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Datos del congreso",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = RegistroCongresoDto.class),
        examples = {
          @ExampleObject(
            name = "Registrar congreso",
            description = "Ejemplo de peticion para registrar un congreso",
            value = """
{
  "nombre": "Di no a las drogas?",
  "resumen": "Maybe?",
  "descripcion": "O no se, como ustedes vean :/",
  "direccion": "Calafornix",
  
  "fechaInicio": "2025-10-11T13:00:00",
  "fechaFin": "2025-10-11T15:00:00",
  
  "inscripcionesFechaInicio": "2025-10-01T00:00:00",
  "inscripcionesFechaFin": "2025-10-10T00:00:00",
  
  "gratuito": true,
  
  "cupo": 300,
  
  "staffCantidad": 20,
  "staffRequerimientos": "Soporte, seguridad, sonido, etc...",
  
  "alumnoAcreditacionAsistenciasRequeridas": 5,
  "alumnoAcreditacionTiempoAsistidoRequeridas": 7200
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
      description = "Registro exitoso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Congreso registrado exitosamente",
          value = """
{
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
  "alumnoAcreditacionTiempoAsistidoRequeridas": 7200
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
  "detail":
   "Validation failed for object='registroCongresoDto'. Error count: 1",
  "instance": "/api/v1/eventos/congreso/registrar",
  "timestamp": "2025-09-24T01:14:16",
  "campos": {
    "registroCongresoDto.nombre":
     "El nombre debe tener entre 1 y 100 caracteres"
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
          description = "El usuario no esta autenticado o no tiene permiso" +
            " para realizar la operacion.",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/eventos/congreso/registrar",
  "timestamp": "2025-09-24T02:59:21"
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
          description = "Ejemplo de error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/eventos/congreso/registrar",
  "timestamp": "2025-09-24T03:01:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.REGISTRAR_CONGRESOS)

  public ResponseEntity<Congreso> registrar (

    @RequestBody
    RegistroCongresoDto dto,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.registrar(actor, dto),
      HttpStatus.CREATED);
  }



  /**
   * Actualiza un registro con los datos especificados.
   *
   * @param id
   * ID del registro a EDITAR.
   *
   * @param congreso
   * Objeto con los datos nuevos del Congreso a EDITAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro editado.
   */
  @PatchMapping("editar/{id}")

  @Operation(
    summary = "Editar congreso",
    description = "Permite a un organizador editar los datos de un congreso" +
      " existente.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Datos actualizados del congreso",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Editar congreso",
          description = "Ejemplo de peticion para editar un congreso",
          value = """
{
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
  
  "cupo": 350,
  "inscritos": 0,
  "asistencias": 0,
  
  "staffCantidad": 25,
  "staffRequerimientos": "Soporte, seguridad, sonido, etc...",
  
  "alumnoAcreditacionAsistenciasRequeridas": 5,
  "alumnoAcreditacionTiempoAsistidoRequeridas": 7200
}
"""
        )
      )
    )
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Edicion exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Congreso editado exitosamente",
          value = """
{
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
  
  "cupo": 350,
  "inscritos": 0,
  "asistencias": 0,
  
  "staffCantidad": 25,
  "staffRequerimientos": "Soporte, seguridad, sonido, etc...",
  
  "alumnoAcreditacionAsistenciasRequeridas": 5,
  "alumnoAcreditacionTiempoAsistidoRequeridas": 7200
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
  "detail": "Validation failed for object='congreso'. Error count: 1",
  "instance": "/api/v1/eventos/congreso/editar/1",
  "timestamp": "2025-10-11T02:14:16",
  "campos": {
    "congreso.nombre": "El nombre debe tener entre 1 y 100 caracteres"
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
          description = "El usuario no esta autenticado o no tiene permiso" +
            " para editar congresos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/eventos/congreso/editar/1",
  "timestamp": "2025-10-11T02:15:21"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Congreso no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El congreso con el ID especificado no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el congreso con ID 1",
  "instance": "/api/v1/eventos/congreso/editar/1",
  "timestamp": "2025-10-11T02:16:17"
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
          description = "Ejemplo de error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/eventos/congreso/editar/1",
  "timestamp": "2025-10-11T02:17:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONGRESOS)

  public ResponseEntity<Congreso> editar (

    @PathVariable
    Long id,

    @RequestBody
    Congreso congreso,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.editar(actor, id, congreso),
      HttpStatus.OK);
  }

  /**
   * Actualiza el registro en el slot de multimedia especificado.
   *
   * @param id
   * ID del registro a EDITAR.
   *
   * @param slot
   * Nombre del campo a editar.
   *
   * @param img {@code [null]}
   * Posible archivo a poner en el slot, {@code null} si se desea limpiarlo.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   */
  @PatchMapping("editar/{id}/media/{slot}")

  @Operation(
    summary = "Editar slot multimedia",
    description = "Permite editar un slot de multimedia de un congreso.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Contenido del archivo multimedia",
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
            name = "Remover",
            description = "Ejemplo de peticion para remover la imagen del slot",
            value = """
{
  "img": null
}
"""
          )
        }
      )
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "204",
      description = "Edicion exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Congreso editado exitosamente",
          value = "{}"
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
          description = "Parametros incorrectos o validacion fallida",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Error al procesar la imagen",
  "instance": "/api/v1/eventos/congreso/editar/1/media/media1",
  "timestamp": "2025-09-24T01:14:16"
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
          description = "Ejemplo de error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/eventos/congreso/editar/1/media/media1",
  "timestamp": "2025-09-24T03:01:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONGRESOS)

  public void editar (

    @PathVariable
    Long id,

    @PathVariable
    String slot,

    @RequestPart(required = false)
    MultipartFile img,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    conSvc.editarMedia(actor, id, slot, img);
  }



  /**
   * Publica un registro para el publico general.
   *
   * @param id
   * ID del registro a PUBLICAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro publicado.
   */
  @PatchMapping("publicar/{id}")

  @Operation(
    summary = "Publicar congreso",
    description = "Permite a un organizador publicar uno de sus congresos, " +
      "haciendolo visible para el publico general. " +
      "Una vez publicado, el congreso podra ser consultado por endpoints" +
      " publicos.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Congreso publicado exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Ejemplo de respuesta cuando el congreso es publicado" +
            " exitosamente",
          value = """
{
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

  "publicado": true,
  "cancelado": false,

  "cupo": 300,
  "inscritos": 0,
  "asistencias": 0,

  "staffCantidad": 20,
  "staffRequerimientos": "Soporte, seguridad, sonido, etc..."
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
          description = "El usuario no esta autenticado o no tiene permiso" +
            " para publicar congresos.",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "El usuario autenticado no tiene permisos para publicar congresos.",
  "instance": "/api/v1/eventos/congreso/publicar/1",
  "timestamp": "2025-10-13T02:01:45"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Congreso no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El congreso con el ID especificado no existe.",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el congreso con ID 1",
  "instance": "/api/v1/eventos/congreso/publicar/1",
  "timestamp": "2025-10-13T02:05:10"
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
          description = "Ejemplo de error no controlado durante la" +
            " publicacion.",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/eventos/congreso/publicar/1",
  "timestamp": "2025-10-13T02:06:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONGRESOS)

  public ResponseEntity<Congreso> publicar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.publicar(actor, id, Congreso.PUBLICADO),
      HttpStatus.OK);
  }

  /**
   * Retracta un CONGRESO para que ya no este publicado.
   *
   * @param id
   * ID del CONGRESO a RETRACTAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El CONGRESO retractado.
   */
  @PatchMapping("retractar/{id}")

  @Operation(
    summary = "Retractar congreso",
    description = "Permite a un organizador retractar uno de sus congresos " +
      "previamente publicados, removiendolo de la vista publica. " +
      "El congreso permanecera registrado internamente, pero no sera " +
      "accesible mediante los endpoints publicos.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Congreso retractado exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Ejemplo de respuesta cuando el congreso es " +
            "retractado exitosamente",
          value = """
{
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
  "staffRequerimientos": "Soporte, seguridad, sonido, etc..."
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
          description = "El usuario no esta autenticado o no tiene permiso " +
            "para retractar congresos.",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "El usuario autenticado no tiene permisos para retractar " +
    "congresos.",
  "instance": "/api/v1/eventos/congreso/retractar/1",
  "timestamp": "2025-10-13T02:12:22"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Congreso no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El congreso con el ID especificado no existe.",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el congreso con ID 1",
  "instance": "/api/v1/eventos/congreso/retractar/1",
  "timestamp": "2025-10-13T02:15:10"
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
          description = "Ejemplo de error no controlado durante la " +
            "retractacion.",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/eventos/congreso/retractar/1",
  "timestamp": "2025-10-13T02:16:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONGRESOS)

  public ResponseEntity<Congreso> retractar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.publicar(actor, id, Congreso.RETRACTADO),
      HttpStatus.OK);
  }



  /**
   * Cancela un registro.
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
  @PatchMapping("cancelar/{id}")

  @Operation(
    summary = "Cancelar congreso",
    description = "Permite a un organizador cancelar uno de sus congresos, " +
      "impidiendo nuevas inscripciones y marcandolo como cancelado. " +
      "El registro permanece accesible para consulta administrativa.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Congreso cancelado exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Ejemplo de respuesta cuando el congreso es" +
            " cancelado exitosamente",
          value = """
{
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

  "publicado": true,
  "cancelado": true,

  "cupo": 300,
  "inscritos": 120,
  "asistencias": 0,

  "staffCantidad": 20,
  "staffRequerimientos": "Soporte, seguridad, sonido, etc..."
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
          description = "El usuario no esta autenticado o no tiene permiso " +
            "para cancelar congresos.",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "El usuario autenticado no tiene permisos para cancelar " +
    "congresos.",
  "instance": "/api/v1/eventos/congreso/cancelar/1",
  "timestamp": "2025-10-13T02:25:00"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Congreso no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El congreso con el ID especificado no existe.",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el congreso con ID 1",
  "instance": "/api/v1/eventos/congreso/cancelar/1",
  "timestamp": "2025-10-13T02:27:18"
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
          description = "Ejemplo de error no controlado durante la" +
            " cancelacion.",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/eventos/congreso/cancelar/1",
  "timestamp": "2025-10-13T02:29:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONGRESOS)

  public ResponseEntity<Congreso> cancelar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.cancelar(actor, id, Congreso.CANCELADO),
      HttpStatus.OK);
  }

  /**
   * Restaura un CONGRESO.
   *
   * @param id
   * ID del CONGRESO a RESTAURAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El CONGRESO restaurado.
   */
  @PatchMapping("restaurar/{id}")

  @Operation(
    summary = "Restaurar congreso",
    description = "Permite a un organizador restaurar un congreso que haya " +
      "sido previamente cancelado, reactivandolo para su uso normal. " +
      "El congreso puede ser publicado nuevamente si es necesario.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Congreso restaurado exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Ejemplo de respuesta cuando el congreso es" +
            " restaurado exitosamente",
          value = """
{
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
  "inscritos": 120,
  "asistencias": 0,

  "staffCantidad": 20,
  "staffRequerimientos": "Soporte, seguridad, sonido, etc..."
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
          description = "El usuario no esta autenticado o no tiene permiso " +
            "para restaurar congresos.",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail":
   "El usuario autenticado no tiene permisos para restaurar congresos.",
  "instance": "/api/v1/eventos/congreso/restaurar/1",
  "timestamp": "2025-10-13T02:35:42"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Congreso no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El congreso con el ID especificado no existe.",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el congreso con ID 1",
  "instance": "/api/v1/eventos/congreso/restaurar/1",
  "timestamp": "2025-10-13T02:37:18"
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
          description = "Ejemplo de error no controlado durante la" +
            " restauracion.",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/eventos/congreso/restaurar/1",
  "timestamp": "2025-10-13T02:39:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONGRESOS)

  public ResponseEntity<Congreso> restaurar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.cancelar(actor, id, Congreso.RESTAURADO),
      HttpStatus.OK);
  }



  /**
   * Elimina un CONGRESO.
   *
   * @param id
   * ID del CONGRESO a ELIMINAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   */
  @DeleteMapping("eliminar/{id}")

  @Operation(
    summary = "Eliminar congreso",
    description = "Permite a un organizador eliminar uno de sus congresos de " +
      "forma permanente.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "204",
      description = "Congreso eliminado exitosamente",
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          name = "Exito",
          description = "Eliminacion exitosa, sin contenido en la respuesta",
          value = "{}"
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
          description = "El usuario no esta autenticado o no tiene permiso " +
            "para eliminar congresos.",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "El usuario autenticado no tiene permisos para eliminar " +
    "congresos.",
  "instance": "/api/v1/eventos/congreso/eliminar/1",
  "timestamp": "2025-10-13T02:46:22"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Congreso no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El congreso con el ID especificado no existe.",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el congreso con ID 1",
  "instance": "/api/v1/eventos/congreso/eliminar/1",
  "timestamp": "2025-10-13T02:47:58"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "409",
      description = "Conflicto de eliminacion",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El congreso no puede eliminarse debido a" +
            " dependencias activas o registros asociados.",
          value = """
{
  "type": "about:blank",
  "title": "Conflict",
  "status": 409,
  "detail": "El congreso no puede eliminarse porque tiene asistencias o " +
    "inscripciones activas.",
  "instance": "/api/v1/eventos/congreso/eliminar/1",
  "timestamp": "2025-10-13T02:49:15"
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
          description = "Ejemplo de error no controlado durante la" +
            " eliminacion.",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/eventos/congreso/eliminar/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.ELIMINAR_CONGRESOS)

  public void eliminar (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    var deleted = conSvc.eliminar(actor, id);
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Consulta los CONGRESOS publicados indiscriminadamente.
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
  @GetMapping("publicados")

  @Operation(
    summary = "Listar congresos publicados",
    description = "Consulta la lista de congresos que han sido publicados y " +
      "son visibles para el publico general.",
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
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Ejemplo de respuesta con una pagina de congresos " +
            "publicados",
          value = """
[
  {
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

    "publicado": true,
    "cancelado": false,

    "cupo": 300,
    "inscritos": 0,
    "asistencias": 0,

    "staffCantidad": 20,
    "staffRequerimientos": "Soporte, seguridad, sonido, etc..."
  },
  {
    "id": 2,
    "fechaCreacion": "2025-10-10T02:12:00",
    "creadorId": 1,
    "organizadorId": 1,

    "nombre": "Salud y bienestar 2025",
    "resumen": "For a healthier community",
    "descripcion": "Encuentro anual de bienestar fisico y mental.",
    "direccion": "Av. Reforma 102, Tijuana",

    "fechaInicio": "2025-11-05T09:00:00",
    "fechaFin": "2025-11-05T18:00:00",

    "inscripcionesFechaInicio": "2025-10-15T00:00:00",
    "inscripcionesFechaFin": "2025-11-03T00:00:00",
  
    "gratuito": true,

    "publicado": true,
    "cancelado": false,

    "cupo": 200,
    "inscritos": 25,
    "asistencias": 0,

    "staffCantidad": 10,
    "staffRequerimientos": "Recepcion, soporte tecnico, limpieza"
  }
]
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Parametros de paginacion invalidos",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Ejemplo de respuesta cuando los parametros page o " +
            "pageSize son invalidos",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for parameters page or pageSize.",
  "instance": "/api/v1/eventos/congreso/publicados",
  "timestamp": "2025-10-13T03:05:10",
  "campos": {
    "page": "Debe ser mayor o igual que 0",
    "pageSize": "Debe ser menor o igual que 100"
  }
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
          description = "Ejemplo de error no controlado durante la consulta.",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/eventos/congreso/publicados",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<List<Congreso>> publicados (

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      conSvc.qPublicados(page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta los CONGRESOS publicados cuya fecha de terminacion aun es a
   * futuro, incluyendo aquellos que se encuentran cancelados.
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
  @GetMapping("publicados/proximos")

  @Operation(
    summary = "Listar congresos publicados proximos",
    description = "Consulta la lista de congresos que han sido publicados y " +
      "cuyo inicio aun no ha ocurrido.",
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
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Ejemplo de respuesta con una pagina de congresos " +
            "publicados proximos",
          value = """
[
  {
    "id": 3,
    "fechaCreacion": "2025-10-12T10:22:00",
    "creadorId": 2,
    "organizadorId": 2,

    "nombre": "Congreso de Innovacion 2025",
    "resumen": "Tendencias tecnicas emergentes",
    "descripcion": "Evento centrado en el intercambio de ideas sobre " +
      "tecnologia aplicada e innovacion.",
    "direccion": "Centro de Convenciones, Guadalajara",

    "fechaInicio": "2025-11-15T09:00:00",
    "fechaFin": "2025-11-16T18:00:00",

    "inscripcionesFechaInicio": "2025-10-20T00:00:00",
    "inscripcionesFechaFin": "2025-11-14T00:00:00",
  
    "gratuito": true,

    "publicado": true,
    "cancelado": false,

    "cupo": 500,
    "inscritos": 150,
    "asistencias": 0,

    "staffCantidad": 30,
    "staffRequerimientos": "Sonido, proyeccion, registro de asistentes"
  },
  {
    "id": 4,
    "fechaCreacion": "2025-10-13T08:00:00",
    "creadorId": 3,
    "organizadorId": 3,

    "nombre": "Educacion y Futuro 2025",
    "resumen": "Repensando la educacion en la era digital",
    "descripcion": "Simposio sobre las nuevas metodologias educativas y " +
      "el papel de la tecnologia en el aprendizaje.",
    "direccion": "Av. Universidad 250, Ciudad de Mexico",

    "fechaInicio": "2025-12-01T09:00:00",
    "fechaFin": "2025-12-02T18:00:00",

    "inscripcionesFechaInicio": "2025-10-15T00:00:00",
    "inscripcionesFechaFin": "2025-11-29T00:00:00",
  
    "gratuito": true,

    "publicado": true,
    "cancelado": false,

    "cupo": 350,
    "inscritos": 40,
    "asistencias": 0,

    "staffCantidad": 15,
    "staffRequerimientos": "Recepcion, soporte tecnico, catering"
  }
]
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Parametros de paginacion invalidos",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Ejemplo de error cuando los parametros page o " +
            "pageSize no son validos",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for parameters page or pageSize.",
  "instance": "/api/v1/eventos/congreso/publicados/proximos",
  "timestamp": "2025-10-13T03:20:15",
  "campos": {
    "page": "Debe ser mayor o igual que 0",
    "pageSize": "Debe ser menor o igual que 100"
  }
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
          description = "Ejemplo de error no controlado durante la consulta.",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/eventos/congreso/publicados/proximos",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<List<Congreso>> publicadosProximos (

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      conSvc.qPublicadosProximos(page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta el CONGRESO publicado con el ID especifico.
   *
   * @param id
   * ID del CONGRESO.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping("publicados/congreso/{id}")

  @Operation(
    summary = "Consultar congresos publicado especifico",
    description = "Consulta un congreso publicado con cierto ID.",
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
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "El congreso con el ID especificado",
          value = """
{
  "id": 5,
  "fechaCreacion": "2025-10-05T12:00:00",
  "creadorId": 2,
  "organizadorId": 2,

  "nombre": "Congreso Internacional de Ingenieria 2025",
  "resumen": "Investigacion aplicada en ingenieria moderna",
  "descripcion": "Evento centrado en compartir avances recientes en " +
    "robotica, energia y materiales inteligentes.",
  "direccion": "Centro Cultural Tijuana, B.C.",

  "fechaInicio": "2025-10-13T08:00:00",
  "fechaFin": "2025-10-14T18:00:00",

  "inscripcionesFechaInicio": "2025-09-15T00:00:00",
  "inscripcionesFechaFin": "2025-10-12T00:00:00",
  
  "gratuito": true,

  "publicado": true,
  "cancelado": false,

  "cupo": 400,
  "inscritos": 350,
  "asistencias": 0,

  "staffCantidad": 25,
  "staffRequerimientos": "Seguridad, registro, soporte tecnico"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Congreso no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El congreso con el ID especificado no existe.",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el congreso con ID 1",
  "instance": "/api/v1/eventos/congreso/publicados/congreso/1",
  "timestamp": "2025-10-13T02:37:18"
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
          description = "Ejemplo de error no controlado durante la consulta.",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/eventos/congreso/publicados/congreso/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<Congreso> qIdPublicado (

    @PathVariable("id")
    Long id
  ) {
    return new ResponseEntity<>(conSvc.afirmarPublicado(id), HttpStatus.OK);
  }



  /**
   * Consulta los CONGRESOS publicados usando una busqueda de texto.
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
  @GetMapping("publicados/buscar")

  @Operation(
    summary = "Buscar congresos publicados",
    description = "Consulta congresos publicados usando busqueda de texto.",
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
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Congresos encontrados",
          value = """
[
  {
    "id": 1,
    "fechaCreacion": "2025-10-11T01:56:11",
    "creadorId": 0,
    "organizadorId": 0,
    
    "nombre": "Congreso de Tecnologia 2025",
    "resumen": "Innovacion y desarrollo tecnologico",
    "descripcion": "Evento sobre las ultimas tendencias...",
    "direccion": "Centro de Convenciones",
    
    "fechaInicio": "2025-11-15T09:00:00",
    "fechaFin": "2025-11-16T18:00:00",
    
    "inscripcionesFechaInicio": "2025-10-20T00:00:00",
    "inscripcionesFechaFin": "2025-11-14T00:00:00",
  
    "gratuito": true,
    
    "publicado": true,
    "cancelado": false,
    
    "cupo": 500,
    "inscritos": 150,
    "asistencias": 0,
    
    "staffCantidad": 30,
    "staffRequerimientos": "Soporte tecnico, registro"
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
  "instance": "/api/v1/eventos/congreso/publicados/buscar",
  "timestamp": "2025-10-13T04:05:10",
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
  "instance": "/api/v1/eventos/congreso/publicados/buscar",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<List<Congreso>> publicadosBuscar (

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
      conSvc.publicadosBuscar(txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta la foto del CONGRESO publicado especificado en el slot de
   * multimedia especificado.
   *
   * @param id
   * ID del CONGRESO.
   *
   * @param slot
   * Slot de multimedia.
   *
   * @return
   * Respuesta HTTP acorde.
   *
   * @throws ResponseStatusException
   * <p>
   * {@code HTTP-NOT_FOUND}
   * Si el CONGRESO no existe o si no tiene una foto.
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si el slot especificado no existe.
   */
  @GetMapping("publicados/congreso/{id}/media/{slot}")

  @Operation(
    summary = "Consultar multimedia de congreso publicado",
    description = "Obtiene el archivo multimedia de un congreso publicado.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Archivo multimedia obtenido",
      content = @Content(
        mediaType = "image/jpeg",
        schema = @Schema(type = "string", format = "binary"),
        examples = @ExampleObject(
          name = "Imagen",
          description = "Bytes de la imagen",
          value = "<BYTES_DE_IMAGEN>"
        )
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
  "detail": "El slot especificado no es valido",
  "instance": "/api/v1/eventos/congreso/publicados/congreso/1/media/invalido",
  "timestamp": "2025-10-13T03:15:10"
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
            name = "Congreso no encontrado",
            description = "El congreso no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el congreso con ID 1",
  "instance": "/api/v1/eventos/congreso/publicados/congreso/1/media/foto",
  "timestamp": "2025-10-13T03:16:17"
}
"""
          ),
          @ExampleObject(
            name = "Sin multimedia",
            description = "No hay archivo en el slot",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "El congreso no tiene archivo en el slot especificado",
  "instance": "/api/v1/eventos/congreso/publicados/congreso/1/media/foto",
  "timestamp": "2025-10-13T03:17:18"
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
  "instance": "/api/v1/eventos/congreso/publicados/congreso/1/media/foto",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<byte[]> qIdPublicadoMedia (

    @PathVariable("id")
    Long id,

    @PathVariable("slot")
    String slot
  ) {
    return conSvc.afirmarMedia(id, slot);
  }



  /**
   * Consulta el CONGRESO con el ID especifico.
   *
   * @param id
   * ID del CONGRESO.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping("congreso/{id}")

  @Operation(
    summary = "Consultar congreso por ID",
    description = "Obtiene un congreso especifico por su ID.",
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
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Congreso encontrado",
          value = """
{
  "id": 1,
  "fechaCreacion": "2025-10-11T01:56:11",
  "creadorId": 0,
  "organizadorId": 0,
  
  "nombre": "Congreso de Investigacion",
  "resumen": "Avances en investigacion cientifica",
  "descripcion": "Presentacion de proyectos de investigacion...",
  "direccion": "Auditorio Principal",
  
  "fechaInicio": "2025-11-20T09:00:00",
  "fechaFin": "2025-11-21T18:00:00",
  
  "inscripcionesFechaInicio": "2025-10-25T00:00:00",
  "inscripcionesFechaFin": "2025-11-19T00:00:00",
  
  "gratuito": true,
  
  "publicado": false,
  "cancelado": false,
  
  "cupo": 200,
  "inscritos": 45,
  "asistencias": 0,
  
  "staffCantidad": 15,
  "staffRequerimientos": "Coordinacion, soporte"
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
  "instance": "/api/v1/eventos/congreso/congreso/1",
  "timestamp": "2025-10-13T03:25:21"
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
          description = "Congreso no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el congreso con ID 1",
  "instance": "/api/v1/eventos/congreso/congreso/1",
  "timestamp": "2025-10-13T03:26:17"
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
  "instance": "/api/v1/eventos/congreso/congreso/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONGRESOS_NO_PUBLICADOS)

  public ResponseEntity<Congreso> qId (

    @PathVariable("id")
    Long id
  ) {
    return new ResponseEntity<>(conSvc.afirmar(id), HttpStatus.OK);
  }



  /**
   * Consulta los CONGRESOS indiscriminadamente usando una busqueda de texto.
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
    summary = "Buscar congresos",
    description = "Busca congresos usando filtro de texto.",
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
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Congresos encontrados",
          value = """
[
  {
    "id": 2,
    "fechaCreacion": "2025-10-10T02:12:00",
    "creadorId": 1,
    "organizadorId": 1,
    
    "nombre": "Congreso de Medicina",
    "resumen": "Avances en salud y medicina",
    "descripcion": "Discusion sobre nuevos tratamientos...",
    "direccion": "Hospital Regional",
    
    "fechaInicio": "2025-12-05T09:00:00",
    "fechaFin": "2025-12-06T18:00:00",
    
    "inscripcionesFechaInicio": "2025-11-01T00:00:00",
    "inscripcionesFechaFin": "2025-12-04T00:00:00",
  
    "gratuito": true,
    
    "publicado": false,
    "cancelado": false,
    
    "cupo": 150,
    "inscritos": 30,
    "asistencias": 0,
    
    "staffCantidad": 10,
    "staffRequerimientos": "Medicos, enfermeras"
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
  "instance": "/api/v1/eventos/congreso/buscar",
  "timestamp": "2025-10-13T03:35:10",
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
  "instance": "/api/v1/eventos/congreso/buscar",
  "timestamp": "2025-10-13T03:36:21"
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
  "instance": "/api/v1/eventos/congreso/buscar",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONGRESOS_NO_PUBLICADOS)

  public ResponseEntity<List<Congreso>> buscar (

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
      conSvc.buscar(txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Permite a un ORGANIZADOR consultar sus propios CONGRESOS.
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
    summary = "Consultar congresos propios",
    description = "Obtiene los congresos del organizador autenticado.",
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
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Congresos del organizador",
          value = """
[
  {
    "id": 3,
    "fechaCreacion": "2025-10-12T10:22:00",
    "creadorId": 2,
    "organizadorId": 2,
    
    "nombre": "Mi Congreso Personal",
    "resumen": "Congreso organizado por mi",
    "descripcion": "Este es un congreso que he organizado...",
    "direccion": "Mi Localidad",
    
    "fechaInicio": "2025-11-25T09:00:00",
    "fechaFin": "2025-11-26T18:00:00",
    
    "inscripcionesFechaInicio": "2025-11-01T00:00:00",
    "inscripcionesFechaFin": "2025-11-24T00:00:00",
  
    "gratuito": true,
    
    "publicado": true,
    "cancelado": false,
    
    "cupo": 100,
    "inscritos": 25,
    "asistencias": 0,
    
    "staffCantidad": 8,
    "staffRequerimientos": "Organizacion local"
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
  "instance": "/api/v1/eventos/congreso/mios",
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
  "instance": "/api/v1/eventos/congreso/mios",
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
  "instance": "/api/v1/eventos/congreso/mios",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONGRESOS_PROPIOS)

  public ResponseEntity<List<Congreso>> mios (

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
      conSvc.mios(actor, page, pageSize),
      HttpStatus.OK);
  }

  /**
   * Permite a un ORGANIZADOR consultar sus propios CONGRESOS usando una
   * busqueda de texto opcional.
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
    summary = "Buscar congresos propios",
    description = "Consulta los congresos del organizador autenticado usando" +
      " una busqueda de texto.",
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
        schema = @Schema(implementation = Congreso.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Congresos encontrados",
          value = """
[
  {
    "id": 4,
    "fechaCreacion": "2025-10-13T08:00:00",
    "creadorId": 3,
    "organizadorId": 3,
    
    "nombre": "Congreso de Innovacion Personal",
    "resumen": "Mis proyectos innovadores",
    "descripcion": "Este congreso presenta mis trabajos...",
    "direccion": "Mi Instituto",
    
    "fechaInicio": "2025-12-10T09:00:00",
    "fechaFin": "2025-12-11T18:00:00",
    
    "inscripcionesFechaInicio": "2025-11-15T00:00:00",
    "inscripcionesFechaFin": "2025-12-09T00:00:00",
  
    "gratuito": true,
    
    "publicado": false,
    "cancelado": false,
    
    "cupo": 80,
    "inscritos": 15,
    "asistencias": 0,
    
    "staffCantidad": 5,
    "staffRequerimientos": "Atencion a participantes"
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
  "instance": "/api/v1/eventos/congreso/buscar/mios",
  "timestamp": "2025-10-13T03:55:10",
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
  "instance": "/api/v1/eventos/congreso/buscar/mios",
  "timestamp": "2025-10-13T03:56:21"
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
  "instance": "/api/v1/eventos/congreso/buscar/mios",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONGRESOS_PROPIOS)

  public ResponseEntity<List<Congreso>> buscarMios (

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
      conSvc.buscarMios(actor, txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta la foto del CONGRESO especificado en el slot de multimedia
   * especificado.
   *
   * @param id
   * ID del CONGRESO.
   *
   * @param slot
   * Slot de multimedia.
   *
   * @return
   * Respuesta HTTP acorde.
   *
   * @throws ResponseStatusException
   * <p>
   * {@code HTTP-NOT_FOUND}
   * Si el CONGRESO no existe o si no tiene una foto.
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si el slot especificado no existe.
   */
  @GetMapping("congreso/{id}/media/{slot}")

  @Operation(
    summary = "Consultar multimedia de congreso",
    description = "Obtiene el archivo multimedia de un congreso.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Archivo multimedia obtenido",
      content = @Content(
        mediaType = "image/jpeg",
        schema = @Schema(type = "string", format = "binary"),
        examples = @ExampleObject(
          name = "Imagen",
          description = "Bytes de la imagen",
          value = "<BYTES_DE_IMAGEN>"
        )
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
  "detail": "El slot especificado no es valido",
  "instance": "/api/v1/eventos/congreso/congreso/1/media/invalido",
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
  "instance": "/api/v1/eventos/congreso/congreso/1/media/foto",
  "timestamp": "2025-10-13T04:06:21"
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
            name = "Congreso no encontrado",
            description = "El congreso no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el congreso con ID 1",
  "instance": "/api/v1/eventos/congreso/congreso/1/media/foto",
  "timestamp": "2025-10-13T04:07:17"
}
"""
          ),
          @ExampleObject(
            name = "Sin multimedia",
            description = "No hay archivo en el slot",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "El congreso no tiene archivo en el slot especificado",
  "instance": "/api/v1/eventos/congreso/congreso/1/media/foto",
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
  "instance": "/api/v1/eventos/congreso/congreso/1/media/foto",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONGRESOS_NO_PUBLICADOS)

  public ResponseEntity<byte[]> qIdMedia (

    @PathVariable("id")
    Long id,

    @PathVariable("slot")
    String slot
  ) {
    return conSvc.afirmarMedia(id, slot);
  }
}
