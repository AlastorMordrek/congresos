package com.tecn.tijuana.congresos.boletos.boleto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO para que un ALUMNO se inscriba a un CONGRESO y se le genere su BOLETO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class BoletoInscribirseDto {


  /**
   * CONGRESO para al cual pertenece el registro.
   * */
  public Long congresoId;
}
