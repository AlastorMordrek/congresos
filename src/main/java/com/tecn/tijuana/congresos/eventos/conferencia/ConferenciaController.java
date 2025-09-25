package com.tecn.tijuana.congresos.eventos.conferencia;

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
@RequestMapping(path = "api/v1/eventos/conferencia")
@Validated
public class ConferenciaController {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final ConferenciaService confSvc;


  /**
   * CONSTRUCTOR principal de esta clase/controller, usado principalmente por
   * Spring para el funcionamiento de la app.
   *
   * @param confSvc
   * Objeto de la capa de servicio de la entidad de CONFERENCIA.
   */
  public ConferenciaController (
    ConferenciaService confSvc
  ) {
    this.confSvc = confSvc;
  }



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite a un ORGANIZADOR registrar una nueva CONFERENCIA.
   *
   * @param conferencia
   * Objeto con los datos del nuevo registro.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El nuevo registro creado.
   */
  @PostMapping("registrar")

  @PreAuthorize(ExpresionSeguridad.REGISTRAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> registrar (

    @RequestBody
    Conferencia conferencia,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      confSvc.registrar(actor, conferencia),
      HttpStatus.CREATED);
  }



  /**
   * Actualiza un registro con los datos especificados.
   *
   * @param id
   * ID del registro a EDITAR.
   *
   * @param conferencia
   * Objeto con los datos nuevos del Conferencia a EDITAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro editado.
   */
  @PatchMapping("editar/{id}")

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> editar (

    @PathVariable
    Long id,

    @RequestBody
    Conferencia conferencia,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      confSvc.editar(actor, id, conferencia),
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

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> editar (

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
      confSvc.editarMedia(actor, id, slot, img),
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

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> publicar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      confSvc.publicar(actor, id, Conferencia.PUBLICADA),
      HttpStatus.OK);
  }

  /**
   * Retracta un registro para que ya no este publicado.
   *
   * @param id
   * ID del registro a RETRACTAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro retractado.
   */
  @PatchMapping("retractar/{id}")

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> retractar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      confSvc.publicar(actor, id, Conferencia.RETRACTADA),
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

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> cancelar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      confSvc.cancelar(actor, id, Conferencia.CANCELADA),
      HttpStatus.OK);
  }

  /**
   * Restaura un registro.
   *
   * @param id
   * ID del registro a RESTAURAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El registro restaurado.
   */
  @PatchMapping("restaurar/{id}")

  @PreAuthorize(ExpresionSeguridad.EDITAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> restaurar (

    @PathVariable
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      confSvc.cancelar(actor, id, Conferencia.RESTAURADA),
      HttpStatus.OK);
  }



  /**
   * Elimina un registro.
   *
   * @param id
   * ID del registro a ELIMINAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * El posible registro eliminado, la respuesta puede estar vacia.
   */
  @DeleteMapping("eliminar/{id}")

  @PreAuthorize(ExpresionSeguridad.ELIMINAR_CONFERENCIAS)

  public ResponseEntity<Conferencia> eliminar (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    var deleted = confSvc.eliminar(actor, id);
    if (Objects.isNull(deleted)) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Consulta los registros publicados de un CONGRESO indiscriminadamente.
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
  @GetMapping("publicadas")

  public ResponseEntity<List<Conferencia>> publicadosCongreso (

    @RequestParam(name = "congresoId")
    Long congresoId,

    @RequestParam(name = "page", required = false, defaultValue = "0")
    @Min(0) @Max(999)
    int page,

    @RequestParam(name = "pageSize", required = false, defaultValue = "10")
    @Min(1) @Max(100)
    int pageSize
  ) {
    return new ResponseEntity<>(
      confSvc.qCongresoPublicadas(congresoId, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta el registro publicado con el ID especifico.
   *
   * @param id
   * ID del registro.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping("publicadas/conferencia/{id}")

  public ResponseEntity<Conferencia> qIdPublicada (

    @PathVariable("id")
    Long id
  ) {
    return new ResponseEntity<>(confSvc.afirmarIdPublicada(id), HttpStatus.OK);
  }



  /**
   * Consulta la foto de la CONFERENCIA publicada especificada en el slot de
   * multimedia especificado.
   *
   * @param id
   * ID de la CONFERENCIA.
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
   * Si la CONFERENCIA no existe o si no tiene una foto.
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si el slot especificado no existe.
   */
  @GetMapping("publicadas/conferencia/{id}/media/{slot}")

  public ResponseEntity<byte[]> qIdPublicadaMedia (

    @PathVariable("id")
    Long id,

    @PathVariable("slot")
    String slot
  ) {
    return confSvc.afirmarMedia(id, slot);
  }



  /**
   * Consulta el registro con el ID especifico.
   *
   * @param id
   * ID del registro.
   *
   * @return
   * El registro encontrado o un error HTTP-404.
   */
  @GetMapping("conferencia/{id}")

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONFERENCIAS_NO_PUBLICADAS)

  public ResponseEntity<Conferencia> qId (

    @PathVariable("id")
    Long id
  ) {
    return new ResponseEntity<>(confSvc.afirmar(id), HttpStatus.OK);
  }



  /**
   * Consulta los registros indiscriminadamente usando una busqueda de texto.
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

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONFERENCIAS_NO_PUBLICADAS)

  public ResponseEntity<List<Conferencia>> buscar (

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
      confSvc.buscar(txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta la foto de la CONFERENCIA especificada en el slot de multimedia
   * especificado.
   *
   * @param id
   * ID de la CONFERENCIA.
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
   * Si la CONFERENCIA no existe o si no tiene una foto.
   * <p>
   * {@code HTTP-BAD_REQUEST}
   * Si el slot especificado no existe.
   */
  @GetMapping("congreso/{id}/media/{slot}")

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_CONFERENCIAS_NO_PUBLICADAS)

  public ResponseEntity<byte[]> qIdMedia (

    @PathVariable("id")
    Long id,

    @PathVariable("slot")
    String slot
  ) {
    return confSvc.afirmarMedia(id, slot);
  }
}
