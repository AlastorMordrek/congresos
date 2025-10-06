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
}
