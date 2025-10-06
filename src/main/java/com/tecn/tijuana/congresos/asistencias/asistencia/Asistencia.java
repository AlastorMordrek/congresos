package com.tecn.tijuana.congresos.asistencias.asistencia;


import com.tecn.tijuana.congresos.boletos.boleto.Boleto;
import com.tecn.tijuana.congresos.eventos.conferencia.Conferencia;
import com.tecn.tijuana.congresos.eventos.congreso.Congreso;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase que representa la entidad de ASISTENCIA en el sistema y en la BD.
 * */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asistencia {

  /**
   * Identificador unico del registro.
   * */
  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "asistencia_sequence"
  )
  @SequenceGenerator(
    name = "asistencia_sequence",
    sequenceName = "asistencia_sequence",
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
   * ID del creador del registro.
   * Normalmente el personal autorizado que registro la ASISTENCIA del ALUMNO.
   * */
  @Column(nullable = false, updatable = false)
  private Long creadorId;

  /**
   * Nombre completo del creador del registro.
   * Normalmente el personal autorizado que registro la ASISTENCIA del ALUMNO.
   * */
  @NotBlank(message = "Nombre de creador vacio")
  @Size(min = 3, max = 120,
    message = "El nombre de creador debe tener entre 3 y 120 caracteres")
  @Column(nullable = false, length = 120)
  private String creadorNombre;



  /**
   * ID del BOLETO.
   * */
  @Column(nullable = false, updatable = false)
  private Long boletoId;

  /**
   * Folio del BOLETO.
   * */
  @NotBlank(message = "El folio de boleto es obligatorio")
  @Size(min = 6, max = 6,
    message = "El folio de boleto debe tener 6 caracteres")
  @Column(nullable = false, updatable = false, length = 6)
  private String boletoFolio;

  /**
   * Folio largo del BOLETO.
   * */
  @NotBlank(message = "El folio largo del boleto es obligatorio")
  @Size(min = 20, max = 20,
    message = "El folio largo del boleto debe tener 20 caracteres")
  @Column(nullable = false, updatable = false, length = 20)
  private String boletoFolioLargo;



  /**
   * CONGRESO para al cual pertenece el registro.
   * */
  @Column(nullable = false, updatable = false)
  private Long congresoId;

  /**
   * Nombre del CONGRESO al que pertenece el registro.
   * */
  @NotBlank(message = "Nombre de congreso vacio")
  @Size(min = 1, max = 100,
    message = "El nombre de congreso debe tener entre 1 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String congresoNombre;



  /**
   * CONFERENCIA para al cual pertenece el registro.
   * */
  @Column(nullable = false, updatable = false)
  private Long conferenciaId;

  /**
   * Nombre del CONGRESO al que pertenece el registro.
   * */
  @NotBlank(message = "Nombre de conferencia vacio")
  @Size(min = 1, max = 100,
    message = "El nombre de conferencia debe tener entre 1 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String conferenciaNombre;



  /**
   * ID del ALUMNO al que pertenece el registro.
   * */
  @Column(nullable = false, updatable = false)
  private Long alumnoId;

  /**
   * Numero de control del ALUMNO al que pertenece el registro.
   * */
  @NotBlank(message = "Numero de control de alumno vacio")
  @Size(min = 8, max = 8,
    message = "El numero de control de alumno debe tener 8 caracteres")
  @Column(nullable = false, length = 8)
  private String alumnoNoControl;

  /**
   * Nombre completo del ALUMNO al que pertenece el registro.
   * */
  @NotBlank(message = "Nombre de alumno vacio")
  @Size(min = 3, max = 120,
    message = "El nombre de alumno debe tener entre 3 y 120 caracteres")
  @Column(nullable = false, length = 120)
  private String alumnoNombre;



  /**
   * Cuando fue la ultima vez que registro entrada a la CONFERENCIA.
   * */
  @Column
  private LocalDateTime fechaUltimaEntrada;

  /**
   * Suma del tiempo que paso en la CONFERENCIA en segundos.
   * */
  @Column
  private Long tiempoAsistido = 0L;



  /**
   * Funcion constructora alternativa para crear registros.
   */
  private Asistencia (
    Long creadorId,
    String creadorNombre,

    Long boletoId,
    String boletoFolio,
    String boletoFolioLargo,

    Long congresoId,
    String congresoNombre,

    Long conferenciaId,
    String conferenciaNombre,

    Long alumnoId,
    String alumnoNoControl,
    String alumnoNombre
  ) {
    var now = LocalDateTime.now();

    this.fechaCreacion       = now;
    this.creadorId           = creadorId;
    this.creadorNombre       = creadorNombre;

    this.boletoId            = boletoId;
    this.boletoFolio         = boletoFolio;
    this.boletoFolioLargo    = boletoFolioLargo;

    this.congresoId          = congresoId;
    this.congresoNombre      = congresoNombre;

    this.conferenciaId       = conferenciaId;
    this.conferenciaNombre   = conferenciaNombre;

    this.alumnoId            = alumnoId;
    this.alumnoNoControl     = alumnoNoControl;
    this.alumnoNombre        = alumnoNombre;

    this.fechaUltimaEntrada  = now;
  }



  /**
   * Funcion constructora alternativa para crear registros.
   *
   * @return
   * El nuevo registro.
   */
  public static Asistencia nuevo (Asistencia reg) {
    return new Asistencia(
      reg.getCreadorId(),
      reg.getCreadorNombre(),
      reg.getBoletoId(),
      reg.getBoletoFolio(),
      reg.getBoletoFolioLargo(),
      reg.getCongresoId(),
      reg.getCongresoNombre(),
      reg.getConferenciaId(),
      reg.getConferenciaNombre(),
      reg.getAlumnoId(),
      reg.getAlumnoNoControl(),
      reg.getAlumnoNombre()
    );
  }



  /**
   * Funcion constructora alternativa para crear registros.
   *
   * @return
   * El nuevo registro.
   */
  public static Asistencia nuevo (
    Usuario actor,
    Usuario alumno,
    Congreso congreso,
    Conferencia conferencia,
    Boleto boleto
  ) {
    return new Asistencia(
      actor.getId(),
      actor.getNombreCompleto(),
      boleto.getId(),
      boleto.getFolio(),
      boleto.getFolioLargo(),
      congreso.getId(),
      congreso.getNombre(),
      conferencia.getId(),
      conferencia.getNombre(),
      alumno.getId(),
      alumno.getNoControl(),
      alumno.getNombreCompleto()
    );
  }



  /**
   * Registra la entrada del ALUMNO a la CONFERENCIA.
   *
   * @return
   * El registro actualizado.
   */
  public Asistencia entrar () {

    // Ultima fecha en que entro a la CONFERENCIA.
    var ultimaEntrada = getFechaUltimaEntrada();

    // Si hay una fecha de ENTRADA pendiente, retornar sin mas.
    if (Objects.nonNull(ultimaEntrada)) {
      return this;
    }

    // Registrar fecha de entrada actual.
    setFechaUltimaEntrada(LocalDateTime.now());

    // Retornar objeto actualizado.
    return this;
  }



  /**
   * Registrar salida para el ALUMNO de la CONFERENCIA.
   * <p>
   * Se asume que ya tenia una fecha de ENTRADA, se calcula cuento tiempo paso
   * desde entonces y la duracion se suma al tiempo total asistido.
   *
   * @return
   * El registro actualizado.
   */
  public Asistencia salir () {

    // Ultima fecha en que entro a la CONFERENCIA.
    var ultima = getFechaUltimaEntrada();

    // Si no hay fecha de entrada previa no hay nada que hacer.
    // Retornar sin mas.
    if (Objects.isNull(ultima)) {
      return this;
    }

    // Cuanto tiempo ha pasado desde que entro.
    var duracion = Duration.between(ultima, LocalDateTime.now()).toSeconds();

    // Tiempo total asistido a la conferencia especifica actualmente.
    var asistido = getTiempoAsistido();

    // Sumar la nueva duracion al tiempo total asistido.
    if (Objects.isNull(asistido) || asistido == 0) {
      setTiempoAsistido(duracion);
    } else {
      setTiempoAsistido(asistido + duracion);
    }

    // Remover fecha de entrada previa.
    setFechaUltimaEntrada(null);

    // Retornar objeto actualizado.
    return this;
  }



  /**
   * Sobreescribir los datos de la asistencia y constar que el ALUMNO asistio a
   * la CONFERENCIA completa.
   *
   * @return
   * El registro actualizado.
   */
  public Asistencia asistioConferenciaCompleta (Conferencia conferencia) {

    // Remover fecha de entrada.
    setFechaUltimaEntrada(null);

    // Establecer tiempo asistido total como la duracion completa de la
    // CONFERENCIA.
    setTiempoAsistido(
      Duration.between(
          conferencia.getFechaInicio(),
          conferencia.getFechaFin())
        .toSeconds()
    );

    // Retornar objeto actualizado.
    return this;
  }
}
