package com.tecn.tijuana.congresos.identidad.validacion;

import com.tecn.tijuana.congresos.identidad.control_de_usuarios.ControlDeUsuariosService;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/identidad/validacion")
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
   * @param usr
   * Objeto con los datos del usuario.
   *
   * @param img {@code [null]}
   * Foto de perfil.
   *
   * @apiNote
   * Si el Usuario es registrado exitosamente retorna {@code HTTP-201}
   */
  @PostMapping("/registrarse")
  public ResponseEntity<Usuario> registrarse (
    @RequestPart
    Usuario usr,
    @RequestPart(required = false)
    MultipartFile img
  ) {
    return new ResponseEntity<>(
      usrSvc.registrarseAlumno(usr, img),
      HttpStatus.CREATED);
  }
}
