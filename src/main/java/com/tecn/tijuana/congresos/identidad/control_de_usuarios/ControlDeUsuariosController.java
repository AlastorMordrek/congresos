package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

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

import java.util.List;
import java.util.Objects;


@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/identidad/control-de-usuarios")
@Validated
public class ControlDeUsuariosController {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private final ControlDeUsuariosService usrSvc;


  /**
   * CONSTRUCTOR principal de esta clase/controller, usado principalmente por
   * Spring para el funcionamiento de la app.
   *
   * @param usrSvc
   * Objeto de la capa de servicio de la entidad de USUARIO.
   */
  public ControlDeUsuariosController (
    ControlDeUsuariosService usrSvc
  ) {
    this.usrSvc = usrSvc;
  }



  //----------------------------------------------------------------------------
  // COMANDOS.

  /**
   * Permite a un miembro del personal REGISTRAR a otro usuario.
   *
   * @param usuario
   * Objeto con los datos del usuario.
   *
   * @param img {@code [null]}
   * Foto de perfil.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @PostMapping("registrar")

  @PreAuthorize(ExpresionSeguridad.REGISTRAR_USUARIOS)

  public ResponseEntity<Usuario> registrar (

    @RequestPart
    Usuario usuario,

    @RequestPart(required = false)
    MultipartFile img,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      usrSvc.registrar(actor, usuario, img),
      HttpStatus.CREATED);
  }



  /**
   * Permite a un USUARIO EDITAR a otro, siempre y cuando tenga los permisos
   * necesarios.
   *
   * @param id
   * ID del USUARIO a EDITAR.
   *
   * @param usr
   * Objeto con los datos nuevos del Usuario a EDITAR.
   *
   * @param img {@code [null]}
   * Foto de perfil.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @PatchMapping("editar/{id}")

  public ResponseEntity<Usuario> editar (

    @PathVariable
    Long id,

    @RequestPart
    Usuario usr,

    @RequestPart(required = false)
    MultipartFile img,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      usrSvc.editar(actor, id, usr, img),
      HttpStatus.OK);
  }



  /**
   * Permite a un USUARIO EDITAR sus propios datos.
   *
   * @param usr
   * Objeto con los datos nuevos del USUARIO.
   *
   * @param img {@code [null]}
   * Foto de perfil.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @PatchMapping("editarme")

  public ResponseEntity<Usuario> editarme (

    @RequestPart
    Usuario usr,

    @RequestPart(required = false)
    MultipartFile img,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(
      usrSvc.editarme(actor, usr, img),
      HttpStatus.OK);
  }



  /**
   * Permite a un USUARIO ELIMINAR a otro siempre y cuando tenga los permisos
   * suficientes.
   *
   * @param id
   * ID del USUARIO a ELIMINAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @DeleteMapping("eliminar/{id}")

  public ResponseEntity<Usuario> eliminar (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    var deleted = usrSvc.eliminar(actor, id);
    if (Objects.isNull(deleted)) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(deleted, HttpStatus.OK);
    }
  }



  /**
   * Permite a un USUARIO BLOQUEAR a otro siempre y cuando tenga los permisos
   * suficientes.
   *
   * @param id
   * ID del USUARIO a BLOQUEAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @PatchMapping("bloquear/{id}")

  public ResponseEntity<Usuario> bloquear (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(usrSvc.bloquear(actor, id), HttpStatus.OK);
  }



  /**
   * Permite a un USUARIO DES-BLOQUEAR a otro siempre y cuando tenga los permisos
   * suficientes.
   *
   * @param id
   * ID del USUARIO a DES-BLOQUEAR.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @PatchMapping("desbloquear/{id}")

  public ResponseEntity<Usuario> desbloquear (

    @PathVariable("id")
    Long id,

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(usrSvc.desbloquear(actor, id), HttpStatus.OK);
  }



  //----------------------------------------------------------------------------
  // CONSULTAS.

  /**
   * Consulta los USUARIOS en general, opcionalmente usando una busqueda de
   * texto.
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
   * Respuesta HTTP acorde.
   */
  @GetMapping("buscar")

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_USUARIOS)

  public ResponseEntity<List<Usuario>> buscar (

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
      txt.isBlank()
        ? usrSvc.q(page, pageSize)
        : usrSvc.buscar(txt, page, pageSize),
      HttpStatus.OK);
  }



  /**
   * Consulta el USUARIO con el ID especifico.
   *
   * @param id
   * ID del USUARIO.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @GetMapping("usuario/{id}")

  @PreAuthorize(ExpresionSeguridad.CONSULTAR_USUARIOS)

  public ResponseEntity<Usuario> qId (

    @PathVariable("id")
    Long id
  ) {
    return new ResponseEntity<>(usrSvc.afirmar(id), HttpStatus.OK);
  }



  /**
   * Consulta el USUARIO propio.
   *
   * @param actor
   * USUARIO responsable de la peticion, inyectado por SpringSecurity.
   *
   * @return
   * Respuesta HTTP acorde.
   */
  @GetMapping("mio")

  public ResponseEntity<Usuario> qMio (

    @AuthenticationPrincipal
    Usuario actor
  ) {
    return new ResponseEntity<>(usrSvc.afirmar(actor.getId()), HttpStatus.OK);
  }
}
