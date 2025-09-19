package com.tecn.tijuana.congresos.boletos.boleto;

import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import com.tecn.tijuana.congresos.security.ExpresionSeguridad;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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
@RequestMapping(path = "api/v1/boletos/boleto")
@Validated
public class BoletoController {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final BoletoService bolSvc;


  /**
   * CONSTRUCTOR principal de esta clase/controller, usado principalmente por
   * Spring para el funcionamiento de la app.
   *
   * @param bolSvc
   * Objeto de la capa de servicio de la entidad de BOLETO.
   */
  public BoletoController (
    BoletoService bolSvc
  ) {
    this.bolSvc = bolSvc;
  }



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite a un ALUMNO inscribirse a CONGRESO, generando su BOLETO.
   *
   * @param boleto
   * Objeto con los datos del BOLETO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El nuevo registro creado.
   */
  @PostMapping("inscribirse")

  @PreAuthorize("hasRole('ALUMNO')")

  public ResponseEntity<Boleto> inscribirse (

    @RequestPart
    Boleto boleto,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.inscribirse(actor, boleto),
      HttpStatus.CREATED);
  }

  /**
   * Permite al personal autorizado inscribir un ALUMNO a un CONGRESO, generando
   * su BOLETO.
   *
   * @param boleto
   * Objeto con los datos del BOLETO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El nuevo registro creado.
   */
  @PostMapping("inscribir")

  @PreAuthorize(ExpresionSeguridad.INSCRIBIR_ALUMNOS)

  public ResponseEntity<Boleto> inscribir (

    @RequestPart
    Boleto boleto,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.inscribir(actor, boleto),
      HttpStatus.CREATED);
  }



  /**
   * Permite a un ALUMNO cancelar un BOLETO propio.
   *
   * @param id
   * ID del registro a CANCELAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro cancelado.
   */
  @PatchMapping("cancelar/mio/{id}")

  @PreAuthorize("hasRole('ALUMNO')")

  public ResponseEntity<Boleto> cancelarMio (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.cancelarMio(actor, id),
      HttpStatus.OK);
  }

  /**
   * Permite al personal autorizado CANCELAR un BOLETO.
   *
   * @param id
   * ID del registro a CANCELAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro CANCELADO.
   */
  @PatchMapping("cancelar/{id}")

  @PreAuthorize(ExpresionSeguridad.GESTIONAR_BOLETOS)

  public ResponseEntity<Boleto> cancelar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.cancelar(actor, id, Boleto.CANCELADO),
      HttpStatus.OK);
  }

  /**
   * Permite al personal autorizado RESTAURAR un BOLETO.
   *
   * @param id
   * ID del registro a RESTAURAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro RESTAURADO.
   */
  @PatchMapping("restaurar/{id}")

  @PreAuthorize(ExpresionSeguridad.GESTIONAR_BOLETOS)

  public ResponseEntity<Boleto> restaurar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.cancelar(actor, id, Boleto.RESTAURADO),
      HttpStatus.OK);
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Consulta los BOLETOS indiscriminadamente usando una busqueda de texto.
   *
   * @param txt {@code [""]}
   * Texto de busqueda.
   *
   * @param page {@code [0]}
   * Numero de pagina.
   *
   * @param pageSize {@code [10]}
   * Tamano de pagina.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("buscar")

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_BOLETOS_AJENOS)

  public ResponseEntity<List<Boleto>> buscar (

    @RequestParam(name = "txt", required = false, defaultValue = "")
    @Size(min = 1, max = 30)
    String txt,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      bolSvc.buscar(txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Permite a un ALUMNO consultar sus boletos usando una busqueda de texto.
   *
   * @param txt {@code [""]}
   * Texto de busqueda.
   *
   * @param page {@code [0]}
   * Numero de pagina.
   *
   * @param pageSize {@code [10]}
   * Tamano de pagina.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("buscar/mios")

  @PreAuthorize("hasRole('ALUMNO')")

  public ResponseEntity<List<Boleto>> buscarMios (

    @RequestParam(name = "txt", required = false, defaultValue = "")
    @Size(min = 1, max = 30)
    String txt,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      bolSvc.buscarMios(actor, txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta BOLETO publicamente usando su Folio Largo.
   *
   * @param folioLargo
   * Folio Largo del BOLETO.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping("publico/boleto/{folioLargo}")

  public ResponseEntity<Boleto> qFolioLargo (

    @PathVariable("folioLargo")
    String folioLargo
  ) {
    return new ResponseEntity<>(
      bolSvc.afirmarFolioLargo(folioLargo),
      HttpStatus.OK);
  }



  /**
   * Permite al personal autorizado consultar BOLETOS via Folio.
   *
   * @param folio
   * Folio del BOLETO.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping()

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_BOLETOS_AJENOS)

  public ResponseEntity<Boleto> qFolio (

    @RequestParam(name = "folio")
    @Size(min = 20, max = 20)
    String folio
  ) {
    return new ResponseEntity<>(
      bolSvc.afirmarFolio(folio),
      HttpStatus.OK);
  }



  /**
   * Consulta los BOLETOS de un CONGRESO indiscriminadamente.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param page {@code [0]}
   * Numero de pagina.
   *
   * @param pageSize {@code [10]}
   * Tamano de pagina.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("congreso/{congresoId}")

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_BOLETOS_AJENOS)

  public ResponseEntity<List<Boleto>> qIdCongreso (

    @PathVariable("congresoId")
    Long congresoId,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      bolSvc.qIdCongreso(congresoId, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta los BOLETOS de un CONGRESO segun el estatus del BOLETO
   * especificado.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param page {@code [0]}
   * Numero de pagina.
   *
   * @param pageSize {@code [10]}
   * Tamano de pagina.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("congreso/{congresoId}/cancelados")

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_BOLETOS_AJENOS)

  public ResponseEntity<List<Boleto>> qIdCongresoCancelados (

    @PathVariable("congresoId")
    Long congresoId,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      bolSvc.qIdCongresoCancelado(
        congresoId, Boleto.CANCELADO, page, pageSize),
      HttpStatus.OK);
  }

  /**
   * Consulta los BOLETOS de un CONGRESO segun el estatus del BOLETO
   * especificado.
   *
   * @param congresoId
   * ID del CONGRESO.
   *
   * @param page {@code [0]}
   * Numero de pagina.
   *
   * @param pageSize {@code [10]}
   * Tamano de pagina.
   *
   * @return
   * Los registros encontrados.
   */
  @GetMapping("congreso/{congresoId}/no-cancelados")

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_BOLETOS_AJENOS)

  public ResponseEntity<List<Boleto>> qIdCongresoNoCancelados (

    @PathVariable("congresoId")
    Long congresoId,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      bolSvc.qIdCongresoCancelado(
        congresoId, Boleto.RESTAURADO, page, pageSize),
      HttpStatus.OK);
  }
}
