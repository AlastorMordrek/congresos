package com.tecn.tijuana.congresos.eventos.congreso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tecn.tijuana.congresos.eventos.congreso.dto.RegistroCongresoDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase que representa la entidad de CONGRESO en el sistema y en la BD.
 * */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Congreso {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  public static final int DURACION_MIN = 60 * 60; // 1h
  public static final int DURACION_MAX = 7 * 24 * 60 * 60; // 7d

  public static final int INSCRIPCIONES_DURACION_MIN = 60 * 60; // 1h
  public static final int INSCRIPCIONES_DURACION_MAX = 30 * 24 * 60 * 60; // 30d

  public static final boolean PUBLICADO = true;
  public static final boolean RETRACTADO = false;

  public static final boolean CANCELADO = true;
  public static final boolean RESTAURADO = false;

  public static final boolean GRATUITO = true;
  public static final boolean NO_GRATUITO = false;



  /**
   * Identificador unico del registro.
   * */
  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "congreso_sequence"
  )
  @SequenceGenerator(
    name = "congreso_sequence",
    sequenceName = "congreso_sequence",
    allocationSize = 1
  )
  @Column(nullable = false, updatable = false)
  private Long id;



  /**
   * Cuando fue creado el registro.
   * */
  @PastOrPresent(message = "La fecha de creacion no puede ser en el futuro")
  @Column(nullable = false, updatable = false)
  private LocalDateTime fechaCreacion;

  /**
   * Posible creador del registro.
   * */
  @Column(updatable = false)
  private Long creadorId;



  /**
   * ORGANIZADOR asignado encargado del CONGRESO.
   * */
  @Column
  private Long organizadorId;



  /**
   * Nombre del CONGRESO.
   * */
  @NotBlank(message = "Nombre vacio")
  @Size(min = 1, max = 100,
    message = "El nombre debe tener entre 1 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String nombre;

  /**
   * Descripcion corta del CONGRESO.
   * */
  @Size(max = 100,
    message = "El resumen debe tener entre 0 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String resumen = "";

  /**
   * Descripcion detallada del CONGRESO.
   * */
  @Size(max = 500,
    message = "La descripcion debe tener entre 0 y 500 caracteres")
  @Column(nullable = false, length = 500)
  private String descripcion = "";

  /**
   * Direccion donde tendra lugar el CONGRESO.
   * */
  @Size(max = 200,
    message = "La direccion debe tener entre 0 y 200 caracteres")
  @Column(nullable = false, length = 200)
  private String direccion = "";



  /**
   * Cuando iniciara el evento.
   * */
  @NotNull(message = "Debe especificar una fecha de inicio")
  @Column(nullable = false)
  private LocalDateTime fechaInicio;

  /**
   * Cuando concluira el evento.
   * */
  @NotNull(message = "Debe especificar una fecha de terminacion")
  @Column(nullable = false)
  private LocalDateTime fechaFin;

  /**
   * Cuando inicia el periodo de inscripciones para el CONGRESO.
   * <p>
   * Los ALUMNOS solo pueden inscribirse dentro del periodo de inscripciones.
   * <p>
   * ORGANIZADORES y STAFF pueden inscirbir ALUMNOS fuera del periodo de
   * inscripciones, siempre y cuando el CONGRESO no haya concluido aun.
   * */
  @NotNull(message = "Debe especificar una fecha de inicio de inscripciones")
  @Column(nullable = false)
  private LocalDateTime inscripcionesFechaInicio;

  /**
   * Cuando termina el periodo de inscripciones para el CONGRESO.
   * <p>
   * Los ALUMNOS solo pueden inscribirse dentro del periodo de inscripciones.
   * <p>
   * ORGANIZADORES y STAFF pueden inscirbir ALUMNOS fuera del periodo de
   * inscripciones, siempre
   * y cuando el CONGRESO no haya concluido aun.
   * */
  @NotNull(message = "Debe especificar una fecha de fin de inscripciones")
  @Column(nullable = false)
  private LocalDateTime inscripcionesFechaFin;



  /**
   * Determina si el CONGRESO tiene costo. {@code true = No tiene costo}.
   * <p>
   * Cuando un CONGRESO tiene costo, los boletos se registraran como:
   * {@code pagado = false}, indicando que no podra ser usado para asistir al
   * evento hasta que el personal autorizado lo marque como:
   * {@code pagado = true} manualmente.
   * */
  private boolean gratuito = GRATUITO;



  /**
   * Determina si el CONGRESO ha sido publicado, es decir, esta disponible para
   * que el publico general lo vea y pueda inscribirse.
   * */
  private boolean publicado = false;

  /**
   * Determina si el CONGRESO ha sido cancelado y ya no se llevara a cabo.
   * */
  private boolean cancelado = false;



  /**
   * Cuantos espacios para inscripciones hay para el CONGRESO.
   * */
  @Min(0) @Max(5000)
  @Column(nullable = false)
  private int cupo = 0;

  /**
   * Cuantos ALUMNOS han sido inscritos al CONGRESO.
   * */
  @Min(0) @Max(5000)
  @Column(nullable = false)
  private int inscritos = 0;

  /**
   * Cuantos ALUMNOS asistieron al CONGRESO cuando sucedio.
   * */
  @Min(0) @Max(5000)
  @Column(nullable = false)
  private int asistencias = 0;



  /**
   * Cuantos integrantes de STAFF se requeriran en el CONGRESO.
   * */
  @Min(0) @Max(100)
  @Column(nullable = false)
  private int staffCantidad = 0;

  /**
   * Cuantos integrantes de STAFF se requeriran en el CONGRESO.
   * */
  @Size(max = 500,
    message = "La descripcion de requerimientos de staff debe ser menor" +
      " o igual a 500 caracteres")
  @Column(nullable = false, length = 500)
  private String staffRequerimientos = "";



  /**
   * Cuantas ASISTENCIAS distintas requiere el ALUMNO para conseguir su
   * acreditacion.
   * */
  @Min(value = 1, message = "Se debe requerir al menos una Asistencia")
  @Max(value = 40, message = "Se deben requerir como maximo 40 Asistencias")
  private int alumnoAcreditacionAsistenciasRequeridas = 1;

  /**
   * Cuanto Tiempo Asistido total requiere el ALUMNO para conseguir su
   * acreditacion (expresado en segundos).
   * <p>
   * Se refiere al tiempo total que paso en todas las CONFERENCIAS a las que
   * asistio.
   * */
  @Min(value = 1,
    message = "Se debe requerir al menos 1 segundo de tiempo asistido")
  @Max(value = 144000,
    message = "Se deben requerir como maximo 40 horas de tiempo asistido")
  private long alumnoAcreditacionTiempoAsistidoRequerido = 1;



  //--------------------------- CAMPOS MULTIMEDIA ------------------------------

  /**
   * Slot de multimedia informativa previa al CONGRESO.
   * <p>
   * Tipo de archivo multimedia de la foto 1.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String media1MimeType;

  /**
   * Slot de multimedia informativa previa al CONGRESO.
   * <p>
   * Contenido crudo de la foto 1.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] media1ImgData;



  /**
   * Slot de multimedia informativa previa al CONGRESO.
   * <p>
   * Tipo de archivo multimedia de la foto 2.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String media2MimeType;

  /**
   * Slot de multimedia informativa previa al CONGRESO.
   * <p>
   * Contenido crudo de la foto 2.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] media2ImgData;



  /**
   * Slot de multimedia informativa previa al CONGRESO.
   * <p>
   * Tipo de archivo multimedia de la foto 3.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String media3MimeType;

  /**
   * Slot de multimedia informativa previa al CONGRESO.
   * <p>
   * Contenido crudo de la foto 3.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] media3ImgData;



  /**
   * Slot de multimedia informativa previa al CONGRESO.
   * <p>
   * Tipo de archivo multimedia de la foto 4.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String media4MimeType;

  /**
   * Slot de multimedia informativa previa al CONGRESO.
   * <p>
   * Contenido crudo de la foto 4.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] media4ImgData;



  /**
   * Slot de multimedia informativa previa al CONGRESO.
   * <p>
   * Tipo de archivo multimedia de la foto 5.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String media5MimeType;

  /**
   * Slot de multimedia informativa previa al CONGRESO.
   * <p>
   * Contenido crudo de la foto 5.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] media5ImgData;



  /**
   * Slot de multimedia informativa previa al CONGRESO.
   * <p>
   * Tipo de archivo multimedia de la foto 6.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String media6MimeType;

  /**
   * Slot de multimedia informativa previa al CONGRESO.
   * <p>
   * Contenido crudo de la foto 6.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] media6ImgData;



  /**
   * Slot de multimedia para subir durante o despues del CONGRESO.
   * <p>
   * Tipo de archivo multimedia de la foto 1.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String mediaEvt1MimeType;

  /**
   * Slot de multimedia para subir durante o despues del CONGRESO.
   * <p>
   * Contenido crudo de la foto 1.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] mediaEvt1ImgData;



  /**
   * Slot de multimedia para subir durante o despues del CONGRESO.
   * <p>
   * Tipo de archivo multimedia de la foto 2.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String mediaEvt2MimeType;

  /**
   * Slot de multimedia para subir durante o despues del CONGRESO.
   * <p>
   * Contenido crudo de la foto 2.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] mediaEvt2ImgData;



  /**
   * Slot de multimedia para subir durante o despues del CONGRESO.
   * <p>
   * Tipo de archivo multimedia de la foto 3.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String mediaEvt3MimeType;

  /**
   * Slot de multimedia para subir durante o despues del CONGRESO.
   * <p>
   * Contenido crudo de la foto 3.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] mediaEvt3ImgData;



  /**
   * Slot de multimedia para subir durante o despues del CONGRESO.
   * <p>
   * Tipo de archivo multimedia de la foto 4.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String mediaEvt4MimeType;

  /**
   * Slot de multimedia para subir durante o despues del CONGRESO.
   * <p>
   * Contenido crudo de la foto 4.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] mediaEvt4ImgData;



  /**
   * Slot de multimedia para subir durante o despues del CONGRESO.
   * <p>
   * Tipo de archivo multimedia de la foto 5.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String mediaEvt5MimeType;

  /**
   * Slot de multimedia para subir durante o despues del CONGRESO.
   * <p>
   * Contenido crudo de la foto 5.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] mediaEvt5ImgData;



  /**
   * Slot de multimedia para subir durante o despues del CONGRESO.
   * <p>
   * Tipo de archivo multimedia de la foto 6.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String mediaEvt6MimeType;

  /**
   * Slot de multimedia para subir durante o despues del CONGRESO.
   * <p>
   * Contenido crudo de la foto 6.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] mediaEvt6ImgData;



  /**
   * Funcion constructora alternativa para crear Congresos.
   */
  private Congreso (
    Long creadorId,
    Long organizadorId,
    String nombre,
    String resumen,
    String descripcion,
    String direccion,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    LocalDateTime inscripcionesFechaInicio,
    LocalDateTime inscripcionesFechaFin,
    boolean gratuito,
    int cupo,
    int staffCantidad,
    String staffRequerimientos,
    int alumnoAcreditacionAsistenciasRequeridas,
    long alumnoAcreditacionTiempoAsistidoRequerido
  ) {
    this.fechaCreacion            = LocalDateTime.now();
    this.creadorId                = creadorId;

    this.organizadorId            = organizadorId;

    this.nombre                   = nombre;
    this.resumen                  = resumen;
    this.descripcion              = descripcion;
    this.direccion                = direccion;

    this.fechaInicio              = fechaInicio;
    this.fechaFin                 = fechaFin;
    this.inscripcionesFechaInicio = inscripcionesFechaInicio;
    this.inscripcionesFechaFin    = inscripcionesFechaFin;

    this.gratuito                 = gratuito;
    this.cupo                     = cupo;

    this.staffCantidad            = staffCantidad;
    this.staffRequerimientos      = staffRequerimientos;

    this.alumnoAcreditacionAsistenciasRequeridas =
      alumnoAcreditacionAsistenciasRequeridas;
    this.alumnoAcreditacionTiempoAsistidoRequerido =
      alumnoAcreditacionTiempoAsistidoRequerido;
  }



  /**
   * Funcion constructora alternativa para crear Congresos.
   *
   * @return
   * El nuevo Congreso.
   */
  public static Congreso nuevo (
    Long creadorId,
    RegistroCongresoDto dto
  ) {
    return new Congreso(
      creadorId,
      creadorId,
      dto.getNombre(),
      dto.getResumen(),
      dto.getDescripcion(),
      dto.getDireccion(),
      dto.getFechaInicio(),
      dto.getFechaFin(),
      dto.getInscripcionesFechaInicio(),
      dto.getInscripcionesFechaFin(),
      dto.isGratuito(),
      dto.getCupo(),
      dto.getStaffCantidad(),
      dto.getStaffRequerimientos(),
      dto.getAlumnoAcreditacionAsistenciasRequeridas(),
      dto.getAlumnoAcreditacionTiempoAsistidoRequerido()
    );
  }

  /**
   * Funcion constructora alternativa para crear Congresos.
   *
   * @return
   * El nuevo Congreso.
   */
  public static Congreso nuevo (Congreso con) {
    return new Congreso(
      con.getCreadorId(),
      con.getOrganizadorId(),
      con.getNombre(),
      con.getResumen(),
      con.getDescripcion(),
      con.getDireccion(),
      con.getFechaInicio(),
      con.getFechaFin(),
      con.getInscripcionesFechaInicio(),
      con.getInscripcionesFechaFin(),
      con.isGratuito(),
      con.getCupo(),
      con.getStaffCantidad(),
      con.getStaffRequerimientos(),
      con.getAlumnoAcreditacionAsistenciasRequeridas(),
      con.getAlumnoAcreditacionTiempoAsistidoRequerido()
    );
  }



  /**
   * Actualiza un Congreso en la BD con la informacion del objeto de Congreso
   * provisto.
   *
   * @param con
   * El Congreso desde el cual se van a extraer los nuevos valores.
   *
   * @return
   * El registro actualizado.
   */
  public Congreso actualizar (Congreso con) {

    setNombre(con.getNombre());
    setResumen(con.getResumen());
    setDescripcion(con.getDescripcion());
    setDireccion(con.getDireccion());

    setFechaInicio(con.getFechaInicio());
    setFechaFin(con.getFechaFin());

    setPublicado(con.isPublicado());
    setCancelado(con.isCancelado());

    setInscripcionesFechaInicio(con.getInscripcionesFechaInicio());
    setInscripcionesFechaFin(con.getInscripcionesFechaFin());

    setGratuito(con.isGratuito());

    setCupo(con.getCupo());

    setStaffCantidad(con.getStaffCantidad());
    setStaffRequerimientos(con.getStaffRequerimientos());

    setAlumnoAcreditacionAsistenciasRequeridas(
      con.getAlumnoAcreditacionAsistenciasRequeridas());
    setAlumnoAcreditacionTiempoAsistidoRequerido(
      con.getAlumnoAcreditacionTiempoAsistidoRequerido());

    return this;
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
  public Congreso setMedia (
    String slot, MultipartFile img
  )
    throws IOException {

    if (Objects.isNull(img)) {
      switch (slot) {

        case "media1":
          setMedia1MimeType(null);
          setMedia1ImgData(null);
          break;

        case "media2":
          setMedia2MimeType(null);
          setMedia2ImgData(null);
          break;

        case "media3":
          setMedia3MimeType(null);
          setMedia3ImgData(null);
          break;

        case "media4":
          setMedia4MimeType(null);
          setMedia4ImgData(null);
          break;

        case "media5":
          setMedia5MimeType(null);
          setMedia5ImgData(null);
          break;

        case "media6":
          setMedia6MimeType(null);
          setMedia6ImgData(null);
          break;

        case "mediaEvt1":
          setMediaEvt1MimeType(null);
          setMediaEvt1ImgData(null);
          break;

        case "mediaEvt2":
          setMediaEvt2MimeType(null);
          setMediaEvt2ImgData(null);
          break;

        case "mediaEvt3":
          setMediaEvt3MimeType(null);
          setMediaEvt3ImgData(null);
          break;

        case "mediaEvt4":
          setMediaEvt4MimeType(null);
          setMediaEvt4ImgData(null);
          break;

        case "mediaEvt5":
          setMediaEvt5MimeType(null);
          setMediaEvt5ImgData(null);
          break;

        case "mediaEvt6":
          setMediaEvt6MimeType(null);
          setMediaEvt6ImgData(null);
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

        case "media1":
          setMedia1MimeType(mime);
          setMedia1ImgData(bytes);
          break;

        case "media2":
          setMedia2MimeType(mime);
          setMedia2ImgData(bytes);
          break;

        case "media3":
          setMedia3MimeType(mime);
          setMedia3ImgData(bytes);
          break;

        case "media4":
          setMedia4MimeType(mime);
          setMedia4ImgData(bytes);
          break;

        case "media5":
          setMedia5MimeType(mime);
          setMedia5ImgData(bytes);
          break;

        case "media6":
          setMedia6MimeType(mime);
          setMedia6ImgData(bytes);
          break;

        case "mediaEvt1":
          setMediaEvt1MimeType(mime);
          setMediaEvt1ImgData(bytes);
          break;

        case "mediaEvt2":
          setMediaEvt2MimeType(mime);
          setMediaEvt2ImgData(bytes);
          break;

        case "mediaEvt3":
          setMediaEvt3MimeType(mime);
          setMediaEvt3ImgData(bytes);
          break;

        case "mediaEvt4":
          setMediaEvt4MimeType(mime);
          setMediaEvt4ImgData(bytes);
          break;

        case "mediaEvt5":
          setMediaEvt5MimeType(mime);
          setMediaEvt5ImgData(bytes);
          break;

        case "mediaEvt6":
          setMediaEvt6MimeType(mime);
          setMediaEvt6ImgData(bytes);
          break;

        default:
          break;
      }
    }
    return this;
  }



  /**
   * Incrementa el contador de asistencias.
   *
   * @return
   * El registro actualizado.
   */
  public Congreso sumarAsistencia () {

    setAsistencias(getAsistencias() + 1);

    return this;
  }



  /**
   * Incrementa el contador de inscripciones.
   *
   * @return
   * El registro actualizado.
   */
  public Congreso sumarInscripcion () {

    setInscritos(getInscritos() + 1);

    return this;
  }
}
