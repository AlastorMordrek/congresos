package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

import com.tecn.tijuana.congresos.utils.Texto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de la composicion del nombre completo de un USUARIO.
 */
class UsuarioNombreCompletoTest {

  @Test
  void componeNombreYAmbosApellidos () {
    var usuario = new Usuario();
    usuario.setNombre("Ana");
    usuario.setApellidoPaterno("Lopez");
    usuario.setApellidoMaterno("Garcia");

    assertEquals("Ana Lopez Garcia", usuario.getNombreCompleto());
  }

  @Test
  void ignoraApellidosNulosOVacios () {
    var usuario = new Usuario();
    usuario.setNombre("Ana");
    usuario.setApellidoPaterno("Lopez");
    usuario.setApellidoMaterno("  ");

    assertEquals("Ana Lopez", usuario.getNombreCompleto());
  }

  @Test
  void devuelveSoloElNombreCuandoNoHayApellidos () {
    var usuario = new Usuario();
    usuario.setNombre("Ana");

    assertEquals("Ana", usuario.getNombreCompleto());
  }

  @Test
  void noDevuelveElLiteralNullCuandoNoHayDatos () {
    var usuario = new Usuario();

    assertEquals("", usuario.getNombreCompleto());
  }

  @Test
  void recortaElNombreCompletoAlLimiteDelBoleto () {
    var usuario = new Usuario();
    usuario.setNombre("a".repeat(40));
    usuario.setApellidoPaterno("b".repeat(40));
    usuario.setApellidoMaterno("c".repeat(40));

    String completo = usuario.getNombreCompleto();
    String recortado = Texto.recortar(completo, 120);

    assertEquals(122, completo.length());
    assertTrue(recortado.length() <= 120);
    assertTrue(recortado.length() < completo.length());
  }
}
