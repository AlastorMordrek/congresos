package com.tecn.tijuana.congresos.boletos.boleto;

import com.tecn.tijuana.congresos.boletos.boleto.dto.RegistroBoletoDto;
import com.tecn.tijuana.congresos.eventos.congreso.Congreso;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.utils.GeneradorDeFolios;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * Clase que representa la entidad de BOLETO en el sistema y en la BD.
 * */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Boleto {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  public static final boolean CANCELADO = true;
  public static final boolean RESTAURADO = false;

  public static final boolean USADO = true;
  public static final boolean NO_USADO = false;



  /**
   * Identificador unico del registro.
   * */
  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "boleto_sequence"
  )
  @SequenceGenerator(
    name = "boleto_sequence",
    sequenceName = "boleto_sequence",
    allocationSize = 1
  )
  @Column(nullable = false, updatable = false)
  private Long id;

  /**
   * Folio unico del registro.
   * */
  @NotBlank(message = "El folio es obligatorio")
  @Size(min = 6, max = 6, message = "El folio debe tener 6 caracteres")
  @Column(unique = true, nullable = false, updatable = false, length = 6)
  private String folio;

  /**
   * Folio largo unico del registro.
   * Permite acceso anonimo al registro.
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
   * Cuando iniciara el evento.
   * */
  @Column(nullable = false)
  private LocalDateTime congresoFechaInicio;

  /**
   * Cuando concluira el evento.
   * */
  @Column(nullable = false)
  private LocalDateTime congresoFechaFin;

  /**
   * Direccion donde tendra lugar el evento.
   * */
  @Size(max = 200,
    message = "La direccion debe tener entre 0 y 200 caracteres")
  @Column(nullable = false, length = 200)
  private String congresoDireccion;



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
   * Nombre del ALUMNO al que pertenece el registro.
   * */
  @NotBlank(message = "Nombre de alumno vacio")
  @Size(min = 3, max = 120,
    message = "El nombre de alumno debe tener entre 3 y 120 caracteres")
  @Column(nullable = false, length = 120)
  private String alumnoNombre;



  /**
   * Determina si el registro se encuentra CANCELADO,
   * es decir, ya no se puede usar.
   * */
  private boolean cancelado = false;

  /**
   * Determina si el registro fue usado al menos una vez, normalmente es para
   * entrar al evento.
   * */
  private boolean usado = false;

  /**
   * A cuantas CONFERENCIAS unicas asistio con este registro.
   * */
  @Min(0) @Max(100)
  @Column(nullable = false)
  private int asistencias;



  /**
   * Funcion constructora alternativa para crear registros.
   */
  private Boleto (
    Long creadorId,

    Long congresoId,
    String congresoNombre,
    LocalDateTime congresoFechaInicio,
    LocalDateTime congresoFechaFin,
    String congresoDireccion,

    Long alumnoId,
    String alumnoNoControl,
    String alumnoNombre
  ) {
    this.folio               = GeneradorDeFolios.folio(6);
    this.folioLargo          = GeneradorDeFolios.folio(20);
    this.fechaCreacion       = LocalDateTime.now();
    this.creadorId           = creadorId;
    this.congresoId          = congresoId;
    this.congresoNombre      = congresoNombre;
    this.congresoFechaInicio = congresoFechaInicio;
    this.congresoFechaFin    = congresoFechaFin;
    this.congresoDireccion   = congresoDireccion;
    this.alumnoId            = alumnoId;
    this.alumnoNoControl     = alumnoNoControl;
    this.alumnoNombre        = alumnoNombre;
  }



  /**
   * Funcion constructora alternativa para crear registros.
   *
   * @return
   * El nuevo registro.
   */
  public static Boleto nuevo (
    Usuario actor,
    RegistroBoletoDto dto,
    Congreso congreso,
    Usuario alumno
  ) {
    return new Boleto(
      actor.getId(),
      congreso.getId(),
      congreso.getNombre(),
      congreso.getFechaInicio(),
      congreso.getFechaFin(),
      congreso.getDireccion(),
      alumno.getId(),
      alumno.getNoControl(),
      alumno.getNombre()
    );
  }



  /**
   * Actualiza un registro con los datos del objeto provisto.
   *
   * @param reg
   * El objeto de con los nuevos valores.
   *
   * @return
   * El registro actualizado.
   */
  public Boleto actualizar (Boleto reg) {

    setCongresoNombre(reg.getCongresoNombre());
    setCongresoFechaInicio(reg.getCongresoFechaInicio());
    setCongresoFechaFin(reg.getCongresoFechaFin());
    setCongresoDireccion(reg.getCongresoDireccion());

    setAlumnoNombre(reg.getAlumnoNombre());

    return this;
  }



  /**
   * Cancela un registro.
   *
   * @return
   * El registro actualizado.
   */
  public Boleto cancelar () {

    setCancelado(CANCELADO);

    return this;
  }



  /**
   * Marca el BOLETO (indica que si se uso para entrar al CONGRESO).
   *
   * @return
   * El registro actualizado.
   */
  public Boleto marcar () {

    setUsado(USADO);

    return this;
  }



  /**
   * Estampa el BOLETO (le suma una asistencia a una nueva CONFERENCIA).
   *
   * @return
   * El registro actualizado.
   */
  public Boleto estampar () {

    setAsistencias(getAsistencias() + 1);

    return this;
  }
}
