package com.tecn.tijuana.congresos.acreditaciones.acreditacion;

import com.tecn.tijuana.congresos.acreditaciones.acreditacion.dto
  .EditarAcreditacionDto;
import com.tecn.tijuana.congresos.boletos.boleto.Boleto;
import com.tecn.tijuana.congresos.eventos.congreso.Congreso;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.utils.Texto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Clase principal de la capa de servicio para la entidad ACREDITACION.
 */
@Service
public class AcreditacionService {

  private final AcreditacionRepository acrRep;

  /**
   * CONSTRUCTOR principal de la clase/servicio.
   *
   * @param acrRep
   * Repositorio de la entidad ACREDITACION.
   */
  public AcreditacionService (AcreditacionRepository acrRep) {
    this.acrRep = acrRep;
  }

  /**
   * Crea o reemplaza la acreditacion de un BOLETO con los datos actuales del
   * ALUMNO y del CONGRESO.
   *
   * @param actor
   * ORGANIZADOR que emite la constancia.
   *
   * @param boleto
   * BOLETO acreditado.
   *
   * @param congreso
   * CONGRESO del boleto.
   *
   * @param alumno
   * ALUMNO del boleto.
   *
   * @return
   * La acreditacion creada o actualizada.
   */
  public Acreditacion crearDesdeBoleto (
    Usuario actor,
    Boleto boleto,
    Congreso congreso,
    Usuario alumno
  ) {
    afirmarAlumnoAcreditable(alumno);

    var acreditacion = acrRep.findByBoletoId(boleto.getId())
      .orElseGet(Acreditacion::new);

    acreditacion.setFechaCreacion(LocalDateTime.now());
    acreditacion.setCreadorId(actor.getId());

    acreditacion.setBoletoId(boleto.getId());
    acreditacion.setBoletoFolio(boleto.getFolio());
    acreditacion.setBoletoFolioLargo(boleto.getFolioLargo());

    acreditacion.setAlumnoId(alumno.getId());
    acreditacion.setAlumnoNoControl(alumno.getNoControl());
    acreditacion.setAlumnoNombre(
      Texto.recortar(alumno.getNombreCompleto(), 120));
    acreditacion.setCarreraCodigo(alumno.getCodigoCarrera());
    acreditacion.setCarreraNombre(null);

    acreditacion.setCongresoId(congreso.getId());
    acreditacion.setCongresoNombre(congreso.getNombre());
    acreditacion.setCongresoFechaInicio(congreso.getFechaInicio());
    acreditacion.setCongresoFechaFin(congreso.getFechaFin());

    acreditacion.setFechaEmision(LocalDate.now());
    acreditacion.setPeriodo(periodoDe(congreso.getFechaInicio()));
    acreditacion.setAnio(congreso.getFechaInicio().getYear());
    acreditacion.setCreditos(congreso.getConstanciaCreditos());

    acreditacion.setInstituto(congreso.getConstanciaInstituto());
    acreditacion.setSubtitulo(congreso.getConstanciaSubtitulo());
    acreditacion.setCiudad(congreso.getConstanciaCiudad());
    acreditacion.setEstado(congreso.getConstanciaEstado());
    acreditacion.setPais(congreso.getConstanciaPais());

    acreditacion.setJefaNombre(congreso.getConstanciaJefaNombre());
    acreditacion.setJefaCargo(congreso.getConstanciaJefaCargo());
    acreditacion.setJefeNombre(congreso.getConstanciaJefeNombre());
    acreditacion.setDepartamento(congreso.getConstanciaDepartamento());

    acreditacion.setLema1(congreso.getConstanciaLema1());
    acreditacion.setLema2(congreso.getConstanciaLema2());
    acreditacion.setPieTexto(congreso.getConstanciaPieTexto());
    acreditacion.setNotaCcp(congreso.getConstanciaNotaCcp());
    acreditacion.setIniciales(congreso.getConstanciaIniciales());
    acreditacion.setActividad(congreso.getConstanciaActividad());
    acreditacion.setNivelDesempeno(
      congreso.getConstanciaNivelDesempeno());

    // Al reemitir se restablecen los ajustes manuales previos.
    acreditacion.setEncabezadoMimeType(null);
    acreditacion.setEncabezadoImgData(null);
    acreditacion.setPieImg1MimeType(congreso.getConstanciaPieImg1MimeType());
    acreditacion.setPieImg1Data(congreso.getConstanciaPieImg1Data());
    acreditacion.setPieImg2MimeType(congreso.getConstanciaPieImg2MimeType());
    acreditacion.setPieImg2Data(congreso.getConstanciaPieImg2Data());
    acreditacion.setPieImg3MimeType(congreso.getConstanciaPieImg3MimeType());
    acreditacion.setPieImg3Data(congreso.getConstanciaPieImg3Data());
    acreditacion.setPieImg4MimeType(congreso.getConstanciaPieImg4MimeType());
    acreditacion.setPieImg4Data(congreso.getConstanciaPieImg4Data());
    acreditacion.setPieImg5MimeType(congreso.getConstanciaPieImg5MimeType());
    acreditacion.setPieImg5Data(congreso.getConstanciaPieImg5Data());
    acreditacion.setPieImg6MimeType(congreso.getConstanciaPieImg6MimeType());
    acreditacion.setPieImg6Data(congreso.getConstanciaPieImg6Data());
    acreditacion.setPieImg7MimeType(congreso.getConstanciaPieImg7MimeType());
    acreditacion.setPieImg7Data(congreso.getConstanciaPieImg7Data());
    acreditacion.setPieImg8MimeType(congreso.getConstanciaPieImg8MimeType());
    acreditacion.setPieImg8Data(congreso.getConstanciaPieImg8Data());
    acreditacion.setPieImg9MimeType(congreso.getConstanciaPieImg9MimeType());
    acreditacion.setPieImg9Data(congreso.getConstanciaPieImg9Data());
    acreditacion.setPieImg10MimeType(congreso.getConstanciaPieImg10MimeType());
    acreditacion.setPieImg10Data(congreso.getConstanciaPieImg10Data());
    acreditacion.setPieFranjaMimeType(congreso.getConstanciaPieFranjaMimeType());
    acreditacion.setPieFranjaData(congreso.getConstanciaPieFranjaData());
    acreditacion.setPieAnioMimeType(congreso.getConstanciaAnioMimeType());
    acreditacion.setPieAnioData(congreso.getConstanciaAnioData());
    acreditacion.setFirmaMimeType(congreso.getConstanciaFirmaMimeType());
    acreditacion.setFirmaData(congreso.getConstanciaFirmaData());

    return acrRep.saveAndFlush(acreditacion);
  }

  /**
   * Encuentra una ACREDITACION por su ID.
   *
   * @param id
   * ID de la acreditacion.
   *
   * @return
   * La acreditacion encontrada.
   */
  public Acreditacion afirmar (Long id) {
    return acrRep.findById(id)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format("No se encontro la acreditacion con ID %s", id)));
  }

  /**
   * Busca la acreditacion de un BOLETO.
   *
   * @param boletoId
   * ID del BOLETO.
   *
   * @return
   * La acreditacion si existe.
   */
  public Optional<Acreditacion> qBoletoId (Long boletoId) {
    return acrRep.findByBoletoId(boletoId);
  }

  /**
   * Busca la acreditacion por folio largo del BOLETO.
   *
   * @param folioLargo
   * Folio largo del BOLETO.
   *
   * @return
   * La acreditacion si existe.
   */
  public Optional<Acreditacion> qFolioLargo (String folioLargo) {
    return acrRep.findByBoletoFolioLargo(folioLargo);
  }

  /**
   * Actualiza los campos editables de una ACREDITACION.
   *
   * @param id
   * ID de la acreditacion.
   *
   * @param dto
   * Valores nuevos.
   *
   * @return
   * La acreditacion actualizada.
   */
  public Acreditacion editar (Long id, EditarAcreditacionDto dto) {
    return acrRep.saveAndFlush(afirmar(id).actualizar(dto));
  }

  /**
   * Actualiza una imagen de la ACREDITACION.
   *
   * @param id
   * ID de la acreditacion.
   *
   * @param slot
   * Slot a editar: encabezado, pieImg1..10, pieFranja, pieAnio.
   *
   * @param img
   * Archivo nuevo, o {@code null} para limpiar el slot.
   *
   * @return
   * La acreditacion actualizada.
   */
  public Acreditacion editarMedia (
    Long id, String slot, MultipartFile img
  ) {
    var acreditacion = afirmar(id);

    try {
      acreditacion.setMedia(slot, img);
    } catch (IOException e) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Error al procesar la imagen");
    }

    return acrRep.saveAndFlush(acreditacion);
  }

  /**
   * Elimina la acreditacion asociada a un BOLETO.
   *
   * @param boletoId
   * ID del BOLETO.
   */
  public void eliminarPorBoletoId (Long boletoId) {
    acrRep.findByBoletoId(boletoId).ifPresent(acrRep::delete);
  }

  /**
   * Valida que el ALUMNO tenga los datos minimos para emitir la constancia.
   *
   * @param alumno
   * ALUMNO a validar.
   */
  private void afirmarAlumnoAcreditable (Usuario alumno) {
    String nombre = Objects.isNull(alumno.getNombre())
      ? null
      : alumno.getNombre().trim();
    String noControl = alumno.getNoControl();
    boolean nombreTemporal = Objects.nonNull(nombre)
      && Objects.nonNull(noControl)
      && nombre.equalsIgnoreCase("Alumno " + noControl.trim());

    if (esVacio(nombre)
      || esVacio(noControl)
      || esVacio(alumno.getCodigoCarrera())
      || nombreTemporal) {

      throw new ResponseStatusException(
        HttpStatus.CONFLICT,
        "El alumno no tiene nombre o carrera completos para generar la" +
          " constancia.");
    }
  }

  /**
   * Determina el periodo escolar a partir de la fecha del evento.
   *
   * @param fecha
   * Fecha de inicio del evento.
   *
   * @return
   * ENERO-JUNIO o AGOSTO-DICIEMBRE.
   */
  private String periodoDe (LocalDateTime fecha) {
    return fecha.getMonthValue() >= 8
      ? "AGOSTO-DICIEMBRE"
      : "ENERO-JUNIO";
  }

  /**
   * Indica si una cadena es nula o esta en blanco.
   *
   * @param valor
   * Cadena a evaluar.
   *
   * @return
   * {@code true} si no contiene texto util.
   */
  private boolean esVacio (String valor) {
    return Objects.isNull(valor) || valor.isBlank();
  }
}
