package com.tecn.tijuana.congresos.acreditaciones.acreditacion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tecn.tijuana.congresos.utils.Texto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Constancia de acreditacion de actividad complementaria de un ALUMNO.
 * <p>
 * Guarda una copia de todos los datos que se imprimen en el PDF, de modo que
 * cambios posteriores en el ALUMNO, BOLETO o CONGRESO no alteren una
 * constancia ya emitida.
 */
@Entity
@Data
@NoArgsConstructor
public class Acreditacion {

  /**
   * Identificador unico del registro.
   * */
  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "acreditacion_sequence"
  )
  @SequenceGenerator(
    name = "acreditacion_sequence",
    sequenceName = "acreditacion_sequence",
    allocationSize = 1
  )
  @Column(nullable = false, updatable = false)
  private Long id;

  /**
   * Cuando fue emitida la constancia.
   * */
  @PastOrPresent(message = "La fecha de creacion no puede ser futura")
  @Column(nullable = false, updatable = false)
  private LocalDateTime fechaCreacion;

  /**
   * ORGANIZADOR que emitio la constancia.
   * */
  @Column(updatable = false)
  private Long creadorId;



  /**
   * BOLETO que origino la constancia. Solo puede existir una por boleto.
   * */
  @NotNull(message = "El boleto es obligatorio")
  @Column(nullable = false, unique = true)
  private Long boletoId;

  /** Folio corto del BOLETO. */
  @Size(max = 6, message = "El folio no puede exceder 6 caracteres")
  @Column(length = 6)
  private String boletoFolio;

  /** Folio largo del BOLETO. */
  @Size(max = 20, message = "El folio largo no puede exceder 20 caracteres")
  @Column(length = 20)
  private String boletoFolioLargo;



  /** ID del ALUMNO. */
  @Column(nullable = false)
  private Long alumnoId;

  /** Numero de control del ALUMNO. */
  @Size(max = 8, message = "El numero de control no puede exceder 8 caracteres")
  @Column(length = 8)
  private String alumnoNoControl;

  /** Nombre completo del ALUMNO. */
  @Size(max = 120, message = "El nombre no puede exceder 120 caracteres")
  @Column(length = 120)
  private String alumnoNombre;

  /** Clave oficial de la carrera del ALUMNO. */
  @Size(max = 13, message = "La clave de carrera no puede exceder 13 caracteres")
  @Column(length = 13)
  private String carreraCodigo;

  /** Nombre de la carrera; si es nulo el frontend lo resuelve del catalogo. */
  @Size(max = 150, message = "La carrera no puede exceder 150 caracteres")
  @Column(length = 150)
  private String carreraNombre;



  /** ID del CONGRESO. */
  @Column(nullable = false)
  private Long congresoId;

  /** Nombre del CONGRESO. */
  @Size(max = 100, message = "El congreso no puede exceder 100 caracteres")
  @Column(length = 100)
  private String congresoNombre;

  /** Fecha de inicio del CONGRESO. */
  private LocalDateTime congresoFechaInicio;

  /** Fecha de fin del CONGRESO. */
  private LocalDateTime congresoFechaFin;



  /** Fecha que aparece impresa en la constancia. */
  @Column(nullable = false)
  private LocalDate fechaEmision;

  /** Periodo escolar impreso, por ejemplo ENERO-JUNIO. */
  @Size(max = 30, message = "El periodo no puede exceder 30 caracteres")
  @Column(length = 30)
  private String periodo;

  /** Año del periodo escolar. */
  private int anio;

  /** Creditos otorgados. */
  @Min(value = 1, message = "La constancia debe otorgar al menos 1 credito")
  @Max(value = 20, message = "La constancia no puede otorgar mas de 20 creditos")
  private int creditos = 1;



  /** Nombre de la institucion en el encabezado. */
  @Size(max = 150, message = "El instituto no puede exceder 150 caracteres")
  @Column(length = 150)
  private String instituto;

  /** Subtitulo oficial de la constancia. */
  @Size(max = 150, message = "El subtitulo no puede exceder 150 caracteres")
  @Column(length = 150)
  private String subtitulo;

  /** Ciudad de emision. */
  @Size(max = 80, message = "La ciudad no puede exceder 80 caracteres")
  @Column(length = 80)
  private String ciudad;

  /** Estado de emision. */
  @Size(max = 80, message = "El estado no puede exceder 80 caracteres")
  @Column(length = 80)
  private String estado;

  /** Pais de emision. */
  @Size(max = 80, message = "El pais no puede exceder 80 caracteres")
  @Column(length = 80)
  private String pais;



  /** Nombre de la jefatura de servicios escolares. */
  @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
  @Column(length = 100)
  private String jefaNombre;

  /** Cargo de la jefatura de servicios escolares. */
  @Size(max = 150, message = "El cargo no puede exceder 150 caracteres")
  @Column(length = 150)
  private String jefaCargo;

  /** Nombre de quien firma. */
  @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
  @Column(length = 100)
  private String jefeNombre;

  /** Departamento de quien firma. */
  @Size(max = 150, message = "El departamento no puede exceder 150 caracteres")
  @Column(length = 150)
  private String departamento;

  /** Primer lema institucional. */
  @Size(max = 150, message = "El lema no puede exceder 150 caracteres")
  @Column(length = 150)
  private String lema1;

  /** Segundo lema institucional. */
  @Size(max = 150, message = "El lema no puede exceder 150 caracteres")
  @Column(length = 150)
  private String lema2;

  /** Texto del pie de pagina. */
  @Size(max = 500, message = "El pie no puede exceder 500 caracteres")
  @Column(length = 500)
  private String pieTexto;

  /** Nota opcional de copia de correspondencia. */
  @Size(max = 100, message = "La nota no puede exceder 100 caracteres")
  @Column(length = 100)
  private String notaCcp;

  /** Iniciales opcionales de firma y captura. */
  @Size(max = 50, message = "Las iniciales no pueden exceder 50 caracteres")
  @Column(length = 50)
  private String iniciales;

  /** Actividad realizada, opcional. */
  @Size(max = 200, message = "La actividad no puede exceder 200 caracteres")
  @Column(length = 200)
  private String actividad;

  /** Nivel de desempeno, opcional. */
  @Size(max = 100, message = "El nivel no puede exceder 100 caracteres")
  @Column(length = 100)
  private String nivelDesempeno;



  /** Tipo de archivo del encabezado. */
  @JsonIgnore
  @Column(length = 100)
  private String encabezadoMimeType;

  /** Imagen del encabezado. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(length = 2 * 1024 * 1024)
  private byte[] encabezadoImgData;

  /** Tipo de archivo del logo 1 del pie. */
  @JsonIgnore
  @Column(name = "pie_img_1_mime_type", length = 100)
  private String pieImg1MimeType;

  /** Imagen del logo 1 del pie. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "pie_img_1_data", length = 2 * 1024 * 1024)
  private byte[] pieImg1Data;

  /** Tipo de archivo del logo 2 del pie. */
  @JsonIgnore
  @Column(name = "pie_img_2_mime_type", length = 100)
  private String pieImg2MimeType;

  /** Imagen del logo 2 del pie. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "pie_img_2_data", length = 2 * 1024 * 1024)
  private byte[] pieImg2Data;

  /** Tipo de archivo del logo 3 del pie. */
  @JsonIgnore
  @Column(name = "pie_img_3_mime_type", length = 100)
  private String pieImg3MimeType;

  /** Imagen del logo 3 del pie. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "pie_img_3_data", length = 2 * 1024 * 1024)
  private byte[] pieImg3Data;

  /** Tipo de archivo del logo 4 del pie. */
  @JsonIgnore
  @Column(name = "pie_img_4_mime_type", length = 100)
  private String pieImg4MimeType;

  /** Imagen del logo 4 del pie. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "pie_img_4_data", length = 2 * 1024 * 1024)
  private byte[] pieImg4Data;

  /** Tipo de archivo del logo 5 del pie. */
  @JsonIgnore
  @Column(name = "pie_img_5_mime_type", length = 100)
  private String pieImg5MimeType;

  /** Imagen del logo 5 del pie. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "pie_img_5_data", length = 2 * 1024 * 1024)
  private byte[] pieImg5Data;

  /** Tipo de archivo del logo 6 del pie. */
  @JsonIgnore
  @Column(name = "pie_img_6_mime_type", length = 100)
  private String pieImg6MimeType;

  /** Imagen del logo 6 del pie. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "pie_img_6_data", length = 2 * 1024 * 1024)
  private byte[] pieImg6Data;

  /** Tipo de archivo del logo 7 del pie. */
  @JsonIgnore
  @Column(name = "pie_img_7_mime_type", length = 100)
  private String pieImg7MimeType;

  /** Imagen del logo 7 del pie. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "pie_img_7_data", length = 2 * 1024 * 1024)
  private byte[] pieImg7Data;

  /** Tipo de archivo del logo 8 del pie. */
  @JsonIgnore
  @Column(name = "pie_img_8_mime_type", length = 100)
  private String pieImg8MimeType;

  /** Imagen del logo 8 del pie. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "pie_img_8_data", length = 2 * 1024 * 1024)
  private byte[] pieImg8Data;

  /** Tipo de archivo del logo 9 del pie. */
  @JsonIgnore
  @Column(name = "pie_img_9_mime_type", length = 100)
  private String pieImg9MimeType;

  /** Imagen del logo 9 del pie. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "pie_img_9_data", length = 2 * 1024 * 1024)
  private byte[] pieImg9Data;

  /** Tipo de archivo del logo 10 del pie. */
  @JsonIgnore
  @Column(name = "pie_img_10_mime_type", length = 100)
  private String pieImg10MimeType;

  /** Imagen del logo 10 del pie. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "pie_img_10_data", length = 2 * 1024 * 1024)
  private byte[] pieImg10Data;

  /** Tipo de archivo de la franja inferior. */
  @JsonIgnore
  @Column(length = 100)
  private String pieFranjaMimeType;

  /** Imagen de la franja inferior. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(length = 2 * 1024 * 1024)
  private byte[] pieFranjaData;

  /** Tipo de archivo de la imagen del anio. */
  @JsonIgnore
  @Column(length = 100)
  private String pieAnioMimeType;

  /** Imagen del anio. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(length = 2 * 1024 * 1024)
  private byte[] pieAnioData;

  /** Tipo de archivo de la firma. */
  @JsonIgnore
  @Column(name = "firma_mime_type", length = 100)
  private String firmaMimeType;

  /** Imagen de la firma. */
  @JsonIgnore
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "firma_data", length = 2 * 1024 * 1024)
  private byte[] firmaData;

  /**
   * Devuelve el encabezado como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("encabezadoUrl")
  public String getEncabezadoUrl () {
    return dataUrl(encabezadoMimeType, encabezadoImgData);
  }

  /**
   * Devuelve el logo 1 del pie como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("pieImg1Url")
  public String getPieImg1Url () {
    return dataUrl(pieImg1MimeType, pieImg1Data);
  }

  /**
   * Devuelve el logo 2 del pie como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("pieImg2Url")
  public String getPieImg2Url () {
    return dataUrl(pieImg2MimeType, pieImg2Data);
  }

  /**
   * Devuelve el logo 3 del pie como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("pieImg3Url")
  public String getPieImg3Url () {
    return dataUrl(pieImg3MimeType, pieImg3Data);
  }

  /**
   * Devuelve el logo 4 del pie como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("pieImg4Url")
  public String getPieImg4Url () {
    return dataUrl(pieImg4MimeType, pieImg4Data);
  }

  /**
   * Devuelve el logo 5 del pie como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("pieImg5Url")
  public String getPieImg5Url () {
    return dataUrl(pieImg5MimeType, pieImg5Data);
  }

  /**
   * Devuelve el logo 6 del pie como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("pieImg6Url")
  public String getPieImg6Url () {
    return dataUrl(pieImg6MimeType, pieImg6Data);
  }

  /**
   * Devuelve el logo 7 del pie como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("pieImg7Url")
  public String getPieImg7Url () {
    return dataUrl(pieImg7MimeType, pieImg7Data);
  }

  /**
   * Devuelve el logo 8 del pie como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("pieImg8Url")
  public String getPieImg8Url () {
    return dataUrl(pieImg8MimeType, pieImg8Data);
  }

  /**
   * Devuelve el logo 9 del pie como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("pieImg9Url")
  public String getPieImg9Url () {
    return dataUrl(pieImg9MimeType, pieImg9Data);
  }

  /**
   * Devuelve el logo 10 del pie como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("pieImg10Url")
  public String getPieImg10Url () {
    return dataUrl(pieImg10MimeType, pieImg10Data);
  }

  /**
   * Devuelve la franja inferior como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("pieFranjaUrl")
  public String getPieFranjaUrl () {
    return dataUrl(pieFranjaMimeType, pieFranjaData);
  }

  /**
   * Devuelve la imagen del anio como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("pieAnioUrl")
  public String getPieAnioUrl () {
    return dataUrl(pieAnioMimeType, pieAnioData);
  }

  /**
   * Devuelve la firma de la constancia como data URL.
   *
   * @return
   * Data URL o {@code null}.
   */
  @com.fasterxml.jackson.annotation.JsonProperty("firmaUrl")
  public String getFirmaUrl () {
    return dataUrl(firmaMimeType, firmaData);
  }

  /**
   * Convierte una imagen a data URL.
   *
   * @param mime
   * Tipo de archivo.
   *
   * @param datos
   * Contenido binario.
   *
   * @return
   * Data URL o {@code null}.
   */
  private String dataUrl (String mime, byte[] datos) {
    if (Objects.isNull(datos) || datos.length == 0) {
      return null;
    }

    return "data:" + (Objects.isNull(mime) ? "image/png" : mime)
      + ";base64,"
      + java.util.Base64.getEncoder().encodeToString(datos);
  }



  /**
   * Actualiza una imagen de la constancia.
   *
   * @param slot
   * Slot a editar: encabezado, pieImg1..10, pieFranja, pieAnio.
   *
   * @param img
   * Archivo nuevo, o {@code null} para limpiar el slot.
   *
   * @return
   * El registro actualizado.
   *
   * @throws java.io.IOException
   * Si no se puede leer el archivo.
   */
  public Acreditacion setMedia (
    String slot, org.springframework.web.multipart.MultipartFile img
  ) throws java.io.IOException {

    String mime = Objects.isNull(img) ? null : img.getContentType();
    byte[] bytes = Objects.isNull(img) ? null : img.getBytes();

    switch (slot) {
      case "encabezado" -> {
        setEncabezadoMimeType(mime);
        setEncabezadoImgData(bytes);
      }
      case "pieImg1" -> {
        setPieImg1MimeType(mime);
        setPieImg1Data(bytes);
      }
      case "pieImg2" -> {
        setPieImg2MimeType(mime);
        setPieImg2Data(bytes);
      }
      case "pieImg3" -> {
        setPieImg3MimeType(mime);
        setPieImg3Data(bytes);
      }
      case "pieImg4" -> {
        setPieImg4MimeType(mime);
        setPieImg4Data(bytes);
      }
      case "pieImg5" -> {
        setPieImg5MimeType(mime);
        setPieImg5Data(bytes);
      }
      case "pieImg6" -> {
        setPieImg6MimeType(mime);
        setPieImg6Data(bytes);
      }
      case "pieImg7" -> {
        setPieImg7MimeType(mime);
        setPieImg7Data(bytes);
      }
      case "pieImg8" -> {
        setPieImg8MimeType(mime);
        setPieImg8Data(bytes);
      }
      case "pieImg9" -> {
        setPieImg9MimeType(mime);
        setPieImg9Data(bytes);
      }
      case "pieImg10" -> {
        setPieImg10MimeType(mime);
        setPieImg10Data(bytes);
      }
      case "pieFranja" -> {
        setPieFranjaMimeType(mime);
        setPieFranjaData(bytes);
      }
      case "pieAnio" -> {
        setPieAnioMimeType(mime);
        setPieAnioData(bytes);
      }
      default -> throw new IllegalArgumentException(
        "Slot de multimedia no valido: " + slot);
    }

    return this;
  }



  /**
   * Actualiza los campos editables de la constancia.
   *
   * @param dto
   * Valores nuevos.
   *
   * @return
   * El registro actualizado.
   */
  public Acreditacion actualizar (com.tecn.tijuana.congresos.acreditaciones
    .acreditacion.dto.EditarAcreditacionDto dto) {

    setAlumnoNombre(dto.alumnoNombre);
    setCarreraNombre(dto.carreraNombre);
    setFechaEmision(dto.fechaEmision);
    setPeriodo(Texto.blancoANull(dto.periodo));
    setAnio(dto.anio);
    setCreditos(dto.creditos);
    setInstituto(Texto.blancoANull(dto.instituto));
    setSubtitulo(Texto.blancoANull(dto.subtitulo));
    setCiudad(Texto.blancoANull(dto.ciudad));
    setEstado(Texto.blancoANull(dto.estado));
    setPais(Texto.blancoANull(dto.pais));
    setJefaNombre(Texto.blancoANull(dto.jefaNombre));
    setJefaCargo(Texto.blancoANull(dto.jefaCargo));
    setJefeNombre(Texto.blancoANull(dto.jefeNombre));
    setDepartamento(Texto.blancoANull(dto.departamento));
    setLema1(Texto.blancoANull(dto.lema1));
    setLema2(Texto.blancoANull(dto.lema2));
    setPieTexto(Texto.blancoANull(dto.pieTexto));
    setNotaCcp(Texto.blancoANull(dto.notaCcp));
    setIniciales(Texto.blancoANull(dto.iniciales));
    setActividad(Texto.blancoANull(dto.actividad));
    setNivelDesempeno(Texto.blancoANull(dto.nivelDesempeno));

    return this;
  }
}
