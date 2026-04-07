package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

import com.tecn.tijuana.congresos.identidad.autenticacion.dto.LoginDto;
import com.tecn.tijuana.congresos.identidad.validacion.dto.RegistroAlumnoDto;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.dto.RegistroUsuarioDto;
import com.tecn.tijuana.congresos.utils.Api;
import com.tecn.tijuana.congresos.security.JwtService;
//import org.springframework.beans.factory.annotation.Autowired;
import com.tecn.tijuana.congresos.utils.GeneradorDeFolios;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication
  .UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite al personal registrar USUARIOS en el sistema.
   *
   * @param dto
   * Datos del USUARIO.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @return
   * El USUARIO recien registrado en la BD.
   */
  public Usuario registrar (
    Usuario actor, RegistroUsuarioDto dto
  )
    throws ResponseStatusException {

    return registrar(actor, Usuario.nuevoUsuario(dto));
  }

  /**
   * Permite al personal registrar USUARIOS en el sistema.
   *
   * @param usr
   * Datos del USUARIO.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @return
   * El USUARIO recien registrado en la BD.
   */
  public Usuario registrar (Usuario actor, Usuario usr)
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
      case Rol.ADMINISTRADOR -> registrarAdmin(usr);
      case Rol.ORGANIZADOR   -> registrarOrganizador(usr);
      case Rol.STAFF         -> registrarStaff(usr);
      case Rol.ALUMNO        -> registrarAlumno(usr);
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
   * @return
   * El Usuario recien registrado en la BD.
   */
  public Usuario registrarAdmin (Usuario usr)
    throws ResponseStatusException {

    return usrRep.saveAndFlush(
      Usuario.nuevoAdmin(
        codificarPassword(
          afirmarEmailNoTomado(usr),
          pwdEnc)));
  }



  /**
   * Registra un USUARIO tipo ORGANIZADOR.
   *
   * @param usr
   * Datos del Usuario.
   *
   * @return
   * El Usuario recien registrado en la BD.
   */
  public Usuario registrarOrganizador (Usuario usr)
    throws ResponseStatusException {

    return usrRep.saveAndFlush(
      Usuario.nuevoOrganizador(
        codificarPassword(
          afirmarEmailNoTomado(usr),
          pwdEnc)));
  }



  /**
   * Registra un USUARIO tipo STAFF.
   *
   * @param usr
   * Datos del Usuario.
   *
   * @return
   * El Usuario recien registrado en la BD.
   */
  public Usuario registrarStaff (Usuario usr)
    throws ResponseStatusException {

    return usrRep.saveAndFlush(
      Usuario.nuevoStaff(
        codificarPassword(
          afirmarEmailNoTomado(usr),
          pwdEnc)));
  }



  /**
   * Permite al personal registrar Alumnos en el sistema.
   * <p>
   * Para uso exlusivo de Usuarios tipo ADMINISTRADOR, ORGANIZADOR, STAFF.
   *
   * @param usr
   * Datos de Usuario del Alumno.
   *
   * @return
   * El Usuario recien registrado en la BD.
   */
  public Usuario registrarAlumno (Usuario usr)
    throws ResponseStatusException {

    return usrRep.saveAndFlush(
      Usuario.nuevoAlumno(
        codificarPassword(
          afirmarEmailNoTomado(
            afirmarEmailInstitucionalNoTomado(usr)),
          pwdEnc)));
  }



  /**
   * Permite a un nuevo Alumno registrarse por su propia cuenta en el sistema.
   *
   * @param dto
   * Datos de Usuario del Alumno.
   *
   * @return
   * El Usuario recien registrado en la BD.
   */
  public Usuario registrarseAlumno (
    RegistroAlumnoDto dto
  )
    throws ResponseStatusException {

    return registrarseAlumno(
      Usuario.nuevoAlumnoAutoRegistrado(dto));
  }

  /**
   * Permite a un nuevo Alumno registrarse por su propia cuenta en el sistema.
   *
   * @param usr
   * Datos de Usuario del Alumno.
   *
   * @return
   * El Usuario recien registrado en la BD.
   */
  public Usuario registrarseAlumno (Usuario usr)
    throws ResponseStatusException {

    return usrRep.saveAndFlush(
      Usuario.nuevoAlumnoAutoRegistrado(
        codificarPassword(
          afirmarNoControlNoTomado(
            afirmarEmailNoTomado(
              afirmarEmailInstitucionalNoTomado(usr))),
          pwdEnc)));
  }



  /**
   * Actualiza un Registro en la BD con los nuevos datos provistos.
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param id
   * ID del Registro a editar.
   *
   * @param usr
   * El Registro a editar.
   *
   * @return
   * El Registro editado.
   */
  public Usuario editar (
    Usuario actor, Long id, Usuario usr
  ) throws ResponseStatusException {

    // Si no tiene los permisos necesarios lanzar error HTTP-401.
    if (!puedeEditarAUsuario(actor, usr)) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "No tiene permiso para editar un usuario de ese tipo.");
    }

    // Encontrar el USUARIO a editar.
    // Comprobar que los campos unicos se mantengan unicos incluso despues de
    // una posible edicion.
    // Actualizar, guardar y retornar el USUARIO.
    return usrRep.saveAndFlush(
      afirmarEdicionDeCamposUnicosValida(afirmar(id), usr).actualizar(usr));
  }



  /**
   * Permite editar un slot de multimedia del registro.
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param id
   * ID del registro.
   *
   * @param slot
   * Slot a editar.
   *
   * @param img {@code [null]}
   * Posible imagen a usar como foto de info.
   * Si es {@code null} se remueve la imagen de ese slot.
   *
   * @return
   * El registro editado.
   */
  public Usuario editarMedia (
    Usuario actor, Long id, String slot, MultipartFile img
  )
    throws ResponseStatusException {

    // Encontrar el Usuario.
    var usuario = afirmar(id);

    // Si no tiene los permisos necesarios lanzar error HTTP-401.
    if (!puedeEditarAUsuario(actor, usuario)) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "No tiene permiso para editar un usuario de ese tipo.");
    }

    try {
      usuario.setMedia(slot, img);
    } catch (IOException e) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Error al procesar la imagen");
    }

    // Actualizar, guardar y retornar el registro.
    return usrRep.saveAndFlush(usuario);
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
   * @return
   * El USUARIO perteneciente al {@code actor} actualizado.
   */
  public Usuario editarme (
    Usuario actor, Usuario usr
  ) throws ResponseStatusException {

    // Comprobar que los campos unicos se mantengan unicos incluso despues de
    // una posible edicion.
    // Actualizar, guardar y retornar el USUARIO.
    return usrRep.saveAndFlush(
      afirmarEdicionDeCamposUnicosValida(afirmar(actor.getId()), usr)
        .actualizarse(usr));
  }



  /**
   * Permite editar un slot de multimedia del USUARIO propio.
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param slot
   * El nombre del slot a editar.
   *
   * @param img {@code [null]}
   * Posible imagen a usar como foto de info.
   * Si es {@code null} se remueve la imagen de ese slot.
   *
   * @return
   * El Congreso editado.
   */
  public Usuario editarmeMedia (
    Usuario actor, String slot, MultipartFile img
  )
    throws ResponseStatusException {

    // Encontrar el Congreso.
    var usuario = afirmar(actor.getId());

    try {
      usuario.setMedia(slot, img);
    } catch (IOException e) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Error al procesar la imagen");
    }

    // Actualizar, guardar y retornar el registro.
    return usrRep.saveAndFlush(usuario);
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
        HttpStatus.UNAUTHORIZED, "No puede bloquearse a si mismo.");
    }

    // Si no tiene los permisos necesarios, lanzar error HTTP-401.
    if (!puedeEditarAUsuario(actor, usr)) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "No tiene permiso para bloquear ese usuario.");
    }

    usr.setBloqueado(true);

    return usrRep.saveAndFlush(usr);
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
        HttpStatus.UNAUTHORIZED, "No puede des-bloquearse a si mismo.");
    }

    // Si no tiene los permisos necesarios, lanzar error HTTP-401.
    if (!puedeEditarAUsuario(actor, usr)) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "No tiene permiso para bloquear ese usuario.");
    }

    usr.setBloqueado(false);

    return usrRep.saveAndFlush(usr);
  }



  /**
   * Intenta agregar al Usuario los detalles de la foto especificada.
   * Si hay algun error lanza un error HTTP-400.
   *
   * @param usuario
   * El Usuario al que se agregara la foto.
   *
   * @param img
   * La imagen a agregar.
   *
   * @return
   * El Usuario con la foto agregada.
   */
  private Usuario agregarFoto (
    Usuario usuario, MultipartFile img
  )
    throws ResponseStatusException {

    if (Objects.isNull(img) || img.isEmpty()) {
      return usuario;
    }

    try {
      usuario.setMedia("foto", img);
    } catch (IOException e) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Error al procesar la imagen");
    }

    return usrRep.saveAndFlush(usuario);
  }



  /**
   * Reinicia el password del USUARIO especificado a una cadena de texto
   * aleatoria de 8 caracteres.
   *
   * @param actor
   * El Usuario ejecutor de la operacion.
   *
   * @param usuarioId
   * ID del USUARIO a reiniciar.
   *
   * @return
   * El password nuevo generado.
   */
  public String reiniciarPassword (
    Usuario actor, Long usuarioId
  )
    throws ResponseStatusException {

    // Comprobar que no intenta reiniciarse a si mismo.
    if (Objects.equals(actor.getId(), usuarioId)) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "No puede reiniciar su propio password usando este metodo.");
    }

    // Encontrar al USUARIO.
    var usr = afirmar(usuarioId);

    // Si no tiene los permisos necesarios, lanzar error HTTP-401.
    if (!puedeEditarAUsuario(actor, usr)) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "No tiene permiso para editar ese usuario.");
    }

    // Generar password aleatorio.
    var pwd = GeneradorDeFolios.folio(8);

    // Establecer password sin codificar.
    usr.setPassword(pwd);

    // Codificar el password.
    // Guardar el USUARIO.
    usrRep.saveAndFlush(codificarPassword(usr, pwdEnc));

    // Retornar nuevo password.
    return pwd;
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

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



  //----------------------------------------------------------------------------
  // ASERCIONES.

  /**
   * Comprueba que el USUARIO con el ID especificado existe.
   *
   * @param id
   * El ID del registro.
   *
   * @return
   * El registro encontrado.
   *
   * @throws ResponseStatusException
   * <p>
   * {@code HTTP-NOT_FOUND} si no existe.
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
   * Comprueba que el USUARIO con el ID especificado existe y que es un ALUMNO.
   *
   * @param id
   * El ID del registro.
   *
   * @return
   * El registro encontrado.
   *
   * @throws ResponseStatusException
   * <p>
   * {@code HTTP-NOT_FOUND} si no existe.
   * <p>
   * {@code HTTP-PRECONDITION_FAILED} si no es un ALUMNO.
   */
  public Usuario afirmarAlumno (
    Long id
  )
    throws ResponseStatusException {

    // Encontrar el USUARIO.
    var usr = usrRep.findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format("Usuario con ID: %s no encontrado", id)));

    // Comprobar que es un ALUMNO.
    if (usr.getRol() != Rol.ALUMNO) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        String.format("Usuario con ID: %s no es un alumno", id));
    }

    return usr;
  }



  /**
   * Comprueba que el USUARIO con el Numero de Control especificado existe y que
   * es un ALUMNO.
   *
   * @param noControl
   * Identificador del registro.
   *
   * @return
   * El registro encontrado.
   *
   * @throws ResponseStatusException
   * <p>
   * {@code HTTP-NOT_FOUND} si no existe.
   * <p>
   * {@code HTTP-PRECONDITION_FAILED} si no es un ALUMNO.
   */
  public Usuario afirmarNoControlAlumno (
    String noControl
  )
    throws ResponseStatusException {

    // Encontrar USUARIO.
    var usr = usrRep.qNoControl(noControl)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format(
            "Usuario con Numero de Control: %s no encontrado",
            noControl)));

    // Comprobar que es un ALUMNO.
    if (usr.getRol() != Rol.ALUMNO) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        String.format(
          "Usuario con Numero de Control: %s no es un alumno",
          noControl));
    }

    return usr;
  }



  /**
   * Recibe una version 'anterior' y una 'posterior' de un Usuario.
   * <p>
   * La version posterior es similar a la anterior pero incluye todas las
   * modificaciones deseadas.
   * <p>
   * Si un campo que deberia ser unico como Email, #Control, etc.... incluye una
   * edicion, se comprueba que el nuevo valor no se repita en la base de datos.
   *
   * @param anterior
   * Version anterior/pre-existente del registro en la BD.
   *
   * @param posterior
   * Version posterior/nueva del registro para guardar en la BD.
   *
   * @return
   * La version anterior del USUARIO tal como se recibio en los parametros.
   */
  public Usuario afirmarEdicionDeCamposUnicosValida (
    Usuario anterior, Usuario posterior
  ) throws ResponseStatusException {

    // Al editar #Control, comprobar que no este tomado.
    if (!Objects.equals(anterior.getNoControl(), posterior.getNoControl())) {
      afirmarNoControlNoTomado(posterior);
    }
    // Al editar Email, comprobar que no este tomado.
    if (!Objects.equals(anterior.getEmail(), posterior.getEmail())) {
      afirmarEmailNoTomado(posterior);
    }
    // Al editar Email Institucional, comprobar que no este tomado.
    if (!Objects.equals(
      anterior.getEmailInstitucional(), posterior.getEmailInstitucional())) {
      afirmarEmailInstitucionalNoTomado(posterior);
    }

    return anterior;
  }



  /**
   * Lanza una excepcion si el Usuario tiene un #Control que ya esta tomado en
   * la BD, de lo contrario solo retorna el mismo objeto.
   *
   * @param usr
   * El Usuario cuyo #Control se desea validar en la BD.
   *
   * @return
   * El Usuario juzgado.
   */
  public Usuario afirmarNoControlNoTomado (Usuario usr)
    throws ResponseStatusException {

    if (noControlTomado(usr.getNoControl())) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "No. de Control no disponible");
    }

    return usr;
  }



  /**
   * Lanza una excepcion si el Usuario tiene un Email que ya esta tomado en la
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
   * Lanza una excepcion si el Usuario tiene un Email Institucional que ya esta
   * tomado en la BD, de lo contrario solo retorna el mismo objeto.
   *
   * @param usr
   * El Usuario cuyo email se desea validar en la BD.
   *
   * @return
   * El Usuario juzgado.
   */
  public Usuario afirmarEmailInstitucionalNoTomado (Usuario usr)
    throws ResponseStatusException {

    if (emailInstitucionalTomado(usr.getEmailInstitucional())) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "Email Institucional no disponible");
    }

    return usr;
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
    return afirmarNoBloqueado(afirmarNoControlAlumno(noControl));
  }



  /**
   * La imagen de un registro en el slot especificado.
   *
   * @param id
   * ID del registro.
   *
   * @param slot
   * Slot para multimedia.
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
  public ResponseEntity<byte[]> afirmarMedia (
    Long id, String slot
  )
    throws ResponseStatusException {

    // Encontrar registro.
    Usuario usuario = afirmar(id);

    // Aux.
    String fotoMimeType;

    // Extraer datos de la foto.
    byte[] fotoImgData = switch (slot) {
      case "foto" -> {
        fotoMimeType = usuario.getFotoMimeType();
        yield usuario.getFotoImgData();
      }
      default -> throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Slot no valido.");
    };

    // Si hay algo malo con la foto, retornar error 404.
    if (
      Objects.isNull(fotoImgData) || fotoImgData.length == 0
        || Objects.isNull(fotoMimeType) || fotoMimeType.isBlank()
    ) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        String.format("Foto del usuario con ID: %s no encontrada.", id));
    }

    // Retornar respuesta ya lista con el contenido de la foto.
    return ResponseEntity.ok()
      .contentType(MediaType.valueOf(fotoMimeType))
      .body(fotoImgData);
  }



  //----------------------------------------------------------------------------
  // AUXILIARES.

  /**
   * Autentica un Usuario y obtiene sus detalles como roles y estatuses.
   *
   * @param dto
   * Credenciales del USUARIO.
   *
   * @return
   * El token (JWT) de acceso unico para el USUARIO.
   */
  public String verify (
    LoginDto dto
  )
    throws ResponseStatusException {

    // Aux.
    UserDetails usrDts;

    // Encontrar el USUARIO via su Email.
    try {
      usrDts = usrDSvc.loadUserByUsername(dto.getEmail());
    } catch (UsernameNotFoundException e) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Usuario no registrado");
    }

    // Comprobar credenciales.
    if (!pwdEnc.matches(dto.getPassword(), usrDts.getPassword())) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "Credenciales incorrectas");
    }

    // Crear token de autenticacion con el detalle de USUARIO.
    Authentication auth = new UsernamePasswordAuthenticationToken(
      usrDts, null, usrDts.getAuthorities());

    // Establecer autenticacion usando el contexto global de seguridad.
    SecurityContextHolder.getContext().setAuthentication(auth);

    // Si el USUARIO esta autenticado, generar y retornar su JWT.
    if (auth.isAuthenticated()) {
      return jwtSvc.generateToken(usrDts.getUsername());
    }

    // Si se llego a este punto, se debe retornar un error.
    return "No se pudo autenticar";
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
   * Determina si un email institucional ya esta tomado por un Usuario en la BD.
   *
   * @param email
   * El email a validar.
   *
   * @return
   * true = ya esta tomado.
   * false = no esta tomado.
   */
  public boolean emailInstitucionalTomado (String email) {
    return usrRep.qEmailInstitucional(email).isPresent();
  }



  /**
   * Determina si un #Control ya esta tomado por un Usuario en la BD.
   *
   * @param noControl
   * El #Control a validar.
   *
   * @return
   * true = ya esta tomado.
   * false = no esta tomado.
   */
  public boolean noControlTomado (String noControl) {
    return usrRep.qNoControl(noControl).isPresent();
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

    // Aux.
    var rolActivo = actor.getRol();
    var rolPasivo = usr.getRol();

    // Determinar y retornar veredicto.
    return switch (rolPasivo) {
      case Rol.ADMINISTRADOR, Rol.ORGANIZADOR ->
        rolActivo == Rol.ADMINISTRADOR;

      case Rol.STAFF ->
        rolActivo == Rol.ADMINISTRADOR || rolActivo == Rol.ORGANIZADOR;

      case Rol.ALUMNO ->
        rolActivo == Rol.ADMINISTRADOR || rolActivo == Rol.ORGANIZADOR
          || (
          rolActivo == Rol.STAFF
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
}
