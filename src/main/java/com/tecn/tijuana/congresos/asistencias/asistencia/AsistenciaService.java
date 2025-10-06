package com.tecn.tijuana.congresos.asistencias.asistencia;

import com.tecn.tijuana.congresos.boletos.boleto.Boleto;
import com.tecn.tijuana.congresos.boletos.boleto.BoletoService;
import com.tecn.tijuana.congresos.eventos.conferencia.Conferencia;
import com.tecn.tijuana.congresos.eventos.conferencia.ConferenciaService;
import com.tecn.tijuana.congresos.eventos.congreso.CongresoService;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios
  .ControlDeUsuariosService;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Rol;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.utils.Api;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    // Encontrar el ALUMNO del BOLETO.
    // Comprobar que el ALUMNO no este BLOQUEADO.
    var alumno = usrSvc.afirmarNoBloqueado(boleto.getAlumnoId());

    // Comprobar que el CONGRESO no este CANCELADO.
    // Comprobar que el CONGRESO este en curso actualmente.
    CongresoService.afirmarNoCanceladoEnCurso(congreso);

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

    // Encontrar el ALUMNO del BOLETO.
    // Comprobar que el ALUMNO no este BLOQUEADO.
    var alumno = usrSvc.afirmarNoBloqueado(boleto.getAlumnoId());

    // Encontrar la CONFERENCIA.
    // Comprobar que la CONFERENCIA no este CANCELADA.
    // Comprobar que la CONFERENCIA este en curso actualmente.
    ConferenciaService.afirmarNoCanceladaEnCurso(conferencia);

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

    // Encontrar el ALUMNO del BOLETO.
    // Comprobar que el ALUMNO no este BLOQUEADO.
    var alumno = usrSvc.afirmarNoBloqueado(boleto.getAlumnoId());

    // Encontrar la CONFERENCIA.
    // Comprobar que la CONFERENCIA no este CANCELADA.
    // Comprobar que la CONFERENCIA este en curso actualmente.
    ConferenciaService.afirmarNoCanceladaEnCurso(conferencia);

    // Encontrar la ASISTENCIA existente.
    var asistencia = afirmarConferenciaIdAlumnoId(
      conferencia.getId(), boleto.getAlumnoId());

    // Actualizar, guardar y retornar el registro.
    return astRep.saveAndFlush(asistencia.salir());
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

    // Actualizar, guardar y retornar el registro.
    return astRep.saveAndFlush(
      asistencia.asistioConferenciaCompleta(conferencia));
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
    Long conferenciaId, int page, int pageSize
  ) {
    return astRep
      .qConferenciaId(conferenciaId, Api.pagina(page, pageSize))
      .getContent();
  }



  /**
   * Permite consultar las ASISTENCIAS de un BOLETO via su Folio Largo.
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
   * Lista de registros encontrados.
   */
  public List<Asistencia> qBoletoFolioLargo (
    String folio, int page, int pageSize
  ) {
    return astRep
      .qBoletoFolioLargo(folio, Api.pagina(page, pageSize))
      .getContent();
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
