package com.tecn.tijuana.congresos.eventos.conferencia.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.*;
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

public class RegistroConferenciaDto {


  /**
   * CONGRESO donde tendra lugar la CONFERENCIA.
   * */
  @NotNull(message = "Debe especificar el ID del Congreso al que pertenece" +
    " la Conferencia")
  public Long congresoId;



  /**
   * Nombre de la CONFERENCIA.
   * */
  @NotBlank(message = "Debe especificar un nombre de Conferencia")
  @Size(min = 1, max = 100,
    message = "El nombre debe tener entre 1 y 100 caracteres")
  public String nombre;

  /**
   * Descripcion corta de la CONFERENCIA.
   * */
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  @Size(max = 100,
    message = "El resumen debe tener entre 0 y 100 caracteres")
  public String resumen = "";

  /**
   * Descripcion detallada de la CONFERENCIA.
   * */
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  @Size(max = 500,
    message = "La descripcion debe tener entre 0 y 500 caracteres")
  public String descripcion = "";

  /**
   * Direccion donde tendra lugar la CONFERENCIA.
   * */
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  @Size(max = 100,
    message = "La sala debe tener entre 0 y 100 caracteres")
  public String sala = "";



  /**
   * Cuando iniciara el evento.
   * */
  @NotNull(message = "Debe especificar una fecha de inicio")
  public LocalDateTime fechaInicio;

  /**
   * Cuando concluira el evento.
   * */
  @NotNull(message = "Debe especificar una fecha de terminacion")
  public LocalDateTime fechaFin;



  /**
   * Cuantos espacios para inscripciones hay para la CONFERENCIA.
   * {@code 0 = sin limite}.
   * */
  @Min(value = 0, message = "El cupo debe ser mayor a 0")
  @Max(value = 5000, message = "El cupo debe ser menor a 5000")
  public int cupo = 0;



  /**
   * Cuantos integrantes de STAFF se requeriran en la CONFERENCIA.
   * {@code 0 = sin staff requerido}.
   * */
  @Min(value = 0, message = "El staff requerido debe ser mayor o igual a 0")
  @Max(value = 100, message = "El staff requerido debe ser menor o igual a 100")
  public int staffCantidad = 0;

  /**
   * Cuantos integrantes de STAFF se requeriran en la CONFERENCIA.
   * */
  @JsonSetter(nulls = Nulls.AS_EMPTY)
  @Size(max = 500,
    message = "La descripcion de requerimientos de staff debe ser menor" +
      " o igual a 500 caracteres")
  public String staffRequerimientos = "";



  /**
   * Nombre del conferencista a cargo.
   * */
  @NotBlank(message = "Nombre de conferencista vacio")
  @Size(min = 1, max = 100,
    message = "El nombre de conferencista debe tener entre 1 y 100 caracteres")
  public String conferencistaNombre;

  /**
   * Email del conferencista a cargo.
   * */
  @NotBlank(message = "Email de conferencista vacio")
  @Size(min = 1, max = 100,
    message = "El email de conferencista debe tener entre 1 y 100 caracteres")
  public String conferencistaEmail;

  /**
   * Codigo pais del telefono del conferencista a cargo.
   * */
  @NotBlank(message = "Prefijo de telefono de conferencista vacio")
  @Size(min = 1, max = 7,
    message = "El prefijo de telefono de conferencista debe tener entre" +
      " 1 y 7 caracteres")
  public String conferencistaTelPref;

  /**
   * Numero nacional del telefono del conferencista a cargo.
   * */
  @NotBlank(message = "Numero de telefono de conferencista vacio")
  @Size(min = 4, max = 14,
    message = "El telefono de conferencista debe tener entre" +
      " 4 y 14 caracteres")
  public String conferencistaTelSuf;

  /**
   * Semblanza del conferencista a cargo.
   * */
  @NotBlank(message = "Semblanza de conferencista vacia")
  @Size(min = 1, max = 200,
    message = "La semblanza del conferencista debe tener entre" +
      " 1 y 200 caracteres")
  public String conferencistaSemblanza;
}
