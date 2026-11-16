package com.tecn.tijuana.congresos.acreditaciones.acreditacion.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO con los campos editables de una ACREDITACION.
 * <p>
 * Permite ajustar manualmente cualquier dato impreso en la constancia.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class EditarAcreditacionDto {

  /** Nombre completo del ALUMNO. */
  @NotBlank(message = "El nombre del alumno es obligatorio")
  @Size(max = 120, message = "El nombre no puede exceder 120 caracteres")
  public String alumnoNombre;

  /** Nombre de la carrera; si es nulo el frontend lo resuelve. */
  @Size(max = 150)
  public String carreraNombre;

  /** Fecha impresa en la constancia. */
  @NotNull(message = "La fecha de emision es obligatoria")
  public LocalDate fechaEmision;

  /** Periodo escolar. */
  @Size(max = 30)
  public String periodo;

  /** Año del periodo. */
  @Min(value = 2000, message = "El año no es valido")
  @Max(value = 2100, message = "El año no es valido")
  public int anio;

  /** Creditos otorgados. */
  @Min(value = 1, message = "La constancia debe otorgar al menos 1 credito")
  @Max(value = 20, message = "La constancia no puede otorgar mas de 20 creditos")
  public int creditos = 1;

  /** Nombre de la institucion. */
  @Size(max = 150)
  public String instituto;

  /** Subtitulo de la constancia. */
  @Size(max = 150)
  public String subtitulo;

  /** Ciudad de emision. */
  @Size(max = 80)
  public String ciudad;

  /** Estado de emision. */
  @Size(max = 80)
  public String estado;

  /** Pais de emision. */
  @Size(max = 80)
  public String pais;

  /** Nombre de la jefatura de servicios escolares. */
  @Size(max = 100)
  public String jefaNombre;

  /** Cargo de la jefatura de servicios escolares. */
  @Size(max = 150)
  public String jefaCargo;

  /** Nombre de quien firma. */
  @Size(max = 100)
  public String jefeNombre;

  /** Departamento de quien firma. */
  @Size(max = 150)
  public String departamento;

  /** Primer lema institucional. */
  @Size(max = 150)
  public String lema1;

  /** Segundo lema institucional. */
  @Size(max = 150)
  public String lema2;

  /** Texto del pie. */
  @Size(max = 500)
  public String pieTexto;

  /** Nota opcional de copia. */
  @Size(max = 100)
  public String notaCcp;

  /** Iniciales opcionales. */
  @Size(max = 50)
  public String iniciales;

  /** Actividad opcional. */
  @Size(max = 200)
  public String actividad;

  /** Nivel de desempeno opcional. */
  @Size(max = 100)
  public String nivelDesempeno;
}
