package com.tecn.tijuana.congresos.acreditaciones.acreditacion;

import com.tecn.tijuana.congresos.acreditaciones.acreditacion.dto
  .EditarAcreditacionDto;
import com.tecn.tijuana.congresos.security.ExpresionSeguridad;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controlador REST de la entidad ACREDITACION.
 */
@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/acreditaciones/acreditacion")
public class AcreditacionController {

  private final AcreditacionService acrSvc;

  /**
   * CONSTRUCTOR principal del controlador.
   *
   * @param acrSvc
   * Servicio de la entidad ACREDITACION.
   */
  public AcreditacionController (AcreditacionService acrSvc) {
    this.acrSvc = acrSvc;
  }

  /**
   * Consulta una ACREDITACION por su ID.
   *
   * @param id
   * ID de la acreditacion.
   *
   * @return
   * La acreditacion encontrada.
   */
  @GetMapping("{id}")

  @Operation(
    summary = "Consultar acreditacion",
    description = "Consulta una constancia de acreditacion por su ID."
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Acreditacion.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Acreditacion no encontrada",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class)
      )
    )
  })

  @PreAuthorize(ExpresionSeguridad.ACREDITAR_ALUMNOS)

  public ResponseEntity<Acreditacion> qId (@PathVariable Long id) {
    return new ResponseEntity<>(acrSvc.afirmar(id), HttpStatus.OK);
  }

  /**
   * Consulta la ACREDITACION de un BOLETO.
   *
   * @param boletoId
   * ID del BOLETO.
   *
   * @return
   * La acreditacion encontrada.
   */
  @GetMapping("boleto/{boletoId}")

  @Operation(
    summary = "Consultar acreditacion por boleto",
    description = "Consulta la constancia de acreditacion de un boleto."
  )

  @PreAuthorize(ExpresionSeguridad.ACREDITAR_ALUMNOS)

  public ResponseEntity<Acreditacion> qBoletoId (
    @PathVariable Long boletoId
  ) {
    return new ResponseEntity<>(
      acrSvc.qBoletoId(boletoId)
        .orElseThrow(() ->
          new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "El boleto no tiene una acreditacion registrada")),
      HttpStatus.OK);
  }

  /**
   * Consulta publica de la acreditacion por folio largo del BOLETO.
   *
   * @param folioLargo
   * Folio largo del BOLETO.
   *
   * @return
   * La acreditacion encontrada.
   */
  @GetMapping("publico/boleto/{folioLargo}")

  @Operation(
    summary = "Consultar acreditacion publica",
    description = "Consulta la constancia de acreditacion de un boleto usando" +
      " su folio largo. No requiere autenticacion."
  )

  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Consulta exitosa",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = Acreditacion.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Acreditacion no encontrada",
      content = @Content(
        mediaType = "application/problem+json",
        schema = @Schema(
          implementation = org.springframework.http.ProblemDetail.class)
      )
    )
  })

  public ResponseEntity<Acreditacion> qPublicoFolioLargo (
    @PathVariable String folioLargo
  ) {
    return new ResponseEntity<>(
      acrSvc.qFolioLargo(folioLargo)
        .orElseThrow(() ->
          new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "El boleto no tiene una acreditacion registrada")),
      HttpStatus.OK);
  }



  /**
   * Actualiza los datos editables de una ACREDITACION.
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
  @PatchMapping("editar/{id}")

  @Operation(
    summary = "Editar acreditacion",
    description = "Permite ajustar manualmente los datos de la constancia."
  )

  @PreAuthorize(ExpresionSeguridad.ACREDITAR_ALUMNOS)

  public ResponseEntity<Acreditacion> editar (
    @PathVariable Long id,
    @RequestBody @Valid EditarAcreditacionDto dto
  ) {
    return new ResponseEntity<>(acrSvc.editar(id, dto), HttpStatus.OK);
  }

  /**
   * Actualiza una imagen de la ACREDITACION.
   *
   * @param id
   * ID de la acreditacion.
   *
   * @param slot
   * Slot de imagen.
   *
   * @param img
   * Archivo nuevo o {@code null} para limpiar.
   *
   * @return
   * La acreditacion actualizada.
   */
  @PatchMapping("editar/{id}/media/{slot}")

  @Operation(
    summary = "Editar imagen de acreditacion",
    description = "Permite reemplazar o limpiar una imagen de la constancia." +
      " Slots: encabezado, pieImg1..pieImg10, pieFranja, pieAnio."
  )

  @PreAuthorize(ExpresionSeguridad.ACREDITAR_ALUMNOS)

  public ResponseEntity<Acreditacion> editarMedia (
    @PathVariable Long id,
    @PathVariable String slot,
    @RequestParam(name = "img", required = false) MultipartFile img
  ) {
    return new ResponseEntity<>(
      acrSvc.editarMedia(id, slot, img),
      HttpStatus.OK);
  }
}
