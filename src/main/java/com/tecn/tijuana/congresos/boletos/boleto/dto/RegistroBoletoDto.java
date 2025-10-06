package com.tecn.tijuana.congresos.boletos.boleto.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO para el registro de BOLETOS,
 * exponiendo solo los campos necesarios.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegistroBoletoDto {


  /**
   * CONGRESO para al cual pertenece el registro.
   * */
  public Long congresoId;



  /**
   * ID del ALUMNO al que pertenece el registro.
   * */
  public Long alumnoId;

  /**
   * Numero de control del ALUMNO al que pertenece el registro.
   * */
  @Size(min = 8, max = 8,
    message = "El numero de control de alumno debe tener 8 caracteres")
  public String alumnoNoControl;
}
