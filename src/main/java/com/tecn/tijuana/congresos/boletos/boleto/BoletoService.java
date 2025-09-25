package com.tecn.tijuana.congresos.boletos.boleto;

import com.tecn.tijuana.congresos.boletos.boleto.dto.RegistroBoletoDto;
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
   * Datos del registro.
   *
   * @return
   * El registro creado.
   */
  public Boleto inscribirse (
    Usuario actor, RegistroBoletoDto dto
  )
    throws ResponseStatusException {

    // Comprobar que el ALUMNO no esta inscrito ya a ese CONGRESO.
    afirmarAlumnoNoInscrito(dto.getCongresoId(), actor.getId());

    // Comprobar que el CONGRESO existe y validarlo.
    var congreso = congSvc
      .afirmarNoCanceladoPublicadoEnPeriodoDeInscripcionesConCupoDisponible(
        dto.getCongresoId());

    // Crear, guardar y retornar el nuevo registro.
    return bolRep.saveAndFlush(Boleto.nuevo(actor, dto, congreso, actor));
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
    CongresoService.afirmarNoCanceladoConCupoDisponibleFechaFinFutura(
      congreso);

    // Crear, guardar y retornar el nuevo registro.
    return bolRep.saveAndFlush(Boleto.nuevo(actor, dto, congreso, alumno));
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
  public Boleto cancelar (
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

    // Actualizar el BOLETO.
    boleto.setCancelado(estatus);

    // Actualizar, guardar y retornar el registro.
    return bolRep.saveAndFlush(boleto);
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
   * Consulta los registros de un CONGRESO.
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
  public List<Boleto> qIdCongreso (
    Long congresoId, int page, int pageSize
  ) {
    return bolRep
      .qCongresoId(congresoId, Api.pagina(page, pageSize))
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
      return bolRep.findAll(pg).getContent();
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
}
