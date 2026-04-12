package com.tecn.tijuana.congresos.boletos.boleto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoletoRepository
  extends JpaRepository<Boleto, Long> {

  @Query("SELECT r FROM Boleto r WHERE r.folio = ?1")
  Optional<Boleto> qFolio (String folio);

  @Query("SELECT r FROM Boleto r WHERE r.folioLargo = ?1")
  Optional<Boleto> qFolioLargo (String folio);



  @Query("SELECT r FROM Boleto r WHERE" +
    " r.congresoId = ?1" +
    " ORDER BY r.id DESC")
  Page<Boleto> qCongresoId (Long id, Pageable pageable);

  @Query("SELECT r FROM Boleto r WHERE" +
    " r.congresoId = ?1 AND r.cancelado = ?2" +
    " ORDER BY r.id DESC")
  Page<Boleto> qCongresoIdCancelado (
    Long idCon, boolean estatus, Pageable pageable);



  @Query("SELECT r FROM Boleto r WHERE" +
    " r.alumnoId = ?1" +
    " ORDER BY r.id DESC")
  Page<Boleto> qAlumnoId (Long id, Pageable pageable);



  @Query("SELECT r FROM Boleto r WHERE" +
    " r.congresoId = ?1 AND r.alumnoId = ?2")
  Optional<Boleto> qCongresoIdAlumnoId (Long idCon, Long idAl);



  @Query("SELECT r FROM Boleto r WHERE" +
    " r.congresoId = ?1 AND r.alumnoNoControl = ?2")
  Optional<Boleto> qCongresoIdAlumnoNoControl (
    Long idCon, String alumnoNoControl);



  @Query("SELECT r FROM Boleto r WHERE" +
    " lower(r.folio)                like %:txt%"+
    " OR lower(r.folioLargo)        like %:txt%"+
    " OR lower(r.congresoNombre)    like %:txt%"+
    " OR lower(r.congresoDireccion) like %:txt%"+
    " OR lower(r.alumnoNoControl)   like %:txt%"+
    " OR lower(r.alumnoNombre)      like %:txt%" +
    " ORDER BY r.id DESC")
  Page<Boleto> buscar (@Param("txt") String txt, Pageable pageable);



  @Query("SELECT r FROM Boleto r WHERE" +
    " r.alumnoId = ?1" +
    " AND (" +
    "   lower(r.folio)                like %:txt%"+
    "   OR lower(r.folioLargo)        like %:txt%"+
    "   OR lower(r.congresoNombre)    like %:txt%"+
    "   OR lower(r.congresoDireccion) like %:txt%"+
    "   OR lower(r.alumnoNoControl)   like %:txt%"+
    "   OR lower(r.alumnoNombre)      like %:txt%" +
    " )" +
    " ORDER BY r.id DESC")
  Page<Boleto> buscarAlumnoId (
    Long alumnoId, @Param("txt") String txt, Pageable pageable);



  // Variante con filtros opcionales de texto y de campos booleanos.
  //
  // El patron (:param IS NULL OR r.campo = :param) hace que si el parametro
  // es null, la condicion siempre sea verdadera y no filtre por ese campo.
  //
  // Se usa esta estrategia porque la cantidad de combinaciones posibles de
  // filtros hace impractico crear una consulta para cada combinacion.
  @Query("SELECT r FROM Boleto r WHERE" +
    " (:txt IS NULL OR (" +
    "   lower(r.folio)                like %:txt%" +
    "   OR lower(r.folioLargo)        like %:txt%" +
    "   OR lower(r.congresoNombre)    like %:txt%" +
    "   OR lower(r.congresoDireccion) like %:txt%" +
    "   OR lower(r.alumnoNoControl)   like %:txt%" +
    "   OR lower(r.alumnoNombre)      like %:txt%" +
    " ))" +
    " AND (:alumnoId   IS NULL OR r.alumnoId   = :alumnoId)" +
    " AND (:congresoId IS NULL OR r.congresoId = :congresoId)" +
    " AND (:excedente  IS NULL OR r.excedente  = :excedente)" +
    " AND (:pagado     IS NULL OR r.pagado     = :pagado)" +
    " AND (:cancelado  IS NULL OR r.cancelado  = :cancelado)" +
    " AND (:usado      IS NULL OR r.usado      = :usado)" +
    " AND (:cumplioRequerimientosDeAsistencia IS NULL OR" +
    "   r.cumplioRequerimientosDeAsistencia =" +
    "     :cumplioRequerimientosDeAsistencia)" +
    " AND (:acreditado IS NULL OR r.acreditado = :acreditado)" +
    " ORDER BY r.id DESC")
  Page<Boleto> qFiltrado (
    @Param("txt")        String txt,
    @Param("alumnoId")   Long alumnoId,
    @Param("congresoId") Long congresoId,
    @Param("excedente")  Boolean excedente,
    @Param("pagado")     Boolean pagado,
    @Param("cancelado")  Boolean cancelado,
    @Param("usado")      Boolean usado,
    @Param("cumplioRequerimientosDeAsistencia")
    Boolean cumplioRequerimientosDeAsistencia,
    @Param("acreditado") Boolean acreditado,
    Pageable pageable);
}
