package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tecn.tijuana.congresos.identidad.validacion.dto.RegistroAlumnoDto;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.dto.RegistroUsuarioDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * Clase que representa la entidad de USUARIO en el sistema y en la BD.
 * */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails {

  /**
   * Identificador unico del registro.
   * */
  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "usuario_sequence"
  )
  @SequenceGenerator(
    name = "usuario_sequence",
    sequenceName = "usuario_sequence",
    allocationSize = 1
  )
  @Column(nullable = false, updatable = false)
  private Long id;



  /**
   * Cuando fue creado el registro.
   * */
  @Column(nullable = false, updatable = false)
  private LocalDateTime fechaCreacion;

  /**
   * Posible creador del registro.
   * */
  @Column(updatable = false)
  private Long creadorId;



  /**
   * Rol del USUARIO.
   * */
  @NotNull(message = "El rol es obligatorio")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, updatable = false)
  private Rol rol = Rol.ALUMNO;



  /**
   * Email del USUARIO.
   * */
  @NotBlank(message = "Email vacio")
  @Email(message = "Email invalido")
  @Size(min = 6, max = 100,
    message = "Email debe tener entre 6 y 100 caracteres")
  @Column(unique = true, nullable = false, length = 100)
  private String email;

  /**
   * Contrasena de acceso codificada del USUARIO.
   * */
  @Schema(hidden = true) // Esconde el campo para OpenAPI/Swagger.
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotBlank(message = "Clave vacia")
  @Size(min = 8, max = 100,
    message = "La clave debe tener entre 8 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String password;



  /**
   * Determina si el USUARIO esta bloqueado, impidiendole hacer uso del sistema.
   * */
  @Column(nullable = false)
  private boolean bloqueado = false;

  /**
   * Determina si la cuenta del USUARIO esta expirada.
   * */
  @Column(nullable = false)
  private boolean expirado = false;

  /**
   * Determina si las credenciales de acceso del USUARIO estan expiradas.
   * */
  @Column(nullable = false)
  private boolean credencialesExpiradas = false;

  /**
   * Determina si la cuenta del USUARIO esta deshabilitada.
   * */
  @Column(nullable = false)
  private boolean deshabilitado = false;



  /**
   * Codigo de pais del telefono del USUARIO.
   * */
  @NotBlank(message = "Prefijo de telefono vacio")
  @Size(min = 1, max = 7,
    message = "Prefijo debe tener entre 1 y 7 digitos")
  @Column(nullable = false, length = 7)
  private String telPref;

  /**
   * Numero de telefono nacional del USUARIO.
   * */
  @NotBlank(message = "Sufijo de telefono vacio")
  @Size(min = 4, max = 14,
    message = "Sufijo debe tener entre 4 y 14 digitos")
  @Column(nullable = false, length = 14)
  private String telSuf;



  /**
   * Nombre del USUARIO.
   * */
  @NotBlank(message = "Nombre vacio")
  @Size(min = 3, max = 40,
    message = "El nombre debe tener entre 3 y 40 caracteres")
  @Column(nullable = false, length = 40)
  private String nombre;

  /**
   * Apellido paterno del USUARIO.
   * */
  @Size(max = 40,
    message = "El apellido paterno debe tener entre 0 y 40 caracteres")
  @Column(length = 40)
  private String apellidoPaterno;

  /**
   * Apellido materno del USUARIO.
   * */
  @Size(max = 40,
    message = "El apellido materno debe tener entre 0 y 40 caracteres")
  @Column(length = 40)
  private String apellidoMaterno;

  /**
   * Fecha de nacimiento del USUARIO.
   * */
  @Past
  @Column(nullable = false)
  private LocalDateTime fechaNacimiento;



  /**
   * Numero de control ALUMNO.
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  @Size(min = 8, max = 8,
    message = "El numero de control debe tener 8 caracteres")
  @Column(length = 8)
  private String noControl;

  /**
   * Codigo de la carrera del ALUMNO.
   * Ej: "ISC".
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  @Size(min = 2, max = 3,
    message = "El codigo de carrera debe tener entre 2 y 3 caracteres")
  @Column(length = 3)
  private String codigoCarrera;

  /**
   * Semestre que esta cursando el ALUMNO al momento del registro.
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  @Min(value = 0, message = "El semestre no puede ser menor que 0")
  @Max(value = 50, message = "El semestre no puede ser mayor que 50")
  private int semestre;

  /**
   * Grupo donde esta cursando el ALUMNO.
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  @Size(min = 1, max = 3,
    message = "El grupo debe tener entre 1 y 3 caracteres")
  @Column(length = 3)
  private String grupo;

  /**
   * Determina si es un ALUMNO externo, proveniente de otra institucion
   * educativa.
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  private boolean externo = false;

  /**
   * CURP del ALUMNO.
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  @Size(min = 18, max = 18,
    message = "La CURP debe tener 18 caracteres")
  @Column(length = 18)
  private String curp;

  /**
   * Email institucional del ALUMNO.
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  @Email(message = "Email invalido")
  @Size(min = 6, max = 100,
    message = "Email debe tener entre 6 y 100 caracteres")
  @Column(unique = true, length = 100)
  private String emailInstitucional;



  /**
   * Describe textualmente las responsabilidades que tiene un miembro del STAFF.
   * Solo es informativo y no afecta comportamiento del sistema.
   * Solo aplica para USUARIOS tipo STAFF.
   * */
  @Size(max = 200,
    message = "Responsabilidades debe tener maximo 200 caracteres")
  @Column(length = 200)
  private String staffResponsabilidades;

  /**
   * Interruptor general de STAFF, sin este no puede ejercer sus funciones.
   * Solo aplica para USUARIOS tipo STAFF.
   * */
  private boolean staffAutorizado = false;

  /**
   * Determins si el STAFF puede realizar tareas de custodio, es decir, validar
   * la entrada de ALUMNOS a eventos de Congresos y Conferencias.
   * Solo aplica para USUARIOS tipo STAFF.
   * */
  private boolean staffCustodio = false;

  /**
   * Determina si el STAFF puede realizar operaciones sobre los ALUMNOS, como
   * registros, ediciones, bloqueos, etc...
   * Solo aplica para USUARIOS tipo STAFF.
   * */
  private boolean staffAlumnos = false;

  /**
   * Determina si el STAFF puede inscribir ALUMNOS a eventos y realizar
   * operaciones relacionadas.
   * Solo aplica para USUARIOS tipo STAFF.
   * */
  private boolean staffInscripciones = false;



  /**
   * Nombre original del archivo de la imagen que se uso como foto del USUARIO.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El nombre de la foto debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String fotoNombre;

  /**
   * Tipo de archivo multimedia de la foto del USUARIO.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de la foto debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String fotoMimeType;

  /**
   * Contenido crudo de la foto del USUARIO.
   * */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "La foto debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] fotoImgData;



  /**
   * Constructor generico de la clase con todos los parametros basicos.
   * <p>
   * Normalmente no se llama a este directamente a menos que se use para definir
   * otro constructor mas especifico.
   */
  private Usuario (
    Rol rol,

    Long creadorId,

    String email,
    String password,

    boolean bloqueado,
    boolean expirado,
    boolean credencialesExpiradas,
    boolean deshabilitado,

    String telPref,
    String telSuf,

    String nombre,
    String apellidoPaterno,
    String apellidoMaterno,
    LocalDateTime fechaNacimiento,

    String noControl,
    String codigoCarrera,
    int semestre,
    String grupo,
    boolean externo,
    String curp,
    String emailInstitucional,

    String staffResponsabilidades,
    boolean staffAutorizado,
    boolean staffCustodio,
    boolean staffAlumnos,
    boolean staffInscripciones
  ) {
    this.rol = rol;

    this.fechaCreacion = LocalDateTime.now();
    this.creadorId = creadorId;

    this.email = email;
    this.password = password;

    this.bloqueado = bloqueado;
    this.expirado = expirado;
    this.credencialesExpiradas = credencialesExpiradas;
    this.deshabilitado = deshabilitado;

    this.telPref = telPref;
    this.telSuf = telSuf;

    this.nombre = nombre;
    this.apellidoPaterno = apellidoPaterno;
    this.apellidoMaterno = apellidoMaterno;
    this.fechaNacimiento = fechaNacimiento;

    this.noControl = noControl;
    this.codigoCarrera = codigoCarrera;
    this.semestre = semestre;
    this.grupo = grupo;
    this.externo = externo;
    this.curp = curp;
    this.emailInstitucional = emailInstitucional;

    this.staffResponsabilidades = staffResponsabilidades;
    this.staffAutorizado = staffAutorizado;
    this.staffCustodio = staffCustodio;
    this.staffAlumnos = staffAlumnos;
    this.staffInscripciones = staffInscripciones;
  }



  /**
   * Funcion constructora alternativa para crear Usuarios.
   *
   * @return
   * El nuevo objeto Usuario.
   */
  public static Usuario nuevoUsuario (RegistroUsuarioDto dto) {
    return new Usuario(
      dto.rol,
      null,
      dto.email,
      dto.password,
      dto.bloqueado,
      dto.expirado,
      dto.credencialesExpiradas,
      dto.deshabilitado,
      dto.telPref,
      dto.telSuf,
      dto.nombre,
      dto.apellidoPaterno,
      dto.apellidoMaterno,
      dto.fechaNacimiento,
      dto.noControl,
      dto.codigoCarrera,
      dto.semestre,
      dto.grupo,
      dto.externo,
      dto.curp,
      dto.emailInstitucional,
      dto.staffResponsabilidades,
      dto.staffAutorizado,
      dto.staffCustodio,
      dto.staffAlumnos,
      dto.staffInscripciones
    );
  }



  /**
   * Funcion constructora alternativa para crear Usuarios tipo ADMINISTRADOR.
   *
   * @return
   * El nuevo objeto Usuario tipo ADMINISTRADOR.
   */
  public static Usuario nuevoAdmin (Usuario usr) {
    return nuevoAdmin(
      usr.creadorId,
      usr.email,
      usr.password,
      usr.telPref,
      usr.telSuf,
      usr.nombre,
      usr.apellidoPaterno,
      usr.apellidoMaterno,
      usr.fechaNacimiento
    );
  }

  /**
   * Funcion constructora alternativa para crear Usuarios tipo ADMINISTRADOR.
   *
   * @return
   * El nuevo objeto Usuario tipo ADMINISTRADOR.
   */
  public static Usuario nuevoAdmin (
    Long idCreador,

    String email,
    String password,

    String telPref,
    String telSuf,

    String nombre,
    String apellidoPaterno,
    String apellidoMaterno,
    LocalDateTime fechaNacimiento
  ) {
    return new Usuario(
      Rol.ADMINISTRADOR,

      idCreador,

      email,
      password,

      false,
      false,
      false,
      false,

      telPref,
      telSuf,

      nombre,
      apellidoPaterno,
      apellidoMaterno,
      fechaNacimiento,

      null,
      null,
      0,
      null,
      false,
      null,
      null,

      null,
      false,
      false,
      false,
      false
    );
  }



  /**
   * Funcion constructora alternativa para crear Usuarios tipo ORGANIZADOR.
   *
   * @return
   * El nuevo objeto Usuario tipo ORGANIZADOR.
   */
  public static Usuario nuevoOrganizador (Usuario usr) {
    return nuevoOrganizador(
      usr.creadorId,
      usr.email,
      usr.password,
      usr.telPref,
      usr.telSuf,
      usr.nombre,
      usr.apellidoPaterno,
      usr.apellidoMaterno,
      usr.fechaNacimiento
    );
  }

  /**
   * Funcion constructora alternativa para crear Usuarios tipo ORGANIZADOR.
   *
   * @return
   * El nuevo objeto Usuario tipo ORGANIZADOR.
   */
  public static Usuario nuevoOrganizador (
    Long idCreador,

    String email,
    String password,

    String telPref,
    String telSuf,

    String nombre,
    String apellidoPaterno,
    String apellidoMaterno,
    LocalDateTime fechaNacimiento
  ) {
    return new Usuario(
      Rol.ORGANIZADOR,

      idCreador,

      email,
      password,

      false,
      false,
      false,
      false,

      telPref,
      telSuf,

      nombre,
      apellidoPaterno,
      apellidoMaterno,
      fechaNacimiento,

      null,
      null,
      0,
      null,
      false,
      null,
      null,

      null,
      false,
      false,
      false,
      false
    );
  }



  /**
   * Funcion constructora alternativa para crear Usuarios tipo STAFF.
   *
   * @return
   * El nuevo objeto Usuario tipo STAFF.
   */
  public static Usuario nuevoStaff (Usuario usr) {
    return nuevoStaff(
      usr.creadorId,
      usr.email,
      usr.password,
      usr.telPref,
      usr.telSuf,
      usr.nombre,
      usr.apellidoPaterno,
      usr.apellidoMaterno,
      usr.fechaNacimiento,
      usr.staffResponsabilidades,
      usr.staffAutorizado,
      usr.staffCustodio,
      usr.staffAlumnos,
      usr.staffInscripciones
    );
  }

  /**
   * Funcion constructora alternativa para crear Usuarios tipo STAFF.
   *
   * @return
   * El nuevo objeto Usuario tipo STAFF.
   */
  public static Usuario nuevoStaff (
    Long idCreador,

    String email,
    String password,

    String telPref,
    String telSuf,

    String nombre,
    String apellidoPaterno,
    String apellidoMaterno,
    LocalDateTime fechaNacimiento,

    String staffResponsabilidades,
    boolean staffAutorizado,
    boolean staffCustodio,
    boolean staffAlumnos,
    boolean staffInscripciones
  ) {
    return new Usuario(
      Rol.STAFF,

      idCreador,

      email,
      password,

      false,
      false,
      false,
      false,

      telPref,
      telSuf,

      nombre,
      apellidoPaterno,
      apellidoMaterno,
      fechaNacimiento,

      null,
      null,
      0,
      null,
      false,
      null,
      null,

      staffResponsabilidades,
      staffAutorizado,
      staffCustodio,
      staffAlumnos,
      staffInscripciones
    );
  }



  /**
   * Version alternativa de nuevoAlumnoAutoRegistrado/n.
   *
   * @return
   * El nuevo objeto Usuario tipo ALUMNO.
   */
  public static Usuario nuevoAlumnoAutoRegistrado (
    RegistroAlumnoDto dto
  ) {
    return Usuario.nuevoAlumnoAutoRegistrado(
      dto.email,
      dto.password,
      dto.telPref,
      dto.telSuf,
      dto.nombre,
      dto.apellidoPaterno,
      dto.apellidoMaterno,
      dto.fechaNacimiento,
      dto.noControl,
      dto.codigoCarrera,
      dto.semestre,
      dto.grupo,
      dto.externo,
      dto.curp,
      dto.emailInstitucional
    );
  }

  /**
   * Version alternativa de nuevoAlumnoAutoRegistrado/n.
   *
   * @return
   * El nuevo objeto Usuario tipo ALUMNO.
   */
  public static Usuario nuevoAlumnoAutoRegistrado (Usuario usr) {
    return Usuario.nuevoAlumnoAutoRegistrado(
      usr.email,
      usr.password,
      usr.telPref,
      usr.telSuf,
      usr.nombre,
      usr.apellidoPaterno,
      usr.apellidoMaterno,
      usr.fechaNacimiento,
      usr.noControl,
      usr.codigoCarrera,
      usr.semestre,
      usr.grupo,
      usr.externo,
      usr.curp,
      usr.emailInstitucional
    );
  }

  /**
   * Funcion constructora alternativa para crear Usuarios tipo ALUMNO.
   * <p>
   * Esta funcion es para uso de los Alumnos, para que estos de puedan
   * auto-registrar en el sistema.
   *
   * @return
   * El nuevo objeto Usuario tipo ALUMNO.
   */
  public static Usuario nuevoAlumnoAutoRegistrado (
    String email,
    String password,

    String telPref,
    String telSuf,

    String nombre,
    String apellidoPaterno,
    String apellidoMaterno,
    LocalDateTime fechaNacimiento,

    String noControl,
    String codigoCarrera,
    int semestre,
    String grupo,
    boolean externo,
    String curp,
    String emailInstitucional
  ) {
    return Usuario.nuevoAlumno(
      null,

      email,
      password,

      telPref,
      telSuf,

      nombre,
      apellidoPaterno,
      apellidoMaterno,
      fechaNacimiento,

      noControl,
      codigoCarrera,
      semestre,
      grupo,
      externo,
      curp,
      emailInstitucional
    );
  }



  /**
   * Version alternativa de nuevoAlumno/n.
   *
   * @return
   * El nuevo objeto Usuario tipo ALUMNO.
   */
  public static Usuario nuevoAlumno (Usuario usr) {
    return Usuario.nuevoAlumno(
      usr.creadorId,
      usr.email,
      usr.password,
      usr.telPref,
      usr.telSuf,
      usr.nombre,
      usr.apellidoPaterno,
      usr.apellidoMaterno,
      usr.fechaNacimiento,
      usr.noControl,
      usr.codigoCarrera,
      usr.semestre,
      usr.grupo,
      usr.externo,
      usr.curp,
      usr.emailInstitucional
    );
  }

  /**
   * Funcion constructora alternativa para crear Usuarios tipo ALUMNO.
   * <p>
   * Esta es para uso exclusivo del personal ADMINISTRADOR, ORGANIZADOR, STAFF, para que
   * estos puedan registrar Alumnos.
   *
   * @return
   * El nuevo objeto Usuario tipo ALUMNO.
   */
  public static Usuario nuevoAlumno (
    Long idCreador,

    String email,
    String password,

    String telPref,
    String telSuf,

    String nombre,
    String apellidoPaterno,
    String apellidoMaterno,
    LocalDateTime fechaNacimiento,

    String noControl,
    String codigoCarrera,
    int semestre,
    String grupo,
    boolean externo,
    String curp,
    String emailInstitucional
  ) {
    return new Usuario(
      Rol.ALUMNO,

      idCreador,

      email,
      password,

      false,
      false,
      false,
      false,

      telPref,
      telSuf,

      nombre,
      apellidoPaterno,
      apellidoMaterno,
      fechaNacimiento,

      noControl,
      codigoCarrera,
      semestre,
      grupo,
      externo,
      curp,
      emailInstitucional,

      null,
      false,
      false,
      false,
      false
    );
  }



  /**
   * Establecer todos los parametros de imagen para el slot de media
   * especificado.
   *
   * @param slot
   * El nombre del slot a editar.
   *
   * @param img
   * El archivo multiparte que contiene la imagen.
   *
   * @return
   * El objeto actualizado.
   *
   * @throws IOException
   * Cuando hay algun problema con la imagen.
   */
  public Usuario setMedia (
    String slot, MultipartFile img
  )
    throws IOException {

    if (Objects.isNull(img)) {
      switch (slot) {

        case "foto":
          setFotoNombre(null);
          setFotoMimeType(null);
          setFotoImgData(null);
          break;

        default:
          break;
      }
    }
    else {
      // Aux.
      var name = img.getOriginalFilename();
      var mime = img.getContentType();
      var bytes = img.getBytes();
      // Editar el slot indicado.
      switch (slot) {

        case "foto":
          setFotoNombre(name);
          setFotoMimeType(mime);
          setFotoImgData(bytes);
          break;

        default:
          break;
      }
    }

    return this;
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
  public Usuario codificarPassword (PasswordEncoder pwdEnc) {
    setPassword(pwdEnc.encode(getPassword()));
    return this;
  }



  // SECCION DE ACTUALIZACIONES A OTROS USUARIOS -------------------------------

  /**
   * Actualiza un Usuario A con los contenidos de otro Usuario B.
   *
   * @param usr
   * El Usuario B desde el cual se van a extraer los nuevos valores.
   *
   * @return
   * El objeto Usuario actualizado.
   */
  public Usuario actualizar (Usuario usr) {
    return switch (usr.getRol()) {
      case Rol.ADMINISTRADOR -> actualizarAdmin(usr);
      case Rol.ORGANIZADOR   -> actualizarOrganizador(usr);
      case Rol.STAFF         -> actualizarStaff(usr);
      case Rol.ALUMNO        -> actualizarAlumno(usr);
    };
  }

  /**
   * Actualiza un Usuario {@code A} con los contenidos de otro Usuario
   * {@code B}. Solo los campos que tiene sentido que se puedan editar en un
   * ADMINISTRADOR.
   *
   * @param usr
   * El Usuario {@code B} desde el cual se van a extraer los nuevos valores.
   *
   * @return
   * El objeto Usuario {@code A} actualizado.
   */
  public Usuario actualizarAdmin (Usuario usr) {
    return actualizarCamposBasicos(usr);
  }

  /**
   * Actualiza un Usuario {@code A} con los contenidos de otro Usuario
   * {@code B}. Solo los campos que tiene sentido que se puedan editar en un
   * ORGANIZADOR.
   *
   * @param usr
   * El Usuario {@code B} desde el cual se van a extraer los nuevos valores.
   *
   * @return
   * El objeto Usuario {@code A} actualizado.
   */
  public Usuario actualizarOrganizador (Usuario usr) {
    return actualizarCamposBasicos(usr);
  }

  /**
   * Actualiza un Usuario {@code A} con los contenidos de otro Usuario
   * {@code B}. Solo los campos que tiene sentido que se puedan editar en un
   * STAFF.
   *
   * @param usr
   * El Usuario {@code B} desde el cual se van a extraer los nuevos valores.
   *
   * @return
   * El objeto Usuario {@code A} actualizado.
   */
  public Usuario actualizarStaff (Usuario usr) {
    return actualizarCamposBasicosDeStaff(
      actualizarCamposBasicos(usr));
  }

  /**
   * Actualiza un Usuario {@code A} con los contenidos de otro Usuario
   * {@code B}. Solo los campos que tiene sentido que se puedan editar en un
   * ALUMNO.
   *
   * @param usr
   * El Usuario {@code B} desde el cual se van a extraer los nuevos valores.
   *
   * @return
   * El objeto Usuario {@code A} actualizado.
   */
  public Usuario actualizarAlumno (Usuario usr) {
    return actualizarCamposBasicosDeAlumno(
      actualizarCamposBasicos(usr));
  }



  // SECCION DE ACTUALIZACIONES AL USUARIO PROPIO ------------------------------

  /**
   * Permite a un USUARIO actualizar sus datos.
   * <p>
   * Solo se permite a cada USUARIO segun su ROL, editar ciertos campos.
   *
   * @param usr
   * El objeto tipo Usuario con los nuevos valores.
   *
   * @return
   * El Usuario actualizado.
   */
  public Usuario actualizarse (Usuario usr) {
    return switch (getRol()) {
      case Rol.ADMINISTRADOR -> actualizarseAdmin(usr);
      case Rol.ORGANIZADOR   -> actualizarseOrganizador(usr);
      case Rol.STAFF         -> actualizarseStaff(usr);
      case Rol.ALUMNO        -> actualizarseAlumno(usr);
    };
  }

  /**
   * Permite a un ADMINISTRADOR actualizar su Usuario.
   *
   * @param usr
   * El objeto tipo Usuario con los nuevos valores.
   *
   * @return
   * El Usuario actualizado.
   */
  public Usuario actualizarseAdmin (Usuario usr) {
    return actualizarCamposBasicos(usr);
  }

  /**
   * Permite a un ORGANIZADOR actualizar su Usuario.
   *
   * @param usr
   * El objeto tipo Usuario con los nuevos valores.
   *
   * @return
   * El Usuario actualizado.
   */
  public Usuario actualizarseOrganizador (Usuario usr) {
    return actualizarCamposBasicos(usr);
  }

  /**
   * Permite a un STAFF actualizar su Usuario.
   *
   * @param usr
   * El objeto tipo Usuario con los nuevos valores.
   *
   * @return
   * El Usuario actualizado.
   */
  public Usuario actualizarseStaff (Usuario usr) {
    return actualizarCamposBasicos(usr);
  }

  /**
   * Permite a un ALUMNO actualizar su Usuario.
   *
   * @param usr
   * El objeto tipo Usuario con los nuevos valores.
   *
   * @return
   * El Usuario actualizado.
   */
  public Usuario actualizarseAlumno (Usuario usr) {
    return actualizarCamposBasicosDeAlumno(
      actualizarCamposBasicos(usr));
  }



  // SECCION DE ACTUALIZACIONES AGNOSTICAS A USUARIOS --------------------------

  /**
   * Permite actualizar los campos especificos de un USUARIO,
   * ya sea que lo edite alguien mas o que el USUARIO se edite a si mismo.
   *
   * @param usr
   * El objeto tipo Usuario con los nuevos valores.
   *
   * @return
   * El USUARIO actualizado.
   */
  private Usuario actualizarCamposBasicos (Usuario usr) {
    setTelPref(usr.getTelPref());
    setTelSuf(usr.getTelSuf());

    setNombre(usr.getNombre());
    setApellidoPaterno(usr.getApellidoPaterno());
    setApellidoMaterno(usr.getApellidoMaterno());
    setFechaNacimiento(usr.getFechaNacimiento());

    return this;
  }

  /**
   * Permite actualizar los campos especificos de un Usuario tipo ALUMNO,
   * ya sea que lo edite alguien mas o que el ALUMNO se edite a si mismo.
   *
   * @param usr
   * El objeto tipo Usuario con los nuevos valores.
   *
   * @return
   * El ALUMNO actualizado.
   */
  private Usuario actualizarCamposBasicosDeAlumno (Usuario usr) {
    setNoControl(usr.getNoControl());
    setCodigoCarrera(usr.getCodigoCarrera());
    setSemestre(usr.getSemestre());
    setGrupo(usr.getGrupo());
    setExterno(usr.isExterno());
    setCurp(usr.getCurp());
    setEmailInstitucional(usr.getEmailInstitucional());

    return this;
  }

  /**
   * Permite actualizar los campos especificos de un Usuario tipo STAFF,
   * ya sea que lo edite alguien mas o que el STAFF se edite a si mismo.
   *
   * @param usr
   * El objeto tipo Usuario con los nuevos valores.
   *
   * @return
   * El STAFF actualizado.
   */
  private Usuario actualizarCamposBasicosDeStaff (Usuario usr) {
    setStaffResponsabilidades(usr.getStaffResponsabilidades());
    setStaffAutorizado(usr.isStaffAutorizado());
    setStaffCustodio(usr.isStaffCustodio());
    setStaffAlumnos(usr.isStaffAlumnos());
    setStaffInscripciones(usr.isStaffInscripciones());

    return this;
  }



  /**
   * Consulta el nombre completo del USUARIO.
   *
   * @return
   * El nombre completo.
   */
  @JsonIgnore
  public String getNombreCompleto () {
    return nombre
      + (Objects.nonNull(apellidoPaterno) ? " " + apellidoPaterno : "")
      + (Objects.nonNull(apellidoMaterno) ? " " + apellidoMaterno : "");
  }



  //----------------------------------------------------------------------------
  // SECCION USER DETAILS.

  @JsonIgnore
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();

    authorities.add(new SimpleGrantedAuthority(
      "ROLE_" + this.rol.toString()));

    if (this.rol == Rol.STAFF) {
      if (this.isStaffAutorizado()) {
        authorities.add(new SimpleGrantedAuthority("STAFF_AUTORIZADO"));
      }
      if (this.isStaffCustodio()) {
        authorities.add(new SimpleGrantedAuthority("STAFF_CUSTODIO"));
      }
      if (this.isStaffAlumnos()) {
        authorities.add(new SimpleGrantedAuthority("STAFF_ALUMNOS"));
      }
      if (this.isStaffInscripciones()) {
        authorities.add(new SimpleGrantedAuthority("STAFF_INSCRIPCIONES"));
      }
    }

    return authorities;
  }

  @JsonIgnore
  @Override
  public String getUsername() {
    return getEmail();
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return !isExpirado();
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return !isBloqueado();
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return !isCredencialesExpiradas();
  }

  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return !isDeshabilitado();
  }
}
