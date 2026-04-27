package com.tecn.tijuana.congresos.identidad.autenticacion.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO para iniciar sesion,
 * exponiendo solo los campos necesarios.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoginDto {

  /**
   * Email del USUARIO.
   * */
  @NotBlank(message = "Debe especificar un Email")
  @Email(message = "Email invalido")
  @Size(min = 6, max = 100,
    message = "Email debe tener entre 6 y 100 caracteres")
  public String email;

  /**
   * Contrasena de acceso codificada del USUARIO.
   * */
  @NotBlank(message = "Debe especificar una Clave de acceso")
  @Size(min = 8, max = 100,
    message = "La clave debe tener entre 8 y 100 caracteres")
  public String password;
}
