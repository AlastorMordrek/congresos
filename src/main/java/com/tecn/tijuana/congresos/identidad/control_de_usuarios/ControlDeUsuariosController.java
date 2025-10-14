package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

import com.tecn.tijuana.congresos.identidad.control_de_usuarios.dto
  .RegistroUsuarioDto;
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
//import java.util.Objects;


@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/identidad/control-de-usuarios")
@Validated
public class ControlDeUsuariosController {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final ControlDeUsuariosService usrSvc;


  /**
   * CONSTRUCTOR principal de esta clase/controller, usado principalmente por
   * Spring para el funcionamiento de la app.
   *
   * @param usrSvc
   * Objeto de la capa de servicio de la entidad de USUARIO.
   */
  public ControlDeUsuariosController (
    ControlDeUsuariosService usrSvc
  ) {
    this.usrSvc = usrSvc;
  }



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite a un miembro del personal REGISTRAR a otro USUARIO.
   *
   * @param dto
   * Objeto con los datos del USUARIO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @PostMapping("registrar")

  @Operation(
    summary = "Registrar un nuevo usuario",
    description = "Permite a un miembro del personal registrar a otro usuario.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Datos del usuario a registrar",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = RegistroUsuarioDto.class),
        examples = {
          @ExampleObject(
            name = "Registrar Administrador",
            description = "Ejemplo de peticion para registrar un Administrador",
            value = """
{
  "rol": "ADMINISTRADOR",
  
  "email": "usuario.nuevo@tijuana.tecnm.mx",
  "password": "claveSegura123",
  
  "telPref": "52",
  "telSuf": "6641112222",
  
  "nombre": "Jose",
  "apellidoPaterno": "Perez",
  "apellidoMaterno": "Perez",
  "fechaNacimiento": "2001-01-01T00:00:00"
}
"""
          ),
          @ExampleObject(
            name = "Registrar Organizador",
            description = "Ejemplo de peticion para registrar un Organizador",
            value = """
{
  "rol": "ORGANIZADOR",
  
  "email": "usuario.nuevo@tijuana.tecnm.mx",
  "password": "claveSegura123",
  
  "telPref": "52",
  "telSuf": "6641112222",
  
  "nombre": "Jose",
  "apellidoPaterno": "Perez",
  "apellidoMaterno": "Perez",
  "fechaNacimiento": "2001-01-01T00:00:00"
}
"""
          ),
          @ExampleObject(
            name = "Registrar Staff",
            description = "Ejemplo de peticion para registrar un Staff",
            value = """
{
  "rol": "STAFF",
  
  "email": "usuario.nuevo@tijuana.tecnm.mx",
  "password": "claveSegura123",
  
  "telPref": "52",
  "telSuf": "6641112222",
  
  "nombre": "Jose",
  "apellidoPaterno": "Perez",
  "apellidoMaterno": "Perez",
  "fechaNacimiento": "2001-01-01T00:00:00",
  
  "staffResponsabilidades": "Este Staff tendra que hacer de todo",
  "staffAutorizado": true,
  "staffCustodio": true,
  "staffAlumnos": true,
  "staffInscripciones": true
}
"""
          ),
          @ExampleObject(
            name = "Registrar Alumno",
            description = "Ejemplo de peticion para registrar un Alumno",
            value = """
{
  "rol": "ALUMNO",
  
  "email": "usuario.nuevo@tijuana.tecnm.mx",
  "password": "claveSegura123",
  
  "telPref": "52",
  "telSuf": "6641112222",
  
  "nombre": "Jose",
  "apellidoPaterno": "Perez",
  "apellidoMaterno": "Perez",
  "fechaNacimiento": "2001-01-01T00:00:00",
  
  "noControl": "12345678",
  "codigoCarrera": "ISC",
  "semestre": 1,
  "grupo": "A",
  "externo": false,
  "curp": "ABCD010101MTSRRNA0",
  "emailInstitucional": "usuario.nuevo@tijuana.tecnm.mx"
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
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Alumno registrado exitosamente",
          value = """
{
    "id": 18,
    "fechaCreacion": "2025-09-24T00:00:00",
    "creadorId": null,
    "rol": "ALUMNO",
    "email": "lalelilolu@pm.me",
    "bloqueado": false,
    "expirado": false,
    "credencialesExpiradas": false,
    "deshabilitado": false,
    "telPref": "52",
    "telSuf": "6641112222",
    "nombre": "Silas",
    "apellidoPaterno": "Mordrek",
    "apellidoMaterno": "Tres",
    "fechaNacimiento": "2000-01-11T00:00:00",
    "noControl": "11211522",
    "codigoCarrera": "ISC",
    "semestre": 1,
    "grupo": "A",
    "externo": false,
    "curp": "ABCD900121HMNSRL01",
    "emailInstitucional": "lalelilolu@pm.me",
    "staffResponsabilidades": null,
    "staffAutorizado": false,
    "staffCustodio": false,
    "staffAlumnos": false,
    "staffInscripciones": false
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
  "detail": "Validation failed for object='registroUsuarioDto'. Error count: 2",
  "instance": "/api/v1/identidad/control-de-usuarios/registrar",
  "timestamp": "2025-09-24T01:14:16.801076563Z",
  "campos": {
    "registroUsuarioDto.emailInstitucional": "Email invalido",
    "registroUsuarioDto.password":
     "La clave debe tener entre 8 y 100 caracteres"
  }
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "409",
      description = "Conflicto",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El email ya esta registrado",
          value = """
{
  "type": "about:blank",
  "title": "Email no disponible",
  "status": 409,
  "detail": "Email no disponible",
  "instance": "/api/v1/identidad/control-de-usuarios/registrar",
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
  "instance": "/api/v1/identidad/control-de-usuarios/registrar",
  "timestamp": "2025-09-24T03:01:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.REGISTRAR_USUARIOS)

  public ResponseEntity<Usuario> registrar (

    @RequestBody
    RegistroUsuarioDto dto,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      usrSvc.registrar(actor, dto),
      HttpStatus.CREATED);
  }



  /**
   * Permite a un USUARIO EDITAR a otro, siempre y cuando tenga los permisos
   * necesarios.
   *
   * @param id
   * ID del USUARIO a EDITAR.
   *
   * @param usuario
   * Objeto con los datos nuevos del Usuario a EDITAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @PatchMapping("editar/{id}")

  @Operation(
    summary = "Editar usuario",
    description = "Permite a un personal autorizado editar a otro usuario",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Datos del usuario editado",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Editar",
          description = "Ejemplo de peticion para editar usuario",
          value = """
{
  "id": 18,
  "fechaCreacion": "2025-09-24T00:00:00",
  "creadorId": null,
  "rol": "ALUMNO",
  "email": "lalelilolu@pm.me",
  "bloqueado": false,
  "expirado": false,
  "credencialesExpiradas": false,
  "deshabilitado": false,
  "telPref": "52",
  "telSuf": "6641112222",
  "nombre": "Silas",
  "apellidoPaterno": "Mordrek",
  "apellidoMaterno": "Tres",
  "fechaNacimiento": "2000-01-11T00:00:00",
  "noControl": "11211522",
  "codigoCarrera": "ISC",
  "semestre": 1,
  "grupo": "A",
  "externo": false,
  "curp": "ABCD900121HMNSRL01",
  "emailInstitucional": "lalelilolu@pm.me",
  "staffResponsabilidades": null,
  "staffAutorizado": false,
  "staffCustodio": false,
  "staffAlumnos": false,
  "staffInscripciones": false
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
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Usuario editado exitosamente",
          value = """
{
    "id": 18,
    "fechaCreacion": "2025-09-24T00:00:00",
    "creadorId": null,
    "rol": "ALUMNO",
    "email": "lalelilolu@pm.me",
    "bloqueado": false,
    "expirado": false,
    "credencialesExpiradas": false,
    "deshabilitado": false,
    "telPref": "52",
    "telSuf": "6641112222",
    "nombre": "Silas",
    "apellidoPaterno": "Mordrek",
    "apellidoMaterno": "Tres",
    "fechaNacimiento": "2000-01-11T00:00:00",
    "noControl": "11211522",
    "codigoCarrera": "ISC",
    "semestre": 1,
    "grupo": "A",
    "externo": false,
    "curp": "ABCD900121HMNSRL01",
    "emailInstitucional": "lalelilolu@pm.me",
    "staffResponsabilidades": null,
    "staffAutorizado": false,
    "staffCustodio": false,
    "staffAlumnos": false,
    "staffInscripciones": false
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
          description = "Parametros incorrectos o validacion fallida",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for object='Usuario'. Error count: 1",
  "instance": "/api/v1/identidad/control-de-usuarios/editar/1",
  "timestamp": "2025-09-24T01:14:16.801076563Z",
  "campos": {
    "Usuario.emailInstitucional": "Email invalido"
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
          description = "Ejemplo de error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/identidad/control-de-usuarios/editar/1",
  "timestamp": "2025-09-24T03:01:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_USUARIOS)

  public ResponseEntity<Usuario> editar (

    @PathVariable
    Long id,

    @RequestBody
    Usuario usuario,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      usrSvc.editar(actor, id, usuario),
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
    summary = "Editar slot de multimedia",
    description = "Edita el contenido multimedia del registro en el slot" +
      " especificado.",
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
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Usuario editado exitosamente",
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
  "instance": "/api/v1/identidad/control-de-usuarios/editar/1/media/foto",
  "timestamp": "2025-09-24T01:14:16.801076563Z"
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
  "instance": "/api/v1/identidad/control-de-usuarios/editar/1/media/foto",
  "timestamp": "2025-09-24T03:01:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.EDITAR_USUARIOS)

  public void editarMedia (

    @PathVariable
    Long id,

    @PathVariable
    String slot,

    @RequestPart(required = false)
    MultipartFile img,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    // Actualizar el slot.
    usrSvc.editarMedia(actor, id, slot, img);
  }



  /**
   * Permite a un USUARIO EDITAR sus propios datos.
   *
   * @param usuario
   * Objeto con los datos nuevos del USUARIO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @PatchMapping("editarme")

  @Operation(
    summary = "Editar usuario propio",
    description = "Permite a un usuario editar sus datos",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Datos nuevos",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Editar",
          description = "Ejemplo de peticion para editar sus propios datos",
          value = """
{
  "id": 18,
  "fechaCreacion": "2025-09-24T00:00:00",
  "creadorId": null,
  "rol": "ALUMNO",
  "email": "lalelilolu@pm.me",
  "bloqueado": false,
  "expirado": false,
  "credencialesExpiradas": false,
  "deshabilitado": false,
  "telPref": "52",
  "telSuf": "6641112222",
  "nombre": "Silas",
  "apellidoPaterno": "Mordrek",
  "apellidoMaterno": "Tres",
  "fechaNacimiento": "2000-01-11T00:00:00",
  "noControl": "11211522",
  "codigoCarrera": "ISC",
  "semestre": 1,
  "grupo": "A",
  "externo": false,
  "curp": "ABCD900121HMNSRL01",
  "emailInstitucional": "lalelilolu@pm.me",
  "staffResponsabilidades": null,
  "staffAutorizado": false,
  "staffCustodio": false,
  "staffAlumnos": false,
  "staffInscripciones": false
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
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Operacion exitosa",
          value = """
{
    "id": 18,
    "fechaCreacion": "2025-09-24T00:00:00",
    "creadorId": null,
    "rol": "ALUMNO",
    "email": "lalelilolu@pm.me",
    "bloqueado": false,
    "expirado": false,
    "credencialesExpiradas": false,
    "deshabilitado": false,
    "telPref": "52",
    "telSuf": "6641112222",
    "nombre": "Silas",
    "apellidoPaterno": "Mordrek",
    "apellidoMaterno": "Tres",
    "fechaNacimiento": "2000-01-11T00:00:00",
    "noControl": "11211522",
    "codigoCarrera": "ISC",
    "semestre": 1,
    "grupo": "A",
    "externo": false,
    "curp": "ABCD900121HMNSRL01",
    "emailInstitucional": "lalelilolu@pm.me",
    "staffResponsabilidades": null,
    "staffAutorizado": false,
    "staffCustodio": false,
    "staffAlumnos": false,
    "staffInscripciones": false
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
          description = "Parametros incorrectos o validacion fallida",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for object='Usuario'. Error count: 1",
  "instance": "/api/v1/identidad/control-de-usuarios/editarme",
  "timestamp": "2025-09-24T01:14:16.801076563Z",
  "campos": {
    "Usuario.emailInstitucional": "Email invalido"
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
          description = "Ejemplo de error no controlado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/identidad/control-de-usuarios/editarme",
  "timestamp": "2025-09-24T03:01:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<Usuario> editarme (

    @RequestBody
    Usuario usuario,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      usrSvc.editarme(actor, usuario),
      HttpStatus.OK);
  }



  /**
   * Actualiza el registro en el slot de multimedia especificado.
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
  @PatchMapping("editarme/media/{slot}")

  @Operation(
    summary = "Editar slot de multimedia propio",
    description = "Permite a un usuario editar sus propiedades tipo" +
      " multimedia (ej: foto de perfil).",
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
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Usuario editado exitosamente",
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
  "instance": "/api/v1/identidad/control-de-usuarios/editarme/media/foto",
  "timestamp": "2025-09-24T01:14:16.801076563Z"
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
  "instance": "/api/v1/identidad/control-de-usuarios/editarme/media/foto",
  "timestamp": "2025-09-24T03:01:37",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public void editarmeMedia (

    @PathVariable
    String slot,

    @RequestPart(required = false)
    MultipartFile img,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    // Actualizar el slot.
    usrSvc.editarmeMedia(actor, slot, img);
  }



  /**
   * Permite a un USUARIO ELIMINAR a otro siempre y cuando tenga los permisos
   * suficientes.
   *
   * @param id
   * ID del USUARIO a ELIMINAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   */
  @DeleteMapping("eliminar/{id}")

  @Operation(
    summary = "Eliminar usuario",
    description = "Permite a un usuario con los permisos suficientes eliminar" +
      " a otro usuario del sistema."
  )
  
  @ApiResponses({
    @ApiResponse(
      responseCode = "204",
      description = "Usuario eliminado exitosamente",
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
      description = "Acceso denegado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario autenticado no tiene permisos para" +
            " eliminar a otros usuarios",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Acceso denegado: permisos insuficientes",
  "instance": "/api/v1/identidad/control-de-usuarios/eliminar/7",
  "timestamp": "2025-09-24T03:21:45.501858123Z"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Usuario no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario con el ID especificado no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el usuario con ID 7",
  "instance": "/api/v1/identidad/control-de-usuarios/eliminar/7",
  "timestamp": "2025-09-24T03:22:17.701247985Z"
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
  "instance": "/api/v1/identidad/control-de-usuarios/eliminar/7",
  "timestamp": "2025-09-24T03:23:09.145127600Z",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })
  
  @PreAuthorize(ExpresionSeguridad.ELIMINAR_USUARIOS)

  public void eliminar (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    var deleted = usrSvc.eliminar(actor, id);
  }



  /**
   * Permite a un USUARIO BLOQUEAR a otro siempre y cuando tenga los permisos
   * suficientes.
   *
   * @param id
   * ID del USUARIO a BLOQUEAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @PatchMapping("bloquear/{id}")

  @Operation(
    summary = "Bloquear usuario",
    description = "Permite a un usuario con permisos suficientes bloquear a" +
      " otro usuario, impidiendo su acceso al sistema."
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Usuario bloqueado exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Ejemplo de respuesta al bloquear un usuario",
          value = """
{
  "id": 18,
  "fechaCreacion": "2025-09-24T00:00:00",
  "creadorId": null,
  "rol": "ALUMNO",
  "email": "lalelilolu@pm.me",
  "bloqueado": true,
  "expirado": false,
  "credencialesExpiradas": false,
  "deshabilitado": false,
  "telPref": "52",
  "telSuf": "6641112222",
  "nombre": "Silas",
  "apellidoPaterno": "Mordrek",
  "apellidoMaterno": "Tres",
  "fechaNacimiento": "2000-01-11T00:00:00",
  "noControl": "11211522",
  "codigoCarrera": "ISC",
  "semestre": 1,
  "grupo": "A",
  "externo": false,
  "curp": "ABCD900121HMNSRL01",
  "emailInstitucional": "lalelilolu@pm.me",
  "staffResponsabilidades": null,
  "staffAutorizado": false,
  "staffCustodio": false,
  "staffAlumnos": false,
  "staffInscripciones": false
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Acceso denegado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario autenticado no tiene permisos para" +
            " bloquear a otros usuarios",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Acceso denegado: permisos insuficientes",
  "instance": "/api/v1/identidad/control-de-usuarios/bloquear/18",
  "timestamp": "2025-09-24T03:45:12.501858123Z"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Usuario no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario con el ID especificado no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el usuario con ID 18",
  "instance": "/api/v1/identidad/control-de-usuarios/bloquear/18",
  "timestamp": "2025-09-24T03:46:17.701247985Z"
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
  "instance": "/api/v1/identidad/control-de-usuarios/bloquear/18",
  "timestamp": "2025-09-24T03:47:09.145127600Z",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })


  @PreAuthorize(ExpresionSeguridad.EDITAR_USUARIOS)

  public ResponseEntity<Usuario> bloquear (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      usrSvc.bloquear(actor, id),
      HttpStatus.OK);
  }



  /**
   * Permite a un USUARIO DES-BLOQUEAR a otro siempre y cuando tenga los permisos
   * suficientes.
   *
   * @param id
   * ID del USUARIO a DES-BLOQUEAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @PatchMapping("desbloquear/{id}")

  @Operation(
    summary = "Desbloquear usuario",
    description = "Permite a un usuario con permisos suficientes desbloquear" +
      " a otro usuario previamente bloqueado, restaurando su acceso al sistema."
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Usuario desbloqueado exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Ejemplo de respuesta al desbloquear un usuario",
          value = """
{
  "id": 18,
  "fechaCreacion": "2025-09-24T00:00:00",
  "creadorId": null,
  "rol": "ALUMNO",
  "email": "lalelilolu@pm.me",
  "bloqueado": false,
  "expirado": false,
  "credencialesExpiradas": false,
  "deshabilitado": false,
  "telPref": "52",
  "telSuf": "6641112222",
  "nombre": "Silas",
  "apellidoPaterno": "Mordrek",
  "apellidoMaterno": "Tres",
  "fechaNacimiento": "2000-01-11T00:00:00",
  "noControl": "11211522",
  "codigoCarrera": "ISC",
  "semestre": 1,
  "grupo": "A",
  "externo": false,
  "curp": "ABCD900121HMNSRL01",
  "emailInstitucional": "lalelilolu@pm.me",
  "staffResponsabilidades": null,
  "staffAutorizado": false,
  "staffCustodio": false,
  "staffAlumnos": false,
  "staffInscripciones": false
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Acceso denegado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario autenticado no tiene permisos para" +
            " desbloquear a otros usuarios",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Acceso denegado: permisos insuficientes",
  "instance": "/api/v1/identidad/control-de-usuarios/desbloquear/18",
  "timestamp": "2025-09-24T03:52:12.501858123Z"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Usuario no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario con el ID especificado no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el usuario con ID 18",
  "instance": "/api/v1/identidad/control-de-usuarios/desbloquear/18",
  "timestamp": "2025-09-24T03:53:17.701247985Z"
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
  "instance": "/api/v1/identidad/control-de-usuarios/desbloquear/18",
  "timestamp": "2025-09-24T03:54:09.145127600Z",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })


  @PreAuthorize(ExpresionSeguridad.EDITAR_USUARIOS)

  public ResponseEntity<Usuario> desbloquear (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(usrSvc.desbloquear(actor, id), HttpStatus.OK);
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Consulta los USUARIOS en general.
   *
   * @param page {@code [0]}
   * Numero de pagina.
   *
   * @param pageSize {@code [10]}
   * Tamano de pagina.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @GetMapping("listar")

  @Operation(
    summary = "Listar usuarios",
    description = "Consulta paginada de los usuarios registrados en el" +
      " sistema. Permite especificar numero de pagina y tamaño de pagina. " +
      "Solo accesible para usuarios con permisos de consulta de usuarios."
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Ejemplo de respuesta con una lista de usuarios" +
            " paginada",
          value = """
[
  {
    "id": 18,
    "fechaCreacion": "2025-09-24T00:00:00",
    "creadorId": null,
    "rol": "ALUMNO",
    "email": "lalelilolu@pm.me",
    "bloqueado": false,
    "expirado": false,
    "credencialesExpiradas": false,
    "deshabilitado": false,
    "telPref": "52",
    "telSuf": "6641112222",
    "nombre": "Silas",
    "apellidoPaterno": "Mordrek",
    "apellidoMaterno": "Tres",
    "fechaNacimiento": "2000-01-11T00:00:00",
    "noControl": "11211522",
    "codigoCarrera": "ISC",
    "semestre": 1,
    "grupo": "A",
    "externo": false,
    "curp": "ABCD900121HMNSRL01",
    "emailInstitucional": "lalelilolu@pm.me",
    "staffResponsabilidades": null,
    "staffAutorizado": false,
    "staffCustodio": false,
    "staffAlumnos": false,
    "staffInscripciones": false
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
          description = "Ejemplo de error por parametros fuera de rango",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for query parameters 'page' or 'pageSize'.",
  "instance": "/api/v1/identidad/control-de-usuarios/listar",
  "timestamp": "2025-09-24T04:12:43.201958613Z",
  "campos": {
    "pageSize": "must be less than or equal to 100"
  }
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Acceso denegado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario autenticado no tiene permisos para listar" +
            " usuarios",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Acceso denegado: permisos insuficientes",
  "instance": "/api/v1/identidad/control-de-usuarios/listar",
  "timestamp": "2025-09-24T04:13:09.421247985Z"
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
          description = "Ejemplo de error no controlado durante la consulta",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/identidad/control-de-usuarios/listar",
  "timestamp": "2025-09-24T04:14:09.145127600Z",
  "exceptionType": "DataAccessException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_USUARIOS)

  public ResponseEntity<List<Usuario>> listar (

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      usrSvc.q(page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta los USUARIOS en general, opcionalmente usando una busqueda de
   * texto.
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
   * Respuesta HTTP acorde.
   */
  @GetMapping("buscar")

  @Operation(
    summary = "Buscar usuarios",
    description = "Permite consultar usuarios de forma paginada aplicando un" +
      " filtro de texto opcional. " +
      "Si no se especifica texto de busqueda, se devuelven los usuarios de" +
      " la pagina indicada. " +
      "Requiere permisos de consulta de usuarios."
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Ejemplo de respuesta al buscar usuarios con el" +
            " texto 'Silas'",
          value = """
[
  {
    "id": 18,
    "fechaCreacion": "2025-09-24T00:00:00",
    "creadorId": null,
    "rol": "ALUMNO",
    "email": "lalelilolu@pm.me",
    "bloqueado": false,
    "expirado": false,
    "credencialesExpiradas": false,
    "deshabilitado": false,
    "telPref": "52",
    "telSuf": "6641112222",
    "nombre": "Silas",
    "apellidoPaterno": "Mordrek",
    "apellidoMaterno": "Tres",
    "fechaNacimiento": "2000-01-11T00:00:00",
    "noControl": "11211522",
    "codigoCarrera": "ISC",
    "semestre": 1,
    "grupo": "A",
    "externo": false,
    "curp": "ABCD900121HMNSRL01",
    "emailInstitucional": "lalelilolu@pm.me",
    "staffResponsabilidades": null,
    "staffAutorizado": false,
    "staffCustodio": false,
    "staffAlumnos": false,
    "staffInscripciones": false
  }
]
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Parametros de busqueda o paginacion invalidos",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "Ejemplo de error por texto de busqueda demasiado" +
            " largo o paginacion invalida",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail":
   "Validation failed for query parameters 'txt', 'page', or 'pageSize'.",
  "instance": "/api/v1/identidad/control-de-usuarios/buscar",
  "timestamp": "2025-09-24T04:25:13.501258613Z",
  "campos": {
    "txt": "size must be between 1 and 30"
  }
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Acceso denegado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario autenticado no tiene permisos para buscar" +
            " usuarios",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Acceso denegado: permisos insuficientes",
  "instance": "/api/v1/identidad/control-de-usuarios/buscar?txt=Silas",
  "timestamp": "2025-09-24T04:26:09.421247985Z"
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
          description = "Ejemplo de error no controlado durante la busqueda",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/identidad/control-de-usuarios/buscar",
  "timestamp": "2025-09-24T04:27:09.145127600Z",
  "exceptionType": "DataAccessException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_USUARIOS)

  public ResponseEntity<List<Usuario>> buscar (

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
      txt.isBlank()
        ? usrSvc.q(page, pageSize)
        : usrSvc.buscar(txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta el USUARIO con el ID especifico.
   *
   * @param id
   * ID del USUARIO.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @GetMapping("usuario/{id}")

  @Operation(
    summary = "Consultar usuario por ID",
    description = "Permite obtener los datos completos de un usuario" +
      " especificado por su identificador unico (ID). " +
      "Requiere permisos de consulta de usuarios."
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Ejemplo de respuesta al consultar un usuario por ID",
          value = """
{
  "id": 18,
  "fechaCreacion": "2025-09-24T00:00:00",
  "creadorId": null,
  "rol": "ALUMNO",
  "email": "lalelilolu@pm.me",
  "bloqueado": false,
  "expirado": false,
  "credencialesExpiradas": false,
  "deshabilitado": false,
  "telPref": "52",
  "telSuf": "6641112222",
  "nombre": "Silas",
  "apellidoPaterno": "Mordrek",
  "apellidoMaterno": "Tres",
  "fechaNacimiento": "2000-01-11T00:00:00",
  "noControl": "11211522",
  "codigoCarrera": "ISC",
  "semestre": 1,
  "grupo": "A",
  "externo": false,
  "curp": "ABCD900121HMNSRL01",
  "emailInstitucional": "lalelilolu@pm.me",
  "staffResponsabilidades": null,
  "staffAutorizado": false,
  "staffCustodio": false,
  "staffAlumnos": false,
  "staffInscripciones": false
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Acceso denegado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario autenticado no tiene permisos para" +
            " consultar usuarios",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Acceso denegado: permisos insuficientes",
  "instance": "/api/v1/identidad/control-de-usuarios/usuario/18",
  "timestamp": "2025-09-24T04:35:09.421247985Z"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Usuario no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario con el ID especificado no existe",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el usuario con ID 18",
  "instance": "/api/v1/identidad/control-de-usuarios/usuario/18",
  "timestamp": "2025-09-24T04:36:17.701247985Z"
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
          description = "Ejemplo de error no controlado durante la consulta" +
            " por ID",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/identidad/control-de-usuarios/usuario/18",
  "timestamp": "2025-09-24T04:37:09.145127600Z",
  "exceptionType": "DataAccessException"
}
"""
        )
      )
    )
  })


  @PreAuthorize(ExpresionSeguridad.CONSULTAR_USUARIOS)

  public ResponseEntity<Usuario> qId (

    @PathVariable("id")
    Long id
  ) {
    return new ResponseEntity<>(usrSvc.afirmar(id), HttpStatus.OK);
  }



  /**
   * Consulta la foto en slot especificado del USUARIO especificado.
   *
   * @param id
   * ID del USUARIO.
   *
   * @param slot
   * Slot de multimedia.
   *
   * @return
   * La imagen.
   *
   * @throws ResponseStatusException
   * <p>
   * {@code HTTP-NOT_FOUND}
   * Si el registro no existe o si no tiene una foto.
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si el slot especificado no existe.
   */
  @GetMapping("usuario/{id}/media/{slot}")

  @Operation(
    summary = "Consultar multimedia de un usuario",
    description = "Obtiene el archivo multimedia (por ejemplo, foto de" +
      " perfil) correspondiente al slot especificado " +
      "de un usuario identificado por su ID. " +
      "Requiere permisos de consulta de usuarios."
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Archivo multimedia obtenido exitosamente",
      content = @Content(
        mediaType = "image/jpeg",
        schema = @Schema(type = "string", format = "binary"),
        examples = {
          @ExampleObject(
            name = "Imagen JPEG (u otro)",
            description = "Ejemplo de respuesta con una imagen en formato" +
              " JPEG u otro, segun el formato del archivo original que fue" +
              " puesto en ese slot",
            value = "<BYTES_DE_IMAGEN>"
          )
        }
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Slot de multimedia invalido",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El nombre del slot especificado no corresponde a un" +
            " campo de multimedia valido",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "El slot 'foto' no es valido para este usuario",
  "instance":
   "/api/v1/identidad/control-de-usuarios/usuario/18/media/foto",
  "timestamp": "2025-09-24T04:45:13.421247985Z"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Usuario o multimedia no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = {
          @ExampleObject(
            name = "Usuario no encontrado",
            description = "El usuario especificado no existe",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el usuario con ID 18",
  "instance": "/api/v1/identidad/control-de-usuarios/usuario/18/media/foto",
  "timestamp": "2025-09-24T04:46:17.701247985Z"
}
"""
          ),
          @ExampleObject(
            name = "Sin multimedia",
            description = "El usuario existe, pero no tiene imagen registrada" +
              " en el slot solicitado",
            value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "El usuario con ID 18 no tiene archivo en el slot 'foto'",
  "instance": "/api/v1/identidad/control-de-usuarios/usuario/18/media/foto",
  "timestamp": "2025-09-24T04:46:55.308312658Z"
}
"""
          )
        }
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "Acceso denegado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario autenticado no tiene permisos para " +
            "consultar archivos multimedia de otros usuarios",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Acceso denegado: permisos insuficientes",
  "instance": "/api/v1/identidad/control-de-usuarios/usuario/18/media/foto",
  "timestamp": "2025-09-24T04:47:09.421247985Z"
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
          description = "Ejemplo de error no controlado durante la lectura" +
            " del archivo multimedia",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred while retrieving media",
  "instance": "/api/v1/identidad/control-de-usuarios/usuario/18/media/foto",
  "timestamp": "2025-09-24T04:48:09.145127600Z",
  "exceptionType": "IOException"
}
"""
        )
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_USUARIOS)

  public ResponseEntity<byte[]> qIdMedia (

    @PathVariable("id")
    Long id,

    @PathVariable("slot")
    String slot
  )
    throws ResponseStatusException {

    return usrSvc.afirmarMedia(id, slot);
  }



  /**
   * Consulta el USUARIO propio.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @GetMapping("mio")

  @Operation(
    summary = "Consultar usuario propio",
    description = "Obtiene los datos completos del usuario actualmente" +
      " autenticado en el sistema. " +
      "No requiere parametros adicionales, ya que la identidad se obtiene" +
      " del contexto de autenticacion."
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Ejemplo de respuesta al consultar los datos del " +
            "usuario autenticado",
          value = """
{
  "id": 18,
  "fechaCreacion": "2025-09-24T00:00:00",
  "creadorId": null,
  "rol": "ALUMNO",
  "email": "lalelilolu@pm.me",
  "bloqueado": false,
  "expirado": false,
  "credencialesExpiradas": false,
  "deshabilitado": false,
  "telPref": "52",
  "telSuf": "6641112222",
  "nombre": "Silas",
  "apellidoPaterno": "Mordrek",
  "apellidoMaterno": "Tres",
  "fechaNacimiento": "2000-01-11T00:00:00",
  "noControl": "11211522",
  "codigoCarrera": "ISC",
  "semestre": 1,
  "grupo": "A",
  "externo": false,
  "curp": "ABCD900121HMNSRL01",
  "emailInstitucional": "lalelilolu@pm.me",
  "staffResponsabilidades": null,
  "staffAutorizado": false,
  "staffCustodio": false,
  "staffAlumnos": false,
  "staffInscripciones": false
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "No autenticado o sesion invalida",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario no ha iniciado sesion o su token es" +
            " invalido",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "El token de autenticacion no es valido o ha expirado",
  "instance": "/api/v1/identidad/control-de-usuarios/mio",
  "timestamp": "2025-09-24T04:52:09.421247985Z"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Usuario autenticado no encontrado en base de datos",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario autenticado no existe en el sistema" +
            " (registro eliminado o inconsistencia de datos)",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "No se encontro el registro del usuario autenticado con ID 18",
  "instance": "/api/v1/identidad/control-de-usuarios/mio",
  "timestamp": "2025-09-24T04:53:17.701247985Z"
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
          description = "Ejemplo de error no controlado durante la obtencion" +
            " de datos del usuario autenticado",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "instance": "/api/v1/identidad/control-de-usuarios/mio",
  "timestamp": "2025-09-24T04:54:09.145127600Z",
  "exceptionType": "DataAccessException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<Usuario> qMio (

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      usrSvc.afirmar(actor.getId()),
      HttpStatus.OK);
  }



  /**
   * Consulta la foto en slot especificado del USUARIO propio.
   *
   * @param slot
   * Slot de multimedia.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * La imagen.
   *
   * @throws ResponseStatusException
   * <p>
   * {@code HTTP-NOT_FOUND}
   * Si el registro no existe o si no tiene una foto.
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si el slot especificado no existe.
   */
  @GetMapping("mio/media/{slot}")

  @Operation(
    summary = "Consultar multimedia del usuario propio",
    description = "Obtiene el archivo multimedia (por ejemplo, foto de" +
      " perfil) correspondiente al slot especificado del usuario" +
      " actualmente autenticado. " +
      "No requiere parametros adicionales, ya que la identidad se obtiene" +
      " del contexto de autenticacion."
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Archivo multimedia obtenido exitosamente",
      content = @Content(
        mediaType = "image/jpeg",
        schema = @Schema(type = "string", format = "binary"),
        examples = {
          @ExampleObject(
            name = "Imagen JPEG (u otro)",
            description = "Ejemplo de respuesta con una imagen en formato" +
              " JPEG u otro, segun el formato del archivo original que fue" +
              " puesto en ese slot",
            value = "<BYTES_DE_IMAGEN>"
          )
        }
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Slot de multimedia invalido",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El nombre del slot especificado no corresponde a un" +
            " campo de multimedia valido",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "El slot 'foto' no es valido para este usuario",
  "instance": "/api/v1/identidad/control-de-usuarios/mio/media/foto",
  "timestamp": "2025-09-24T04:45:13.421247985Z"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Multimedia no encontrado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Sin multimedia",
          description = "El usuario autenticado no tiene imagen registrada" +
            " en el slot solicitado",
          value = """
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "El usuario autenticado no tiene archivo en el slot 'foto'",
  "instance": "/api/v1/identidad/control-de-usuarios/mio/media/foto",
  "timestamp": "2025-09-24T04:46:55.308312658Z"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "401",
      description = "No autenticado",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class),
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario no ha iniciado sesion o su token es" +
            " invalido",
          value = """
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "El token de autenticacion no es valido o ha expirado",
  "instance": "/api/v1/identidad/control-de-usuarios/mio/media/foto",
  "timestamp": "2025-09-24T04:47:09.421247985Z"
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
          description = "Ejemplo de error no controlado durante la lectura" +
            " del archivo multimedia",
          value = """
{
  "type": "/probs/error-no-controlado",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred while retrieving media",
  "instance": "/api/v1/identidad/control-de-usuarios/mio/media/foto",
  "timestamp": "2025-09-24T04:48:09.145127600Z",
  "exceptionType": "IOException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<byte[]> qMioMedia (

    @PathVariable("slot")
    String slot,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return usrSvc.afirmarMedia(actor.getId(), slot);
  }
}
