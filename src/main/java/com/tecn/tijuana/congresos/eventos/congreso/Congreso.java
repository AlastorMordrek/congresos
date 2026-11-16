package com.tecn.tijuana.congresos.eventos.congreso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tecn.tijuana.congresos.eventos.congreso.dto.RegistroCongresoDto;
import com.tecn.tijuana.congresos.utils.Texto;
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
  public static final int DURACION_MAX = 180 * 24 * 60 * 60; // 180d

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
   * Carrera a la que se limita el CONGRESO.
   * <p>
   * Clave oficial del catalogo de carreras del TecNM. Si es {@code null}, el
   * CONGRESO no tiene restriccion por carrera. La restriccion solo aplica al
   * inscribir ALUMNOS: los BOLETOS existentes no se revalidan y conservan su
   * acceso al evento.
   * */
  @Size(max = 13, message = "El codigo de carrera no puede exceder 13 caracteres")
  @Column(length = 13)
  private String codigoCarrera;



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



  //---------------------- CAMPOS DE LA CONSTANCIA -----------------------------

  /**
   * Creditos que otorga la constancia de acreditacion.
   * */
  @Min(value = 1, message = "La constancia debe otorgar al menos 1 credito")
  @Max(value = 20, message = "La constancia no puede otorgar mas de 20 creditos")
  private int constanciaCreditos = 1;

  /**
   * Nombre de la institucion que aparece en el encabezado de la constancia.
   * */
  @Size(max = 150,
    message = "El instituto de la constancia no puede exceder 150 caracteres")
  @Column(length = 150)
  private String constanciaInstituto;

  /**
   * Subtitulo oficial de la constancia.
   * */
  @Size(max = 150, message = "El subtitulo no puede exceder 150 caracteres")
  @Column(length = 150)
  private String constanciaSubtitulo;

  /**
   * Ciudad de emision de la constancia.
   * */
  @Size(max = 80, message = "La ciudad no puede exceder 80 caracteres")
  @Column(length = 80)
  private String constanciaCiudad;

  /**
   * Estado de emision de la constancia.
   * */
  @Size(max = 80, message = "El estado no puede exceder 80 caracteres")
  @Column(length = 80)
  private String constanciaEstado;

  /**
   * Pais de emision de la constancia.
   * */
  @Size(max = 80, message = "El pais no puede exceder 80 caracteres")
  @Column(length = 80)
  private String constanciaPais;

  /**
   * Nombre de la jefatura de servicios escolares a quien se dirige.
   * */
  @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
  @Column(length = 100)
  private String constanciaJefaNombre;

  /**
   * Cargo de la jefatura de servicios escolares.
   * */
  @Size(max = 150, message = "El cargo no puede exceder 150 caracteres")
  @Column(length = 150)
  private String constanciaJefaCargo;

  /**
   * Nombre de la persona que firma la constancia.
   * */
  @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
  @Column(length = 100)
  private String constanciaJefeNombre;

  /**
   * Departamento al que pertenece la persona que firma.
   * */
  @Size(max = 150, message = "El departamento no puede exceder 150 caracteres")
  @Column(length = 150)
  private String constanciaDepartamento;

  /**
   * Primer lema institucional.
   * */
  @Size(max = 150, message = "El lema no puede exceder 150 caracteres")
  @Column(length = 150)
  private String constanciaLema1;

  /**
   * Segundo lema institucional.
   * */
  @Size(max = 150, message = "El lema no puede exceder 150 caracteres")
  @Column(length = 150)
  private String constanciaLema2;

  /**
   * Texto del pie de la constancia.
   * */
  @Size(max = 500, message = "El pie no puede exceder 500 caracteres")
  @Column(length = 500)
  private String constanciaPieTexto;

  /**
   * Nota opcional de copia de correspondencia.
   * */
  @Size(max = 100, message = "La nota no puede exceder 100 caracteres")
  @Column(length = 100)
  private String constanciaNotaCcp;

  /**
   * Iniciales opcionales de la persona que firma y de quien captura.
   * */
  @Size(max = 50, message = "Las iniciales no pueden exceder 50 caracteres")
  @Column(length = 50)
  private String constanciaIniciales;

  /**
   * Actividad realizada, opcional.
   * */
  @Size(max = 200, message = "La actividad no puede exceder 200 caracteres")
  @Column(length = 200)
  private String constanciaActividad;

  /**
   * Nivel de desempeno, opcional.
   * */
  @Size(max = 100, message = "El nivel no puede exceder 100 caracteres")
  @Column(length = 100)
  private String constanciaNivelDesempeno;

  /** Tipo de archivo del logo 1 del pie de la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(name = "constancia_pie_img_1_mime_type", length = 100)
  private String constanciaPieImg1MimeType;

  /** Contenido crudo del logo 1 del pie de la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(name = "constancia_pie_img_1_data", length = 2 * 1024 * 1024)
  private byte[] constanciaPieImg1Data;
  /** Tipo de archivo del logo 2 del pie de la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(name = "constancia_pie_img_2_mime_type", length = 100)
  private String constanciaPieImg2MimeType;

  /** Contenido crudo del logo 2 del pie de la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(name = "constancia_pie_img_2_data", length = 2 * 1024 * 1024)
  private byte[] constanciaPieImg2Data;
  /** Tipo de archivo del logo 3 del pie de la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(name = "constancia_pie_img_3_mime_type", length = 100)
  private String constanciaPieImg3MimeType;

  /** Contenido crudo del logo 3 del pie de la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(name = "constancia_pie_img_3_data", length = 2 * 1024 * 1024)
  private byte[] constanciaPieImg3Data;
  /** Tipo de archivo del logo 4 del pie de la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(name = "constancia_pie_img_4_mime_type", length = 100)
  private String constanciaPieImg4MimeType;

  /** Contenido crudo del logo 4 del pie de la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(name = "constancia_pie_img_4_data", length = 2 * 1024 * 1024)
  private byte[] constanciaPieImg4Data;
  /** Tipo de archivo del logo 5 del pie de la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(name = "constancia_pie_img_5_mime_type", length = 100)
  private String constanciaPieImg5MimeType;

  /** Contenido crudo del logo 5 del pie de la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(name = "constancia_pie_img_5_data", length = 2 * 1024 * 1024)
  private byte[] constanciaPieImg5Data;
  /** Tipo de archivo del logo 6 del pie de la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(name = "constancia_pie_img_6_mime_type", length = 100)
  private String constanciaPieImg6MimeType;

  /** Contenido crudo del logo 6 del pie de la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(name = "constancia_pie_img_6_data", length = 2 * 1024 * 1024)
  private byte[] constanciaPieImg6Data;
  /** Tipo de archivo del logo 7 del pie de la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(name = "constancia_pie_img_7_mime_type", length = 100)
  private String constanciaPieImg7MimeType;

  /** Contenido crudo del logo 7 del pie de la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(name = "constancia_pie_img_7_data", length = 2 * 1024 * 1024)
  private byte[] constanciaPieImg7Data;
  /** Tipo de archivo del logo 8 del pie de la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(name = "constancia_pie_img_8_mime_type", length = 100)
  private String constanciaPieImg8MimeType;

  /** Contenido crudo del logo 8 del pie de la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(name = "constancia_pie_img_8_data", length = 2 * 1024 * 1024)
  private byte[] constanciaPieImg8Data;
  /** Tipo de archivo del logo 9 del pie de la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(name = "constancia_pie_img_9_mime_type", length = 100)
  private String constanciaPieImg9MimeType;

  /** Contenido crudo del logo 9 del pie de la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(name = "constancia_pie_img_9_data", length = 2 * 1024 * 1024)
  private byte[] constanciaPieImg9Data;
  /** Tipo de archivo del logo 10 del pie de la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(name = "constancia_pie_img_10_mime_type", length = 100)
  private String constanciaPieImg10MimeType;

  /** Contenido crudo del logo 10 del pie de la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(name = "constancia_pie_img_10_data", length = 2 * 1024 * 1024)
  private byte[] constanciaPieImg10Data;
  /** Tipo de archivo de la franja inferior de la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(length = 100)
  private String constanciaPieFranjaMimeType;

  /** Contenido crudo de la franja inferior de la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] constanciaPieFranjaData;

  /** Tipo de archivo de la imagen del anio en la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(length = 100)
  private String constanciaAnioMimeType;

  /** Contenido crudo de la imagen del anio en la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] constanciaAnioData;

  /** Tipo de archivo de la firma de la constancia. */
  @JsonIgnore
  @Size(max = 100, message = "El tipo de archivo no puede exceder 100 caracteres")
  @Column(length = 100)
  private String constanciaFirmaMimeType;

  /** Contenido crudo de la firma de la constancia. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Size(max = 2 * 1024 * 1024,
    message = "La imagen no puede pesar mas de 2 MB")
  @Column(length = 2 * 1024 * 1024)
  private byte[] constanciaFirmaData;

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
    String codigoCarrera,
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
    this.codigoCarrera            = codigoCarrera;

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
    var con = new Congreso(
      creadorId,
      creadorId,
      dto.getNombre(),
      dto.getResumen(),
      dto.getDescripcion(),
      dto.getDireccion(),
      Texto.blancoANull(dto.getCodigoCarrera()),
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

    return con.actualizarConstancia(dto);
  }

  /**
   * Funcion constructora alternativa para crear Congresos.
   *
   * @return
   * El nuevo Congreso.
   */
  public static Congreso nuevo (Congreso con) {
    var nuevo = new Congreso(
      con.getCreadorId(),
      con.getOrganizadorId(),
      con.getNombre(),
      con.getResumen(),
      con.getDescripcion(),
      con.getDireccion(),
      con.getCodigoCarrera(),
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

    nuevo.setConstanciaPieImg1MimeType(con.getConstanciaPieImg1MimeType());
    nuevo.setConstanciaPieImg1Data(con.getConstanciaPieImg1Data());
    nuevo.setConstanciaPieImg2MimeType(con.getConstanciaPieImg2MimeType());
    nuevo.setConstanciaPieImg2Data(con.getConstanciaPieImg2Data());
    nuevo.setConstanciaPieImg3MimeType(con.getConstanciaPieImg3MimeType());
    nuevo.setConstanciaPieImg3Data(con.getConstanciaPieImg3Data());
    nuevo.setConstanciaPieImg4MimeType(con.getConstanciaPieImg4MimeType());
    nuevo.setConstanciaPieImg4Data(con.getConstanciaPieImg4Data());
    nuevo.setConstanciaPieImg5MimeType(con.getConstanciaPieImg5MimeType());
    nuevo.setConstanciaPieImg5Data(con.getConstanciaPieImg5Data());
    nuevo.setConstanciaPieImg6MimeType(con.getConstanciaPieImg6MimeType());
    nuevo.setConstanciaPieImg6Data(con.getConstanciaPieImg6Data());
    nuevo.setConstanciaPieImg7MimeType(con.getConstanciaPieImg7MimeType());
    nuevo.setConstanciaPieImg7Data(con.getConstanciaPieImg7Data());
    nuevo.setConstanciaPieImg8MimeType(con.getConstanciaPieImg8MimeType());
    nuevo.setConstanciaPieImg8Data(con.getConstanciaPieImg8Data());
    nuevo.setConstanciaPieImg9MimeType(con.getConstanciaPieImg9MimeType());
    nuevo.setConstanciaPieImg9Data(con.getConstanciaPieImg9Data());
    nuevo.setConstanciaPieImg10MimeType(con.getConstanciaPieImg10MimeType());
    nuevo.setConstanciaPieImg10Data(con.getConstanciaPieImg10Data());
    nuevo.setConstanciaPieFranjaMimeType(con.getConstanciaPieFranjaMimeType());
    nuevo.setConstanciaPieFranjaData(con.getConstanciaPieFranjaData());
    nuevo.setConstanciaAnioMimeType(con.getConstanciaAnioMimeType());
    nuevo.setConstanciaAnioData(con.getConstanciaAnioData());

    return nuevo.actualizarConstancia(con);
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
    setCodigoCarrera(Texto.blancoANull(con.getCodigoCarrera()));

    setFechaInicio(con.getFechaInicio());
    setFechaFin(con.getFechaFin());

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

    return actualizarConstancia(con);
  }



  /**
   * Copia los campos de la constancia desde un DTO.
   *
   * @param dto
   * DTO con los valores de la constancia.
   *
   * @return
   * El registro actualizado.
   */
  private Congreso actualizarConstancia (RegistroCongresoDto dto) {
    setConstanciaCreditos(dto.constanciaCreditos);
    setConstanciaInstituto(Texto.blancoANull(dto.constanciaInstituto));
    setConstanciaSubtitulo(Texto.blancoANull(dto.constanciaSubtitulo));
    setConstanciaCiudad(Texto.blancoANull(dto.constanciaCiudad));
    setConstanciaEstado(Texto.blancoANull(dto.constanciaEstado));
    setConstanciaPais(Texto.blancoANull(dto.constanciaPais));
    setConstanciaJefaNombre(Texto.blancoANull(dto.constanciaJefaNombre));
    setConstanciaJefaCargo(Texto.blancoANull(dto.constanciaJefaCargo));
    setConstanciaJefeNombre(Texto.blancoANull(dto.constanciaJefeNombre));
    setConstanciaDepartamento(Texto.blancoANull(dto.constanciaDepartamento));
    setConstanciaLema1(Texto.blancoANull(dto.constanciaLema1));
    setConstanciaLema2(Texto.blancoANull(dto.constanciaLema2));
    setConstanciaPieTexto(Texto.blancoANull(dto.constanciaPieTexto));
    setConstanciaNotaCcp(Texto.blancoANull(dto.constanciaNotaCcp));
    setConstanciaIniciales(Texto.blancoANull(dto.constanciaIniciales));
    setConstanciaActividad(Texto.blancoANull(dto.constanciaActividad));
    setConstanciaNivelDesempeno(Texto.blancoANull(dto.constanciaNivelDesempeno));
    return this;
  }



  /**
   * Copia los campos de la constancia desde otro CONGRESO.
   *
   * @param con
   * CONGRESO con los valores de la constancia.
   *
   * @return
   * El registro actualizado.
   */
  private Congreso actualizarConstancia (Congreso con) {
    if (con.getConstanciaCreditos() > 0) {
      setConstanciaCreditos(con.getConstanciaCreditos());
    }
    setConstanciaInstituto(Texto.blancoANull(con.getConstanciaInstituto()));
    setConstanciaSubtitulo(Texto.blancoANull(con.getConstanciaSubtitulo()));
    setConstanciaCiudad(Texto.blancoANull(con.getConstanciaCiudad()));
    setConstanciaEstado(Texto.blancoANull(con.getConstanciaEstado()));
    setConstanciaPais(Texto.blancoANull(con.getConstanciaPais()));
    setConstanciaJefaNombre(Texto.blancoANull(con.getConstanciaJefaNombre()));
    setConstanciaJefaCargo(Texto.blancoANull(con.getConstanciaJefaCargo()));
    setConstanciaJefeNombre(Texto.blancoANull(con.getConstanciaJefeNombre()));
    setConstanciaDepartamento(Texto.blancoANull(con.getConstanciaDepartamento()));
    setConstanciaLema1(Texto.blancoANull(con.getConstanciaLema1()));
    setConstanciaLema2(Texto.blancoANull(con.getConstanciaLema2()));
    setConstanciaPieTexto(Texto.blancoANull(con.getConstanciaPieTexto()));
    setConstanciaNotaCcp(Texto.blancoANull(con.getConstanciaNotaCcp()));
    setConstanciaIniciales(Texto.blancoANull(con.getConstanciaIniciales()));
    setConstanciaActividad(Texto.blancoANull(con.getConstanciaActividad()));
    setConstanciaNivelDesempeno(Texto.blancoANull(con.getConstanciaNivelDesempeno()));
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

        case "constanciaPieImg1":
          setConstanciaPieImg1MimeType(null);
          setConstanciaPieImg1Data(null);
          break;

        case "constanciaPieImg2":
          setConstanciaPieImg2MimeType(null);
          setConstanciaPieImg2Data(null);
          break;

        case "constanciaPieImg3":
          setConstanciaPieImg3MimeType(null);
          setConstanciaPieImg3Data(null);
          break;

        case "constanciaPieImg4":
          setConstanciaPieImg4MimeType(null);
          setConstanciaPieImg4Data(null);
          break;

        case "constanciaPieImg5":
          setConstanciaPieImg5MimeType(null);
          setConstanciaPieImg5Data(null);
          break;

        case "constanciaPieImg6":
          setConstanciaPieImg6MimeType(null);
          setConstanciaPieImg6Data(null);
          break;

        case "constanciaPieImg7":
          setConstanciaPieImg7MimeType(null);
          setConstanciaPieImg7Data(null);
          break;

        case "constanciaPieImg8":
          setConstanciaPieImg8MimeType(null);
          setConstanciaPieImg8Data(null);
          break;

        case "constanciaPieImg9":
          setConstanciaPieImg9MimeType(null);
          setConstanciaPieImg9Data(null);
          break;

        case "constanciaPieImg10":
          setConstanciaPieImg10MimeType(null);
          setConstanciaPieImg10Data(null);
          break;

        case "constanciaPieFranja":
          setConstanciaPieFranjaMimeType(null);
          setConstanciaPieFranjaData(null);
          break;

        case "constanciaAnio":
          setConstanciaAnioMimeType(null);
          setConstanciaAnioData(null);
          break;

        case "constanciaFirma":
          setConstanciaFirmaMimeType(null);
          setConstanciaFirmaData(null);
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

        case "constanciaPieImg1":
          setConstanciaPieImg1MimeType(mime);
          setConstanciaPieImg1Data(bytes);
          break;

        case "constanciaPieImg2":
          setConstanciaPieImg2MimeType(mime);
          setConstanciaPieImg2Data(bytes);
          break;

        case "constanciaPieImg3":
          setConstanciaPieImg3MimeType(mime);
          setConstanciaPieImg3Data(bytes);
          break;

        case "constanciaPieImg4":
          setConstanciaPieImg4MimeType(mime);
          setConstanciaPieImg4Data(bytes);
          break;

        case "constanciaPieImg5":
          setConstanciaPieImg5MimeType(mime);
          setConstanciaPieImg5Data(bytes);
          break;

        case "constanciaPieImg6":
          setConstanciaPieImg6MimeType(mime);
          setConstanciaPieImg6Data(bytes);
          break;

        case "constanciaPieImg7":
          setConstanciaPieImg7MimeType(mime);
          setConstanciaPieImg7Data(bytes);
          break;

        case "constanciaPieImg8":
          setConstanciaPieImg8MimeType(mime);
          setConstanciaPieImg8Data(bytes);
          break;

        case "constanciaPieImg9":
          setConstanciaPieImg9MimeType(mime);
          setConstanciaPieImg9Data(bytes);
          break;

        case "constanciaPieImg10":
          setConstanciaPieImg10MimeType(mime);
          setConstanciaPieImg10Data(bytes);
          break;

        case "constanciaPieFranja":
          setConstanciaPieFranjaMimeType(mime);
          setConstanciaPieFranjaData(bytes);
          break;

        case "constanciaAnio":
          setConstanciaAnioMimeType(mime);
          setConstanciaAnioData(bytes);
          break;

        case "constanciaFirma":
          setConstanciaFirmaMimeType(mime);
          setConstanciaFirmaData(bytes);
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
