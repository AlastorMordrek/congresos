package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

import com.tecn.tijuana.congresos.utils.Api;
import com.tecn.tijuana.congresos.security.JwtService;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication
  .UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.tecn.tijuana.congresos.utils.Api.DEFAULT_PAGE;
import static com.tecn.tijuana.congresos.utils.Api.DEFAULT_PAGE_SIZE;


/**
 * Clase principal de la capa de servicio para la entidad.
 */
@Service
public class ControlDeUsuariosService {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final UsuarioRepository usrRep;
  private final UsuarioDetailsService usrDSvc;
  private final PasswordEncoder pwdEnc;
  private final JwtService jwtSvc;


  /**
   * CONSTRUCTOR principal de la clase/servicio, usado principalmente por Spring
   * para el funcionamiento de la app.
   *
   * @param usrRep
   * Repositorio de DB de la entidad de Usuario usado para abstraer consultas y
   * operaciones de la BD.
   *
   * @param usrDSvc
   * Servicio de detalles de usuario general del sistema usado para autenticar
   * Usuarios.
   *
   * @param pwdEnc
   * Codificador de contrasenas usado por el sistema para obfuscar las
   * contrasenas.
   *
   * @param jwtSvc
   * Servicio de JSON Web Tokens usado por el sistema para proveer tokens de
   * acceso a los Usuarios.
   */
//  @Autowired
  public ControlDeUsuariosService (
    UsuarioRepository usrRep,
    UsuarioDetailsService usrDSvc,
    PasswordEncoder pwdEnc,
    JwtService jwtSvc
  ) {
    this.usrRep = usrRep;
    this.usrDSvc = usrDSvc;
    this.pwdEnc = pwdEnc;
    this.jwtSvc = jwtSvc;
  }


  /**
   * Consulta todos los registros de la entidad indiscriminadamente usando los
   * parametros de paginacion por defecto.
   *
   * @return
   * Lista de registros encontrados.
   *
   * @see Api#pagina()
   */
  public List<Usuario> q () {
    return q(DEFAULT_PAGE);
  }

  /**
   * Consulta todos los registros de la entidad indiscriminadamente usando los
   * parametros de paginacion especificados.
   *
   * @param page
   * Numero de pagina.
   *
   * @return
   * Lista de registros encontrados.
   *
   * @see Api#pagina()
   */
  public List<Usuario> q (int page) {
    return q(page, DEFAULT_PAGE_SIZE);
  }

  /**
   * Consulta todos los registros de la entidad indiscriminadamente usando los
   * parametros de paginacion especificados.
   *
   * @param page
   * Numero de pagina.
   *
   * @param pageSize
   * Tamano de pagina.
   *
   * @return
   * Lista de registros encontrados.
   */
  public List<Usuario> q (int page, int pageSize) {
    return usrRep.
      findAll(Api.pagina(page, pageSize))
      .getContent();
  }



  /**
   * Busca registros en la BD cuyos campos tipo String contengan el texto
   * especificado en {@code txt}, utilizando coincidencia parcial
   * case-insensitive.
   * <p>
   * Si {@code txt} es {@code null} o esta vacio, retorna todos los usuarios
   * aplicando solo la paginacion por defecto.
   *
   * @param txt
   * El texto a buscar.
   *
   * @return
   * Lista de registros encontrados.
   *
   * @see ControlDeUsuariosService#buscar(String, int)
   * @see Api#pagina()
   */
  public List<Usuario> buscar (String txt) {
    return buscar(txt, DEFAULT_PAGE);
  }

  /**
   * Busca registros en la BD cuyos campos tipo String contengan el texto
   * especificado en {@code txt}, utilizando coincidencia parcial
   * case-insensitive.
   * <p>
   * Si {@code txt} es {@code null} o esta vacio, retorna todos los usuarios
   * aplicando solo la paginacion especificada.
   * <p>
   * Ejemplo: {@code buscar("juan", 0)} retorna la primera pagina de
   * usuarios que contengan "juan" en alguno de sus campos.
   *
   * @param txt
   * El texto a buscar.
   *
   * @param page
   * Numero de pagina (0-based)
   *
   * @return
   * Lista de registros encontrados.
   *
   * @see ControlDeUsuariosService#buscar(String, int, int)
   */
  public List<Usuario> buscar (String txt, int page) {
    return buscar(txt, page, DEFAULT_PAGE_SIZE);
  }

  /**
   * Busca registros en la BD cuyos campos tipo String contengan el texto
   * especificado en {@code txt}, utilizando coincidencia parcial
   * case-insensitive.
   * <p>
   * Si {@code txt} es {@code null} o esta vacio, retorna todos los usuarios
   * aplicando solo la paginacion especificada.
   * <p>
   * Ejemplo: {@code buscar("texto", 0, 10)} retorna la primera pagina de
   * usuarios que contengan "texto" en alguno de sus campos.
   *
   * @param txt
   * El texto a buscar.
   *
   * @param page
   * Numero de pagina (0-based)
   *
   * @param pageSize
   * Cantidad de resultados por pagina
   *
   * @return
   * Lista de registros encontrados.
   *
   * @see UsuarioRepository#buscar(String, Pageable)
   */
  public List<Usuario> buscar (String txt, int page, int pageSize) {
    Pageable defPage = PageRequest.of(page, pageSize);

    if (Objects.isNull(txt) || txt.isBlank()) {
      return usrRep.findAll(defPage).getContent();
    }

    return usrRep
      .buscar(txt.toLowerCase().trim(), defPage)
      .getContent();
  }



  /**
   * Obtiene el registro con el ID especificado.
   *
   * @param id
   * El ID del registro.
   *
   * @return
   * El registro encontrado o {@code null} si no se encuentra.
   */
  public Usuario qId (Long id) {

    return usrRep.findById(id).orElse(null);
  }



  /**
   * Obtiene el registro con el Email especificado.
   *
   * @param email
   * Email del registro.
   *
   * @return
   * El registro encontrado o {@code null} si no se encuentra.
   */
  public Usuario qEmail (String email) {

    return usrRep.qEmail(email).orElse(null);
  }



  /**
   * Obtiene el registro con el ID especificado o causa un error HTTP-404 si no
   * existe.
   *
   * @param id
   * El ID del registro.
   *
   * @return
   * El registro encontrado.
   */
  public Usuario afirmar (
    Long id
  )
    throws ResponseStatusException {

    return usrRep.findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format("Usuario con ID: %s no encontrado", id)));
  }



  /**
   * Obtiene el registro con el Numero de Control especificado o causa un error
   * HTTP-404 si no existe.
   *
   * @param noControl
   * Identificador del registro.
   *
   * @return
   * El registro encontrado.
   */
  public Usuario afirmarNoControl (
    String noControl
  )
    throws ResponseStatusException {

    return usrRep.qNoControl(noControl)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format(
            "Usuario con Numero de Control: %s no encontrado",
            noControl)));
  }



  /**
   * Permite al personal registrar ADMINISTRADORS en el sistema.
   * <p>
   * Para uso exlusivo de Usuarios ADMINISTRADOR.
   *
   * @param usr
   * Datos del Usuario.
   *
   * @param img
   * Posible imagen para usar como foto del Usuario.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @return
   * El Usuario recien registrado en la BD.
   */
  public Usuario registrar (Usuario actor, Usuario usr, MultipartFile img)
    throws ResponseStatusException {

    // Marcar al Actor como creador del futuro Usuario.
    usr.setCreadorId(actor.getId());

    // Si no tiene los permisos necesarios lanzar error HTTP-401.
    if (!puedeRegistrarAUsuario(actor, usr)) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "No tiene permiso para registrar un usuario de ese tipo.");
    }

    return switch (usr.getRol()) {
      case Rol.ADMINISTRADOR -> registrarAdmin(usr, img);
      case Rol.ORGANIZADOR   -> registrarOrganizador(usr, img);
      case Rol.STAFF         -> registrarStaff(usr, img);
      case Rol.ALUMNO        -> registrarAlumno(usr, img);
    };
  }



  /**
   * Permite al personal registrar ADMINISTRADORS en el sistema.
   * <p>
   * Para uso exlusivo de Usuarios ADMINISTRADOR.
   *
   * @param usr
   * Datos del Usuario.
   *
   * @param img
   * Posible imagen para usar como foto del Usuario.
   *
   * @return
   * El Usuario recien registrado en la BD.
   */
  public Usuario registrarAdmin (Usuario usr, MultipartFile img)
    throws ResponseStatusException {

    return usrRep.saveAndFlush(
      agregarFoto(
        Usuario.nuevoAdmin(
          codificarPassword(
            afirmarEmailNoTomado(usr),
            pwdEnc)),
        img));
  }



  /**
   * Registra un USUARIO tipo ORGANIZADOR.
   *
   * @param usr
   * Datos del Usuario.
   *
   * @param img
   * Posible imagen para usar como foto del Usuario.
   *
   * @return
   * El Usuario recien registrado en la BD.
   */
  public Usuario registrarOrganizador (Usuario usr, MultipartFile img)
    throws ResponseStatusException {

    return usrRep.saveAndFlush(
      agregarFoto(
        Usuario.nuevoOrganizador(
          codificarPassword(
            afirmarEmailNoTomado(usr),
            pwdEnc)),
        img));
  }



  /**
   * Registra un USUARIO tipo STAFF.
   *
   * @param usr
   * Datos del Usuario.
   *
   * @param img
   * Posible imagen para usar como foto del Usuario.
   *
   * @return
   * El Usuario recien registrado en la BD.
   */
  public Usuario registrarStaff (Usuario usr, MultipartFile img)
    throws ResponseStatusException {

    return usrRep.saveAndFlush(
      agregarFoto(
        Usuario.nuevoStaff(
          codificarPassword(
            afirmarEmailNoTomado(usr),
            pwdEnc)),
        img));
  }



  /**
   * Permite al personal registrar Alumnos en el sistema.
   * <p>
   * Para uso exlusivo de Usuarios tipo ADMINISTRADOR, ORGANIZADOR, STAFF.
   *
   * @param usr
   * Datos de Usuario del Alumno.
   *
   * @param img
   * Posible imagen para usar como foto del Alumno.
   *
   * @return
   * El Usuario recien registrado en la BD.
   */
  public Usuario registrarAlumno (Usuario usr, MultipartFile img)
    throws ResponseStatusException {

    return usrRep.saveAndFlush(
      agregarFoto(
        Usuario.nuevoAlumno(
          codificarPassword(
            afirmarEmailNoTomado(usr),
            pwdEnc)),
        img));
  }

  /**
   * Permite a un nuevo Alumno registrarse por su propia cuenta en el sistema.
   *
   * @param usr
   * Datos de Usuario del Alumno.
   *
   * @param img
   * Posible imagen para usar como foto del Alumno.
   *
   * @return
   * El Usuario recien registrado en la BD.
   */
  public Usuario registrarseAlumno (Usuario usr, MultipartFile img)
    throws ResponseStatusException {

    return usrRep.saveAndFlush(
      agregarFoto(
        Usuario.nuevoAlumnoAutoRegistrado(
          codificarPassword(
            afirmarEmailNoTomado(usr),
            pwdEnc)),
        img));
  }



  /**
   * Actualiza un Usuario en la BD con los nuevos datos provistos, incluyendo
   * una posible nueva foto opcional.
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param id
   * ID del Usuario a editar.
   *
   * @param usr
   * El Usuario a editar.
   *
   * @param img {@code [null]}
   * Posible imagen a usar como foto de perfil.
   *
   * @return
   * El Usuario editado.
   */
  public Usuario editar (
    Usuario actor, Long id, Usuario usr, MultipartFile img
  ) throws ResponseStatusException {

    // Si no tiene los permisos necesarios lanzar error HTTP-401.
    if (!puedeEditarAUsuario(actor, usr)) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "No tiene permiso para editar un usuario de ese tipo.");
    }

    return usrRep.saveAndFlush(
      agregarFoto(
        afirmar(id).actualizar(usr),
        img));
  }

  /**
   * Permite a un USUARIO actualizar sus datos, incluyendo una posible nueva
   * foto de perfil.
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param usr
   * El objeto Usuario con los nuevos datos.
   *
   * @param img {@code [null]}
   * Posible imagen a usar como foto de perfil.
   *
   * @return
   * El USUARIO perteneciente al {@code actor} actualizado.
   */
  public Usuario editarme (
    Usuario actor, Usuario usr, MultipartFile img
  ) throws ResponseStatusException {

    return usrRep.saveAndFlush(
      agregarFoto(
        afirmar(actor.getId()).actualizarse(usr),
        img));
  }



  /**
   * Elimina un Usuario, siempre y cuando el Actor tenga los permisos
   * suficientes.
   *
   * @param actor
   * El Usuario ejecutor de la operacion.
   *
   * @param id
   * El ID del Usuario a eliminar.
   *
   * @return
   * El Usuario eliminado o {@code null} si no existia.
   */
  public Usuario eliminar (Usuario actor, Long id)
    throws ResponseStatusException {

    // Si no tiene los permisos necesarios lanzar error HTTP-401.
    if (!puedeEliminarUsuarios(actor)) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "No tiene permiso para eliminar usuarios.");
    }

    // Intentar encontrar el USUARIO.
    var usr = qId(id);

    // Si se encontro USUARIO, eliminarlo.
    if (Objects.nonNull(usr)) {
      usrRep.deleteById(id);
    }

    // Retornar posible USUARIO eliminado, o null.
    return usr;
  }



  /**
   * Bloquea un Usuario, siempre y cuando el Actor tenga los permisos
   * suficientes.
   *
   * @param actor
   * El Usuario ejecutor de la operacion.
   *
   * @param id
   * El ID del Usuario a bloquear.
   *
   * @return
   * El Usuario eliminado.
   */
  public Usuario bloquear (Usuario actor, Long id)
    throws ResponseStatusException {

    // Encontrar el Usuario a bloquear.
    var usr = afirmar(id);

    // Si intenta bloquearse a si mismo, lanzar error HTTP-401.
    if (Objects.equals(actor.getId(), usr.getId())) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED, "No bloquearse a si mismo.");
    }

    // Si no tiene los permisos necesarios, lanzar error HTTP-401.
    if (!puedeEditarAUsuario(actor, usr)) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "No tiene permiso para bloquear ese usuario.");
    }

    usr.setBloqueado(true);

    return usr;
  }



  /**
   * Desbloquea un Usuario, siempre y cuando el Actor tenga los permisos
   * suficientes.
   *
   * @param actor
   * El Usuario ejecutor de la operacion.
   *
   * @param id
   * El ID del Usuario a desbloquear.
   *
   * @return
   * El Usuario eliminado.
   */
  public Usuario desbloquear (Usuario actor, Long id)
    throws ResponseStatusException {

    // Encontrar el Usuario a desbloquear.
    var usr = afirmar(id);

    // Si intenta bloquearse a si mismo, lanzar error HTTP-401.
    if (Objects.equals(actor.getId(), usr.getId())) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED, "No des-bloquearse a si mismo.");
    }

    // Si no tiene los permisos necesarios, lanzar error HTTP-401.
    if (!puedeEditarAUsuario(actor, usr)) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "No tiene permiso para bloquear ese usuario.");
    }

    usr.setBloqueado(false);

    return usr;
  }



  /**
   * Autentica un Usuario y obtiene sus detalles como roles y estatuses.
   *
   * @param usr
   * El Usuario que se desea autenticar.
   *
   * @return
   * El token (JWT) de acceso unico para el Usuario.
   */
  public String verify (Usuario usr)
    throws BadCredentialsException {

    UserDetails usrDts = usrDSvc.loadUserByUsername(usr.getEmail());

    if (!pwdEnc.matches(usr.getPassword(), usrDts.getPassword())) {
      throw new BadCredentialsException("Credenciales incorrectas");
    }

    Authentication auth = new UsernamePasswordAuthenticationToken(
      usrDts, null, usrDts.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);

    if (auth.isAuthenticated()) {
      return jwtSvc.generateToken(usrDts.getUsername());
    }

    return "No se pudo autenticar";
  }



  /**
   * Intenta agregar al Usuario los detalles de la foto especificada.
   * Si hay algun error lanza un error HTTP-400.
   *
   * @param usr
   * El Usuario al que se agregara la foto.
   *
   * @param img
   * La imagen a agregar.
   *
   * @return
   * El Usuario con la foto agregada.
   */
  private Usuario agregarFoto (Usuario usr, MultipartFile img)
    throws ResponseStatusException {

    if (Objects.isNull(img) || img.isEmpty()) {
      return usr;
    }

    try {
      usr.setFoto(img);
    } catch (IOException e) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Error al procesar la imagen");
    }

    return usr;
  }



  /**
   * Lanza una excepcion si el Usuario tiene un email que ya esta tomado en la
   * BD, de lo contrario solo retorna el mismo objeto.
   *
   * @param usr
   * El Usuario cuyo email se desea validar en la BD.
   *
   * @return
   * El Usuario juzgado.
   */
  public Usuario afirmarEmailNoTomado (Usuario usr)
    throws ResponseStatusException {

    if (emailTomado(usr.getEmail())) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "Email no disponible");
    }

    return usr;
  }

  /**
   * Determina si un email ya esta tomado por un Usuario en la BD.
   *
   * @param email
   * El email a validar.
   *
   * @return
   * true = ya esta tomado.
   * false = no esta tomado.
   */
  public boolean emailTomado (String email) {
    return usrRep.qEmail(email).isPresent();
  }



  /**
   * Codifica el 'password' para que no sea visile a simple vista.
   * <p>
   * Solo debe aplicarse una vez, cuando el objeto es recibido en una peticion
   * de registro, antes de guardarlo en la BD. Si se aplica una segunda vez el
   * 'password' quedaria doblemente codificado, lo que lo haria imposible de
   * autenticar.
   *
   * @param pwdEnc
   * El codificador de passwords que se desea usar, normalmente provisto por la
   * funcion llamante en la capa de servicio.
   *
   * @return
   * El objeto Usuario actualizado.
   */
  public Usuario codificarPassword (Usuario usr, PasswordEncoder pwdEnc) {
    usr.setPassword(pwdEnc.encode(usr.getPassword()));
    return usr;
  }



  /**
   * Determina si el Usuario Actor tiene permiso de REGISTRAR un Usuario como el
   * especificado.
   *
   * @param actor
   * El Usuario ejecutor de la operacion.
   *
   * @param usr
   * Objeto con los datos del Usuario que se desea registrar.
   *
   * @return
   * {@code true} = permitido.
   * {@code false} = no permitido.
   */
  public boolean puedeRegistrarAUsuario (Usuario actor, Usuario usr) {
    var rol = actor.getRol();

    return switch (usr.getRol()) {
      case Rol.ADMINISTRADOR, Rol.ORGANIZADOR ->
        rol == Rol.ADMINISTRADOR;

      case Rol.STAFF ->
        rol == Rol.ADMINISTRADOR || rol == Rol.ORGANIZADOR;

      case Rol.ALUMNO ->
        rol == Rol.ADMINISTRADOR || rol == Rol.ORGANIZADOR
          || (
          rol == Rol.STAFF
            && actor.isStaffAutorizado() && actor.isStaffAlumnos());
    };
  }



  /**
   * Determina si el Usuario Actor tiene permiso de EDITAR un Usuario como el
   * especificado.
   *
   * @param actor
   * El Usuario ejecutor de la operacion.
   *
   * @param usr
   * Objeto con los datos del Usuario que se desea registrar.
   *
   * @return
   * {@code true} = permitido.
   * {@code false} = no permitido.
   */
  public boolean puedeEditarAUsuario (Usuario actor, Usuario usr) {
    return puedeRegistrarAUsuario(actor, usr);
  }



  /**
   * Determina si el Usuario Actor tiene permiso de ELIMINAR USUARIOS en
   * general.
   *
   * @param actor
   * El Usuario ejecutor de la operacion.
   *
   * @return
   * {@code true} = permitido.
   * {@code false} = no permitido.
   */
  public boolean puedeEliminarUsuarios (Usuario actor) {
    return actor.getRol() == Rol.ADMINISTRADOR;
  }



  /**
   * Determina si un registro cumple con los requerimientos nombrados en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED}.
   *
   * @param id
   * El ID del registro a validar.
   *
   * @return
   * El registro validado.
   */
  public Usuario afirmarNoBloqueado (
    Long id
  ) {
    return afirmarNoBloqueado(afirmar(id));
  }

  /**
   * Determina si un registro cumple con los requerimientos nombrados en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED}.
   *
   * @param reg
   * El registro a validar.
   *
   * @return
   * El registro validado.
   */
  public static Usuario afirmarNoBloqueado (
    Usuario reg
  ) {
    // Comprobar que no tiene ya un BOLETO para ese CONGRESO.
    if (reg.isBloqueado()) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        "El usuario esta bloqueado");
    }

    return reg;
  }



  /**
   * Determina si un registro cumple con los requerimientos nombrados en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED}.
   *
   * @param noControl
   * Identificador del registro a validar.
   *
   * @return
   * El registro validado.
   */
  public Usuario afirmarNoControlNoBloqueado (
    String noControl
  ) {
    return afirmarNoBloqueado(afirmarNoControl(noControl));
  }
}
