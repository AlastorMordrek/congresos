package com.tecn.tijuana.congresos.boletos.boleto;

import com.tecn.tijuana.congresos.boletos.boleto.dto.BoletoInscribirseDto;
import com.tecn.tijuana.congresos.boletos.boleto.dto.RegistroBoletoDto;
import com.tecn.tijuana.congresos.eventos.congreso.Congreso;
import com.tecn.tijuana.congresos.eventos.congreso.CongresoService;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.ControlDeUsuariosService;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Rol;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.utils.Api;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;


/**
 * Clase principal de la capa de servicio para la entidad.
 */
@Service
public class BoletoService {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final BoletoRepository bolRep;
  private final CongresoService congSvc;
  private final ControlDeUsuariosService usrSvc;


  /**
   * CONSTRUCTOR principal de la clase/servicio, usado principalmente por Spring
   * para el funcionamiento de la app.
   *
   * @param bolRep
   * Repositorio de DB de la entidad de BOLETO usado para abstraer consultas
   * y operaciones de la BD.
   *
   * @param congSvc
   * Servicio de la entidad de CONGRESO.
   *
   * @param usrSvc
   * Servicio de la entidad de USUARIO.
   */
//  @Autowired
  public BoletoService (
    BoletoRepository bolRep,
    CongresoService congSvc,
    ControlDeUsuariosService usrSvc
  ) {
    this.bolRep = bolRep;
    this.congSvc = congSvc;
    this.usrSvc = usrSvc;
  }



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite ALUMNOS inscribirse a un CONGRESO.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param dto
   * Objeto con los datos del BOLETO.
   *
   * @return
   * El registro creado.
   */
  public Boleto inscribirse (
    Usuario actor, BoletoInscribirseDto dto
  )
    throws ResponseStatusException {

    return inscribirse(actor, dto.congresoId);
  }

  /**
   * Permite ALUMNOS inscribirse a un CONGRESO.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param congresoId
   * ID del CONGRESO al que desea inscribirse.
   *
   * @return
   * El registro creado.
   */
  public Boleto inscribirse (
    Usuario actor, Long congresoId
  )
    throws ResponseStatusException {

    // Comprobar que el ALUMNO no esta inscrito ya a ese CONGRESO.
    afirmarAlumnoNoInscrito(congresoId, actor.getId());

    // Comprobar que el CONGRESO existe y validarlo.
    var congreso = congSvc
      .afirmarNoCanceladoPublicadoEnPeriodoDeInscripcionesConCupoDisponible(
        congresoId);

    // Crear y guardar el registro nuevo.
    var reg = bolRep.saveAndFlush(Boleto.nuevo(actor, congreso, actor));

    // Aumentar el contador de inscripciones al CONGRESO.
    congSvc.sumarInscripcion(congreso);

    // Retornar el registro nuevo.
    return reg;
  }



  /**
   * Permite a ORGANIZADORES inscribir ALUMNOS a un CONGRESO.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param dto
   * Datos del registro.
   *
   * @return
   * El registro creado.
   */
  public Boleto inscribir (
    Usuario actor, RegistroBoletoDto dto
  )
    throws ResponseStatusException {

    // Aux.
    Usuario alumno;

    // Encontrar el ALUMNO mediante el identificador provisto.
    if (Objects.nonNull(dto.getAlumnoId())) {
      alumno = usrSvc.afirmarAlumno(dto.getAlumnoId());
    }
    else if (Objects.nonNull(dto.getAlumnoNoControl())) {
      alumno = usrSvc.afirmarNoControlAlumno(dto.getAlumnoNoControl());
    } else {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Debe especificar un ID o Numero de Control de alumno");
    }

    // Aux.
    var alumnoId = alumno.getId();

    // Encontrar CONGRESO.
    var congreso = congSvc.afirmar(dto.getCongresoId());

    // Comprobar acceso.
    if (actor.getRol() == Rol.ORGANIZADOR) {
      CongresoService.afirmarOrganizadorAsignado(actor, congreso);
    }

    // Comprobar que el ALUMNO no esta inscrito ya a ese CONGRESO.
    afirmarAlumnoNoInscrito(dto.getCongresoId(), alumnoId);

    // Comprobar que el CONGRESO existe y validarlo.
    // Si el Actor lo indico, permitir inscripcion de BOLETO excedente.
    if (dto.registrarComoExcedente) {
      CongresoService.afirmarNoCanceladoFechaFinFutura(congreso);
    } else {
      CongresoService.afirmarNoCanceladoConCupoDisponibleFechaFinFutura(
        congreso);
    }

    // Crear y guardar el registro nuevo.
    var reg = bolRep.saveAndFlush(Boleto.nuevo(actor, congreso, alumno));

    // Aumentar el contador de inscripciones al CONGRESO.
    congSvc.sumarInscripcion(congreso);

    // Retornar el registro nuevo.
    return reg;
  }



  /**
   * Cancela un registro segun el estatus especificado.
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param id
   * ID del registro a editar.
   *
   * @return
   * El registro cancelado.
   */
  public Boleto cancelarMio (
    Usuario actor, Long id
  )
    throws ResponseStatusException {

    // Encontrar el BOLETO y comprobar acceso.
    var boleto = afirmarIdAlumno(actor, id);

    // Encontrar y validar CONGRESO.
    var congreso =
      CongresoService.afirmarFechaInicioFutura(
        CongresoService.afirmarPublicado(
          CongresoService.afirmarNoCancelado(
            congSvc.afirmar(boleto.getCongresoId()))));

    // Actualizar, guardar y retornar el registro.
    return bolRep.saveAndFlush(boleto.cancelar());
  }



  /**
   * Marca un registro como PAGADO/NO_PAGADO segun el estatus especificado.
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param id
   * ID del registro a editar.
   *
   * @param estatus
   * {@code true} = PAGADO.
   * {@code false} = NO_PAGADO.
   *
   * @return
   * El registro actualizado.
   */
  public Boleto pagado (
    Usuario actor, Long id, boolean estatus
  )
    throws ResponseStatusException {

    // Encontrar el BOLETO.
    var boleto = afirmar(id);

    // Comprobar permisos.
    if (actor.getRol() == Rol.ORGANIZADOR) {
      afirmarIdOrganizador(actor, boleto);
    }

    // Encontrar y validar CONGRESO.
    var congreso =
      CongresoService.afirmarFechaFinFutura(
        congSvc.afirmar(boleto.getCongresoId()));

    // Actualizar, guardar y retornar el registro.
    return bolRep.saveAndFlush(boleto.pagadoEstatus(actor, estatus));
  }



  /**
   * Cancela/restaura un registro segun el estatus especificado.
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param id
   * ID del registro a editar.
   *
   * @param estatus
   * {@code true} = cancelado.
   * {@code false} = restaurado.
   *
   * @return
   * El registro cancelado/retractado.
   */
  public Boleto cancelado (
    Usuario actor, Long id, boolean estatus
  )
    throws ResponseStatusException {

    // Encontrar el BOLETO.
    var boleto = afirmar(id);

    // Comprobar permisos.
    if (actor.getRol() == Rol.ORGANIZADOR) {
      afirmarIdOrganizador(actor, boleto);
    }

    // Encontrar y validar CONGRESO.
    var congreso =
      CongresoService.afirmarFechaFinFutura(
        congSvc.afirmar(boleto.getCongresoId()));

    // Actualizar, guardar y retornar el registro.
    return bolRep.saveAndFlush(boleto.canceladoEstatus(estatus));
  }



  /**
   * Marca el BOLETO como usado, es decir, que se uso para entrar al CONGRESO
   * durante el evento.
   *
   * @param boleto
   * El registro.
   *
   *  @return
   * El registro marcado.
   */
  public Boleto marcar (
    Boleto boleto
  )
    throws ResponseStatusException {

    // Actualizar, guardar y retornar el registro.
    return bolRep.saveAndFlush(boleto.marcar());
  }



  /**
   * Estampa el BOLETO, es decir, se le suma una asistencia a una nueva
   * CONFERENCIA.
   *
   * @param boleto
   * El registro.
   *
   *  @return
   * El registro marcado.
   */
  public Boleto estampar (
    Boleto boleto
  )
    throws ResponseStatusException {

    // Actualizar, guardar y retornar el registro.
    return bolRep.saveAndFlush(boleto.estampar());
  }



  /**
   * Acredita a un ALUMNO a travez de su BOLETO, indicando que cumplio con los
   * requerimientos del evento.
   * <p>
   * Solo es posible si el ALUMNO ya cumplio con los requerimientos de
   * ASISTENCIA del CONGRESO ({@code cumplioRequerimientosDeAsistencia = true}).
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param id
   * ID del registro.
   *
   * @return
   * El registro actualizado.
   *
   * @throws ResponseStatusException
   * {@code HTTP-UNAUTHORIZED}
   * Si el actor no es el ORGANIZADOR del CONGRESO.
   * {@code HTTP-PRECONDITION_FAILED}
   * Si el ALUMNO no ha cumplido con los requerimientos de asistencia.
   * {@code HTTP-PRECONDITION_FAILED}
   * Si el CONGRESO aun no ha concluido.
   */
  public Boleto acreditar (
    Usuario actor, Long id
  ) throws ResponseStatusException {

    // Encontrar el BOLETO.
    var boleto = afirmar(id);

    // Encontrar el CONGRESO.
    var congreso = congSvc.afirmar(boleto.getCongresoId());

    // Comprobar permisos.
    CongresoService.afirmarOrganizadorAsignado(actor, congreso);

    // IDEMPOTENCIA.
    if (boleto.isAcreditado()) {
      return boleto;
    }

    // Comprobar que el BOLETO no esta cancelado.
    afirmarNoCancelado(boleto);

    // Comprobar que el CONGRESO no fue cancelado y que ya concluyo.
    CongresoService.afirmarConcluido(
      CongresoService.afirmarNoCancelado(congreso));

    // Encontrar ALUMNO y comprobar que no esta bloqueado.
    var alumno = usrSvc.afirmarNoBloqueado(boleto.getAlumnoId());

    // Comprobar que el ALUMNO cumple los requerimientos de ASISTENCIA.
    afirmarCumplioRequerimientosDeAsistencia(boleto);

    // Actualizar, guardar y retornar el registro.
    return bolRep.saveAndFlush(boleto.acreditar());
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Permite a un ALUMNO consultar sus BOLETOS.
   *
   * @param actor
   * Usuario responsable de la operacion.
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
  public List<Boleto> qMios (
    Usuario actor, int page, int pageSize
  ) {
    return bolRep
      .qAlumnoId(actor.getId(), Api.pagina(page, pageSize))
      .getContent();
  }



  /**
   * Consulta los BOLETOS de un ALUMNO.
   *
   * @param actor
   * Usuario responsable de la operacion.
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
  public List<Boleto> qIdAlumno (
    Usuario actor, Long alumnoId, int page, int pageSize
  ) {
    return bolRep
      .qAlumnoId(alumnoId, Api.pagina(page, pageSize))
      .getContent();
  }



  /**
   * Consulta los registros de un CONGRESO usando filtros opcionales de texto y
   * de campos booleanos.
   *
   * <p>Si ningun filtro es especificado, retorna todos los registros del
   * CONGRESO. Si al menos un filtro es especificado, se usa la consulta
   * condicional que evalua solo los filtros activos (no-nulos).
   *
   * @param txt
   * Texto a buscar en los campos de texto del BOLETO.
   * {@code null} o vacio para no filtrar.
   *
   * @param alumnoId
   * Filtro opcional. {@code null} para no filtrar.
   *
   * @param congresoId
   * Filtro opcional. {@code null} para no filtrar.
   *
   * @param excedente
   * Filtro opcional. {@code null} para no filtrar.
   *
   * @param pagado
   * Filtro opcional. {@code null} para no filtrar.
   *
   * @param cancelado
   * Filtro opcional. {@code null} para no filtrar.
   *
   * @param usado
   * Filtro opcional. {@code null} para no filtrar.
   *
   * @param cumplioRequerimientosDeAsistencia
   * Filtro opcional. {@code null} para no filtrar.
   *
   * @param acreditado
   * Filtro opcional. {@code null} para no filtrar.
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
  public List<Boleto> qFiltrado (
    String txt, Long alumnoId, Long congresoId,
    Boolean excedente, Boolean pagado,
    Boolean cancelado, Boolean usado, Boolean cumplioRequerimientosDeAsistencia,
    Boolean acreditado,
    int page, int pageSize
  ) {
    Pageable pg = Api.pagina(page, pageSize);

    // Normalizar txt: vacio o blanco se convierte en null para que la consulta
    // condicional lo ignore.
    String txtN = (Objects.isNull(txt) || txt.isBlank())
      ? null
      : txt.toLowerCase().trim();

    // Si no hay ningun filtro activo, usar la consulta simple.
    boolean sinFiltros = txtN == null && alumnoId == null && congresoId == null
      && excedente == null && pagado == null && cancelado == null
      && usado == null && cumplioRequerimientosDeAsistencia == null
      && acreditado == null;

    if (sinFiltros) {
      return bolRep.q(pg).getContent();
    }

    // Usar la consulta condicional que evalua solo los filtros activos.
    return bolRep
      .qFiltrado(
        txtN, alumnoId, congresoId, excedente, pagado, cancelado, usado,
        cumplioRequerimientosDeAsistencia, acreditado,
        pg)
      .getContent();
  }



  /**
   * Consulta los registros de un CONGRESO.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param estatus
   * Estatus del BOLETO.
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
  public List<Boleto> qIdCongresoCancelado (
    Long congresoId, boolean estatus, int page, int pageSize
  ) {
    return bolRep
      .qCongresoIdCancelado(congresoId, estatus, Api.pagina(page, pageSize))
      .getContent();
  }



  /**
   * Consulta los registros usando una busqueda de texto.
   *
   * @param txt
   * El texto a buscar.
   *
   * @param page
   * Numero de pagina (0-based)
   *
   * @param pageSize
   * Cantidad de resultados por pagina
   *
   * @return
   * Lista de registros encontrados.
   */
  public List<Boleto> buscar (String txt, int page, int pageSize) {
    Pageable pg = Api.pagina(page, pageSize);

    if (Objects.isNull(txt) || txt.isBlank()) {
      return bolRep.q(pg).getContent();
    }

    return bolRep
      .buscar(txt.toLowerCase().trim(), pg)
      .getContent();
  }



  /**
   * Consulta los registros usando una busqueda de texto.
   *
   * @param actor
   * Usuario responsable de la operacion.
   *
   * @param txt
   * El texto a buscar.
   *
   * @param page
   * Numero de pagina (0-based)
   *
   * @param pageSize
   * Cantidad de resultados por pagina
   *
   * @return
   * Lista de registros encontrados.
   */
  public List<Boleto> buscarMios (
    Usuario actor, String txt, int page, int pageSize
  ) {
    Pageable pg = Api.pagina(page, pageSize);

    if (Objects.isNull(txt) || txt.isBlank()) {
      return bolRep
        .qAlumnoId(actor.getId(), pg)
        .getContent();
    }

    return bolRep
      .buscarAlumnoId(actor.getId(), txt.toLowerCase().trim(), pg)
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
  public Boleto afirmar (
    Long id
  )
    throws ResponseStatusException {

    return bolRep.findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format("Boleto con ID: %s no encontrado", id)));
  }



  /**
   * El registro con los identificadores especificados.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param noControl
   * Numero de Control del ALUMNO.
   *
   * @return
   * El registro encontrado.
   *
   * @throws ResponseStatusException
   * {@code HTTP-NOT_FOUND} si no se encuentra el registro.
   */
  public Boleto afirmarIdCongresoNoControlAlumno (
    Long congresoId, String noControl
  )
    throws ResponseStatusException {

    return bolRep.qCongresoIdAlumnoNoControl(congresoId, noControl)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format(
            "Boleto para el congreso con ID: %s" +
              " y numero de control de alumno: %s" +
              " no encontrado",
            congresoId, noControl)));
  }



  /**
   * El registro con el FOLIO especificado.
   *
   * @param folio
   * El FOLIO del registro.
   *
   * @return
   * El registro encontrado.
   *
   * @throws ResponseStatusException
   * {@code HTTP-NOT_FOUND} si no se encuentra el registro.
   */
  public Boleto afirmarFolio (
    String folio
  )
    throws ResponseStatusException {

    return bolRep.qFolio(folio)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format("Boleto con FOLIO: %s no encontrado", folio)));
  }

  /**
   * El registro con el FOLIO_LARGO especificado.
   *
   * @param folio
   * El FOLIO del registro.
   *
   * @return
   * El registro encontrado.
   *
   * @throws ResponseStatusException
   * {@code HTTP-NOT_FOUND} si no se encuentra el registro.
   */
  public Boleto afirmarFolioLargo (
    String folio
  )
    throws ResponseStatusException {

    return bolRep.qFolioLargo(folio)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format("Boleto con FOLIO LARGO: %s no encontrado", folio)));
  }



  /**
   * Obtiene el registro con el ID especificado o causa un error HTTP-404 si no
   * existe. Si el Actor no tiene acceso retorna error HTTP-401.
   *
   * @param actor
   * Usuario responsable de la operacion.
   *
   * @param idReg
   * El ID del registro.
   *
   * @return
   * El registro encontrado.
   *
   * @throws ResponseStatusException
   * {@code HTTP-NOT_FOUND} si no se encuentra el registro.
   * {@code HTTP-UNAUTHORIZED} si {@code actor} no es el ALUMNO del registro.
   */
  public Boleto afirmarIdAlumno (
    Usuario actor, Long idReg
  )
    throws ResponseStatusException {

    // Encontrar registro.
    var boleto = afirmar(idReg);

    // Comprobar acceso.
    if (actor.getRol() == Rol.ALUMNO
      && !Objects.equals(boleto.getAlumnoId(), actor.getId())) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        String.format("No tiene acceso al boleto con ID: %s", idReg));
    }

    return boleto;
  }



  /**
   * Determina si el actor es el ORGANIZADOR del registro especificado.
   * Sino, lanza error HTTP-401.
   *
   * @param actor
   * El USUARIO a validar.
   *
   * @param boleto
   * Registro a validar.
   *
   * @return
   * El registro, si el actor es el ORGANIZADOR.
   *
   * @throws ResponseStatusException
   * Si no es el ORGANIZADOR asignado.
   */
  public Boleto afirmarIdOrganizador (
    Usuario actor, Boleto boleto
  )
    throws ResponseStatusException {

    // Encontrar el CONGRESO y comprobar acceso.
    CongresoService.afirmarOrganizadorAsignado(
      actor, congSvc.afirmar(boleto.getCongresoId()));

    return boleto;
  }



  /**
   * Comprueba que un ALUMNO no se ha inscrito a un CONGRESO, de lo contrario
   * retorna un error HTTP-CONFLICT.
   *
   * @param congresoId
   * ID del CONGRESO en cuestion.
   *
   * @param alumnoId
   * ID del ALUMNO en cuestion.
   *
   * @return
   * {@code true} = no esta inscrito.
   * {@code false} = esta inscrito.
   */
  public boolean afirmarAlumnoNoInscrito (Long congresoId, Long alumnoId) {
    // Encontrar posible BOLETO existente de ese ALUMNO para ese CONGRESO.
    var _reg = bolRep.qCongresoIdAlumnoId(congresoId, alumnoId)
      .orElse(null);

    // Comprobar que no tiene ya un BOLETO para ese CONGRESO.
    if (Objects.nonNull(_reg)) {
      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "Solo puede inscribirse una vez por congreso");
    }

    return true;
  }



  /**
   * Determina si un registro cumple con los requerimientos nombrados en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED}.
   *
   * @param id
   * El ID del registro a validar.
   *
   * @return
   * El registro validado.
   */
  public Boleto afirmarPagado (
    Long id
  ) {
    return afirmarPagado(afirmar(id));
  }

  /**
   * Determina si un registro cumple con los requerimientos nombrados en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED}.
   *
   * @param reg
   * El registro a validar.
   *
   * @return
   * El registro validado.
   */
  public static Boleto afirmarPagado (
    Boleto reg
  ) {
    if (!reg.isPagado()) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "El boleto no esta pagado");
    }
    return reg;
  }



  /**
   * Determina si un registro cumple con los requerimientos nombrados en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED}.
   *
   * @param id
   * El ID del registro a validar.
   *
   * @return
   * El registro validado.
   */
  public Boleto afirmarNoCancelado (
    Long id
  ) {
    return afirmarNoCancelado(afirmar(id));
  }

  /**
   * Determina si un registro cumple con los requerimientos nombrados en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED}.
   *
   * @param reg
   * El registro a validar.
   *
   * @return
   * El registro validado.
   */
  public static Boleto afirmarNoCancelado (
    Boleto reg
  ) {
    // Comprobar que no tiene ya un BOLETO para ese CONGRESO.
    if (reg.isCancelado()) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "El boleto esta cancelado");
    }

    return reg;
  }



  /**
   * Determina si un registro cumple con los requerimientos nombrados en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED}.
   *
   * @param folio
   * El ID del registro a validar.
   *
   * @return
   * El registro validado.
   */
  public Boleto afirmarFolioNoCancelado (
    String folio
  ) {
    return afirmarNoCancelado(afirmarFolio(folio));
  }



  /**
   * Determina si un registro cumple con los requerimientos nombrados en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED}.
   *
   * @param boletoFolio
   * El ID del registro a validar.
   *
   * @param congresoId
   * ID auxiliar a validar.
   *
   * @return
   * El registro validado.
   */
  public Boleto afirmarFolioIdCongreso (
    String boletoFolio, Long congresoId
  ) {
    return afirmarIdCongreso(afirmarFolio(boletoFolio), congresoId);
  }

  /**
   * Determina si un registro cumple con los requerimientos nombrados en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED}.
   *
   * @param idBoleto
   * El ID del registro a validar.
   *
   * @param congresoId
   * ID auxiliar a validar.
   *
   * @return
   * El registro validado.
   */
  public Boleto afirmarIdCongreso (
    Long idBoleto, Long congresoId
  ) {
    return afirmarIdCongreso(afirmar(idBoleto), congresoId);
  }

  /**
   * Determina si un registro cumple con los requerimientos nombrados en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED}.
   *
   * @param reg
   * El registro a validar.
   *
   * @param congresoId
   * ID auxiliar a validar.
   *
   * @return
   * El registro validado.
   */
  public static Boleto afirmarIdCongreso (
    Boleto reg, Long congresoId
  ) {
    // Comprobar que no tiene ya un BOLETO para ese CONGRESO.
    if (!Objects.equals(reg.getCongresoId(), congresoId)) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "El boleto es para otro congreso");
    }

    return reg;
  }



  /**
   * Determina si un registro cumple con los requerimientos nombrados en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED}.
   *
   * @param reg
   * El registro a validar.
   *
   * @return
   * El registro validado.
   */
  public static Boleto afirmarCumplioRequerimientosDeAsistencia (
    Boleto reg
  ) {
    // Comprobar que el ALUMNO cumplio con los requerimientos de asistencia.
    if (!reg.isCumplioRequerimientosDeAsistencia()) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        "El alumno no completo sus asistencias");
    }
    return reg;
  }



  //----------------------------------------------------------------------------
  // AUXILIARES.

  /**
   * Edita el BOLETO (le suma la nueva duracion al total de tiempo asistido),
   * verifica si el ALUMNO cumplio con los requerimientos de asistencia del
   * CONGRESO y lo guarda en la base de datos.
   *
   * @param congreso
   * El CONGRESO cuyas reglas de acreditacion se utilizaran para la verificacion.
   *
   * @return
   * El registro actualizado.
   */
  public Boleto sumarTiempoAsistido (
    Boleto boleto, long duracion, Congreso congreso
  ) {
    return bolRep.saveAndFlush(
      boleto
        .sumarTiempoAsistido(duracion)
        .aplicarCumplimientoDeRequisitosDeAsistencia(
          congreso.getAlumnoAcreditacionAsistenciasRequeridas(),
          congreso.getAlumnoAcreditacionTiempoAsistidoRequerido()));
  }
}
