package com.tecn.tijuana.congresos.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de las utilidades de texto.
 */
class TextoTest {

  @Test
  void recortaAlLimiteSinDejarEspacioFinal () {
    String texto = "Nombre ApellidoPaterno ApellidoMaterno";
    String resultado = Texto.recortar(texto, 20);

    assertTrue(resultado.length() <= 20);
    assertEquals(resultado.trim(), resultado);
  }

  @Test
  void retrocedeAlEspacioCuandoElCorteParteUnaPalabra () {
    assertEquals("Nombre Apellido", Texto.recortar("Nombre Apellido Materno", 16));
  }

  @Test
  void cortaSinEspacioCuandoNoHayUnoUtil () {
    assertEquals("Palabra", Texto.recortar("PalabraMuyLarga", 7));
  }

  @Test
  void conservaElTextoCuandoCabe () {
    assertEquals("Ana Lopez", Texto.recortar("  Ana Lopez  ", 120));
  }

  @Test
  void devuelveNullCuandoElValorEsNull () {
    assertNull(Texto.recortar(null, 120));
  }
}
