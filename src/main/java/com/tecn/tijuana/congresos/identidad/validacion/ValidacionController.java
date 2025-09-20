package com.tecn.tijuana.congresos.identidad.validacion;

import com.tecn.tijuana.congresos.identidad.control_de_usuarios.ControlDeUsuariosService;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
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



  /**
   * Permite a un Alumno registrarse en el sistema.
   *
   * @param usuario
   * Objeto con los datos del usuario.
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
  @PostMapping("/registrarse")

  public ResponseEntity<Usuario> registrarse (

    @RequestBody
    Usuario usuario
  )
    throws ResponseStatusException {

    return new ResponseEntity<>(
      usrSvc.registrarseAlumno(usuario),
      HttpStatus.CREATED);
  }
}
