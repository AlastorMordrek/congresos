package com.tecn.tijuana.congresos.identidad.control_de_usuarios.dto;

import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Rol;
import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO de resumen de un USUARIO.
 * <p>
 * Expone solo los datos necesarios para identificar visualmente a un USUARIO
 * en pantallas operativas, por ejemplo al registrar una ASISTENCIA. No incluye
 * datos sensibles como Email, Telefono o CURP.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UsuarioResumenDto {

  /**
   * ID del USUARIO.
   * */
  public Long id;

  /**
   * Rol del USUARIO.
   * */
  public Rol rol;

  /**
   * Numero de Control del USUARIO.
   * */
  public String noControl;

  /**
   * Nombre del USUARIO.
   * */
  public String nombre;

  /**
   * Apellido paterno del USUARIO.
   * */
  public String apellidoPaterno;

  /**
   * Apellido materno del USUARIO.
   * */
  public String apellidoMaterno;

  /**
   * Codigo de la carrera del ALUMNO.
   * */
  public String codigoCarrera;

  /**
   * Semestre del ALUMNO.
   * */
  public int semestre;

  /**
   * Grupo del ALUMNO.
   * */
  public String grupo;



  /**
   * Construye el resumen a partir de un USUARIO.
   *
   * @param usr
   * El USUARIO del cual se extraen los datos.
   *
   * @return
   * El resumen del USUARIO.
   */
  public static UsuarioResumenDto de (Usuario usr) {

    return new UsuarioResumenDto(
      usr.getId(),
      usr.getRol(),
      usr.getNoControl(),
      usr.getNombre(),
      usr.getApellidoPaterno(),
      usr.getApellidoMaterno(),
      usr.getCodigoCarrera(),
      usr.getSemestre(),
      usr.getGrupo());
  }
}
