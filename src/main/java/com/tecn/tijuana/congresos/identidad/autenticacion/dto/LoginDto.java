package com.tecn.tijuana.congresos.identidad.autenticacion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO para el registro de USUARIOS,
 * exponiendo solo los campos necesarios.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class LoginDto {

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
}
