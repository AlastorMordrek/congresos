package com.tecn.tijuana.congresos.identidad.autenticacion;

import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.ControlDeUsuariosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/identidad/autenticacion")
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
   * Permite a los usuarios autenticarse en el sistema con sus credenciales de
   * acceso.
   *
   * @param usr
   * Objeto con los datos del usuario.
   */
  @PostMapping("iniciar-sesion")
  public ResponseEntity<String> login (
    @RequestBody
    Usuario usr
  ) throws BadCredentialsException {
    return new ResponseEntity<>(usrSvc.verify(usr), HttpStatus.OK);
  }

}
