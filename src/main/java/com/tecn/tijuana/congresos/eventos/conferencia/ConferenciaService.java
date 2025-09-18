package com.tecn.tijuana.congresos.eventos.conferencia;

import com.tecn.tijuana.congresos.eventos.congreso.CongresoService;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.utils.Api;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


/**
 * Clase principal de la capa de servicio para la entidad.
 */
@Service
public class ConferenciaService {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final ConferenciaRepository confRep;
  private final CongresoService congSvc;


  /**
   * CONSTRUCTOR principal de la clase/servicio, usado principalmente por Spring
   * para el funcionamiento de la app.
   *
   * @param confRep
   * Repositorio de DB de la entidad de CONFERENCIA usado para abstraer consultas
   * y operaciones de la BD.
   *
   * @param congSvc
   * Servicio de la entidad de CONGRESO.
   */
//  @Autowired
  public ConferenciaService (
    ConferenciaRepository confRep,
    CongresoService congSvc
  ) {
    this.confRep = confRep;
    this.congSvc = congSvc;
  }



  /**
   * Consulta las CONFERENCIAS de un CONGRESO.
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
  public List<Conferencia> qCongreso (
    Long congresoId,
    int page,
    int pageSize
  ) {
    return confRep
      .qCongresoId(congresoId, Api.pagina(page, pageSize))
      .getContent();
  }



  /**
   * Consulta las CONFERENCIAS publicadas de un CONGRESO.
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
  public List<Conferencia> qCongresoPublicadas (
    Long congresoId,
    int page,
    int pageSize
  ) {
    return confRep
      .qCongresoIdPublicadas(congresoId, Api.pagina(page, pageSize))
      .getContent();
  }



  /**
   * Consulta las CONFERENCIAS usando una busqueda de texto.
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
  public List<Conferencia> buscar (String txt, int page, int pageSize) {
    Pageable pg = Api.pagina(page, pageSize);

    if (Objects.isNull(txt) || txt.isBlank()) {
      return confRep.findAll(pg).getContent();
    }

    return confRep
      .buscar(txt.toLowerCase().trim(), pg)
      .getContent();
  }



  /**
   * Obtiene el registro con el ID especificado o causa un error HTTP-404 si no
   * existe.
   *
   * @param id
   * El ID del registro.
   *
   * @return
   * El registro encontrado.
   */
  public Conferencia afirmar (Long id)
    throws ResponseStatusException {

    return confRep.findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format("Conferencia con ID: %s no encontrado", id)));
  }



  /**
   * Obtiene el registro con el ID especificado.
   *
   * @param id
   * El ID del registro.
   *
   * @return
   * El registro encontrado o {@code null} si no existe.
   */
  public Conferencia qId (Long id)
    throws ResponseStatusException {

    return confRep.findById(id).orElse(null);
  }



  /**
   * Obtiene la CONFERENCIA publicada de un CONGRESO publicado, con el ID
   * especificado. Si la CONFERENCIA o el CONGRESO no se encuentran o no estan
   * publicados, lanza una excepcion.
   *
   * @param id
   * El ID del registro.
   *
   * @return
   * El registro encontrado.
   */
  public Conferencia qIdPublicada (Long id)
    throws ResponseStatusException {

    // Encontrar registros.
    var conferencia = afirmar(id);
    var congreso = congSvc.afirmar(conferencia.getCongresoId());

    // Comprobar estatus.
    if (!conferencia.isPublicada() || !congreso.isPublicado()) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        String.format("Conferencia publicado con ID: %s no encontrado", id));
    }

    return conferencia;
  }



  /**
   * Permite ORGANIZADORES registrar nuevas CONFERENCIAS.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param conferencia
   * Datos del Conferencia.
   *
   * @return
   * El Conferencia recien registrado en la BD.
   */
  public Conferencia registrar (Usuario actor, Conferencia conferencia)
    throws ResponseStatusException {

    // Comprobar permisos.
    afirmarOrganizadorAsignado(actor, conferencia);

    // Validar las fechas del Conferencia o retornar el error HTTP acorde.
    afirmarPeriodoEventoValido(conferencia);

    // Marcar al Actor como creador del registro.
    conferencia.setCreadorId(actor.getId());

    return confRep.saveAndFlush(Conferencia.nueva(conferencia));
  }



  /**
   * Actualiza un registro con los nuevos datos provistos.
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param id
   * ID del registro a editar.
   *
   * @param conferencia
   * El objeto con los nuevos datos.
   *
   * @return
   * El registro de la BD editado.
   */
  public Conferencia editar (
    Usuario actor, Long id, Conferencia conferencia
  ) throws ResponseStatusException {

    // Encontrar el Conferencia.
    var conf = afirmar(id);

    // Comprobar permisos.
    afirmarOrganizadorAsignado(actor, conf);

    // Validar cambios.
    afirmarPeriodoEventoValido(conferencia);

    // Actualizar, guardar y retornar el registro.
    return confRep.saveAndFlush(conf.actualizar(conferencia));
  }



  /**
   * Permite editar un slot de multimedia de un registro.
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param id
   * ID del registro.
   *
   * @param slot
   * El nombre del slot a editar.
   *
   * @param img {@code [null]}
   * Posible imagen a usar.
   * Si es {@code null} se remueve la imagen de ese slot.
   *
   * @return
   * El registro editado.
   */
  public Conferencia editarMedia (
    Usuario actor, Long id, String slot, MultipartFile img
  )
    throws ResponseStatusException {

    // Encontrar registros.
    var conferencia = afirmar(id);

    // Comprobar permisos.
    afirmarOrganizadorAsignado(actor, conferencia);

    try {
      conferencia.setMedia(slot, img);
    } catch (IOException e) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Error al procesar la imagen");
    }

    // Actualizar, guardar y retornar el registro.
    return confRep.saveAndFlush(conferencia);
  }



  /**
   * Elimina un Conferencia y sus registros derivados de otras entidades que ya
   * no sean requeridos, siempre y cuando el Actor tenga los permisos
   * suficientes.
   *
   * @param actor
   * El Usuario ejecutor de la operacion.
   *
   * @param id
   * El ID del registro a eliminar.
   *
   * @return
   * El registro eliminado.
   */
  public Conferencia eliminar (
    Usuario actor, Long id
  )
    throws ResponseStatusException {

    // Encontrar el registro.
    var conferencia = qId(id);

    if (Objects.nonNull(conferencia)) {
      confRep.deleteById(id);
    }

    return conferencia;
  }



  /**
   * Publica/retracta un registro segun el estatus especificado.
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param id
   * ID del registro a editar.
   *
   * @param estatus
   * {@code true} = publicado.
   * {@code false} = retractado.
   *
   * @return
   * El registro publicado/retractado.
   */
  public Conferencia publicar (
    Usuario actor, Long id, boolean estatus
  ) throws ResponseStatusException {

    // Encontrar el Conferencia.
    var conferencia = afirmar(id);

    // Comprobar permisos.
    afirmarOrganizadorAsignado(actor, conferencia);

    // Actualizar registro.
    conferencia.setPublicada(estatus);

    // Guardar y retornar el registro.
    return confRep.saveAndFlush(conferencia);
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
  public Conferencia cancelar (
    Usuario actor, Long id, boolean estatus
  ) throws ResponseStatusException {

    // Encontrar el Conferencia.
    var conferencia = afirmar(id);

    // Comprobar permisos.
    afirmarOrganizadorAsignado(actor, conferencia);

    // Actualizar registro.
    conferencia.setCancelada(estatus);

    // Guardar y retornar el registro.
    return confRep.saveAndFlush(conferencia);
  }



  /**
   * Determina si el actor es el ORGANIZADOR del registro especificado.
   * Sino, lanza error HTTP-401.
   *
   * @param actor
   * El USUARIO a validar.
   *
   * @param conferencia
   * Registro a validar.
   *
   * @return
   * El registro, si el actor es el ORGANIZADOR.
   *
   * @throws ResponseStatusException
   * Si no es el ORGANIZADOR asignado.
   */
  public Conferencia afirmarOrganizadorAsignado (
    Usuario actor, Conferencia conferencia
  )
    throws ResponseStatusException {

    // Encontrar el CONGRESO y comprobar acceso.
    CongresoService.afirmarOrganizadorAsignado(
      actor, congSvc.afirmar(conferencia.getCongresoId()));

    return conferencia;
  }



  /**
   * Determina si el registro tiene un rango de fechas valido.
   *
   * @param conferencia
   * Registro a validar.
   *
   * @return
   * El registro si el rango de fechas es valido.
   *
   * @throws ResponseStatusException
   * Si el rango de fechas rompe alguno de los requerimientos.
   */
  public Conferencia afirmarPeriodoEventoValido (
    Conferencia conferencia
  )
    throws ResponseStatusException {

    var inicio = conferencia.getFechaInicio();
    var fin = conferencia.getFechaFin();

    if (!CongresoService.periodoFuturo(inicio, fin)) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Las fechas deben ser en el futuro.");
    }

    if (!CongresoService.periodoOrdenCorrecto(inicio, fin)) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "La fecha de terminacion debe ser posterior a la de inicio.");
    }

    if (!CongresoService.periodoRangoValido(
      inicio, fin, Conferencia.DURACION_MIN, Conferencia.DURACION_MAX)
    ) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "La duracion debe ser al menos 1 hora y maximo 7 dias.");
    }

    return conferencia;
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
  public Conferencia afirmarEnCurso (
    Long id
  ) {
    return afirmarEnCurso(afirmar(id));
  }

  /**
   * Determina si un registro cumple con el requerimiento nombrado en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED} con la descripcion del error.
   *
   * @param reg
   * El registro a validar.
   *
   * @return
   * El registro validado.
   */
  public static Conferencia afirmarEnCurso (
    Conferencia reg
  ) {
    var now = LocalDate.now();
    if (
      now.isBefore(reg.getFechaInicio())
        || now.isAfter(reg.getFechaFin())
    ) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        "La conferencia no esta en curso");
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
  public Conferencia afirmarNoCancelada (
    Long id
  ) {
    return afirmarNoCancelada(afirmar(id));
  }

  /**
   * Determina si un registro cumple con el requerimiento nombrado en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED} con la descripcion del error.
   *
   * @param reg
   * El registro a validar.
   *
   * @return
   * El registro validado.
   */
  public static Conferencia afirmarNoCancelada (
    Conferencia reg
  ) {
    if (reg.isCancelada()) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        "La conferencia esta cancelada");
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
  public Conferencia afirmarNoCanceladaEnCurso (
    Long id
  ) {
    return afirmarNoCanceladaEnCurso(afirmar(id));
  }

  /**
   * Determina si un registro cumple con el requerimiento nombrado en la
   * funcion, de lo contrario lanza una excepcion que retorna un error
   * {@code HTTP-PRECONDITION_FAILED} con la descripcion del error.
   *
   * @param reg
   * El registro a validar.
   *
   * @return
   * El registro validado.
   */
  public static Conferencia afirmarNoCanceladaEnCurso (
    Conferencia reg
  ) {
    return afirmarEnCurso(afirmarNoCancelada(reg));
  }
}
