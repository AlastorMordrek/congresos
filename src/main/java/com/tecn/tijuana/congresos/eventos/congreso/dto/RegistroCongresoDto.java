package com.tecn.tijuana.congresos.eventos.congreso.dto;

import com.tecn.tijuana.congresos.eventos.congreso.Congreso;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * DTO para el registro de CONGRESOS,
 * exponiendo solo los campos necesarios.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegistroCongresoDto {


  /**
   * Nombre del CONGRESO.
   * */
  @NotBlank(message = "Nombre vacio")
  @Size(min = 1, max = 100,
    message = "El nombre debe tener entre 1 y 100 caracteres")
  public String nombre;

  /**
   * Descripcion corta del CONGRESO.
   * */
  @Size(max = 100,
    message = "El resumen debe tener entre 0 y 100 caracteres")
  public String resumen;

  /**
   * Descripcion detallada del CONGRESO.
   * */
  @Size(max = 500,
    message = "La descripcion debe tener entre 0 y 500 caracteres")
  public String descripcion;

  /**
   * Direccion donde tendra lugar el CONGRESO.
   * */
  @Size(max = 200,
    message = "La direccion debe tener entre 0 y 200 caracteres")
  public String direccion;



  /**
   * Cuando iniciara el evento.
   * */
  public LocalDateTime fechaInicio;

  /**
   * Cuando concluira el evento.
   * */
  public LocalDateTime fechaFin;



  /**
   * Cuando inicia el periodo de inscripciones para el CONGRESO.
   * <p>
   * Los ALUMNOS solo pueden inscribirse dentro del periodo de inscripciones.
   * <p>
   * ORGANIZADORES y STAFF pueden inscirbir ALUMNOS fuera del periodo de
   * inscripciones, siempre y cuando el CONGRESO no haya concluido aun.
   * */
  public LocalDateTime inscripcionesFechaInicio;

  /**
   * Cuando termina el periodo de inscripciones para el CONGRESO.
   * <p>
   * Los ALUMNOS solo pueden inscribirse dentro del periodo de inscripciones.
   * <p>
   * ORGANIZADORES y STAFF pueden inscirbir ALUMNOS fuera del periodo de
   * inscripciones, siempre y cuando el CONGRESO no haya concluido aun.
   * */
  public LocalDateTime inscripcionesFechaFin;



  /**
   * Determina si el CONGRESO tiene costo. {@code true = No tiene costo}.
   * */
  public boolean gratuito = Congreso.GRATUITO;



  /**
   * Cuantos espacios para inscripciones hay para el CONGRESO.
   * */
  @Min(0) @Max(5000)
  public int cupo;



  /**
   * Cuantos integrantes de STAFF se requeriran en el CONGRESO.
   * */
  @Min(0) @Max(100)
  public int staffCantidad;

  /**
   * Cuantos integrantes de STAFF se requeriran en el CONGRESO.
   * */
  @Size(max = 500,
    message = "La descripcion de requerimientos de staff debe ser menor" +
      " o igual a 500 caracteres")
  public String staffRequerimientos;
}
