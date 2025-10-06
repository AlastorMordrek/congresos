package com.tecn.tijuana.congresos.asistencias.asistencia;

import com.tecn.tijuana.congresos.boletos.boleto.Boleto;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.security.ExpresionSeguridad;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase controladora principal de la entidad.
 * */
@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/asistencias/asistencia")
@Validated
public class AsistenciaController {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final AsistenciaService astSvc;


  /**
   * CONSTRUCTOR principal de esta clase/controller, usado principalmente por
   * Spring para el funcionamiento de la app.
   *
   * @param astSvc
   * Objeto de la capa de servicio de la entidad de ASISTENCIA.
   */
  public AsistenciaController (
    AsistenciaService astSvc
  ) {
    this.astSvc = astSvc;
  }



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a un
   * CONGRESO usando su BOLETO.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param boletoFolio
   * Folio del BOLETO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El BOLETO marcado como usado.
   */
  @PostMapping("asistirCongreso/{congresoId}/" +
    "boletoFolio/{boletoFolio}")

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Boleto> asistirCongresoConFolio (

    @PathVariable
    Long congresoId,

    @PathVariable
    String boletoFolio,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.asistirCongresoConBoleto(actor, congresoId, boletoFolio),
      HttpStatus.CREATED);
  }



  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a un
   * CONGRESO usando su Numero de Control.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param noControlAlumno
   * Numero de Control del ALUMNO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El BOLETO marcado como usado.
   */
  @PostMapping("asistirCongreso/{congresoId}/" +
    "noControlAlumno/{noControlAlumno}")

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Boleto> asistirCongresoConNoControl (

    @PathVariable
    Long congresoId,

    @PathVariable
    String noControlAlumno,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.asistirCongresoConNoControl(
        actor, congresoId, noControlAlumno),
      HttpStatus.CREATED);
  }



  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a una
   * CONFERENCIA usando su BOLETO.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param boletoFolio
   * Folio del BOLETO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * La ASISTENCIA registrada/actualizada.
   */
  @PostMapping("asistirConferencia/{conferenciaId}/" +
    "boletoFolio/{boletoFolio}")

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Asistencia> asistirConferenciaConFolio (

    @PathVariable
    Long conferenciaId,

    @PathVariable
    String boletoFolio,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.asistirConferenciaConBoleto(
        actor, conferenciaId, boletoFolio),
      HttpStatus.CREATED);
  }



  /**
   * Permite al personal autorizado registrar la ENTRADA de un ALUMNO a una
   * CONFERENCIA usando su Numero de Control.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param noControlAlumno
   * Numero de Control del ALUMNO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * La ASISTENCIA registrada/actualizada.
   */
  @PostMapping("asistirConferencia/{conferenciaId}/" +
    "noControlAlumno/{noControlAlumno}")

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Asistencia> asistirConferenciaConNoControl (

    @PathVariable
    Long conferenciaId,

    @PathVariable
    String noControlAlumno,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.asistirConferenciaConNoControl(
        actor, conferenciaId, noControlAlumno),
      HttpStatus.CREATED);
  }



  /**
   * Permite al personal autorizado registrar la SALIDA de un ALUMNO a una
   * CONFERENCIA usando su BOLETO.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param boletoFolio
   * Folio del BOLETO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * La ASISTENCIA registrada/actualizada.
   */
  @PostMapping("salirDeConferencia/{conferenciaId}/" +
    "boletoFolio/{boletoFolio}")

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Asistencia> salirDeConferenciaConFolio (

    @PathVariable
    Long conferenciaId,

    @PathVariable
    String boletoFolio,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.salirDeConferenciaConBoleto(
        actor, conferenciaId, boletoFolio),
      HttpStatus.CREATED);
  }



  /**
   * Permite al personal autorizado registrar la SALIDA de un ALUMNO a una
   * CONFERENCIA usando su Numero de Control.
   *
   * @param conferenciaId
   * ID de la CONFERENCIA.
   *
   * @param noControlAlumno
   * Numero de Control del ALUMNO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * La ASISTENCIA registrada/actualizada.
   */
  @PostMapping("salirDeConferencia/{conferenciaId}/" +
    "noControlAlumno/{noControlAlumno}")

  @PreAuthorize(ExpresionSeguridad.CUSTODIAR_ENTRADA)

  public ResponseEntity<Asistencia> salirDeConferenciaConNoControl (

    @PathVariable
    Long conferenciaId,

    @PathVariable
    String noControlAlumno,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      astSvc.salirDeConferenciaConNoControl(
        actor, conferenciaId, noControlAlumno),
      HttpStatus.CREATED);
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Consulta las asistencias de un ALUMNO via el Folio Largo de su BOLETO.
   *
   * @param boletoFolioLargo
   * Folio Largo del BOLETO.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("publico/boleto/{boletoFolioLargo}")

  public ResponseEntity<List<Asistencia>> qBoletoFolioLargo (

    @PathVariable("boletoFolioLargo")
    String boletoFolioLargo,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      astSvc.qBoletoFolioLargo(boletoFolioLargo, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta la lista de asistencias de una CONFERENCIA.
   *
   * @param conferenciaId
   * ID de la ASISTENCIA.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("conferencia/{conferenciaId}")

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_ASISTENCIAS_AJENAS)

  public ResponseEntity<List<Asistencia>> qConferenciaId (

    @PathVariable("conferenciaId")
    Long conferenciaId,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      astSvc.qConferenciaId(conferenciaId, page, pageSize),
      HttpStatus.OK);
  }
}
