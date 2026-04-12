package com.tecn.tijuana.congresos.eventos.congreso;

import com.tecn.tijuana.congresos.eventos.congreso.dto.RegistroCongresoDto;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.utils.Api;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.tecn.tijuana.congresos.utils.Api.DEFAULT_PAGE;
import static com.tecn.tijuana.congresos.utils.Api.DEFAULT_PAGE_SIZE;


/**
 * Clase principal de la capa de servicio para la entidad.
 */
@Service
public class CongresoService {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final CongresoRepository conRep;


  /**
   * CONSTRUCTOR principal de la clase/servicio, usado principalmente por Spring
   * para el funcionamiento de la app.
   *
   * @param conRep
   * Repositorio de DB de la entidad de CONGRESO usado para abstraer consultas
   * y operaciones de la BD.
   */
//  @Autowired
  public CongresoService (
    CongresoRepository conRep
  ) {
    this.conRep = conRep;
  }



  /**
   * Consulta todos los registros de la entidad indiscriminadamente usando los
   * parametros de paginacion por defecto.
   *
   * @return
   * Lista de registros encontrados.
   *
   * @see Api#pagina()
   */
  public List<Congreso> q () {
    return q(DEFAULT_PAGE);
  }

  /**
   * Consulta todos los registros de la entidad indiscriminadamente usando los
   * parametros de paginacion especificados.
   *
   * @param page
   * Numero de pagina.
   *
   * @return
   * Lista de registros encontrados.
   *
   * @see Api#pagina()
   */
  public List<Congreso> q (int page) {
    return q(page, DEFAULT_PAGE_SIZE);
  }

  /**
   * Consulta todos los registros de la entidad indiscriminadamente usando los
   * parametros de paginacion especificados.
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
  public List<Congreso> q (int page, int pageSize) {
    return conRep.
      findAll(Api.pagina(page, pageSize))
      .getContent();
  }



  /**
   * Consulta todos los CONGRESOS publicados usando filtros opcionales de texto,
   * fechaFinMin y gratuito.
   *
   * @param txt
   * Texto a buscar. {@code null} o vacio para no filtrar.
   *
   * @param fechaFinMin
   * Si se especifica, solo retorna congresos cuya fechaFin sea posterior a
   * esta fecha. {@code null} para no filtrar.
   *
   * @param gratuito
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
  public List<Congreso> qPublicados (
    String txt, LocalDateTime fechaFinMin, Boolean gratuito,
    int page, int pageSize
  ) {
    Pageable pg = Api.pagina(page, pageSize);

    // Normalizar txt.
    String txtN = (Objects.isNull(txt) || txt.isBlank())
      ? null
      : txt.toLowerCase().trim();

    // Si no hay ningun filtro activo, usar la consulta simple.
    if (txtN == null && fechaFinMin == null && gratuito == null) {
      return conRep.publicados(pg).getContent();
    }

    // Usar la consulta condicional.
    return conRep
      .qPublicadosFiltrado(txtN, fechaFinMin, gratuito, pg)
      .getContent();
  }

  /**
   * Consulta todos los CONGRESOS publicados cuya fecha aun es a futuro,
   * incluyendo aquellos que se encuentran cancelados, usando los parametros de
   * paginacion especificados.
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
  public List<Congreso> qPublicadosProximos (int page, int pageSize) {
    return conRep.publicadosProximos(
      Api.pagina(page, pageSize)).getContent();
  }



  /**
   * Busca registros en la BD cuyos campos tipo String contengan el texto
   * especificado en {@code txt}, utilizando coincidencia parcial
   * case-insensitive.
   * <p>
   * Si {@code txt} es {@code null} o esta vacio, retorna todos los usuarios
   * aplicando solo la paginacion especificada.
   * <p>
   * Ejemplo: {@code buscar("texto", 0, 10)} retorna la primera pagina de
   * usuarios que contengan "texto" en alguno de sus campos.
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
  public List<Congreso> buscar (String txt, int page, int pageSize) {
    Pageable pg = Api.pagina(page, pageSize);

    if (Objects.isNull(txt) || txt.isBlank()) {
      return conRep.findAll(pg).getContent();
    }

    return conRep
      .buscar(txt.toLowerCase().trim(), pg)
      .getContent();
  }



  /**
   * Permite a un ORGANIZADOR consultar sus propios CONGRESOS.
   *
   * @param page
   * Numero de pagina (0-based).
   *
   * @param pageSize
   * Cantidad de resultados por pagina.
   *
   * @return
   * Lista de registros encontrados.
   */
  public List<Congreso> mios (
    Usuario actor, int page, int pageSize
  ) {
    return conRep
      .porOrganizador(actor.getId(), Api.pagina(page, pageSize))
      .getContent();
  }

  /**
   * Permite consultar los CONGRESOS de un ORGANIZADOR usando una posible
   * busqueda de texto y filtros opcionales de publicado y cancelado.
   *
   * @param actor
   * USUARIO ejecutor de la operacion (organizador).
   *
   * @param txt
   * El texto a buscar.
   *
   * @param publicado
   * Filtro opcional por estado de publicacion. {@code null} para no filtrar.
   *
   * @param cancelado
   * Filtro opcional por estado de cancelacion. {@code null} para no filtrar.
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
  public List<Congreso> buscarMios (
    Usuario actor, String txt,
    Boolean publicado, Boolean cancelado,
    int page, int pageSize
  ) {
    Pageable pg     = Api.pagina(page, pageSize);
    Long     idOrg  = actor.getId();
    boolean  hayTxt = !(Objects.isNull(txt) || txt.isBlank());
    String   txtN   = hayTxt ? txt.toLowerCase().trim() : null;

    // Seleccionar la consulta exacta segun la combinacion de filtros activos.

    // Sin filtro de publicado ni cancelado.
    if (publicado == null && cancelado == null) {
      return hayTxt
        ? conRep.buscarPorOrganizador(idOrg, txtN, pg).getContent()
        : conRep.porOrganizador(idOrg, pg).getContent();
    }

    // Solo filtro de publicado.
    if (cancelado == null) {
      return hayTxt
        ? conRep.buscarPorOrganizadorPublicado(
          idOrg, txtN, publicado, pg).getContent()
        : conRep.porOrganizadorPublicado(idOrg, publicado, pg).getContent();
    }

    // Solo filtro de cancelado.
    if (publicado == null) {
      return hayTxt
        ? conRep.buscarPorOrganizadorCancelado(
          idOrg, txtN, cancelado, pg).getContent()
        : conRep.porOrganizadorCancelado(idOrg, cancelado, pg).getContent();
    }

    // Ambos filtros activos.
    return hayTxt
      ? conRep.buscarPorOrganizadorPublicadoCancelado(
        idOrg, txtN, publicado, cancelado, pg).getContent()
      : conRep.porOrganizadorPublicadoCancelado(
        idOrg, publicado, cancelado, pg).getContent();
  }



  /**
   * Consulta los CONGRESOS publicados usando una busqueda de texto.
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
  public List<Congreso> publicadosBuscar (
    String txt, int page, int pageSize
  ) {
    Pageable pg = Api.pagina(page, pageSize);

    if (Objects.isNull(txt) || txt.isBlank()) {
      return conRep.publicados(pg).getContent();
    }

    return conRep
      .buscarPublicados(txt.toLowerCase().trim(), pg)
      .getContent();
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
  public Congreso qId (Long id)
    throws ResponseStatusException {

    return conRep.findById(id).orElse(null);
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
  public Congreso afirmar (Long id)
    throws ResponseStatusException {

    return conRep.findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format("Congreso con ID: %s no encontrado", id)));
  }



  /**
   * Obtiene CONGRESO publicado con el ID especificado. Sino lo encuentra o no
   * esta publicado, lanza una excepcion.
   *
   * @param id
   * El ID del registro.
   *
   * @return
   * El registro encontrado.
   */
  public Congreso afirmarPublicado (
    Long id
  )
    throws ResponseStatusException {

    // Encontrar registro.
    var con = afirmar(id);

    // Comprobar estatus.
    if (!con.isPublicado()) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        String.format("Congreso publicado con ID: %s no encontrado", id));
    }

    return con;
  }



  /**
   * La imagen de un CONGRESO en el slot especificado.
   *
   * @param id
   * ID del registro.
   *
   * @param slot
   * Slot para multimedia.
   *
   * @return
   * La imagen.
   *
   * @throws ResponseStatusException
   * <p>
   * {@code HTTP-NOT_FOUND}
   * Si el CONGRESO no existe o si no tiene una foto.
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si el slot especificado no existe.
   */
  public ResponseEntity<byte[]> afirmarMedia (
    Long id, String slot
  )
    throws ResponseStatusException {

    // Encontrar registro.
    Congreso congreso = afirmar(id);

    // Aux.
    String fotoMimeType;

    // Extraer datos de la foto.
    byte[] fotoImgData = switch (slot) {
      case "media1" -> {
        fotoMimeType = congreso.getMedia1MimeType();
        yield congreso.getMedia1ImgData();
      }
      case "media2" -> {
        fotoMimeType = congreso.getMedia2MimeType();
        yield congreso.getMedia2ImgData();
      }
      case "media3" -> {
        fotoMimeType = congreso.getMedia3MimeType();
        yield congreso.getMedia3ImgData();
      }
      case "media4" -> {
        fotoMimeType = congreso.getMedia4MimeType();
        yield congreso.getMedia4ImgData();
      }
      case "media5" -> {
        fotoMimeType = congreso.getMedia5MimeType();
        yield congreso.getMedia5ImgData();
      }
      case "media6" -> {
        fotoMimeType = congreso.getMedia6MimeType();
        yield congreso.getMedia6ImgData();
      }
      case "mediaEvt1" -> {
        fotoMimeType = congreso.getMediaEvt1MimeType();
        yield congreso.getMediaEvt1ImgData();
      }
      case "mediaEvt2" -> {
        fotoMimeType = congreso.getMediaEvt2MimeType();
        yield congreso.getMediaEvt2ImgData();
      }
      case "mediaEvt3" -> {
        fotoMimeType = congreso.getMediaEvt3MimeType();
        yield congreso.getMediaEvt3ImgData();
      }
      case "mediaEvt4" -> {
        fotoMimeType = congreso.getMediaEvt4MimeType();
        yield congreso.getMediaEvt4ImgData();
      }
      case "mediaEvt5" -> {
        fotoMimeType = congreso.getMediaEvt5MimeType();
        yield congreso.getMediaEvt5ImgData();
      }
      case "mediaEvt6" -> {
        fotoMimeType = congreso.getMediaEvt6MimeType();
        yield congreso.getMediaEvt6ImgData();
      }
      default -> throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Slot no valido.");
    };

    // Si hay algo malo con la foto, retornar error 404.
    if (
      Objects.isNull(fotoImgData) || fotoImgData.length == 0
        || Objects.isNull(fotoMimeType) || fotoMimeType.isBlank()
    ) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        String.format("Foto del congreso con ID: %s no encontrada.", id));
    }

    // Retornar respuesta ya lista con el contenido de la foto.
    return ResponseEntity.ok()
      .contentType(MediaType.valueOf(fotoMimeType))
      .body(fotoImgData);
  }



  /**
   * Permite ORGANIZADORES registrar nuevos CONGRESOS.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param dto
   * Datos del CONGRESO.
   *
   * @return
   * El CONGRESO recien registrado en la BD.
   */
  public Congreso registrar (Usuario actor, RegistroCongresoDto dto)
    throws ResponseStatusException {

    return registrar(actor, Congreso.nuevo(actor.getId(), dto));
  }

  /**
   * Permite ORGANIZADORES registrar nuevos CONGRESOS.
   *
   * @param actor
   * USUARIO ejecutor de la operacion.
   *
   * @param congreso
   * Datos del CONGRESO.
   *
   * @return
   * El CONGRESO recien registrado en la BD.
   */
  public Congreso registrar (Usuario actor, Congreso congreso)
    throws ResponseStatusException {

    // Validar las fechas del Congreso o retornar el error HTTP acorde.
    afirmarPeriodoEventoValido(congreso);
    afirmarPeriodoInscripcionesValido(congreso);

    // Marcar al Actor como creador del registro.
    congreso.setCreadorId(actor.getId());
    congreso.setOrganizadorId(actor.getId());

    return conRep.saveAndFlush(Congreso.nuevo(congreso));
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
   * @param congreso
   * El objeto con los nuevos datos.
   *
   * @return
   * El registro de la BD editado.
   */
  public Congreso editar (
    Usuario actor, Long id, Congreso congreso
  ) throws ResponseStatusException {

    // Encontrar el Congreso.
    var con = afirmar(id);

    // Comprobar permisos.
    afirmarOrganizadorAsignado(actor, con);

    // Validar cambios.
    afirmarPeriodoEventoValido(congreso);
    afirmarPeriodoInscripcionesValido(congreso);

    // Actualizar, guardar y retornar el registro.
    return conRep.saveAndFlush(con.actualizar(congreso));
  }



  /**
   * Permite editar un slot de multimedia del Congreso.
   *
   * @param actor
   * Usuario ejecutor de la operacion.
   *
   * @param id
   * ID del Congreso.
   *
   * @param slot
   * El nombre del slot a editar.
   *
   * @param img {@code [null]}
   * Posible imagen a usar como foto de info.
   * Si es {@code null} se remueve la imagen de ese slot.
   *
   * @return
   * El Congreso editado.
   */
  public Congreso editarMedia (
    Usuario actor, Long id, String slot, MultipartFile img
  )
    throws ResponseStatusException {

    // Encontrar el Congreso.
    var con = afirmar(id);

    // Comprobar permisos.
    afirmarOrganizadorAsignado(actor, con);

    try {
      con.setMedia(slot, img);
    } catch (IOException e) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Error al procesar la imagen");
    }

    // Actualizar, guardar y retornar el registro.
    return conRep.saveAndFlush(con);
  }



  /**
   * Elimina un Congreso y sus registros derivados de otras entidades que ya no
   * sean requeridos, siempre y cuando el Actor tenga los permisos suficientes.
   *
   * @param actor
   * El Usuario ejecutor de la operacion.
   *
   * @param id
   * El ID del Congreso a eliminar.
   *
   * @return
   * El Congreso eliminado.
   */
  public Congreso eliminar (
    Usuario actor, Long id
  )
    throws ResponseStatusException {

    // Intentar encontrar el registro.
    var con = qId(id);

    // Si el registro existe eliminarlo.
    if (Objects.nonNull(con)) {
      conRep.deleteById(id);
    }

    // Retornar el posible registro eliminado o null.
    return con;
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
  public Congreso publicar (
    Usuario actor, Long id, boolean estatus
  ) throws ResponseStatusException {

    // Encontrar el Congreso.
    var con = afirmar(id);

    // Comprobar permisos.
    afirmarOrganizadorAsignado(actor, con);

    // Actualizar registro.
    con.setPublicado(estatus);

    // Guardar y retornar el registro.
    return conRep.saveAndFlush(con);
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
  public Congreso cancelar (
    Usuario actor, Long id, boolean estatus
  ) throws ResponseStatusException {

    // Encontrar el Congreso.
    var con = afirmar(id);

    // Comprobar permisos.
    afirmarOrganizadorAsignado(actor, con);

    // Actualizar registro.
    con.setCancelado(estatus);

    // Guardar y retornar el registro.
    return conRep.saveAndFlush(con);
  }



  /**
   * Incrementa el contador de asistencias.
   *
   * @param id
   * ID del registro a editar.
   *
   * @return
   * El registro actualizado.
   */
  public Congreso sumarAsistencia (
    Long id
  ) {
    return sumarAsistencia(afirmar(id));
  }

  /**
   * Incrementa el contador de asistencias.
   *
   * @param congreso
   * El registro a editar.
   *
   * @return
   * El registro actualizado.
   */
  public Congreso sumarAsistencia (
    Congreso congreso
  ) {
    // Actualizar, guardar y retornar el registro.
    return conRep.saveAndFlush(congreso.sumarAsistencia());
  }



  /**
   * Incrementa el contador de inscripciones.
   *
   * @param id
   * ID del registro a editar.
   *
   * @return
   * El registro actualizado.
   */
  public Congreso sumarInscripcion (
    Long id
  ) {
    return sumarInscripcion(afirmar(id));
  }

  /**
   * Incrementa el contador de inscripciones.
   *
   * @param congreso
   * El registro a editar.
   *
   * @return
   * El registro actualizado.
   */
  public Congreso sumarInscripcion (
    Congreso congreso
  ) {
    // Actualizar, guardar y retornar el registro.
    return conRep.saveAndFlush(congreso.sumarInscripcion());
  }



  /**
   * Determina si el registro tiene un rango de fechas valido.
   *
   * @param congreso
   * Registro a validar.
   *
   * @return
   * El registro si el rango de fechas es valido.
   *
   * @throws ResponseStatusException
   * Si el rango de fechas rompe alguno de los requerimientos.
   */
  public static Congreso afirmarPeriodoInscripcionesValido (
    Congreso congreso
  )
    throws ResponseStatusException {

    var inicio = congreso.getInscripcionesFechaInicio();
    var fin = congreso.getInscripcionesFechaFin();

    if (!periodoOrdenCorrecto(inicio, fin)) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "La fecha de terminacion de inscripciones debe ser posterior a la de" +
          " inicio.");
    }

    if (!periodoRangoValido(inicio, fin,
      Congreso.INSCRIPCIONES_DURACION_MIN, Congreso.INSCRIPCIONES_DURACION_MAX)
    ) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "El periodo de inscripciones debe ser" +
          " al menos 1 hora y maximo 30 dias.");
    }

    if (!periodoOrdenCorrecto(fin, congreso.getFechaFin())) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "La fecha de terminacion de inscripciones debe ser antes que la fecha" +
          " de terminacion del congreso.");
    }

    return congreso;
  }

  /**
   * Determina si el registro tiene un rango de fechas valido.
   *
   * @param congreso
   * Registro a validar.
   *
   * @return
   * El registro si el rango de fechas es valido.
   *
   * @throws ResponseStatusException
   * Si el rango de fechas rompe alguno de los requerimientos.
   */
  public static Congreso afirmarPeriodoEventoValido (Congreso congreso)
    throws ResponseStatusException {

    var inicio = congreso.getFechaInicio();
    var fin = congreso.getFechaFin();

    if (!periodoOrdenCorrecto(inicio, fin)) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "La fecha de terminacion debe ser posterior a la de inicio.");
    }

    if (!periodoRangoValido(
      inicio, fin, Congreso.DURACION_MIN, Congreso.DURACION_MAX)
    ) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "La duracion del evento debe ser al menos 1 hora y maximo 7 dias.");
    }

    return congreso;
  }

  /**
   * Determina si el periodo descrito por la fecha de inicio y la fecha de
   * terminacion especificadas esta en el orden correcto.
   *
   * @param inicio
   * Fecha de inicio.
   *
   * @param fin
   * Fecha de terminacion.
   *
   * @return
   * true = correcto.
   * true = incorrecto.
   */
  public static boolean periodoOrdenCorrecto (
    LocalDateTime inicio, LocalDateTime fin
  ) {
    return fin.isAfter(inicio);
  }

  /**
   * Determina si la duracion del Congreso es valida.
   *
   * @param inicio
   * Fecha de inicio.
   *
   * @param fin
   * Fecha de terminacion.
   *
   * @param minSecs
   * Duracion minima permitida en segundos.
   *
   * @param maxSecs
   * Duracion maxima permitida en segundos.
   *
   * @return
   * true = valido.
   * true = invalido.
   */
  public static boolean periodoRangoValido (
    LocalDateTime inicio, LocalDateTime fin, int minSecs, int maxSecs
  ) {
    var secs = Duration.between(inicio, fin).toSeconds();

    return secs >= minSecs && secs <= maxSecs;
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
  public Congreso
  afirmarNoCanceladoPublicadoEnPeriodoDeInscripcionesConCupoDisponible (
    Long id
  ) {
    return
      afirmarNoCanceladoPublicadoEnPeriodoDeInscripcionesConCupoDisponible(
        afirmar(id));
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
  public static Congreso
  afirmarNoCanceladoPublicadoEnPeriodoDeInscripcionesConCupoDisponible (
    Congreso reg
  ) {
    return afirmarConCupoDisponible(
      afirmarEnPeriodoDeInscripciones(
        afirmarPublicado(
          afirmarNoCancelado(
            reg))));
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
  public Congreso
  afirmarNoCanceladoConCupoDisponibleFechaFinFutura (
    Long id
  ) {
    return afirmarNoCanceladoConCupoDisponibleFechaFinFutura(afirmar(id));
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
  public static Congreso
  afirmarNoCanceladoConCupoDisponibleFechaFinFutura (
    Congreso reg
  ) {
    return afirmarFechaFinFutura(
      afirmarConCupoDisponible(
        afirmarNoCancelado(
          reg)));
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
  public Congreso afirmarNoCanceladoFechaFinFutura (
    Long id
  ) {
    return afirmarNoCanceladoFechaFinFutura(afirmar(id));
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
  public static Congreso afirmarNoCanceladoFechaFinFutura (
    Congreso reg
  ) {
    return afirmarFechaFinFutura(
      afirmarNoCancelado(
        reg));
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
  public Congreso afirmarNoCanceladoEnCurso (
    Long id
  ) {
    return afirmarEnCurso(afirmarNoCancelado(afirmar(id)));
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
  public static Congreso afirmarNoCanceladoEnCurso (
    Congreso reg
  ) {
    return afirmarEnCurso(afirmarNoCancelado(reg));
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
  public static Congreso afirmarNoCancelado (Congreso reg) {
    if (reg.isCancelado()) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        "El congreso esta cancelado");
    }
    return reg;
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
  public static Congreso afirmarPublicado (Congreso reg) {
    if (!reg.isPublicado()) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        "El congreso no esta publicado");
    }
    return reg;
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
  public static Congreso afirmarEnPeriodoDeInscripciones (
    Congreso reg
  ) {
    var now = LocalDateTime.now();
    if (
      now.isBefore(reg.getInscripcionesFechaInicio())
        || now.isAfter(reg.getInscripcionesFechaFin())
    ) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        "El congreso no esta en periodo de inscripciones");
    }
    return reg;
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
  public static Congreso afirmarConCupoDisponible (Congreso reg) {
    var cupo = reg.getCupo();

    if (cupo != 0 && reg.getInscritos() >= cupo) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        "El congreso no tiene cupo disponible");
    }
    return reg;
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
  public static Congreso afirmarFechaFinFutura (Congreso reg) {
    var now = LocalDateTime.now();
    if (now.isAfter(reg.getFechaFin())) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        "El congreso ya concluyo");
    }
    return reg;
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
  public static Congreso afirmarFechaInicioFutura (Congreso reg) {
    var now = LocalDateTime.now();
    if (now.isAfter(reg.getFechaInicio())) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        "El congreso ya concluyo");
    }
    return reg;
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
  public static Congreso afirmarEnCurso (
    Congreso reg
  ) {
    var now = LocalDateTime.now();
    if (
      now.isBefore(reg.getFechaInicio())
        || now.isAfter(reg.getFechaFin())
    ) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        "El congreso no esta en curso");
    }
    return reg;
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
  public static Congreso afirmarConcluido (
    Congreso reg
  ) {
    if (LocalDateTime.now().isBefore(reg.getFechaFin())) {
      throw new ResponseStatusException(
        HttpStatus.PRECONDITION_FAILED,
        "El congreso no ha concluido");
    }
    return reg;
  }

  /**
   * Determina si un registro cumple con el requerimiento nombrado en la
   * funcion, de lo contrario lanza una excepcion que retorna un error.
   *
   * @param actor
   * Usuario responsable de la operacion.
   *
   * @param reg
   * El registro a validar.
   *
   * @return
   * El registro validado.
   *
   * @throws ResponseStatusException
   * {@code HTTP-UNAUTHORIZED} Si no es el ORGANIZADOR asignado.
   */
  public static Congreso afirmarOrganizadorAsignado (
    Usuario actor, Congreso reg
  )
    throws ResponseStatusException {

    if (!Objects.equals(reg.getOrganizadorId(), actor.getId())) {
      throw new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "No es el organizador asignado.");
    }

    return reg;
  }
}
