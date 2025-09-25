package com.tecn.tijuana.congresos.identidad.control_de_usuarios.dto;

import com.tecn.tijuana.congresos.identidad.control_de_usuarios.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * DTO para el registro de USUARIOS,
 * exponiendo solo los campos necesarios.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegistroUsuarioDto {

  /**
   * Rol del USUARIO.
   * */
  @NotNull(message = "El rol es obligatorio")
  @Enumerated(EnumType.STRING)
  public Rol rol = Rol.ALUMNO;



  /**
   * Email del USUARIO.
   * */
  @NotBlank(message = "Email vacio")
  @Email(message = "Email invalido")
  @Size(min = 6, max = 100,
    message = "Email debe tener entre 6 y 100 caracteres")
  public String email;

  /**
   * Contrasena de acceso codificada del USUARIO.
   * */
  @NotBlank(message = "Clave vacia")
  @Size(min = 8, max = 100,
    message = "La clave debe tener entre 8 y 100 caracteres")
  public String password;



  /**
   * Determina si el USUARIO esta bloqueado, impidiendole hacer uso del sistema.
   * */
  public boolean bloqueado = false;

  /**
   * Determina si la cuenta del USUARIO esta expirada.
   * */
  public boolean expirado = false;

  /**
   * Determina si las credenciales de acceso del USUARIO estan expiradas.
   * */
  public boolean credencialesExpiradas = false;

  /**
   * Determina si la cuenta del USUARIO esta deshabilitada.
   * */
  public boolean deshabilitado = false;



  /**
   * Codigo de pais del telefono del USUARIO.
   * */
  @NotBlank(message = "Prefijo de telefono vacio")
  @Size(min = 1, max = 7,
    message = "Prefijo debe tener entre 1 y 7 digitos")
  public String telPref;

  /**
   * Numero de telefono nacional del USUARIO.
   * */
  @NotBlank(message = "Sufijo de telefono vacio")
  @Size(min = 4, max = 14,
    message = "Sufijo debe tener entre 4 y 14 digitos")
  public String telSuf;



  /**
   * Nombre del USUARIO.
   * */
  @NotBlank(message = "Nombre vacio")
  @Size(min = 3, max = 40,
    message = "El nombre debe tener entre 3 y 40 caracteres")
  public String nombre;

  /**
   * Apellido paterno del USUARIO.
   * */
  @Size(max = 40,
    message = "El apellido paterno debe tener entre 0 y 40 caracteres")
  public String apellidoPaterno;

  /**
   * Apellido materno del USUARIO.
   * */
  @Size(max = 40,
    message = "El apellido materno debe tener entre 0 y 40 caracteres")
  public String apellidoMaterno;

  /**
   * Fecha de nacimiento del USUARIO.
   * */
  @Past
  public LocalDateTime fechaNacimiento;



  /**
   * Numero de control ALUMNO.
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  @Size(min = 8, max = 8,
    message = "El numero de control debe tener 8 caracteres")
  public String noControl;

  /**
   * Codigo de la carrera del ALUMNO.
   * Ej: "ISC".
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  @Size(min = 2, max = 3,
    message = "El codigo de carrera debe tener entre 2 y 3 caracteres")
  public String codigoCarrera;

  /**
   * Semestre que esta cursando el ALUMNO al momento del registro.
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  @Min(value = 0, message = "El semestre no puede ser menor que 0")
  @Max(value = 50, message = "El semestre no puede ser mayor que 50")
  public int semestre;

  /**
   * Grupo donde esta cursando el ALUMNO.
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  @Size(min = 1, max = 3,
    message = "El grupo debe tener entre 1 y 3 caracteres")
  public String grupo;

  /**
   * Determina si es un ALUMNO externo, proveniente de otra institucion
   * educativa.
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  public boolean externo = false;

  /**
   * CURP del ALUMNO.
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  @Size(min = 18, max = 18,
    message = "La CURP debe tener 18 caracteres")
  public String curp;

  /**
   * Email institucional del ALUMNO.
   * Solo aplica para USUARIOS tipo ALUMNO.
   * */
  @Email(message = "Email invalido")
  @Size(min = 6, max = 100,
    message = "Email debe tener entre 6 y 100 caracteres")
  public String emailInstitucional;



  /**
   * Describe textualmente las responsabilidades que tiene un miembro del STAFF.
   * Solo es informativo y no afecta comportamiento del sistema.
   * Solo aplica para USUARIOS tipo STAFF.
   * */
  @Size(max = 200,
    message = "Responsabilidades debe tener maximo 200 caracteres")
  public String staffResponsabilidades;

  /**
   * Interruptor general de STAFF, sin este no puede ejercer sus funciones.
   * Solo aplica para USUARIOS tipo STAFF.
   * */
  public boolean staffAutorizado = false;

  /**
   * Determins si el STAFF puede realizar tareas de custodio, es decir, validar
   * la entrada de ALUMNOS a eventos de Congresos y Conferencias.
   * Solo aplica para USUARIOS tipo STAFF.
   * */
  public boolean staffCustodio = false;

  /**
   * Determina si el STAFF puede realizar operaciones sobre los ALUMNOS, como
   * registros, ediciones, bloqueos, etc...
   * Solo aplica para USUARIOS tipo STAFF.
   * */
  public boolean staffAlumnos = false;

  /**
   * Determina si el STAFF puede inscribir ALUMNOS a eventos y realizar
   * operaciones relacionadas.
   * Solo aplica para USUARIOS tipo STAFF.
   * */
  public boolean staffInscripciones = false;
}
