package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

import com.tecn.tijuana.congresos.identidad.control_de_usuarios.dto.RegistroUsuarioDto;
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
  "fechaNacimiento": "2001-01-01"
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
  "fechaNacimiento": "2001-01-01"
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
  "fechaNacimiento": "2001-01-01",
  
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
  "fechaNacimiento": "2001-01-01",
  
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
    "fechaCreacion": "2025-09-24",
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
    "fechaNacimiento": "2000-01-11",
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
        mediaType = "application/json",
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
    "registroUsuarioDto.email": "Email invalido",
    "registroUsuarioDto.password": "La clave debe tener entre 8 y 100 caracteres"
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
        mediaType = "application/json",
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
  "timestamp": "2025-09-24T02:59:21.450858486Z"
}
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "500",
      description = "Error interno",
      content = @Content(
        mediaType = "application/json",
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
  "timestamp": "2025-09-24T03:01:37.181558255Z",
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
    description = "Edita el contenido multimedoa del registro en el slot especificado.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Contenido del archivo multimedia",
      required = true,
      content = @Content(
        mediaType = "multipart/form-data",
        examples = {
          @ExampleObject(
            name = "Nueva imagen",
            description = "Ejemplo de peticion para poner una nueva imagen en el slot",
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
    summary = "Editar slot de multimedia",
    description = "Edita el contenido multimedoa del registro en el slot especificado.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Contenido del archivo multimedia",
      required = true,
      content = @Content(
        mediaType = "multipart/form-data",
        examples = {
          @ExampleObject(
            name = "Nueva imagen",
            description = "Ejemplo de peticion para poner una nueva imagen en el slot",
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

  public void eliminar (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    var deleted = usrSvc.eliminar(actor, id);
//    if (Objects.isNull(deleted)) {
//      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    } else {
//      return new ResponseEntity<>(deleted, HttpStatus.OK);
//    }
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

  public ResponseEntity<Usuario> bloquear (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(usrSvc.bloquear(actor, id), HttpStatus.OK);
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

  public ResponseEntity<Usuario> qMio (

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(usrSvc.afirmar(actor.getId()), HttpStatus.OK);
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

  public ResponseEntity<byte[]> qMioMedia (

    @PathVariable("slot")
    String slot,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return usrSvc.afirmarMedia(actor.getId(), slot);
  }
}
