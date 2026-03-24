package com.tecn.tijuana.congresos.acreditaciones.constancia;

import com.tecn.tijuana.congresos.boletos.boleto.Boleto;
import com.tecn.tijuana.congresos.eventos.congreso.Congreso;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * Clase que representa una CONSTANCIA de acreditacion de participacion por
 * parte de un ALUMNO en un CONGRESO.
 * */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Constancia {

  /**
   * Identificador unico del registro.
   * */
  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "constancia_sequence"
  )
  @SequenceGenerator(
    name = "constancia_sequence",
    sequenceName = "constancia_sequence",
    allocationSize = 1
  )
  @Column(nullable = false, updatable = false)
  private Long id;

  /**
   * Folio unico del registro.
   * <p>
   * Se debe heredar del campo FOLIO del BOLETO que se uso en el evento.
   * */
  @NotBlank(message = "El folio es obligatorio")
  @Size(min = 6, max = 6, message = "El folio debe tener 6 caracteres")
  @Column(unique = true, nullable = false, updatable = false, length = 6)
  private String folio;

  /**
   * Folio largo unico del registro.
   * Permite acceso anonimo al registro.
   * <p>
   * Se debe heredar del campo FOLIO_LARGO del BOLETO que se uso en el evento.
   * */
  @NotBlank(message = "El folio largo es obligatorio")
  @Size(min = 20, max = 20, message = "El folio debe tener 20 caracteres")
  @Column(unique = true, nullable = false, updatable = false, length = 20)
  private String folioLargo;



  /**
   * Cuando fue creado el registro.
   * */
  @Column(nullable = false, updatable = false)
  private LocalDateTime fechaCreacion;

  /**
   * Posible creador del registro.
   * */
  @Column(nullable = false, updatable = false)
  private Long creadorId;



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
   * Periodo escolar dentro del cual se llevo acabo el evento.
   * */
  @NotBlank(message = "Periodo escolar vacio")
  @Size(min = 1, max = 100,
    message = "El periodo escolar debe tener entre 1 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String periodoEscolar;



  /**
   * ID del ALUMNO.
   * */
  @Column(nullable = false, updatable = false)
  private Long alumnoId;

  /**
   * Numero de control del ALUMNO.
   * */
  @NotBlank(message = "Numero de control de alumno vacio")
  @Size(min = 8, max = 8,
    message = "El numero de control de alumno debe tener 8 caracteres")
  @Column(nullable = false, length = 8)
  private String alumnoNoControl;

  /**
   * Nombre del ALUMNO.
   * */
  @NotBlank(message = "Nombre de alumno vacio")
  @Size(min = 3, max = 120,
    message = "El nombre de alumno debe tener entre 3 y 120 caracteres")
  @Column(nullable = false, length = 120)
  private String alumnoNombre;

  /**
   * Nombre de la CARRERA que estudia el ALUMNO.
   * */
  @NotBlank(message = "Nombre de carrera vacio")
  @Size(min = 1, max = 100,
    message = "El nombre de la carrera debe tener entre 1 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String alumnoCarrera;



  /**
   * Nombre de la actividad que el ALUMNO realizo y por la cual se le esta
   * otorgando la acreditacion.
   * */
  @Size(min = 1, max = 100,
    message = "El nombre de la actividad debe tener entre 1 y 100 caracteres")
  @Column(length = 100)
  private String actividadNombre;

  /**
   * Numero de creditos otorgados por la acreditacion.
   * */
  @Column(nullable = false)
  private Integer actividadCreditos = 0;



  /**
   * Nombre de quien recibe la constancia para su aceptacion e integracion en el
   * sistema academico/escolar relevante.
   * <p>
   * Usualmente el jefe del departamento relevante o alguien asignado a estas
   * tareas.
   * */
  @NotBlank(message = "Nombre del receptor de admon. vacio")
  @Size(min = 1, max = 100,
    message = "El nombre del receptor de admon. debe tener entre 1 y 100" +
      " caracteres")
  @Column(nullable = false, length = 100)
  private String admonReceptorNombre;

  /**
   * Nombre del cargo del miembro de la administracion que recibe la constancia
   * para su aceptacion.
   * <p>
   * Ejemplo: "Jefe(a) de Departamento de Servicios Escolares".
   * */
  @NotBlank(message = "Nombre del cargo del receptor de admon. vacio")
  @Size(min = 1, max = 100,
    message = "El nombre cargo del del representante del Departamento de" +
      " Servicios Escolares debe tener entre 1 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String admonReceptorCargo;

  /**
   * Nombre de quien emite la constancia para que el receptor la acepte e
   * integre al sistema relevante.
   * <p>
   * Usualmente el representante de la carrera que estudia el ALUMNO.
   * */
  @NotBlank(message = "Nombre del emisor vacio")
  @Size(min = 1, max = 100,
    message = "El nombre del emisor debe tener entre 1 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String admonEmisorNombre;

  /**
   * Nombre del cargo del emisor.
   * <p>
   * Ejemplo: "Jefa del departamento de Sistemas y Computacion".
   * */
  @NotBlank(message = "Nombre del cargo del emisor vacio")
  @Size(min = 1, max = 100,
    message = "El nombre del cargo del emisor debe tener entre 1 y 100" +
      " caracteres")
  @Column(nullable = false, length = 100)
  private String admonEmisorCargo;



  /**
   * Funcion constructora alternativa para crear registros.
   */
  private Constancia (
    Long creadorId,

    String folio,
    String folioLargo,

    Long congresoId,
    String congresoNombre,

    Long alumnoId,
    String alumnoNoControl,
    String alumnoNombre,

    String alumnoCarrera,
    String periodoEscolar,

    String actividadNombre,
    Integer actividadCreditos,

    String admonReceptorNombre,
    String admonReceptorCargo,
    String admonEmisorNombre,
    String admonEmisorCargo
  ) {
    this.creadorId           = creadorId;
    this.fechaCreacion       = LocalDateTime.now();

    this.folio               = folio;
    this.folioLargo          = folioLargo;

    this.congresoId          = congresoId;
    this.congresoNombre      = congresoNombre;

    this.alumnoId            = alumnoId;
    this.alumnoNoControl     = alumnoNoControl;
    this.alumnoNombre        = alumnoNombre;

    this.alumnoCarrera       = alumnoCarrera;
    this.periodoEscolar      = periodoEscolar;

    this.actividadNombre     = actividadNombre;
    this.actividadCreditos   = actividadCreditos;

    this.admonReceptorNombre = admonReceptorNombre;
    this.admonReceptorCargo  = admonReceptorCargo;
    this.admonEmisorNombre   = admonEmisorNombre;
    this.admonEmisorCargo    = admonEmisorCargo;
  }



  /**
   * Funcion constructora alternativa para crear registros.
   *
   * @return
   * El nuevo registro.
   */
  public static Constancia nuevo (
    Usuario actor,
    Boleto boleto,
    Congreso congreso,
    Usuario alumno,

    String alumnoCarrera,
    String periodoEscolar,

    String actividadNombre,
    Integer actividadCreditos,

    String admonReceptorNombre,
    String admonReceptorCargo,
    String admonEmisorNombre,
    String admonEmisorCargo
  ) {
    return new Constancia(
      actor.getId(),
      boleto.getFolio(),
      boleto.getFolioLargo(),
      congreso.getId(),
      congreso.getNombre(),
      alumno.getId(),
      alumno.getNoControl(),
      alumno.getNombre(),
      alumnoCarrera,
      periodoEscolar,
      actividadNombre,
      actividadCreditos,
      admonReceptorNombre,
      admonReceptorCargo,
      admonEmisorNombre,
      admonEmisorCargo
    );
  }
}
