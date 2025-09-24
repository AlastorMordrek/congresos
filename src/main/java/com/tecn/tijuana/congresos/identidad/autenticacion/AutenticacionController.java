package com.tecn.tijuana.congresos.identidad.autenticacion;

import com.tecn.tijuana.congresos.identidad.autenticacion.dto.LoginDto;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.ControlDeUsuariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/identidad/autenticacion")
@Validated
public class AutenticacionController {

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
  public AutenticacionController (
    ControlDeUsuariosService usrSvc
  ) {
    this.usrSvc = usrSvc;
  }



  /**
   * Permite a los USUARIOS autenticarse en el sistema con sus credenciales de
   * acceso.
   *
   * @param dto
   * Objeto con los datos del USUARIO.
   *
   * @throws ResponseStatusException
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si se proveen parametros incorrectos.
   * <p>
   * {@code HTTP-UNAUTHORIZED}
   * Si el USUARIO esta bloqueado o hay alguna otra situacion que le impida
   * iniciar sesion.
   */
  @PostMapping("iniciar-sesion")

  @Operation(
    summary = "Iniciar sesion",
    description = "Permite a un usuario iniciar sesion en el sistema",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Credenciales del usuario",
      required = true,
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = LoginDto.class),
        examples = @ExampleObject(
          name = "Login",
          description = "Ejemplo de peticion para iniciar sesion",
          value = """
{
  "email": "alumno.nuevo@tijuana.tecnm.mx",
  "password": "claveSegura123"
}
"""
        )
      )
    )
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "201",
      description = "Inicio exitoso",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Usuario.class),
        examples = @ExampleObject(
          name = "Exito",
          description = "Usuario autenticado exitosamente",
          value = """
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30
"""
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Error de autenticacion",
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          name = "Error",
          description = "Parametros incorrectos o autenticacion fallida",
          value = """
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed for object='LoginDto'. Error count: 2",
  "instance": "/api/v1/identidad/autenticacion/iniciar-sesion",
  "timestamp": "2025-09-24T01:14:16.801076563Z",
  "campos": {
    "LoginDto.email": "Email invalido",
    "LoginDto.password": "La clave debe tener entre 8 y 100 caracteres"
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
        mediaType = "application/json",
        examples = @ExampleObject(
          name = "Error",
          description = "El usuario esta bloqueado, deshabilitado o por alguna otra razon no tiene permitido iniciar sesion.",
          value = """
{
  "type": "about:blank",
  "title": "Usuario bloqueado",
  "status": 401,
  "detail": "Usuario bloqueado",
  "instance": "/api/v1/identidad/autenticacion/iniciar-sesion",
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
  "instance": "/api/v1/identidad/autenticacion/iniciar-sesion",
  "timestamp": "2025-09-24T03:01:37.181558255Z",
  "exceptionType": "DataIntegrityViolationException"
}
"""
        )
      )
    )
  })

  public ResponseEntity<String> login (

    @RequestBody
    LoginDto dto
  )
    throws BadCredentialsException {

    return new ResponseEntity<>(
      usrSvc.verify(dto),
      HttpStatus.OK);
  }

}
