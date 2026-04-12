package com.tecn.tijuana.congresos.eventos.conferencia;

import com.tecn.tijuana.congresos.eventos.conferencia.dto.RegistroConferenciaDto;
import com.tecn.tijuana.congresos.eventos.congreso.Congreso;
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
@RequestMapping(path = "api/v1/eventos/conferencia")
@Validated
public class ConferenciaController {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final ConferenciaService confSvc;


  /**
   * CONSTRUCTOR principal de esta clase/controller, usado principalmente por
   * Spring para el funcionamiento de la app.
   *
   * @param confSvc
   * Objeto de la capa de servicio de la entidad de CONFERENCIA.
   */
  public ConferenciaController (
    ConferenciaService confSvc
  ) {
    this.confSvc = confSvc;
  }



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite a un ORGANIZADOR registrar una nueva CONFERENCIA.
   *
   * @param dto
   * Objeto con los datos del nuevo registro.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El nuevo registro creado.
   */
  @PostMapping("registrar")

  @Operation(
    summary = "Registrar conferencia",
    description = "Permite a un organizador registrar una nueva conferencia.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Datos de la conferencia",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = RegistroConferenciaDto.class),
        examples = @ExampleObject(
          name = "Registrar",
          description = "Ejemplo para registrar conferencia",
          value = """
{
  "congresoId": 1,
  
  "nombre": "Inteligencia Artificial Aplicada",
  "resumen": "Aplicaciones practicas de IA",
  "descripcion": "Conferencia sobre implementacion de IA en la industria...",
  
  "sala": "Aula Magna",
  
  "fechaInicio": "2025-10-11T10:00:00",
  "fechaFin": "2025-10-11T12:00:00",
  
  "cupo": 100,
  
  "staffCantidad": 3,
  "staffRequerimientos": "Proyector, sonido, asistencia",
  
  "conferencistaNombre": "Dr. Juan Perez",
  "conferencistaEmail": "juan.perez@universidad.edu",
  "conferencistaTelPref": "52",
  "conferencistaTelSuf": "6641234567",
  "conferencistaSemblanza": "Experto en IA con 10 anos de experiencia..."
}
"""
        )
      )
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "201",
      description = "Registro exitoso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Conferencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Conferencia registrada",
          value = """
{
  "id": 1,
  "fechaCreacion": "2025-10-11T01:56:11",
  "creadorId": 0,
  "congresoId": 1,
  
  "nombre": "Inteligencia Artificial Aplicada",
  "resumen": "Aplicaciones practicas de IA",
  "descripcion": "Conferencia sobre implementacion de IA en la industria...",
  
  "sala": "Aula Magna",
  
  "fechaInicio": "2025-10-11T10:00:00",
  "fechaFin": "2025-10-11T12:00:00",
  
  "publicada": false,
  "cancelada": false,
  
  "cupo": 100,
  "inscritos": 0,
  "asistencias": 0,
  
  "staffCantidad": 3,
  "staffRequerimientos": "Proyector, sonido, asistencia",
  
  "conferencistaNombre": "Dr. Juan Perez",
  "conferencistaEmail": "juan.perez@universidad.edu",
  "conferencistaTelPref": "52",
  "conferencistaTelSuf": "6641234567",
  "conferencistaSemblanza": "Experto en IA con 10 anos de experiencia..."
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
  "detail": "Validation failed for object='registroConferenciaDto'",
  "instance": "/api/v1/eventos/conferencia/registrar",
  "timestamp": "2025-10-13T04:15:16",
  "campos": {
    "registroConferenciaDto.nombre":
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
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/eventos/conferencia/registrar",
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
  "instance": "/api/v1/eventos/conferencia/registrar",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.REGISTRAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> registrar (

    @RequestBody
    RegistroConferenciaDto dto,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      confSvc.registrar(actor, dto),
      HttpStatus.CREATED);
  }



  /**
   * Actualiza un registro con los datos especificados.
   *
   * @param id
   * ID del registro a EDITAR.
   *
   * @param conferencia
   * Objeto con los datos nuevos del Conferencia a EDITAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro editado.
   */
  @PatchMapping("editar/{id}")

  @Operation(
    summary = "Editar conferencia",
    description = "Permite a un organizador editar una conferencia existente.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Datos actualizados de la conferencia",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Conferencia.class),
        examples = @ExampleObject(
          name = "Editar",
          description = "Ejemplo para editar conferencia",
          value = """
{
  "id": 1,
  "fechaCreacion": "2025-10-11T01:56:11",
  "creadorId": 0,
  "congresoId": 1,
  
  "nombre": "Inteligencia Artificial Avanzada",
  "resumen": "Aplicaciones avanzadas de IA",
  "descripcion": "Conferencia sobre tecnicas avanzadas de IA...",
  
  "sala": "Auditorio Principal",
  
  "fechaInicio": "2025-10-11T11:00:00",
  "fechaFin": "2025-10-11T13:00:00",
  
  "publicada": false,
  "cancelada": false,
  
  "cupo": 150,
  "inscritos": 0,
  "asistencias": 0,
  
  "staffCantidad": 4,
  "staffRequerimientos": "Proyector, sonido, asistencia, networking",
  "conferencistaNombre": "Dr. Juan Perez Rodriguez",
  "conferencistaEmail": "juan.rodriguez@universidad.edu",
  "conferencistaTelPref": "52",
  "conferencistaTelSuf": "6641234567",
  "conferencistaSemblanza": "Experto en IA con 15 anos de experiencia..."
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
        schema = @Schema(implementation = Conferencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Conferencia editada",
          value = """
{
  "id": 1,
  "fechaCreacion": "2025-10-11T01:56:11",
  "creadorId": 0,
  "congresoId": 1,
  
  "nombre": "Inteligencia Artificial Avanzada",
  "resumen": "Aplicaciones avanzadas de IA",
  "descripcion": "Conferencia sobre tecnicas avanzadas de IA...",
  
  "sala": "Auditorio Principal",
  
  "fechaInicio": "2025-10-11T11:00:00",
  "fechaFin": "2025-10-11T13:00:00",
  
  "publicada": false,
  "cancelada": false,
  
  "cupo": 150,
  "inscritos": 0,
  "asistencias": 0,
  
  "staffCantidad": 4,
  "staffRequerimientos": "Proyector, sonido, asistencia, networking",
  
  "conferencistaNombre": "Dr. Juan Perez Rodriguez",
  "conferencistaEmail": "juan.rodriguez@universidad.edu",
  "conferencistaTelPref": "52",
  "conferencistaTelSuf": "6641234567",
  "conferencistaSemblanza": "Experto en IA con 15 anos de experiencia..."
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
  "detail": "Validation failed for object='conferencia'",
  "instance": "/api/v1/eventos/conferencia/editar/1",
  "timestamp": "2025-10-13T04:25:16",
  "campos": {
    "conferencia.nombre":
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
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/eventos/conferencia/editar/1",
  "timestamp": "2025-10-13T04:26:21"
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
  "detail": "No se encontro la conferencia con ID 1",
  "instance": "/api/v1/eventos/conferencia/editar/1",
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
  "instance": "/api/v1/eventos/conferencia/editar/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> editar (

    @PathVariable
    Long id,

    @RequestBody
    Conferencia conferencia,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      confSvc.editar(actor, id, conferencia),
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
    summary = "Editar multimedia de conferencia",
    description = "Permite editar un slot multimedia de una conferencia.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Contenido del archivo multimedia",
      required = true,
      content = @Content(
        mediaType = "multipart/form-data",
        examples = {
          @ExampleObject(
            name = "Nueva imagen",
            description = "Ejemplo para poner nueva imagen en el slot",
            value = """
{
  "img": <CONTENIDO_DEL_ARCHIVO>
}
"""
          ),
          @ExampleObject(
            name = "Remover",
            description = "Ejemplo para remover imagen del slot",
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
          description = "Slot invalido o archivo incorrecto",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "El slot especificado no es valido",
  "instance": "/api/v1/eventos/conferencia/editar/1/media/invalido",
  "timestamp": "2025-10-13T04:35:10"
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
  "instance": "/api/v1/eventos/conferencia/editar/1/media/conferencistaFoto",
  "timestamp": "2025-10-13T04:36:21"
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
  "detail": "No se encontro la conferencia con ID 1",
  "instance": "/api/v1/eventos/conferencia/editar/1/media/conferencistaFoto",
  "timestamp": "2025-10-13T04:37:17"
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
  "instance": "/api/v1/eventos/conferencia/editar/1/media/conferencistaFoto",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONFERENCIAS)

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
    confSvc.editarMedia(actor, id, slot, img);
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
    summary = "Publicar conferencia",
    description = "Permite a un organizador publicar una conferencia.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Publicacion exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Conferencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Conferencia publicada",
          value = """
{
  "id": 1,
  "fechaCreacion": "2025-10-11T01:56:11",
  "creadorId": 0,
  "congresoId": 1,
  
  "nombre": "Inteligencia Artificial Avanzada",
  "resumen": "Aplicaciones avanzadas de IA",
  "descripcion": "Conferencia sobre tecnicas avanzadas de IA...",
  
  "sala": "Auditorio Principal",
  
  "fechaInicio": "2025-10-11T11:00:00",
  "fechaFin": "2025-10-11T13:00:00",
  
  "publicada": true,
  "cancelada": false,
  
  "cupo": 150,
  "inscritos": 0,
  "asistencias": 0,
  
  "staffCantidad": 4,
  "staffRequerimientos": "Proyector, sonido, asistencia, networking",
  
  "conferencistaNombre": "Dr. Juan Perez Rodriguez",
  "conferencistaEmail": "juan.rodriguez@universidad.edu",
  "conferencistaTelPref": "52",
  "conferencistaTelSuf": "6641234567",
  "conferencistaSemblanza": "Experto en IA con 15 anos de experiencia..."
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
  "instance": "/api/v1/eventos/conferencia/publicar/1",
  "timestamp": "2025-10-13T04:45:21"
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
  "detail": "No se encontro la conferencia con ID 1",
  "instance": "/api/v1/eventos/conferencia/publicar/1",
  "timestamp": "2025-10-13T04:46:17"
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
  "instance": "/api/v1/eventos/conferencia/publicar/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> publicar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      confSvc.publicar(actor, id, Conferencia.PUBLICADA),
      HttpStatus.OK);
  }



  /**
   * Retracta un registro para que ya no este publicado.
   *
   * @param id
   * ID del registro a RETRACTAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro retractado.
   */
  @PatchMapping("retractar/{id}")

  @Operation(
    summary = "Retractar conferencia",
    description = "Permite a un organizador retractar una conferencia" +
      " publicada.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Retractacion exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Conferencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Conferencia retractada",
          value = """
{
  "id": 1,
  "fechaCreacion": "2025-10-11T01:56:11",
  "creadorId": 0,
  "congresoId": 1,
  
  "nombre": "Inteligencia Artificial Avanzada",
  "resumen": "Aplicaciones avanzadas de IA",
  "descripcion": "Conferencia sobre tecnicas avanzadas de IA...",
  
  "sala": "Auditorio Principal",
  
  "fechaInicio": "2025-10-11T11:00:00",
  "fechaFin": "2025-10-11T13:00:00",
  
  "publicada": false,
  "cancelada": false,
  
  "cupo": 150,
  "inscritos": 0,
  "asistencias": 0,
  
  "staffCantidad": 4,
  "staffRequerimientos": "Proyector, sonido, asistencia, networking",
  
  "conferencistaNombre": "Dr. Juan Perez Rodriguez",
  "conferencistaEmail": "juan.rodriguez@universidad.edu",
  "conferencistaTelPref": "52",
  "conferencistaTelSuf": "6641234567",
  "conferencistaSemblanza": "Experto en IA con 15 anos de experiencia..."
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
  "instance": "/api/v1/eventos/conferencia/retractar/1",
  "timestamp": "2025-10-13T04:55:21"
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
  "detail": "No se encontro la conferencia con ID 1",
  "instance": "/api/v1/eventos/conferencia/retractar/1",
  "timestamp": "2025-10-13T04:56:17"
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
  "instance": "/api/v1/eventos/conferencia/retractar/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> retractar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      confSvc.publicar(actor, id, Conferencia.RETRACTADA),
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
    summary = "Cancelar conferencia",
    description = "Permite a un organizador cancelar una conferencia.",
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
        schema = @Schema(implementation = Conferencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Conferencia cancelada",
          value = """
{
  "id": 1,
  "fechaCreacion": "2025-10-11T01:56:11",
  "creadorId": 0,
  "congresoId": 1,
  
  "nombre": "Inteligencia Artificial Avanzada",
  "resumen": "Aplicaciones avanzadas de IA",
  "descripcion": "Conferencia sobre tecnicas avanzadas de IA...",
  
  "sala": "Auditorio Principal",
  
  "fechaInicio": "2025-10-11T11:00:00",
  "fechaFin": "2025-10-11T13:00:00",
  
  "publicada": true,
  "cancelada": true,
  
  "cupo": 150,
  "inscritos": 25,
  "asistencias": 0,
  
  "staffCantidad": 4,
  "staffRequerimientos": "Proyector, sonido, asistencia, networking",
  
  "conferencistaNombre": "Dr. Juan Perez Rodriguez",
  "conferencistaEmail": "juan.rodriguez@universidad.edu",
  "conferencistaTelPref": "52",
  "conferencistaTelSuf": "6641234567",
  "conferencistaSemblanza": "Experto en IA con 15 anos de experiencia..."
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
  "instance": "/api/v1/eventos/conferencia/cancelar/1",
  "timestamp": "2025-10-13T05:05:21"
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
  "detail": "No se encontro la conferencia con ID 1",
  "instance": "/api/v1/eventos/conferencia/cancelar/1",
  "timestamp": "2025-10-13T05:06:17"
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
  "instance": "/api/v1/eventos/conferencia/cancelar/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> cancelar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      confSvc.cancelar(actor, id, Conferencia.CANCELADA),
      HttpStatus.OK);
  }



  /**
   * Restaura un registro.
   *
   * @param id
   * ID del registro a RESTAURAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro restaurado.
   */
  @PatchMapping("restaurar/{id}")

  @Operation(
    summary = "Restaurar conferencia",
    description = "Permite a un organizador restaurar una conferencia" +
      " cancelada.",
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
        schema = @Schema(implementation = Conferencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Conferencia restaurada",
          value = """
{
  "id": 1,
  "fechaCreacion": "2025-10-11T01:56:11",
  "creadorId": 0,
  "congresoId": 1,
  
  "nombre": "Inteligencia Artificial Avanzada",
  "resumen": "Aplicaciones avanzadas de IA",
  "descripcion": "Conferencia sobre tecnicas avanzadas de IA...",
  
  "sala": "Auditorio Principal",
  
  "fechaInicio": "2025-10-11T11:00:00",
  "fechaFin": "2025-10-11T13:00:00",
  
  "publicada": false,
  "cancelada": false,
  
  "cupo": 150,
  "inscritos": 25,
  "asistencias": 0,
  
  "staffCantidad": 4,
  "staffRequerimientos": "Proyector, sonido, asistencia, networking",
  
  "conferencistaNombre": "Dr. Juan Perez Rodriguez",
  "conferencistaEmail": "juan.rodriguez@universidad.edu",
  "conferencistaTelPref": "52",
  "conferencistaTelSuf": "6641234567",
  "conferencistaSemblanza": "Experto en IA con 15 anos de experiencia..."
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
  "instance": "/api/v1/eventos/conferencia/restaurar/1",
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
          description = "Conferencia no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro la conferencia con ID 1",
  "instance": "/api/v1/eventos/conferencia/restaurar/1",
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
  "instance": "/api/v1/eventos/conferencia/restaurar/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> restaurar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      confSvc.cancelar(actor, id, Conferencia.RESTAURADA),
      HttpStatus.OK);
  }



  /**
   * Elimina un registro.
   *
   * @param id
   * ID del registro a ELIMINAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   */
  @DeleteMapping("eliminar/{id}")

  @Operation(
    summary = "Eliminar conferencia",
    description = "Permite a un organizador eliminar una conferencia.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "No requiere cuerpo en la peticion."
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "204",
      description = "Eliminacion exitosa",
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          name = "Exito",
          description = "Eliminacion exitosa",
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
          description = "Sin permisos",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Unauthorized",
  "instance": "/api/v1/eventos/conferencia/eliminar/1",
  "timestamp": "2025-10-13T05:25:21"
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
  "detail": "No se encontro la conferencia con ID 1",
  "instance": "/api/v1/eventos/conferencia/eliminar/1",
  "timestamp": "2025-10-13T05:26:17"
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
  "instance": "/api/v1/eventos/conferencia/eliminar/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.ELIMINAR_CONFERENCIAS)

  public void eliminar (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    var deleted = confSvc.eliminar(actor, id);
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Consulta los registros publicados de un CONGRESO, con busqueda de texto
   * opcional y filtros opcionales de publicada y cancelada.
   *
   * @param congresoId
   * Id del CONGRESO.
   *
   * @param txt {@code [""]}
   * Texto de busqueda.
   *
   * @param publicada {@code [null]}
   * Filtro opcional por estado de publicacion. Si no se especifica, no filtra.
   *
   * @param cancelada {@code [null]}
   * Filtro opcional por estado de cancelacion. Si no se especifica, no filtra.
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
  @GetMapping("publicadas/congreso/{congresoId}")

  @Operation(
    summary = "Consultar conferencias publicadas de congreso",
    description = "Obtiene las conferencias publicadas de un congreso, con" +
      " busqueda de texto y filtros opcionales de publicada y cancelada.",
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
        schema = @Schema(implementation = Conferencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Conferencias encontradas",
          value = """
[
  {
    "id": 1,
    "fechaCreacion": "2025-10-11T01:56:11",
    "creadorId": 0,
    "congresoId": 1,
    "nombre": "Inteligencia Artificial Aplicada",
    "resumen": "Aplicaciones practicas de IA",
    "descripcion": "Conferencia sobre implementacion de IA...",
    "sala": "Aula Magna",
    "fechaInicio": "2025-10-11T10:00:00",
    "fechaFin": "2025-10-11T12:00:00",
    "publicada": true,
    "cancelada": false,
    "cupo": 100,
    "inscritos": 25,
    "asistencias": 0,
    "staffCantidad": 3,
    "staffRequerimientos": "Proyector, sonido, asistencia",
    "conferencistaNombre": "Dr. Juan Perez",
    "conferencistaEmail": "juan.perez@universidad.edu",
    "conferencistaTelPref": "52",
    "conferencistaTelSuf": "6641234567",
    "conferencistaSemblanza": "Experto en IA con 10 anos de experiencia..."
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
  "instance": "/api/v1/eventos/conferencia/publicadas/congreso/1",
  "timestamp": "2025-10-13T05:35:10",
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
  "instance": "/api/v1/eventos/conferencia/publicadas/congreso/1",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<List<Conferencia>> qCongresoIdPublicado (

    @PathVariable("congresoId")
    Long congresoId,

    @RequestParam(name = "txt", required = false, defaultValue = "")
    @Size(max = 30)
    String txt,

    @RequestParam(name = "publicada", required = false)
    Boolean publicada,

    @RequestParam(name = "cancelada", required = false)
    Boolean cancelada,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      confSvc.qCongresoIdPublicadas(
        congresoId, txt, publicada, cancelada, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta el registro publicado con el ID especifico.
   *
   * @param id
   * ID del registro.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping("publicadas/conferencia/{id}")

  @Operation(
    summary = "Consultar conferencia publicada por ID",
    description = "Obtiene una conferencia publicada especifica por su ID.",
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
        schema = @Schema(implementation = Conferencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Conferencia encontrada",
          value = """
{
  "id": 3,
  "fechaCreacion": "2025-10-13T08:00:00",
  "creadorId": 2,
  "congresoId": 1,
  
  "nombre": "Ciberseguridad Moderna",
  "resumen": "Proteccion contra amenazas digitales",
  "descripcion": "Conferencia sobre las ultimas tecnicas de ciberseguridad...",
  
  "sala": "Auditorio Central",
  
  "fechaInicio": "2025-10-15T09:00:00",
  "fechaFin": "2025-10-15T11:00:00",
  
  "publicada": true,
  "cancelada": false,
  
  "cupo": 200,
  "inscritos": 75,
  "asistencias": 0,
  
  "staffCantidad": 5,
  "staffRequerimientos": "Seguridad, registro, soporte tecnico",
  
  "conferencistaNombre": "Ing. Carlos Ramirez",
  "conferencistaEmail": "carlos.ramirez@seguridad.com",
  "conferencistaTelPref": "52",
  "conferencistaTelSuf": "6645551234",
  "conferencistaSemblanza": "Especialista en ciberseguridad con 12 anos de
   experiencia"
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
          description = "Conferencia no existe o no esta publicada",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro la conferencia publicada con ID 3",
  "instance": "/api/v1/eventos/conferencia/publicadas/conferencia/3",
  "timestamp": "2025-10-13T05:46:17"
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
  "instance": "/api/v1/eventos/conferencia/publicadas/conferencia/3",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<Conferencia> qIdPublicada (

    @PathVariable("id")
    Long id
  ) {
    return new ResponseEntity<>(confSvc.afirmarIdPublicada(id), HttpStatus.OK);
  }



  /**
   * Consulta la foto de la CONFERENCIA publicada especificada en el slot de
   * multimedia especificado.
   *
   * @param id
   * ID de la CONFERENCIA.
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
   * Si la CONFERENCIA no existe o si no tiene una foto.
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si el slot especificado no existe.
   */
  @GetMapping("publicadas/conferencia/{id}/media/{slot}")

  @Operation(
    summary = "Consultar multimedia de conferencia publicada",
    description = "Obtiene el archivo multimedia de una conferencia publicada.",
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
  "instance":
   "/api/v1/eventos/conferencia/publicadas/conferencia/1/media/invalido",
  "timestamp": "2025-10-13T05:55:10"
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
            name = "Conferencia no encontrada",
            description = "La conferencia no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro la conferencia publicada con ID 1",
  "instance": "/api/v1/eventos/conferencia/publicadas/conferencia/1/media/"+
    "conferencistaFoto",
  "timestamp": "2025-10-13T05:56:17"
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
  "detail": "La conferencia no tiene archivo en el slot especificado",
  "instance": "/api/v1/eventos/conferencia/publicadas/conferencia/1/media/"+
    "conferencistaFoto",
  "timestamp": "2025-10-13T05:57:18"
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
  "instance": "/api/v1/eventos/conferencia/publicadas/conferencia/1/media/"+
    "conferencistaFoto",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<byte[]> qIdPublicadaMedia (

    @PathVariable("id")
    Long id,

    @PathVariable("slot")
    String slot
  ) {
    return confSvc.afirmarMedia(id, slot);
  }



  /**
   * Consulta el registro con el ID especifico.
   *
   * @param id
   * ID del registro.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping("conferencia/{id}")

  @Operation(
    summary = "Consultar conferencia por ID",
    description = "Obtiene una conferencia especifica por su ID.",
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
        schema = @Schema(implementation = Conferencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Conferencia encontrada",
          value = """
{
  "id": 4,
  "fechaCreacion": "2025-10-14T09:30:00",
  "creadorId": 3,
  
  "congresoId": 2,
  "nombre": "Desarrollo Web Moderno",
  "resumen": "Tecnologias web actuales",
  "descripcion": "Conferencia sobre frameworks y herramientas modernas...",
  
  "sala": "Laboratorio de Software",
  
  "fechaInicio": "2025-10-16T13:00:00",
  "fechaFin": "2025-10-16T15:00:00",
  
  "publicada": false,
  "cancelada": false,
  
  "cupo": 80,
  "inscritos": 15,
  "asistencias": 0,
  
  "staffCantidad": 3,
  "staffRequerimientos": "Computadoras, internet, asistencia",
  
  "conferencistaNombre": "Lic. Ana Garcia",
  "conferencistaEmail": "ana.garcia@desarrollo.com",
  "conferencistaTelPref": "52",
  "conferencistaTelSuf": "6647778888",
  "conferencistaSemblanza": "Desarrolladora fullstack con 7 anos de experiencia"
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
  "instance": "/api/v1/eventos/conferencia/conferencia/4",
  "timestamp": "2025-10-13T06:05:21"
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
  "detail": "No se encontro la conferencia con ID 4",
  "instance": "/api/v1/eventos/conferencia/conferencia/4",
  "timestamp": "2025-10-13T06:06:17"
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
  "instance": "/api/v1/eventos/conferencia/conferencia/4",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONFERENCIAS_NO_PUBLICADAS)

  public ResponseEntity<Conferencia> qId (

    @PathVariable("id")
    Long id
  ) {
    return new ResponseEntity<>(confSvc.afirmar(id), HttpStatus.OK);
  }



  /**
   * Consulta los registros de un CONGRESO, con busqueda de texto opcional y
   * filtros opcionales de publicada y cancelada.
   *
   * @param congresoId
   * Id del CONGRESO.
   *
   * @param txt {@code [""]}
   * Texto de busqueda.
   *
   * @param publicada {@code [null]}
   * Filtro opcional por estado de publicacion. Si no se especifica, no filtra.
   *
   * @param cancelada {@code [null]}
   * Filtro opcional por estado de cancelacion. Si no se especifica, no filtra.
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
    summary = "Consultar conferencias de congreso",
    description = "Obtiene las conferencias de un congreso especifico, con" +
      " busqueda de texto y filtros opcionales de publicada y cancelada.",
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
        schema = @Schema(implementation = Conferencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Conferencias encontradas",
          value = """
[
  {
    "id": 5,
    "fechaCreacion": "2025-10-15T10:00:00",
    "creadorId": 4,
    "congresoId": 2,
    "nombre": "Base de Datos NoSQL",
    "resumen": "Manejo de bases de datos no relacionales",
    "descripcion": "Conferencia sobre MongoDB, Cassandra y otras tecnologias...",
    "sala": "Aula de Bases de Datos",
    "fechaInicio": "2025-10-17T11:00:00",
    "fechaFin": "2025-10-17T13:00:00",
    "publicada": false,
    "cancelada": false,
    "cupo": 60,
    "inscritos": 8,
    "asistencias": 0,
    "staffCantidad": 2,
    "staffRequerimientos": "Servidores, conexion a internet",
    "conferencistaNombre": "Ing. Roberto Silva",
    "conferencistaEmail": "roberto.silva@databases.com",
    "conferencistaTelPref": "52",
    "conferencistaTelSuf": "6643334444",
    "conferencistaSemblanza": "Especialista en bases de datos con 9 anos de experiencia"
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
  "instance": "/api/v1/eventos/conferencia/congreso/2",
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
  "instance": "/api/v1/eventos/conferencia/congreso/2",
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
  "instance": "/api/v1/eventos/conferencia/congreso/2",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONFERENCIAS_NO_PUBLICADAS)

  public ResponseEntity<List<Conferencia>> qCongresoId (

    @PathVariable("congresoId")
    Long congresoId,

    @RequestParam(name = "txt", required = false, defaultValue = "")
    @Size(max = 30)
    String txt,

    @RequestParam(name = "publicada", required = false)
    Boolean publicada,

    @RequestParam(name = "cancelada", required = false)
    Boolean cancelada,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      confSvc.qCongresoId(
        congresoId, txt, publicada, cancelada, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta los registros indiscriminadamente usando una busqueda de texto.
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
    summary = "Buscar conferencias",
    description = "Busca conferencias usando filtro de texto.",
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
        schema = @Schema(implementation = Conferencia.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Conferencias encontradas",
          value = """
[
  {
    "id": 7,
    "fechaCreacion": "2025-10-17T08:45:00",
    "creadorId": 5,
    "congresoId": 3,
    "nombre": "Inteligencia Artificial en Medicina",
    "resumen": "Aplicaciones de IA en diagnostico medico",
    "descripcion": "Uso de algoritmos de IA para mejorar diagnosticos...",
    "sala": "Auditorio de Medicina",
    "fechaInicio": "2025-10-19T10:00:00",
    "fechaFin": "2025-10-19T12:00:00",
    "publicada": false,
    "cancelada": false,
    "cupo": 120,
    "inscritos": 18,
    "asistencias": 0,
    "staffCantidad": 4,
    "staffRequerimientos": "Equipo medico, proyector, audio",
    "conferencistaNombre": "Dra. Sofia Hernandez",
    "conferencistaEmail": "sofia.hernandez@hospital.edu",
    "conferencistaTelPref": "52",
    "conferencistaTelSuf": "6642223333",
    "conferencistaSemblanza": "Medica especialista en IA aplicada a la salud"
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
  "instance": "/api/v1/eventos/conferencia/buscar",
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
  "instance": "/api/v1/eventos/conferencia/buscar",
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
  "instance": "/api/v1/eventos/conferencia/buscar",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONFERENCIAS_NO_PUBLICADAS)

  public ResponseEntity<List<Conferencia>> buscar (

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
      confSvc.buscar(txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta la foto de la CONFERENCIA especificada en el slot de multimedia
   * especificado.
   *
   * @param id
   * ID de la CONFERENCIA.
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
   * Si la CONFERENCIA no existe o si no tiene una foto.
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si el slot especificado no existe.
   */
  @GetMapping("conferencia/{id}/media/{slot}")

  @Operation(
    summary = "Consultar multimedia de conferencia",
    description = "Obtiene el archivo multimedia de una conferencia.",
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
  "instance": "/api/v1/eventos/conferencia/conferencia/1/media/invalido",
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
   "/api/v1/eventos/conferencia/conferencia/1/media/conferencistaFoto",
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
            name = "Conferencia no encontrada",
            description = "La conferencia no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro la conferencia con ID 1",
  "instance":
   "/api/v1/eventos/conferencia/conferencia/1/media/conferencistaFoto",
  "timestamp": "2025-10-13T06:37:17"
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
  "detail": "La conferencia no tiene archivo en el slot especificado",
  "instance":
   "/api/v1/eventos/conferencia/conferencia/1/media/conferencistaFoto",
  "timestamp": "2025-10-13T06:38:18"
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
   "/api/v1/eventos/conferencia/conferencia/1/media/conferencistaFoto",
  "timestamp": "2025-10-13T02:51:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONFERENCIAS_NO_PUBLICADAS)

  public ResponseEntity<byte[]> qIdMedia (

    @PathVariable("id")
    Long id,

    @PathVariable("slot")
    String slot
  ) {
    return confSvc.afirmarMedia(id, slot);
  }
}
