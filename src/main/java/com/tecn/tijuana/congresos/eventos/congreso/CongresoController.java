package com.tecn.tijuana.congresos.eventos.congreso;

import com.tecn.tijuana.congresos.eventos.congreso.dto.RegistroCongresoDto;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;


@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/eventos/congreso")
@Validated
public class CongresoController {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final CongresoService conSvc;


  /**
   * CONSTRUCTOR principal de esta clase/controller, usado principalmente por
   * Spring para el funcionamiento de la app.
   *
   * @param conSvc
   * Objeto de la capa de servicio de la entidad de CONGRESO.
   */
  public CongresoController (
    CongresoService conSvc
  ) {
    this.conSvc = conSvc;
  }



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite a un ORGANIZADOR registrar un nuevo CONGRESO.
   *
   * @param dto
   * Objeto con los datos del CONGRESO.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El nuevo registro creado.
   */
  @PostMapping("registrar")

  @PreAuthorize(ExpresionSeguridad.REGISTRAR_CONGRESOS)

  public ResponseEntity<Congreso> registrar (

    @RequestBody
    RegistroCongresoDto dto,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.registrar(actor, dto),
      HttpStatus.CREATED);
  }



  /**
   * Actualiza un registro con los datos especificados.
   *
   * @param id
   * ID del registro a EDITAR.
   *
   * @param congreso
   * Objeto con los datos nuevos del Congreso a EDITAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro editado.
   */
  @PatchMapping("editar/{id}")

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONGRESOS)

  public ResponseEntity<Congreso> editar (

    @PathVariable
    Long id,

    @RequestBody
    Congreso congreso,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.editar(actor, id, congreso),
      HttpStatus.OK);
  }

  /**
   * Actualiza el registro en el slot de multimedia especificado.
   *
   * @param id
   * ID del registro a EDITAR.
   *
   * @param slot
   * Nombre del campo a editar.
   *
   * @param img {@code [null]}
   * Posible archivo a poner en el slot, {@code null} si se desea limpiarlo.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro editado.
   */
  @PatchMapping("editar/{id}/media/{slot}")

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONGRESOS)

  public ResponseEntity<Congreso> editar (

    @PathVariable
    Long id,

    @PathVariable
    String slot,

    @RequestPart(required = false)
    MultipartFile img,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.editarMedia(actor, id, slot, img),
      HttpStatus.OK);
  }



  /**
   * Publica un registro para el publico general.
   *
   * @param id
   * ID del registro a PUBLICAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro publicado.
   */
  @PatchMapping("publicar/{id}")

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONGRESOS)

  public ResponseEntity<Congreso> publicar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.publicar(actor, id, Congreso.PUBLICADO),
      HttpStatus.OK);
  }

  /**
   * Retracta un CONGRESO para que ya no este publicado.
   *
   * @param id
   * ID del CONGRESO a RETRACTAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El CONGRESO retractado.
   */
  @PatchMapping("retractar/{id}")

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONGRESOS)

  public ResponseEntity<Congreso> retractar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.publicar(actor, id, Congreso.RETRACTADO),
      HttpStatus.OK);
  }



  /**
   * Cancela un registro.
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
  @PatchMapping("cancelar/{id}")

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONGRESOS)

  public ResponseEntity<Congreso> cancelar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.cancelar(actor, id, Congreso.CANCELADO),
      HttpStatus.OK);
  }

  /**
   * Restaura un CONGRESO.
   *
   * @param id
   * ID del CONGRESO a RESTAURAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El CONGRESO restaurado.
   */
  @PatchMapping("restaurar/{id}")

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONGRESOS)

  public ResponseEntity<Congreso> restaurar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      conSvc.cancelar(actor, id, Congreso.RESTAURADO),
      HttpStatus.OK);
  }



  /**
   * Elimina un CONGRESO.
   *
   * @param id
   * ID del CONGRESO a ELIMINAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El posible CONGRESO eliminado, la respuesta puede estar vacia.
   */
  @DeleteMapping("eliminar/{id}")

  @PreAuthorize(ExpresionSeguridad.ELIMINAR_CONGRESOS)

  public ResponseEntity<Congreso> eliminar (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    var deleted = conSvc.eliminar(actor, id);
    if (Objects.isNull(deleted)) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Consulta los CONGRESOS publicados indiscriminadamente.
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
  @GetMapping("publicados")

  public ResponseEntity<List<Congreso>> publicados (

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      conSvc.qPublicados(page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta los CONGRESOS publicados cuya fecha de terminacion aun es a
   * futuro, incluyendo aquellos que se encuentran cancelados.
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
  @GetMapping("publicados/proximos")

  public ResponseEntity<List<Congreso>> publicadosProximos (

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      conSvc.qPublicadosProximos(page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta el CONGRESO publicado con el ID especifico.
   *
   * @param id
   * ID del CONGRESO.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping("publicados/congreso/{id}")

  public ResponseEntity<Congreso> qIdPublicado (

    @PathVariable("id")
    Long id
  ) {
    return new ResponseEntity<>(conSvc.afirmarPublicado(id), HttpStatus.OK);
  }



  /**
   * Consulta los CONGRESOS publicados usando una busqueda de texto.
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
  @GetMapping("publicados/buscar")

  public ResponseEntity<List<Congreso>> publicadosBuscar (

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
      conSvc.publicadosBuscar(txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta la foto del CONGRESO publicado especificado en el slot de
   * multimedia especificado.
   *
   * @param id
   * ID del CONGRESO.
   *
   * @param slot
   * Slot de multimedia.
   *
   * @return
   * Respuesta HTTP acorde.
   *
   * @throws ResponseStatusException
   * <p>
   * {@code HTTP-NOT_FOUND}
   * Si el CONGRESO no existe o si no tiene una foto.
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si el slot especificado no existe.
   */
  @GetMapping("publicados/congreso/{id}/media/{slot}")

  public ResponseEntity<byte[]> qIdPublicadoMedia (

    @PathVariable("id")
    Long id,

    @PathVariable("slot")
    String slot
  ) {
    return conSvc.afirmarMedia(id, slot);
  }



  /**
   * Consulta el CONGRESO con el ID especifico.
   *
   * @param id
   * ID del CONGRESO.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping("congreso/{id}")

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONGRESOS_NO_PUBLICADOS)

  public ResponseEntity<Congreso> qId (

    @PathVariable("id")
    Long id
  ) {
    return new ResponseEntity<>(conSvc.afirmar(id), HttpStatus.OK);
  }



  /**
   * Consulta los CONGRESOS indiscriminadamente usando una busqueda de texto.
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

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONGRESOS_NO_PUBLICADOS)

  public ResponseEntity<List<Congreso>> buscar (

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
      conSvc.buscar(txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Permite a un ORGANIZADOR consultar sus propios CONGRESOS usando una
   * busqueda de texto opcional.
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

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONGRESOS_PROPIOS)

  public ResponseEntity<List<Congreso>> buscarMios (

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
      conSvc.buscarMios(actor, txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta la foto del CONGRESO especificado en el slot de multimedia
   * especificado.
   *
   * @param id
   * ID del CONGRESO.
   *
   * @param slot
   * Slot de multimedia.
   *
   * @return
   * Respuesta HTTP acorde.
   *
   * @throws ResponseStatusException
   * <p>
   * {@code HTTP-NOT_FOUND}
   * Si el CONGRESO no existe o si no tiene una foto.
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si el slot especificado no existe.
   */
  @GetMapping("congreso/{id}/media/{slot}")

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONGRESOS_NO_PUBLICADOS)

  public ResponseEntity<byte[]> qIdMedia (

    @PathVariable("id")
    Long id,

    @PathVariable("slot")
    String slot
  ) {
    return conSvc.afirmarMedia(id, slot);
  }
}
