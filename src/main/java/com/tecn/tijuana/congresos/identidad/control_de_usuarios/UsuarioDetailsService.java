package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final UsuarioRepository usrRep;


  /**
   * CONSTRUCTOR principal de esta clase/controller, usado principalmente por
   * Spring para el funcionamiento de la app.
   *
   * @param usrRep
   * Objeto de la capa de servicio de la entidad de USUARIO.
   */
  @Autowired
  public UsuarioDetailsService (UsuarioRepository usrRep) {
    this.usrRep = usrRep;
  }

  /**
   * @param username
   * El nombre de usuario (email) de quien busca autorizacion.
   *
   * @return
   * Objeto UserDetails que el AuthenticationProvider usara para validar al
   * Usuario.
   *
   * @throws UsernameNotFoundException Cuando el Usuario no es encontrado.
   */
  @Override
  public UserDetails loadUserByUsername (String username)
    throws UsernameNotFoundException {

//    return usrRep.qEmail(username)
//      .map(usr -> {
//          var bldr = User.builder()
//            .username(usr.getEmail())
//            .password(usr.getPassword())
//
//            .roles(usr.getRol().toString());
//
//          return autoridadesStaff(usr, bldr)
//
//            .accountExpired(usr.isExpirado())
//            .accountLocked(usr.isBloqueado())
//            .credentialsExpired(usr.isCredencialesExpiradas())
//            .disabled(usr.isDeshabilitado())
//
//            .build();
//        }
//      )
//
//      .orElseThrow(() ->
//        new UsernameNotFoundException("Usuario no encontrado"));

    return usrRep.qEmail(username)
      .orElseThrow(() ->
        new UsernameNotFoundException("Usuario no encontrado"));
  }



  /**
   * Agrega las autoridades correspondientes al UserBuilder, cuando describe a
   * un USUARIO del tipo STAFF, segun las tenga asignadas.
   *
   * @param usr
   * El objeto de USUARIO.
   *
   * @param bldr
   * El objeto UserBuilder para los UserDetails.
   *
   * @return
   * El UserBuilder para que se le continue agregando los demas campos.
   *
   * @throws UsernameNotFoundException Cuando el Usuario no es encontrado.
   */
  private User.UserBuilder autoridadesStaff (
    Usuario usr, User.UserBuilder bldr
  ) {
    if (usr.getRol() == Rol.STAFF) {
      if (usr.isStaffAutorizado()) {
        bldr.authorities("STAFF_AUTORIZADO");
      }
      if (usr.isStaffCustodio()) {
        bldr.authorities("STAFF_CUSTODIO");
      }
      if (usr.isStaffAlumnos()) {
        bldr.authorities("STAFF_ALUMNOS");
      }
      if (usr.isStaffInscripciones()) {
        bldr.authorities("STAFF_INSCRIPCIONES");
      }
    }
    return bldr;
  }
}
