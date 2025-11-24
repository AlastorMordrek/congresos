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



  /**
   * Permite a un ORGANIZADOR o STAFF autorizado registrar un BOLETO incluso si
   * ya se acabo el cupo disponible para el CONGRESO.
   * <p>
   * Los ALUMNOS no pueden inscribirse y registrar su BOLETO si el CONGRESO ya
   * completo su cupo.
   * */
  public boolean registrarComoExcedente = false;
}
