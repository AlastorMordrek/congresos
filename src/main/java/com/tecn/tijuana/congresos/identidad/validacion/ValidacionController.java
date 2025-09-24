package com.tecn.tijuana.congresos.identidad.validacion;

import com.tecn.tijuana.congresos.identidad.control_de_usuarios.ControlDeUsuariosService;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.identidad.validacion.dto.RegistroAlumnoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/identidad/validacion")
@Validated
public class ValidacionController {

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
  public ValidacionController (
    ControlDeUsuariosService usrSvc
  ) {
    this.usrSvc = usrSvc;
  }



//  /**
//   * Permite a un Alumno registrarse en el sistema.
//   *
//   * @param usuario
//   * Objeto con los datos del usuario.
//   *
//   * @return
//   * El registro creado.
//   *
//   * @throws ResponseStatusException
//   * <p>
//   * {@code HTTP-BAD_REQUEST}
//   * Si se proveen parametros incorrectos.
//   * <p>
//   * {@code HTTP-CONFLICT}
//   * Si hay alguna situacion que impida el registro.
//   * Ej: el email ya esta tomado.
//   *
//   * @apiNote
//   * Si el Usuario es registrado exitosamente retorna {@code HTTP-201}
//   */
//  @PostMapping("/registrarse")
//
//  public ResponseEntity<Usuario> registrarse (
//
//    @RequestBody
//    Usuario usuario
//  )
//    throws ResponseStatusException {
//
//    return new ResponseEntity<>(
//      usrSvc.registrarseAlumno(usuario),
//      HttpStatus.CREATED);
//  }



  /**
   * Permite a un ALUMNO registrarse en el sistema.
   *
   * @return
   * El registro creado.
   *
   * @throws ResponseStatusException
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si se proveen parametros incorrectos.
   * <p>
   * {@code HTTP-CONFLICT}
   * Si hay alguna situacion que impida el registro.
   * Ej: el email ya esta tomado.
   *
   * @apiNote
   * Si el Usuario es registrado exitosamente retorna {@code HTTP-201}
   */
  @PostMapping(path = "registrarse")

  @Operation(
    summary = "Registrar un nuevo alumno",
    description = "Permite a un alumno registrarse en el sistema",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Datos del alumno a registrar",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = RegistroAlumnoDto.class),
        examples = @ExampleObject(
          name = "Registrar",
          description = "Ejemplo de peticion para registrar Alumno",
          value = """
{
  "email": "alumno.nuevo@tijuana.tecnm.mx",
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
  "emailInstitucional": "alumno.nuevo@tijuana.tecnm.mx"
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
          description = "Parametros incorrectos o validacion fallida",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for object='registroAlumnoDto'. Error count: 2",
  "instance": "/api/v1/identidad/validacion/registrarse",
  "timestamp": "2025-09-24T01:14:16.801076563Z",
  "campos": {
    "registroAlumnoDto.email": "Email invalido",
    "registroAlumnoDto.password": "La clave debe tener entre 8 y 100 caracteres"
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
  "instance": "/api/v1/identidad/validacion/registrarse",
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
  "instance": "/api/v1/identidad/validacion/registrarse",
  "timestamp": "2025-09-24T03:01:37.181558255Z",
  "exceptionType": "DataIntegrityViolationException"
}

"""
        )
      )
    )
  })

  public ResponseEntity<Usuario> registrarse (

    @RequestBody
    RegistroAlumnoDto dto
  )
    throws ResponseStatusException {

    return new ResponseEntity<>(
      usrSvc.registrarseAlumno(dto),
      HttpStatus.CREATED);
  }
}



