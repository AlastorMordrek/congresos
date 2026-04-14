package com.tecn.tijuana.congresos.eventos.conferencia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tecn.tijuana.congresos.eventos.conferencia.dto.RegistroConferenciaDto;
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
 * Clase que representa la entidad de CONFERENCIA en el sistema y en la BD.
 * */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conferencia {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  public static final int DURACION_MIN = 60 * 60; // 1h
  public static final int DURACION_MAX = 8 * 60 * 60; // 8h

  public static final boolean PUBLICADA = true;
  public static final boolean RETRACTADA = false;

  public static final boolean CANCELADA = true;
  public static final boolean RESTAURADA = false;



  /**
   * Identificador unico del registro.
   * */
  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "conferencia_sequence"
  )
  @SequenceGenerator(
    name = "conferencia_sequence",
    sequenceName = "conferencia_sequence",
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
   * CONGRESO donde tendra lugar la CONFERENCIA.
   * */
  @Column(nullable = false, updatable = false)
  private Long congresoId;



  /**
   * Nombre de la CONFERENCIA.
   * */
  @NotBlank(message = "Nombre vacio")
  @Size(min = 1, max = 100,
    message = "El nombre debe tener entre 1 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String nombre;

  /**
   * Descripcion corta de la CONFERENCIA.
   * */
  @Size(max = 100,
    message = "El resumen debe tener entre 0 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String resumen = "";

  /**
   * Descripcion detallada de la CONFERENCIA.
   * */
  @Size(max = 500,
    message = "La descripcion debe tener entre 0 y 500 caracteres")
  @Column(nullable = false, length = 500)
  private String descripcion = "";

  /**
   * Direccion donde tendra lugar la CONFERENCIA.
   * */
  @Size(max = 100,
    message = "La sala debe tener entre 0 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String sala = "";



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
   * Determina si la CONFERENCIA ha sido publicada, es decir, esta disponible
   * para que el publico general la vea y pueda inscribirse.
   * */
  private boolean publicada = false;

  /**
   * Determina si la CONFERENCIA ha sido cancelada y ya no se llevara a cabo.
   * */
  private boolean cancelada = false;



  /**
   * Cuantos espacios para inscripciones hay para la CONFERENCIA.
   * */
  @Min(0) @Max(5000)
  @Column(nullable = false)
  private int cupo = 0;

  /**
   * Cuantos ALUMNOS han sido inscritos a la CONFERENCIA.
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
   * Cuantos integrantes de STAFF se requeriran.
   * */
  @Min(0) @Max(100)
  @Column(nullable = false)
  private int staffCantidad = 0;

  /**
   * Cuantos integrantes de STAFF se requeriran.
   * */
  @Size(max = 500,
    message = "La descripcion de requerimientos de staff debe ser menor" +
      " o igual a 500 caracteres")
  @Column(nullable = false, length = 500)
  private String staffRequerimientos = "";



  /**
   * Nombre del conferencista a cargo.
   * */
  @NotBlank(message = "Nombre de conferencista vacio")
  @Size(min = 1, max = 100,
    message = "El nombre de conferencista debe tener entre 1 y 100 caracteres")
  @Column(length = 100)
  private String conferencistaNombre;

  /**
   * Email del conferencista a cargo.
   * */
  @NotBlank(message = "Email de conferencista vacio")
  @Size(min = 1, max = 100,
    message = "El email de conferencista debe tener entre 1 y 100 caracteres")
  @Column(length = 100)
  private String conferencistaEmail;

  /**
   * Codigo pais del telefono del conferencista a cargo.
   * */
  @NotBlank(message = "Prefijo de telefono de conferencista vacio")
  @Size(min = 1, max = 7,
    message = "El prefijo de telefono de conferencista debe tener entre" +
      " 1 y 7 caracteres")
  @Column(length = 7)
  private String conferencistaTelPref;

  /**
   * Numero nacional del telefono del conferencista a cargo.
   * */
  @NotBlank(message = "Numero de telefono de conferencista vacio")
  @Size(min = 4, max = 14,
    message = "El telefono de conferencista debe tener entre" +
      " 4 y 14 caracteres")
  @Column(length = 14)
  private String conferencistaTelSuf;

  /**
   * Semblanza del conferencista a cargo.
   * */
  @NotBlank(message = "Semblanza de conferencista vacia")
  @Size(min = 1, max = 200,
    message = "La semblanza del conferencista debe tener entre" +
      " 1 y 200 caracteres")
  @Column(length = 200)
  private String conferencistaSemblanza;

  //----------------------------------------
  /**
   * Tipo de archivo multimedia de la foto del conferencista.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String conferencistaFotoMimeType;

  /**
   * Contenido crudo de la foto del conferencista.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] conferencistaFotoImgData;

  //----------------------------------------
  /**
   * Tipo de archivo multimedia del banner del conferencista.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String conferencistaBannerMimeType;

  /**
   * Contenido crudo del banner del conferencista.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] conferencistaBannerImgData;

  //----------------------------------------
  /**
   * Tipo de archivo multimedia del logo de empresa del conferencista.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String conferencistaLogoEmpresaMimeType;

  /**
   * Contenido crudo del logo de empresa del conferencista.
   * */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @JsonIgnore
  @Size(min = 1024, max = 2 * 1024 * 1024,
    message = "El archivo debe pesar entre 1 KB y 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] conferencistaLogoEmpresaImgData;



  /**
   * Slot de multimedia informativa previa al evento.
   * <p>
   * Tipo de archivo multimedia de la foto 1.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String media1MimeType;

  /**
   * Slot de multimedia informativa previa al evento.
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
   * Slot de multimedia informativa previa al evento.
   * <p>
   * Tipo de archivo multimedia de la foto 2.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String media2MimeType;

  /**
   * Slot de multimedia informativa previa al evento.
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
   * Slot de multimedia informativa previa al evento.
   * <p>
   * Tipo de archivo multimedia de la foto 3.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String media3MimeType;

  /**
   * Slot de multimedia informativa previa al evento.
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
   * Slot de multimedia informativa previa al evento.
   * <p>
   * Tipo de archivo multimedia de la foto 4.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String media4MimeType;

  /**
   * Slot de multimedia informativa previa al evento.
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
   * Slot de multimedia informativa previa al evento.
   * <p>
   * Tipo de archivo multimedia de la foto 5.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String media5MimeType;

  /**
   * Slot de multimedia informativa previa al evento.
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
   * Slot de multimedia informativa previa al evento.
   * <p>
   * Tipo de archivo multimedia de la foto 6.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String media6MimeType;

  /**
   * Slot de multimedia informativa previa al evento.
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
   * Slot de multimedia para subir durante o despues del evento.
   * <p>
   * Tipo de archivo multimedia de la foto 1.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String mediaEvt1MimeType;

  /**
   * Slot de multimedia para subir durante o despues del evento.
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
   * Slot de multimedia para subir durante o despues del evento.
   * <p>
   * Tipo de archivo multimedia de la foto 2.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String mediaEvt2MimeType;

  /**
   * Slot de multimedia para subir durante o despues del evento.
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
   * Slot de multimedia para subir durante o despues del evento.
   * <p>
   * Tipo de archivo multimedia de la foto 3.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String mediaEvt3MimeType;

  /**
   * Slot de multimedia para subir durante o despues del evento.
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
   * Slot de multimedia para subir durante o despues del evento.
   * <p>
   * Tipo de archivo multimedia de la foto 4.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String mediaEvt4MimeType;

  /**
   * Slot de multimedia para subir durante o despues del evento.
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
   * Slot de multimedia para subir durante o despues del evento.
   * <p>
   * Tipo de archivo multimedia de la foto 5.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String mediaEvt5MimeType;

  /**
   * Slot de multimedia para subir durante o despues del evento.
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
   * Slot de multimedia para subir durante o despues del evento.
   * <p>
   * Tipo de archivo multimedia de la foto 6.
   * */
  @JsonIgnore
  @Size(min = 1, max = 100,
    message = "El tipo de archivo debe tener maximo 100 caracteres")
  @Column(length = 100)
  private String mediaEvt6MimeType;

  /**
   * Slot de multimedia para subir durante o despues del evento.
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
   * Funcion constructora alternativa para crear Conferencias.
   */
  private Conferencia (
    Long creadorId,
    Long congresoId,

    String nombre,
    String resumen,
    String descripcion,
    String sala,

    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,

    int cupo,
    int staffCantidad,
    String staffRequerimientos,

    String conferencistaNombre,
    String conferencistaEmail,
    String conferencistaTelPref,
    String conferencistaTelSuf,
    String conferencistaSemblanza
  ) {
    this.fechaCreacion            = LocalDateTime.now();
    this.creadorId                = creadorId;
    this.congresoId               = congresoId;
    this.nombre                   = nombre;
    this.resumen                  = resumen;
    this.descripcion              = descripcion;
    this.sala                     = sala;
    this.fechaInicio              = fechaInicio;
    this.fechaFin                 = fechaFin;
    this.cupo                     = cupo;
    this.staffCantidad            = staffCantidad;
    this.staffRequerimientos      = staffRequerimientos;
    this.conferencistaNombre      = conferencistaNombre;
    this.conferencistaEmail       = conferencistaEmail;
    this.conferencistaTelPref     = conferencistaTelPref;
    this.conferencistaTelSuf      = conferencistaTelSuf;
    this.conferencistaSemblanza   = conferencistaSemblanza;
  }



  /**
   * Funcion constructora alternativa para crear Conferencias.
   *
   * @return
   * La nueva Conferencia.
   */
  public static Conferencia nueva (
    Long creadorId,
    RegistroConferenciaDto dto
  ) {
    return new Conferencia(
      creadorId,
      dto.getCongresoId(),
      dto.getNombre(),
      dto.getResumen(),
      dto.getDescripcion(),
      dto.getSala(),
      dto.getFechaInicio(),
      dto.getFechaFin(),
      dto.getCupo(),
      dto.getStaffCantidad(),
      dto.getStaffRequerimientos(),
      dto.getConferencistaNombre(),
      dto.getConferencistaEmail(),
      dto.getConferencistaTelPref(),
      dto.getConferencistaTelSuf(),
      dto.getConferencistaSemblanza()
    );
  }

  /**
   * Funcion constructora alternativa para crear Conferencias.
   *
   * @return
   * La nueva Conferencia.
   */
  public static Conferencia nueva (Conferencia reg) {
    return new Conferencia(
      reg.getCreadorId(),
      reg.getCongresoId(),
      reg.getNombre(),
      reg.getResumen(),
      reg.getDescripcion(),
      reg.getSala(),
      reg.getFechaInicio(),
      reg.getFechaFin(),
      reg.getCupo(),
      reg.getStaffCantidad(),
      reg.getStaffRequerimientos(),
      reg.getConferencistaNombre(),
      reg.getConferencistaEmail(),
      reg.getConferencistaTelPref(),
      reg.getConferencistaTelSuf(),
      reg.getConferencistaSemblanza()
    );
  }



  /**
   * Actualiza una Conferencia en la BD con la informacion del objeto de
   * Conferencia provisto.
   *
   * @param reg
   * El objeto de Conferencia con los nuevos valores.
   *
   * @return
   * El registro actualizado.
   */
  public Conferencia actualizar (Conferencia reg) {

    setNombre(reg.getNombre());
    setResumen(reg.getResumen());
    setDescripcion(reg.getDescripcion());
    setSala(reg.getSala());

    setFechaInicio(reg.getFechaInicio());
    setFechaFin(reg.getFechaFin());

    setPublicada(reg.isPublicada());
    setCancelada(reg.isCancelada());

    setCupo(reg.getCupo());

    setStaffCantidad(reg.getStaffCantidad());
    setStaffRequerimientos(reg.getStaffRequerimientos());

    setConferencistaNombre(reg.getConferencistaNombre());
    setConferencistaEmail(reg.getConferencistaEmail());
    setConferencistaTelPref(reg.getConferencistaTelPref());
    setConferencistaTelSuf(reg.getConferencistaTelSuf());
    setConferencistaSemblanza(reg.getConferencistaSemblanza());

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
  public Conferencia setMedia (
    String slot, MultipartFile img
  )
    throws IOException {

    if (Objects.isNull(img)) {
      switch (slot) {

        case "conferencistaFoto":
          setConferencistaFotoMimeType(null);
          setConferencistaFotoImgData(null);
          break;

        case "conferencistaBanner":
          setConferencistaBannerMimeType(null);
          setConferencistaBannerImgData(null);
          break;

        case "conferencistaLogoEmpresa":
          setConferencistaLogoEmpresaMimeType(null);
          setConferencistaLogoEmpresaImgData(null);
          break;

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

        case "conferencistaFoto":
          setConferencistaFotoMimeType(mime);
          setConferencistaFotoImgData(bytes);
          break;

        case "conferencistaBanner":
          setConferencistaBannerMimeType(mime);
          setConferencistaBannerImgData(bytes);
          break;

        case "conferencistaLogoEmpresa":
          setConferencistaLogoEmpresaMimeType(mime);
          setConferencistaLogoEmpresaImgData(bytes);
          break;

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
  public Conferencia sumarAsistencia () {

    setAsistencias(getAsistencias() + 1);

    return this;
  }
}
