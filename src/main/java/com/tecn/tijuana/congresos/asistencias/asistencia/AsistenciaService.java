package com.tecn.tijuana.congresos.asistencias.asistencia;

import com.tecn.tijuana.congresos.boletos.boleto.Boleto;
import com.tecn.tijuana.congresos.boletos.boleto.BoletoService;
import com.tecn.tijuana.congresos.eventos.conferencia.Conferencia;
import com.tecn.tijuana.congresos.eventos.conferencia.ConferenciaService;
import com.tecn.tijuana.congresos.eventos.congreso.Congreso;
import com.tecn.tijuana.congresos.eventos.congreso.CongresoService;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios
  .ControlDeUsuariosService;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Rol;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.utils.Api;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Clase principal de la capa de servicio para la entidad.
 */
@Service
public class AsistenciaService {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final AsistenciaRepository astRep;
  private final CongresoService congSvc;
  private final ConferenciaService confSvc;
  private final BoletoService bolSvc;
  private final ControlDeUsuariosService usrSvc;


  /**
   * CONSTRUCTOR principal de la clase/servicio, usado principalmente por Spring
   * para el funcionamiento de la app.
   *
   * @param astRep
   * Repositorio de DB de la entidad de ASISTENCIA usado para abstraer consultas
   * y operaciones de la BD.
   *
   * @param congSvc
   * Servicio de la entidad de CONGRESO.
   *
   * @param confSvc
   * Servicio de la entidad de CONFERENCIA.
   *
   * @param bolSvc
   * Servicio de la entidad de BOLETO.
   *
   * @param usrSvc
   * Servicio de la entidad de USUARIO.
   */
//  @Autowired
  public AsistenciaService (
    AsistenciaRepository astRep,
    CongresoService congSvc,
    ConferenciaService confSvc,
    BoletoService bolSvc,
    ControlDeUsuariosService usrSvc
  ) {
    this.astRep = astRep;
    this.congSvc = congSvc;
    this.confSvc = confSvc;
    this.bolSvc = bolSvc;
    this.usrSvc = usrSvc;
  }



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a un
   * CONGRESO, usando Numero de Control del ALUMNO en cuestion.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param noControl
   * Numero de Control del ALUMNO.
   *
   * @return
   * El registro creado.
   */
  public Boleto asistirCongresoConNoControl (
    Usuario actor, Long congresoId, String noControl
  )
    throws ResponseStatusException {

    // Encontrar BOLETO y continuar con la operacion.
    return asistirCongresoConBoleto(
      actor,
      bolSvc.afirmarIdCongresoNoControlAlumno(congresoId, noControl));
  }

  /**
   * Permite a l personal autorizado registrar la ENTRADA de un ALUMNO a un
   * CONGRESO, usando el Folio del BOLETO en cuestion.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param boletoFolio
   * Folio del BOLETO.
   *
   * @return
   * El registro creado.
   */
  public Boleto asistirCongresoConBoleto (
    Usuario actor, Long congresoId, String boletoFolio
  )
    throws ResponseStatusException {

    // Encontrar BOLETO y continuar con la operacion.
    return asistirCongresoConBoleto(
      actor, bolSvc.afirmarFolioIdCongreso(boletoFolio, congresoId));
  }

  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a un
   * CONGRESO, usando el BOLETO en cuestion.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param boleto
   * El BOLETO.
   *
   * @return
   * El registro creado.
   */
  public Boleto asistirCongresoConBoleto (
    Usuario actor, Boleto boleto
  )
    throws ResponseStatusException {

    // Encontrar el CONGRESO.
    var congreso = congSvc.afirmar(boleto.getCongresoId());

    // Comprobar acceso.
    if (actor.getRol() == Rol.ORGANIZADOR) {
      CongresoService.afirmarOrganizadorAsignado(actor, congreso);
    }

    // Comprobar que el BOLETO no este CANCELADO.
    BoletoService.afirmarNoCancelado(boleto);

    // Si el CONGRESO no es gratuito, comprobar que el BOLETO esta PAGADO.
    if (!congreso.isGratuito()) {
      BoletoService.afirmarPagado(boleto);
    }

    // Encontrar el ALUMNO del BOLETO.
    // Comprobar que el ALUMNO no este BLOQUEADO.
    var alumno = usrSvc.afirmarNoBloqueado(boleto.getAlumnoId());

    // Comprobar que el CONGRESO este PUBLICADO.
    // Comprobar que el CONGRESO no este CANCELADO.
    // Comprobar que el CONGRESO este en curso actualmente.
    CongresoService.afirmarPublicadoNoCanceladoEnCurso(congreso);

    // Si el BOLETO ya esta marcado, no es necesario repetir la operacion,
    // solo hay que regresar una respuesta exitosa.
    if (boleto.isUsado()) {
      return boleto;
    }

    // Incrementar contador de asistencias del CONGRESO.
    congSvc.sumarAsistencia(congreso);

    // Actualizar, guardar y retornar el registro.
    return bolSvc.marcar(boleto);
  }



  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a una
   * CONFERENCIA, usando el Numero de control del ALUMNO en cuestion.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param noControl
   * Numero de Control del ALUMNO.
   *
   * @return
   * El registro creado.
   */
  public Asistencia asistirConferenciaConNoControl (
    Usuario actor, Long conferenciaId, String noControl
  )
    throws ResponseStatusException {

    // Encontrar la CONFERENCIA.
    var conferencia = confSvc.afirmar(conferenciaId);

    // Encontrar el BOLETO y continuar con la operacion.
    return asistirConferenciaConBoleto(
      actor, conferencia,
      bolSvc.afirmarIdCongresoNoControlAlumno(
        conferencia.getCongresoId(), noControl));
  }

  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a una
   * CONFERENCIA, usando el Folio del BOLETO en cuestion.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param boletoFolio
   * Folio del  BOLETO.
   *
   * @return
   * El registro creado.
   */
  public Asistencia asistirConferenciaConBoleto (
    Usuario actor, Long conferenciaId, String boletoFolio
  )
    throws ResponseStatusException {

    // Encontrar el BOLETO y continuar con la operacion.
    return asistirConferenciaConBoleto(
      actor, conferenciaId, bolSvc.afirmarFolio(boletoFolio));
  }

  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a una
   * CONFERENCIA, usando el BOLETO en cuestion.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param boleto
   * El BOLETO.
   *
   * @return
   * El registro creado.
   */
  public Asistencia asistirConferenciaConBoleto (
    Usuario actor, Long conferenciaId, Boleto boleto
  )
    throws ResponseStatusException {

    // Encontrar la CONFERENCIA y continuar con la operacion.
    return asistirConferenciaConBoleto(
      actor, confSvc.afirmar(conferenciaId), boleto);
  }

  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a una
   * CONFERENCIA, usando el BOLETO en cuestion.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferencia
   * La CONFERENCIA.
   *
   * @param boleto
   * El BOLETO.
   *
   * @return
   * El registro creado.
   */
  public Asistencia asistirConferenciaConBoleto (
    Usuario actor, Conferencia conferencia, Boleto boleto
  )
    throws ResponseStatusException {

    // Encontrar el CONGRESO.
    var congreso = congSvc.afirmar(boleto.getCongresoId());

    // Comprobar acceso.
    if (actor.getRol() == Rol.ORGANIZADOR) {
      CongresoService.afirmarOrganizadorAsignado(actor, congreso);
    }

    // Comprobar que el CONGRESO no este CANCELADO.
    // Comprobar que el CONGRESO este en curso actualmente.
    CongresoService.afirmarNoCanceladoEnCurso(congreso);

    // Comprobar que el BOLETO no este CANCELADO.
    BoletoService.afirmarNoCancelado(boleto);

    // Si el CONGRESO no es gratuito, comprobar que el BOLETO esta PAGADO.
    if (!congreso.isGratuito()) {
      BoletoService.afirmarPagado(boleto);
    }

    // Encontrar el ALUMNO del BOLETO.
    // Comprobar que el ALUMNO no este BLOQUEADO.
    var alumno = usrSvc.afirmarNoBloqueado(boleto.getAlumnoId());

    // Encontrar la CONFERENCIA.
    // Comprobar que la CONFERENCIA no este CANCELADA.
    // Comprobar que la CONFERENCIA este en curso actualmente.
    ConferenciaService.afirmarPublicadaNoCanceladaEnCurso(conferencia);

    if (!boleto.isUsado()) {
      // Marcar el BOLETO.
      bolSvc.marcar(boleto);

      // Incrementar contador de asistencias del CONGRESO.
      congSvc.sumarAsistencia(congreso);
    }

    // Intentar encontrar una ASISTENCIA existente.
    var asistencia = qConferenciaIdAlumnoId(
      conferencia.getId(), boleto.getAlumnoId());

    // Si ya tenia ASISTENCIA previa.
    if (Objects.nonNull(asistencia)) {

      // Si hay una fecha de entrada previa, retornar sin mas.
      if (Objects.nonNull(asistencia.getFechaUltimaEntrada())) {
        return asistencia;
      }

      // Registrar nueva fecha de entrada, guardar y retornar registro.
      return astRep.saveAndFlush(asistencia.entrar());
    }

    // Estampar el BOLETO.
    bolSvc.estampar(boleto);

    // Incrementar contador de asistencias de la CONFERENCIA.
    confSvc.sumarAsistencia(conferencia);

    // Registrar y retornar la nueva AISTENCIA.
    return astRep.saveAndFlush(Asistencia.nuevo(
      actor, alumno, congreso, conferencia, boleto));
  }



  /**
   * Permite al personal autorizado registrar la SALIDA de un ALUMNO a una
   * CONFERENCIA, usando el Numero de control del ALUMNO en cuestion.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param noControl
   * Numero de Control del ALUMNO.
   *
   * @return
   * El registro creado.
   */
  public Asistencia salirDeConferenciaConNoControl (
    Usuario actor, Long conferenciaId, String noControl
  )
    throws ResponseStatusException {

    // Encontrar la CONFERENCIA.
    var conferencia = confSvc.afirmar(conferenciaId);

    // Encontrar el BOLETO y continuar con la operacion.
    return salirDeConferenciaConBoleto(
      actor, conferencia,
      bolSvc.afirmarIdCongresoNoControlAlumno(
        conferencia.getCongresoId(), noControl));
  }

  /**
   * Permite al personal autorizado registrar la SALIDA de un ALUMNO a una
   * CONFERENCIA, usando el Folio del BOLETO en cuestion.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param boletoFolio
   * Folio del  BOLETO.
   *
   * @return
   * El registro creado.
   */
  public Asistencia salirDeConferenciaConBoleto (
    Usuario actor, Long conferenciaId, String boletoFolio
  )
    throws ResponseStatusException {

    // Encontrar el BOLETO y continuar con la operacion.
    return salirDeConferenciaConBoleto(
      actor, conferenciaId, bolSvc.afirmarFolio(boletoFolio));
  }

  /**
   * Permite al personal autorizado registrar la SALIDA de un ALUMNO a una
   * CONFERENCIA, usando el BOLETO en cuestion.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param boleto
   * El BOLETO.
   *
   * @return
   * El registro creado.
   */
  public Asistencia salirDeConferenciaConBoleto (
    Usuario actor, Long conferenciaId, Boleto boleto
  )
    throws ResponseStatusException {

    // Encontrar la CONFERENCIA y continuar con la operacion.
    return salirDeConferenciaConBoleto(
      actor, confSvc.afirmar(conferenciaId), boleto);
  }

  /**
   * Permite al personal autorizado registrar la SALIDA de un ALUMNO a una
   * CONFERENCIA, usando el BOLETO en cuestion.
   * <p>
   * Actualiza la ASISTENCIA para que conste el tiempo total asistido a la
   * CONFERENCIA, sumando el tiempo transcurrido.
   * <p>
   * Tambien actualiza el BOLETO para que conste el tiempo total asistido a
   * todas las CONFERENCIAS.
   * <p>
   * Sino hay una entrada previa registrada en la ASISTENCIA, no se puede
   * registrar una salida, solo retorna el registro sin mas.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferencia
   * La CONFERENCIA.
   *
   * @param boleto
   * El BOLETO.
   *
   * @return
   * El registro creado.
   */
  public Asistencia salirDeConferenciaConBoleto (
    Usuario actor, Conferencia conferencia, Boleto boleto
  )
    throws ResponseStatusException {

    // Encontrar el CONGRESO.
    var congreso = congSvc.afirmar(boleto.getCongresoId());

    // Comprobar acceso.
    if (actor.getRol() == Rol.ORGANIZADOR) {
      CongresoService.afirmarOrganizadorAsignado(actor, congreso);
    }

    // Comprobar que el CONGRESO no este CANCELADO.
    // Comprobar que el CONGRESO este en curso actualmente.
    CongresoService.afirmarNoCanceladoEnCurso(congreso);

    // Comprobar que el BOLETO no este CANCELADO.
    BoletoService.afirmarNoCancelado(boleto);

    // Si el CONGRESO no es gratuito, comprobar que el BOLETO esta PAGADO.
    if (!congreso.isGratuito()) {
      BoletoService.afirmarPagado(boleto);
    }

    // Encontrar el ALUMNO del BOLETO.
    // Comprobar que el ALUMNO no este BLOQUEADO.
    var alumno = usrSvc.afirmarNoBloqueado(boleto.getAlumnoId());

    // Encontrar la CONFERENCIA.
    // Comprobar que la CONFERENCIA no este CANCELADA.
    // Comprobar que la CONFERENCIA este en curso actualmente.
    ConferenciaService.afirmarPublicadaNoCanceladaNoFutura(conferencia);

    // Encontrar la ASISTENCIA existente.
    var asistencia = afirmarConferenciaIdAlumnoId(
      conferencia.getId(), boleto.getAlumnoId());

    // Registrar y retornar la salida.
    return registrarSalida(asistencia, boleto, congreso, conferencia);
  }



  /**
   * Registra la salida de un ALUMNO de una CONFERENCIA, calculando y sumando
   * el tiempo asistido desde su ultima entrada al BOLETO y a la ASISTENCIA.
   * <p>
   * Si no habia una entrada activa ({@code fechaUltimaEntrada} nula), retorna
   * el registro sin modificaciones, sin lanzar ninguna excepcion.
   *
   * @param asistencia
   * La ASISTENCIA a actualizar.
   *
   * @param boleto
   * El BOLETO del ALUMNO (para acumular su tiempo total asistido).
   *
   * @param congreso
   * El CONGRESO (para verificar la acreditacion del ALUMNO tras sumar tiempo).
   *
   * @param conferencia
   * La CONFERENCIA (para evitar que el ALUMNO acumule tiempo de mas).
   *
   * @return
   * La ASISTENCIA actualizada.
   */
  private Asistencia registrarSalida (
    Asistencia asistencia, Boleto boleto, Congreso congreso,
    Conferencia conferencia
  ) {
    // Ultima fecha en que entro a la CONFERENCIA.
    var _ultima = asistencia.getFechaUltimaEntrada();

    // Si no hay una entrada previa activa, no hay nada que hacer.
    if (Objects.isNull(_ultima)) {
      return asistencia;
    }

    // Determinar la fecha de ultima entrada correcta a usar.
    // Esto nos permite proteger contra robo de tiempo pre-evento, pero nos deja
    // registrar la entrada al evento antes de que este empiece, para agilizar
    // el proceso..
    var ultima  = _ultima.isBefore(conferencia.getFechaInicio())
      ? conferencia.getFechaInicio()
      : _ultima;

    // Hora actual.
    var ahora = LocalDateTime.now();

    // Tomar como momento de salida el menor entre ahora y el fin de la
    // CONFERENCIA, para no acumular tiempo mas alla de cuando concluyo.
    var efectivo = ahora.isBefore(conferencia.getFechaFin())
      ? ahora
      : conferencia.getFechaFin();

    // Cuanto tiempo ha pasado desde que entro.
    var _duracion = Duration.between(ultima, efectivo).toSeconds();
    var duracion  = _duracion < 0 ? 0 : _duracion;

    // Tiempo total asistido a la CONFERENCIA actualmente.
    var asistido = asistencia.getTiempoAsistido();

    // Sumar la nueva duracion al tiempo total asistido.
    asistencia.setTiempoAsistido(asistido == 0
      ? duracion : asistido + duracion);

    // Remover fecha de entrada previa.
    asistencia.setFechaUltimaEntrada(null);

    // Actualizar y guardar el registro.
    var asistencia_2 = astRep.saveAndFlush(asistencia);

    // Sumar al BOLETO la misma duracion que se le sumo a la ASISTENCIA.
    // Verificar si el ALUMNO cumplio con los requerimientos de acreditacion.
    bolSvc.sumarTiempoAsistido(boleto, duracion, congreso);

    // Retornar el registro actualizado.
    return asistencia_2;
  }



  /**
   * Constar que un ALUMNO asistio toda la duracion de una CONFERENCIA.
   * <p>
   * Permite al personal correjir posibles contratiempos y constar que el ALUMNO
   * si asistio a la CONFERENCIA en su totalidad, aunque no se haya podido
   * registrar correctamente durante el evento.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param noControl
   * Numero de Control del ALUMNO.
   *
   * @return
   * El registro creado.
   */
  public Asistencia alumnoAsistioConferenciaCompletaConNoControl (
    Usuario actor, Long conferenciaId, String noControl
  )
    throws ResponseStatusException {

    // Encontrar la CONFERENCIA.
    var conferencia = confSvc.afirmar(conferenciaId);

    // Encontrar el BOLETO y continuar con la operacion.
    return alumnoAsistioConferenciaCompletaConBoleto(
      actor, conferencia,
      bolSvc.afirmarIdCongresoNoControlAlumno(
        conferencia.getCongresoId(), noControl));
  }

  /**
   * Constar que un ALUMNO asistio toda la duracion de una CONFERENCIA.
   * <p>
   * Permite al personal correjir posibles contratiempos y constar que el ALUMNO
   * si asistio a la CONFERENCIA en su totalidad, aunque no se haya podido
   * registrar correctamente durante el evento.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param boletoFolio
   * Folio del  BOLETO.
   *
   * @return
   * El registro creado.
   */
  public Asistencia alumnoAsistioConferenciaCompletaConBoleto (
    Usuario actor, Long conferenciaId, String boletoFolio
  )
    throws ResponseStatusException {

    // Encontrar el BOLETO y continuar con la operacion.
    return alumnoAsistioConferenciaCompletaConBoleto(
      actor, conferenciaId, bolSvc.afirmarFolio(boletoFolio));
  }

  /**
   * Constar que un ALUMNO asistio toda la duracion de una CONFERENCIA.
   * <p>
   * Permite al personal correjir posibles contratiempos y constar que el ALUMNO
   * si asistio a la CONFERENCIA en su totalidad, aunque no se haya podido
   * registrar correctamente durante el evento.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param boleto
   * El BOLETO.
   *
   * @return
   * El registro creado.
   */
  public Asistencia alumnoAsistioConferenciaCompletaConBoleto (
    Usuario actor, Long conferenciaId, Boleto boleto
  )
    throws ResponseStatusException {

    // Encontrar la CONFERENCIA y continuar con la operacion.
    return alumnoAsistioConferenciaCompletaConBoleto(
      actor, confSvc.afirmar(conferenciaId), boleto);
  }

  /**
   * Constar que un ALUMNO asistio toda la duracion de una CONFERENCIA.
   * <p>
   * Permite al personal correjir posibles contratiempos y constar que el ALUMNO
   * si asistio a la CONFERENCIA en su totalidad, aunque no se haya podido
   * registrar correctamente durante el evento.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferencia
   * La CONFERENCIA.
   *
   * @param boleto
   * El BOLETO.
   *
   * @return
   * El registro creado.
   */
  public Asistencia alumnoAsistioConferenciaCompletaConBoleto (
    Usuario actor, Conferencia conferencia, Boleto boleto
  )
    throws ResponseStatusException {

    // Encontrar el CONGRESO.
    var congreso = congSvc.afirmar(boleto.getCongresoId());

    // Comprobar acceso.
    if (actor.getRol() == Rol.ORGANIZADOR) {
      CongresoService.afirmarOrganizadorAsignado(actor, congreso);
    }

    // Comprobar que el CONGRESO no este CANCELADO.
    // Comprobar que el CONGRESO este en curso actualmente.
    CongresoService.afirmarNoCanceladoEnCurso(congreso);

    // Comprobar que el BOLETO no este CANCELADO.
    BoletoService.afirmarNoCancelado(boleto);

    // Si el CONGRESO no es gratuito, comprobar que el BOLETO esta PAGADO.
    if (!congreso.isGratuito()) {
      BoletoService.afirmarPagado(boleto);
    }

    // Encontrar el ALUMNO del BOLETO.
    // Comprobar que el ALUMNO no este BLOQUEADO.
    var alumno = usrSvc.afirmarNoBloqueado(boleto.getAlumnoId());

    // Encontrar la CONFERENCIA.
    // Comprobar que la CONFERENCIA no este CANCELADA.
    // Comprobar que la CONFERENCIA este en curso actualmente.
    ConferenciaService.afirmarNoCanceladaConcluida(conferencia);

    if (!boleto.isUsado()) {
      // Marcar el BOLETO.
      bolSvc.marcar(boleto);

      // Incrementar contador de asistencias del CONGRESO.
      congSvc.sumarAsistencia(congreso);
    }

    // Intentar encontrar una ASISTENCIA existente.
    var asistencia = qConferenciaIdAlumnoId(
      conferencia.getId(), boleto.getAlumnoId());

    // Si no tenia ASISTENCIA previa registrarla.
    if (Objects.isNull(asistencia)) {
      asistencia = astRep.saveAndFlush(Asistencia.nuevo(
        actor, alumno, congreso, conferencia, boleto));

      // Estampar el BOLETO.
      bolSvc.estampar(boleto);

      // Incrementar contador de asistencias de la CONFERENCIA.
      confSvc.sumarAsistencia(conferencia);
    }

    // Tiempo asistido previamente antes de esta operacion/correccion.
    var tiempoAsistidoPrevio = asistencia.getTiempoAsistido();

    // Calcular la duracion de la CONFERENCIA.
    var duracion = Duration.between(
        conferencia.getFechaInicio(),
        conferencia.getFechaFin())
      .toSeconds();

    // Remover fecha de entrada.
    asistencia.setFechaUltimaEntrada(null);

    // Establecer tiempo asistido total como la duracion completa de la
    // CONFERENCIA.
    asistencia.setTiempoAsistido(duracion);

    // Guardar el registro.
    var asistencia_2 = astRep.saveAndFlush(asistencia);

    // Actualizar el tiempo asistido del BOLETO.
    // Si ya habia una duracion previa de ASISTENCIA, se resta de la duracion
    // total de CONFERENCIA lo cual sirve como correccion.
    // Verificar si el ALUMNO cumplio con los requerimientos de acreditacion.
    // Actualizar y guardar el BOLETO.
    var boleto_2 = bolSvc.sumarTiempoAsistido(
      boleto, duracion - tiempoAsistidoPrevio, congreso);

    // Retornar el registro actualizado.
    return asistencia_2;
  }



  //----------------------------------------------------------------------------
  // TRANSICION MASIVA DE CONFERENCIA.

  /**
   * Registra la transicion masiva de ALUMNOS de una CONFERENCIA de origen a
   * una CONFERENCIA de destino, usando una lista blanca o negra de Folios de
   * BOLETO.
   * <p>
   * Lista blanca:
   *   Solo pasan a la nueva CONFERENCIA los ALUMNOS cuyo Folio de BOLETO
   *   aparezca en la lista.
   * <p>
   * Lista negra:
   *   Pasan todos excepto los que aparezcan en la lista.
   * <p>
   * Esta funcion esta envuelta en una transaccion de base de datos. Los errores
   * estructurales (CONGRESO o CONFERENCIA no encontrada, sin autorizacion, etc)
   * son lanzados inmediatamente, revertiendo todos los cambios de la operacion.
   * Los errores por-folio (ALUMNO bloqueado, BOLETO cancelado, etc.) son
   * registrados en la lista de "omitidos" y no interrumpen el proceso.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferenciaAnteriorId
   * ID de la CONFERENCIA de origen.
   *
   * @param conferenciaPosteriorId
   * ID de la CONFERENCIA de destino.
   *
   * @param folios
   * Lista de Folios de BOLETO (0-100 elementos).
   *
   * @param listaBlanca
   * {@code true} = lista blanca; {@code false} = lista negra.
   *
   * @return
   * <p>{
   * <p>  "conferencia_anterior"  : CONFERENCIA de origen actualizada,
   * <p>  "conferencia_posterior" : CONFERENCIA de destino actualizada,
   * <p>  "asistencia_anterior"   : Lista de ASISTENCIAS de origen procesadas,
   * <p>  "asistencia_posterior"  : Lista de ASISTENCIAS de destino procesadas,
   * <p>  "omitidos"              : Lista de {folioBoleto, razon} no procesados.
   * <p>}
   */
  @Transactional
  public Map<String, Object> transicionarConferenciaConFolios (
    Usuario actor,
    Long conferenciaAnteriorId,
    Long conferenciaPosteriorId,
    List<String> folios,
    boolean listaBlanca
  )
    throws ResponseStatusException {

    // Ambas conferencias deben ser distintas.
    if (conferenciaAnteriorId.equals(conferenciaPosteriorId)) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "La conferencia de origen y la de destino no pueden ser la misma");
    }

    // Validar CONFERENCIA anterior.
    // Debe existir, estar publicada y no estar cancelada.
    // No se exige que este en curso porque es valido transicionar mientras la
    // conferencia anterior esta por concluir o acaba de concluir.
    var confAnterior = confSvc.afirmarPublicadaNoCancelada(
      conferenciaAnteriorId);

    // Validar CONFERENCIA posterior.
    // Debe existir, estar publicada, no estar cancelada y estar en curso.
    var confPosterior = confSvc.afirmarPublicadaNoCanceladaEnCurso(
      conferenciaPosteriorId);

    // Ambas conferencias deben pertenecer al mismo CONGRESO.
    if (!confAnterior.getCongresoId().equals(confPosterior.getCongresoId())) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Las conferencias no pertenecen al mismo congreso");
    }

    // Encontrar el CONGRESO comun a ambas.
    var congreso = congSvc.afirmar(confAnterior.getCongresoId());
    // Comprobar que el actor tiene acceso al CONGRESO.
    if (actor.getRol() == Rol.ORGANIZADOR) {
      CongresoService.afirmarOrganizadorAsignado(actor, congreso);
    }
    // Comprobar que este en curso, publicado y no cancelado.
    CongresoService.afirmarPublicadoNoCanceladoEnCurso(congreso);


    // Seleccionar candidatos de la CONFERENCIA anterior.
    // Lista blanca vacia => solo los presentes en el aula pasan.
    // Lista negra vacia  => todos pasan.
    List<Asistencia> candidatas;
    if (folios == null || folios.isEmpty()) {
      candidatas = listaBlanca
        ? astRep.qConferenciaIdListPresente(conferenciaAnteriorId)
        : astRep.qConferenciaIdList(conferenciaAnteriorId);
    } else {
      candidatas = listaBlanca
        ? astRep.qConferenciaIdBoletoFolioIn(conferenciaAnteriorId, folios)
        : astRep.qConferenciaIdBoletoFolioNotIn(conferenciaAnteriorId, folios);
    }

    // Listas de resultados.
    List<Asistencia> asistenciasAnteriores = new ArrayList<>();
    List<Asistencia> asistenciasPosteriores = new ArrayList<>();
    List<Map<String, String>> omitidos = new ArrayList<>();

    for (Asistencia astAnterior : candidatas) {

      var folio = astAnterior.getBoletoFolio();

      try {

        // Obtener el BOLETO.
        var boleto = bolSvc.afirmarFolio(folio);

        // Comprobar que el BOLETO pertenece al CONGRESO en cuestion.
        BoletoService.afirmarIdCongreso(boleto, congreso.getId());

        // Comprobar que el BOLETO no este CANCELADO.
        BoletoService.afirmarNoCancelado(boleto);

        // Si el CONGRESO no es gratuito, comprobar que el BOLETO este PAGADO.
        if (!congreso.isGratuito()) {
          BoletoService.afirmarPagado(boleto);
        }

        // Comprobar que el ALUMNO no este BLOQUEADO.
        usrSvc.afirmarNoBloqueado(boleto.getAlumnoId());

        // Marcar SALIDA de la CONFERENCIA anterior.
        // Si no tenia entrada activa, el registro retorna sin cambios; de
        // todas formas se intenta la entrada a la CONFERENCIA posterior
        // (comportamiento identico a la API original).
        astAnterior = registrarSalida(
          astAnterior, boleto, congreso, confAnterior);
        asistenciasAnteriores.add(astAnterior);

        // Registrar ENTRADA a la CONFERENCIA posterior.
        // Reutiliza la logica existente, que maneja de forma idempotente el
        // caso en que el ALUMNO ya tenga una entrada activa en la conferencia
        // de destino (retorna el registro pre-existente sin modificarlo).
        var astPosterior = asistirConferenciaConBoleto(
          actor, confPosterior, boleto);
        asistenciasPosteriores.add(astPosterior);

      } catch (ResponseStatusException ex) {

        // Error por-folio: registrar en omitidos sin interrumpir el proceso.
        Map<String, String> omitido = new LinkedHashMap<>();
        omitido.put("folioBoleto", folio);
        omitido.put("razon", Objects.nonNull(ex.getReason())
          ? ex.getReason()
          : ex.getMessage());
        omitidos.add(omitido);
      }
    }

    // Recargar las conferencias para reflejar contadores actualizados.
    var confAnteriorFinal = confSvc.afirmar(conferenciaAnteriorId);
    var confPosteriorFinal = confSvc.afirmar(conferenciaPosteriorId);

    // Armar respuesta.
    Map<String, Object> resultado = new LinkedHashMap<>();
    resultado.put("conferencia_anterior",  confAnteriorFinal);
    resultado.put("conferencia_posterior", confPosteriorFinal);
    resultado.put("asistencia_anterior",   asistenciasAnteriores);
    resultado.put("asistencia_posterior",  asistenciasPosteriores);
    resultado.put("omitidos",              omitidos);

    return resultado;
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Consulta el registro con los identificadores especificados.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param alumnoId
   * ID del ALUMNO.
   *
   * @return
   * El registro o {@code null} si no existe.
   */
  public Asistencia qConferenciaIdAlumnoId (
    Long conferenciaId, Long alumnoId
  ) {
    return astRep
      .qConferenciaIdAlumnoId(conferenciaId, alumnoId)
      .orElse(null);
  }



  /**
   * Permite a un ALUMNO consultar sus ASISTENCIAS respecto a un CONGRESO.
   *
   * @param actor
   * Usuario responsable de la operacion.
   *
   * @param congresoId
   * ID del CONGRESO.
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
  public List<Asistencia> qCongresoIdMios (
    Usuario actor, Long congresoId, int page, int pageSize
  ) {
    return astRep
      .qCongresoIdAlumnoId(
        congresoId, actor.getId(), Api.pagina(page, pageSize))
      .getContent();
  }



  /**
   * Permite consultar las ASISTENCIAS a una CONFERENCIA.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param minTiempoAsistido
   * Filtro de tiempo minimo asistido en segundos, opcional.
   * Rango: 0 a 144000 (40 horas).
   *
   * @param presente
   * Filtro de estado de asistencia, opcional.
   * {@code true}  = solo registros con fechaUltimaEntrada != null (presente).
   * {@code false} = solo registros con fechaUltimaEntrada == null (ausente).
   * {@code null}  = sin filtro.
   *
   * @param txt
   * Filtro de texto libre, opcional. Busca en alumnoNombre, alumnoNoControl,
   * creadorNombre, boletoFolio y boletoFolioLargo.
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
  public List<Asistencia> qConferenciaId (
    Long conferenciaId, Long minTiempoAsistido, Boolean presente,
    String txt, int page, int pageSize
  ) {
    var pageable = Api.pagina(page, pageSize);

    var t = (Objects.isNull(txt) || txt.isBlank())
      ? null
      : txt.toLowerCase().trim();

    // Si no se especifico un tiempo minimo asistido.
    if (minTiempoAsistido == null) {
      if (presente == null) // Indiferente.
        return t == null
          ? astRep.qConferenciaId(conferenciaId, pageable).getContent()
          : astRep.buscarConferenciaId(
            conferenciaId, t, pageable).getContent();
      if (presente) // Presente.
        return t == null
          ? astRep.qConferenciaIdPresente(conferenciaId, pageable).getContent()
          : astRep.buscarConferenciaIdPresente(
            conferenciaId, t, pageable).getContent();
      // Ausente.
      return t == null
        ? astRep.qConferenciaIdAusente(conferenciaId, pageable).getContent()
        : astRep.buscarConferenciaIdAusente(
          conferenciaId, t, pageable).getContent();
    }
    if (presente == null) // Indiferente.
      return t == null
        ? astRep.qConferenciaIdMinTiempoAsistido(
            conferenciaId, minTiempoAsistido, pageable).getContent()
        : astRep.buscarConferenciaIdMinTiempoAsistido(
            conferenciaId, minTiempoAsistido, t, pageable).getContent();
    if (presente) // Presente.
      return t == null
        ? astRep.qConferenciaIdMinTiempoAsistidoPresente(
            conferenciaId, minTiempoAsistido, pageable).getContent()
        : astRep.buscarConferenciaIdMinTiempoAsistidoPresente(
            conferenciaId, minTiempoAsistido, t, pageable).getContent();
    // Ausente.
    return t == null
      ? astRep.qConferenciaIdMinTiempoAsistidoAusente(
          conferenciaId, minTiempoAsistido, pageable).getContent()
      : astRep.buscarConferenciaIdMinTiempoAsistidoAusente(
          conferenciaId, minTiempoAsistido, t, pageable).getContent();
  }



  /**
   * Permite consultar las ASISTENCIAS de un BOLETO via su Folio Largo.
   * Retorna tambien el BOLETO y el CONGRESO correspondientes.
   *
   * @param folio
   * Folio Largo del BOLETO.
   *
   * @param page
   * Numero de pagina.
   *
   * @param pageSize
   * Tamano de pagina.
   *
   * @return
   * <p>{
   * <p>  "boleto"     : BOLETO,
   * <p>  "congreso"   : CONGRESO,
   * <p>  "asistencia" : ASISTENCIAS,
   * <p>}
   */
  public Map<String, Object> qBoletoFolioLargo (
    String folio, int page, int pageSize
  ) {
    // ENcontrar BOLETO.
    var boleto = bolSvc.afirmarFolioLargo(folio);
    // Encontrar CONGRESO.
    var congreso = congSvc.afirmar(boleto.getCongresoId());
    // Encontrar ASISTENCIAS.
    var asistencias = astRep
      .qBoletoFolioLargo(folio, Api.pagina(page, pageSize))
      .getContent();

    // Armar respuesta.
    Map<String, Object> resultado = new LinkedHashMap<>();
    resultado.put("congreso", congreso);
    resultado.put("boleto", boleto);
    resultado.put("asistencia", asistencias);

    // Retornar respuesta exitosa.
    return resultado;
  }



  //----------------------------------------------------------------------------
  // ASERCIONES.

  /**
   * El registro con el ID especificado.
   *
   * @param id
   * El ID del registro.
   *
   * @return
   * El registro encontrado.
   *
   * @throws ResponseStatusException
   * {@code HTTP-NOT_FOUND} si no se encuentra el registro.
   */
  public Asistencia afirmar (
    Long id
  )
    throws ResponseStatusException {

    return astRep.findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format("Asistencia con ID: %s no encontrada", id)));
  }



  /**
   * El registro con los identificadores especificados.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param alumnoId
   * ID del ALUMNO.
   *
   * @return
   * El registro encontrado.
   *
   * @throws ResponseStatusException
   * {@code HTTP-NOT_FOUND} si no se encuentra el registro.
   */
  public Asistencia afirmarConferenciaIdAlumnoId (
    Long conferenciaId, Long alumnoId
  )
    throws ResponseStatusException {

    return astRep.qConferenciaIdAlumnoId(conferenciaId, alumnoId)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Asistencia no encontrada"));
  }
}
