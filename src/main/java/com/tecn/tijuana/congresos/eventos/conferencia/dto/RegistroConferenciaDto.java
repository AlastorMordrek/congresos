package com.tecn.tijuana.congresos.eventos.conferencia.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * DTO para el registro de CONFERENCIAS,
 * exponiendo solo los campos necesarios.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public  class RegistroConferenciaDto {


  /**
   * CONGRESO donde tendra lugar la CONFERENCIA.
   * */
  public Long congresoId;



  /**
   * Nombre de la CONFERENCIA.
   * */
  @NotBlank(message = "Nombre vacio")
  @Size(min = 1, max = 100,
    message = "El nombre debe tener entre 1 y 100 caracteres")
  public String nombre;

  /**
   * Descripcion corta de la CONFERENCIA.
   * */
  @Size(max = 100,
    message = "El resumen debe tener entre 0 y 100 caracteres")
  public String resumen;

  /**
   * Descripcion detallada de la CONFERENCIA.
   * */
  @Size(max = 500,
    message = "La descripcion debe tener entre 0 y 500 caracteres")
  public String descripcion;

  /**
   * Direccion donde tendra lugar la CONFERENCIA.
   * */
  @Size(max = 100,
    message = "La sala debe tener entre 0 y 100 caracteres")
  public String sala;



  /**
   * Cuando iniciara el evento.
   * */
  public LocalDateTime fechaInicio;

  /**
   * Cuando concluira el evento.
   * */
  public LocalDateTime fechaFin;



  /**
   * Cuantos espacios para inscripciones hay para la CONFERENCIA.
   * */
  @Min(0) @Max(5000)
  public int cupo;



  /**
   * Cuantos integrantes de STAFF se requeriran.
   * */
  @Min(0) @Max(100)
  public int staffCantidad;

  /**
   * Cuantos integrantes de STAFF se requeriran.
   * */
  @Size(max = 500,
    message = "La descripcion de requerimientos de staff debe ser menor" +
      " o igual a 500 caracteres")
  public String staffRequerimientos;



  /**
   * Nombre del conferencista a cargo.
   * */
  @Size(min = 1, max = 100,
    message = "El nombre de conferencista debe tener entre 1 y 100 caracteres")
  public String conferencistaNombre;

  /**
   * Email del conferencista a cargo.
   * */
  @Size(min = 1, max = 100,
    message = "El email de conferencista debe tener entre 1 y 100 caracteres")
  public String conferencistaEmail;

  /**
   * Codigo pais del telefono del conferencista a cargo.
   * */
  @Size(min = 1, max = 7,
    message = "El prefijo de telefono de conferencista debe tener entre" +
      " 1 y 7 caracteres")
  public String conferencistaTelPref;

  /**
   * Numero nacional del telefono del conferencista a cargo.
   * */
  @Size(min = 4, max = 14,
    message = "El telefono de conferencista debe tener entre" +
      " 4 y 14 caracteres")
  public String conferencistaTelSuf;

  /**
   * Semblanza del conferencista a cargo.
   * */
  @Size(min = 1, max = 200,
    message = "La semblanza del conferencista debe tener entre" +
      " 1 y 200 caracteres")
  public String conferencistaSemblanza;
}
