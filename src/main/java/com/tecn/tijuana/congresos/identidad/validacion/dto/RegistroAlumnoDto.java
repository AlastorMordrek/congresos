package com.tecn.tijuana.congresos.identidad.validacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class RegistroAlumnoDto {
  public String email;
  public String password;
  public String telPref;
  public String telSuf;
  public String nombre;
  public String apellidoPaterno;
  public String apellidoMaterno;
  public LocalDate fechaNacimiento;
  public String noControl;
  public String codigoCarrera;
  public int semestre;
  public String grupo;
  public boolean externo;
  public String curp;
  public String emailInstitucional;
}
