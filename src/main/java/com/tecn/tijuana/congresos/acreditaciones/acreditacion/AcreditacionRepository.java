package com.tecn.tijuana.congresos.acreditaciones.acreditacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de la entidad ACREDITACION.
 */
@Repository
public interface AcreditacionRepository
  extends JpaRepository<Acreditacion, Long> {

  /**
   * Busca la acreditacion de un BOLETO.
   *
   * @param boletoId
   * ID del BOLETO.
   *
   * @return
   * La acreditacion si existe.
   */
  Optional<Acreditacion> findByBoletoId (Long boletoId);

  /**
   * Busca la acreditacion por el folio largo del BOLETO.
   *
   * @param folioLargo
   * Folio largo del BOLETO.
   *
   * @return
   * La acreditacion si existe.
   */
  Optional<Acreditacion> findByBoletoFolioLargo (String folioLargo);
}
