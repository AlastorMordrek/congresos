package com.tecn.tijuana.congresos.boletos.boleto;

import com.tecn.tijuana.congresos.eventos.congreso.Congreso;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.utils.GeneradorDeFolios;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
  public static final boolean NO_CANCELADO = RESTAURADO;

  public static final boolean USADO = true;
  public static final boolean NO_USADO = false;

  public static final boolean EXCEDENTE = true;
  public static final boolean NO_EXCEDENTE = false;

  public static final boolean PAGADO = true;
  public static final boolean NO_PAGADO = false;



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
  @NotBlank(message = "Debe especificar el Folio del Boleto")
  @Size(min = 6, max = 6, message = "El folio debe tener 6 caracteres")
  @Column(unique = true, nullable = false, updatable = false, length = 6)
  private String folio;

  /**
   * Folio largo unico del registro.
   * Permite acceso anonimo al registro.
   * */
  @NotBlank(message = "Debe especificar el Folio Largo del Boleto")
  @Size(min = 20, max = 20, message = "El folio debe tener 20 caracteres")
  @Column(unique = true, nullable = false, updatable = false, length = 20)
  private String folioLargo;



  /**
   * Cuando fue creado el registro.
   * */
  @NotNull(message = "Debe especificar La Fecha de Creacion del Boleto")
  @PastOrPresent(message = "La fecha de creacion no puede ser futura")
  @Column(nullable = false, updatable = false)
  private LocalDateTime fechaCreacion;

  /**
   * Posible creador del registro.
   * */
  @NotNull(message = "Debe especificar el ID de Creador del Boleto")
  @Column(nullable = false, updatable = false)
  private Long creadorId;



  /**
   * CONGRESO para al cual pertenece el registro.
   * */
  @NotNull(message = "Debe especificar el ID de Congreso")
  @Column(nullable = false, updatable = false)
  private Long congresoId;

  /**
   * Nombre del CONGRESO al que pertenece el registro.
   * */
  @NotBlank(message = "Debe especificar el Nombre del Congreso")
  @Size(min = 1, max = 100,
    message = "El nombre de congreso debe tener entre 1 y 100 caracteres")
  @Column(nullable = false, length = 100)
  private String congresoNombre;

  /**
   * Cuando iniciara el evento.
   * */
  @NotNull(message = "Debe especificar la Fecha de Inicio del Congreso")
  @Column(nullable = false)
  private LocalDateTime congresoFechaInicio;

  /**
   * Cuando concluira el evento.
   * */
  @NotNull(message = "Debe especificar la Fecha de Terminacion del Congreso")
  @Column(nullable = false)
  private LocalDateTime congresoFechaFin;

  /**
   * Direccion donde tendra lugar el evento.
   * */
  @NotNull(message = "Debe especificar la Direccion del Congreso")
  @Size(max = 200,
    message = "La direccion debe tener entre 0 y 200 caracteres")
  @Column(nullable = false, length = 200)
  private String congresoDireccion = "";



  /**
   * Determina si el BOLETO fue registrado despues de que ya se habia llenado el
   * cupo del CONGRESO.
   * */
  @Column(updatable = false)
  private boolean excedente = NO_EXCEDENTE;



  /**
   * Determina si el BOLETO fue pagado.
   * <p>
   * Algunos CONGRESOS tienen costo y sus BOLETOS solo podran ser usados para
   * asistir al evento hasta que sean marcados como {@code pagado = true} por el
   * personal autorizado.
   * */
  private boolean pagado = NO_PAGADO;

  /**
   * Usuario que valido que el BOLETO ya fue pagado.
   * <p>
   * Consta el ultimo USUARIO que edito el estatus de "pagado" del BOLETO.
   * */
  private Long usuarioEditoPagado;



  /**
   * ID del ALUMNO al que pertenece el registro.
   * */
  @NotNull(message = "Debe especificar el ID de Alumno")
  @Column(nullable = false, updatable = false)
  private Long alumnoId;

  /**
   * Numero de control del ALUMNO al que pertenece el registro.
   * */
  @NotBlank(message = "Debe especificar el No. de Control del Alumno")
  @Size(min = 8, max = 8,
    message = "El numero de control de alumno debe tener 8 caracteres")
  @Column(nullable = false, length = 8)
  private String alumnoNoControl;

  /**
   * Nombre del ALUMNO al que pertenece el registro.
   * */
  @NotBlank(message = "Debe especificar el Noombre del Alumno")
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
  @Min(value = 0, message = "Las asistencias no pueden ser negativas")
  @Max(value = 100, message = "Las asistencias no pueden ser mayores a 100")
  private int asistencias = 0;

  /**
   * Suma del tiempo que paso en las CONFERENCIAS en segundos.
   * */
  @Min(value = 0, message = "El tiempo asistido no puede ser negativo")
  private long tiempoAsistido = 0L;

  /**
   * Determina si el ALUMNO cumplio con los requerimientos para obtener su
   * acreditacion.
   * */
  private boolean cumplioRequerimientosDeAsistencia = false;

  /**
   * Determina si el ALUMNO fue acreditado por el ORGANIZADOR del CONGRESO.
   * <p>
   * Para ser acreditado debe cumplir con los requerimientos de ASISTENCIAS
   * establecidos en el CONGRESO (es decir
   * {@code cumplioRequerimientosDeAsistencia = true}).
   * */
  private boolean acreditado = false;



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
    String alumnoNombre,

    boolean excedente
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

    this.excedente           = excedente;
  }



  /**
   * Funcion constructora alternativa para crear registros.
   *
   * @return
   * El nuevo registro.
   */
  public static Boleto nuevo (
    Usuario actor,
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
      alumno.getNombreCompleto(),
      congreso.getInscritos() >= congreso.getCupo()
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
   * Cambia el estatus de PAGADO/NO_PAGADO de un BOLETO.
   *
   * @return
   * El registro actualizado.
   */
  public Boleto pagadoEstatus (
    Usuario actor, boolean estatus
  ) {
    setUsuarioEditoPagado(actor.getId());
    setPagado(estatus);
    return this;
  }



  /**
   * Cancela un registro.
   *
   * @return
   * El registro actualizado.
   */
  public Boleto cancelar () {
    return canceladoEstatus(CANCELADO);
  }

  /**
   * Cancela un registro.
   *
   * @return
   * El registro actualizado.
   */
  public Boleto canceladoEstatus (boolean estatus) {
    setCancelado(estatus);
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



  /**
   * Acredita el BOLETO, marcando que el ALUMNO fue aprobado por el ORGANIZADOR.
   *
   * @return
   * El registro actualizado.
   */
  public Boleto acreditar () {
    setAcreditado(true);
    return this;
  }



  /**
   * Edita el BOLETO (le suma la nueva duracion al total de tiempo asistido).
   *
   * @return
   * El registro actualizado.
   */
  public Boleto sumarTiempoAsistido (long duracion) {
    setTiempoAsistido(getTiempoAsistido() + duracion);
    return this;
  }



  /**
   * Verifica si el ALUMNO cumplio con los requerimientos de asistencia para
   * obtener su acreditacion y de ser asi marca el BOLETO.
   *
   * @param asistenciasRequeridas
   * Numero minimo de CONFERENCIAS distintas a las que debio asistir el ALUMNO.
   *
   * @param tiempoAsistidoRequerido
   * Tiempo total minimo asistido requerido, en segundos.
   *
   * @return
   * El registro actualizado.
   */
  public Boleto aplicarCumplimientoDeRequisitosDeAsistencia (
    int asistenciasRequeridas, long tiempoAsistidoRequerido
  ) {
    if (!cumplioRequerimientosDeAsistencia
        && asistencias >= asistenciasRequeridas
        && tiempoAsistido >= tiempoAsistidoRequerido) {
      setCumplioRequerimientosDeAsistencia(true);
    }
    return this;
  }
}
