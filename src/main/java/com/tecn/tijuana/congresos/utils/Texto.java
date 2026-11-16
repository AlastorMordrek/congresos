package com.tecn.tijuana.congresos.utils;

import java.util.Objects;

/**
 * Utilidades para el manejo de texto.
 */
public abstract class Texto {

  /**
   * Convierte una cadena vacia o en blanco en {@code null}.
   *
   * @param valor
   * Valor a evaluar.
   *
   * @return
   * El valor original o {@code null} si no contiene texto util.
   */
  public static String blancoANull (String valor) {
    return Objects.isNull(valor) || valor.isBlank() ? null : valor;
  }



  /**
   * Recorta una cadena al limite indicado, sin dejar espacios en los extremos.
   * <p>
   * Si el corte parte una palabra, retrocede al ultimo espacio para no dejar
   * una palabra incompleta, salvo que no exista un espacio util antes del
   * limite.
   *
   * @param valor
   * Valor a recortar.
   *
   * @param maximo
   * Longitud maxima permitida.
   *
   * @return
   * El valor recortado, o {@code null} si el valor es {@code null}.
   */
  public static String recortar (String valor, int maximo) {
    if (Objects.isNull(valor)) {
      return null;
    }

    String limpio = valor.trim();

    if (limpio.length() <= maximo) {
      return limpio;
    }

    String corte = limpio.substring(0, maximo);
    int espacio = corte.lastIndexOf(' ');

    if (espacio > 0) {
      corte = corte.substring(0, espacio);
    }

    return corte.trim();
  }
}
