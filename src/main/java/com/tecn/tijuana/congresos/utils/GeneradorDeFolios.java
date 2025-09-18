package com.tecn.tijuana.congresos.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.EnumSet;


public class GeneradorDeFolios implements BeforeExecutionGenerator {

  //----------------------------------------------------------------------------
  // Variables auxiliares de clase.

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();
  private int longitud;



  /**
   * CONSTRUCTOR.
   */
  public GeneradorDeFolios () {}



  /**
   * Funcion usada por hibernate para configurar el generador.
   *
   * @param annotation
   * Parametro de hibernate?.
   *
   * @param propertyType
   * Parametro de hibernate?.
   */
  public void initialize (
    FolioGenerado annotation, Class<?> propertyType
  ) {
    this.longitud = annotation.longitud();
  }



  /**
   * Describe para que tipos de eventos de la BD se usara el generador.
   *
   * @return
   * El Token generado.
   */
  @Override
  public EnumSet<EventType> getEventTypes () {
    return EnumSet.of(EventType.INSERT);
  }



  /**
   * Funcion llamada por hibernate para generar el valor.
   *
   * @param sessionFactory
   * Parametro de hibernate?.
   *
   * @param owner
   * Parametro de hibernate?.
   *
   * @param currentValue
   * Parametro de hibernate?.
   *
   * @param eventType
   * Parametro de hibernate?.
   *
   * @return
   * El Token generado.
   */
  @Override
  public Object generate (
    SharedSessionContractImplementor sessionFactory,
    Object owner, Object currentValue, EventType eventType
  ) {
    if (eventType == EventType.INSERT) {
      return generateRandomBase36(this.longitud);
    }
    return currentValue;
  }




  /**
   * Un Token string aleatorio con la longitud especificada.
   *
   * @param longitud
   * La longitud del Token deseada.
   *
   * @return
   * El Token generado.
   */
  private String generateRandomBase36 (
    int longitud
  ) {

    // Contenedor para los random bytes.
    byte[] randomBytes = new byte[longitud * 2];

    // Generar bytes.
    SECURE_RANDOM.nextBytes(randomBytes);

    // Convertir bytes a un numero entero.
    BigInteger bigInt = new BigInteger(1, randomBytes);

    // Convertir el numero entero a una string base 36.
    StringBuilder base36 = new StringBuilder(
      bigInt.toString(36).toUpperCase());

    // Si el Token es demasiado largo, usar solo los ultimos N caracteres.
    if (base36.length() > longitud) {
      return base36.substring(base36.length() - longitud);
    }

    // Si el Token es demasiado corto rellenarlo con ceros al principio.
    while (base36.length() < longitud) {
      base36.insert(0, "0");
    }

    // Retornar token.
    return base36.toString();
  }
}



