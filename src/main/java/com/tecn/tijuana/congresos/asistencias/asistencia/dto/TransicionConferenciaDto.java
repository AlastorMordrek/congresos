package com.tecn.tijuana.congresos.asistencias.asistencia.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * DTO para la transicion masiva de ALUMNOS entre CONFERENCIAS,
 * usando una lista blanca o negra de Folios de BOLETO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TransicionConferenciaDto {


  /**
   * ID de la CONFERENCIA de origen.
   */
  @NotNull(message = "Se debe especificar el ID de la conferencia de origen")
  public Long conferenciaAnteriorId;

  /**
   * ID de la CONFERENCIA de destino.
   */
  @NotNull(message = "Se debe especificar el ID de la conferencia de destino")
  public Long conferenciaPosteriorId;

  /**
   * Lista de Folios de BOLETO (0-100 elementos, cada uno de exactamente 6
   * caracteres).
   */
  @NotNull(message = "Se debe especificar la lista de folios")
  @Size(max = 100, message = "La lista puede tener a lo maximo 100 elementos")
  public List<
    @NotNull(message = "Los folios no pueden ser nulos")
    @Size(min = 6, max = 6,
      message = "Cada folio debe tener exactamente 6 caracteres")
    String> folios;

  /**
   * Modo de la lista: {@code true} = lista blanca (solo los folios listados
   * pasan); {@code false} = lista negra (pasan todos excepto los listados).
   */
  @NotNull(message = "Se debe especificar si la lista es blanca (true) " +
    "o negra (false)")
  public Boolean listaBlanca;
}

