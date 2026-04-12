package com.tecn.tijuana.congresos.asistencias.asistencia;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AsistenciaRepository
  extends JpaRepository<Asistencia, Long> {

  @Query("select r from Asistencia r ORDER BY r.id DESC")
  Page<Asistencia> q (Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.boletoFolio = ?1" +
    " ORDER BY r.id DESC")
  Page<Asistencia> qBoletoFolio (
    String boletoFolio, Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.boletoFolioLargo = ?1" +
    " ORDER BY r.id DESC")
  Page<Asistencia> qBoletoFolioLargo (
    String boletoFolio, Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = ?1" +
    " ORDER BY r.id DESC")
  Page<Asistencia> qConferenciaId (
    Long conferenciaId, Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = ?1" +
    " AND r.tiempoAsistido >= ?2" +
    " ORDER BY r.id DESC")
  Page<Asistencia> qConferenciaIdMinTiempoAsistido (
    Long conferenciaId, Long minTiempoAsistido, Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = ?1" +
    " AND r.fechaUltimaEntrada IS NOT NULL" +
    " ORDER BY r.id DESC")
  Page<Asistencia> qConferenciaIdPresente (
    Long conferenciaId, Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = ?1" +
    " AND r.fechaUltimaEntrada IS NULL" +
    " ORDER BY r.id DESC")
  Page<Asistencia> qConferenciaIdAusente (
    Long conferenciaId, Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = ?1" +
    " AND r.tiempoAsistido >= ?2" +
    " AND r.fechaUltimaEntrada IS NOT NULL" +
    " ORDER BY r.id DESC")
  Page<Asistencia> qConferenciaIdMinTiempoAsistidoPresente (
    Long conferenciaId, Long minTiempoAsistido, Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = ?1" +
    " AND r.tiempoAsistido >= ?2" +
    " AND r.fechaUltimaEntrada IS NULL" +
    " ORDER BY r.id DESC")
  Page<Asistencia> qConferenciaIdMinTiempoAsistidoAusente (
    Long conferenciaId, Long minTiempoAsistido, Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = :conferenciaId" +
    " AND (lower(r.alumnoNombre)   like %:txt%" +
    " OR lower(r.alumnoNoControl)  like %:txt%" +
    " OR lower(r.creadorNombre)    like %:txt%" +
    " OR lower(r.boletoFolio)      like %:txt%" +
    " OR lower(r.boletoFolioLargo) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Asistencia> buscarConferenciaId (
    @Param("conferenciaId") Long conferenciaId,
    @Param("txt") String txt,
    Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = :conferenciaId" +
    " AND r.fechaUltimaEntrada IS NOT NULL" +
    " AND (lower(r.alumnoNombre)   like %:txt%" +
    " OR lower(r.alumnoNoControl)  like %:txt%" +
    " OR lower(r.creadorNombre)    like %:txt%" +
    " OR lower(r.boletoFolio)      like %:txt%" +
    " OR lower(r.boletoFolioLargo) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Asistencia> buscarConferenciaIdPresente (
    @Param("conferenciaId") Long conferenciaId,
    @Param("txt") String txt,
    Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = :conferenciaId" +
    " AND r.fechaUltimaEntrada IS NULL" +
    " AND (lower(r.alumnoNombre)   like %:txt%" +
    " OR lower(r.alumnoNoControl)  like %:txt%" +
    " OR lower(r.creadorNombre)    like %:txt%" +
    " OR lower(r.boletoFolio)      like %:txt%" +
    " OR lower(r.boletoFolioLargo) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Asistencia> buscarConferenciaIdAusente (
    @Param("conferenciaId") Long conferenciaId,
    @Param("txt") String txt,
    Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = :conferenciaId" +
    " AND r.tiempoAsistido >= :minTiempoAsistido" +
    " AND (lower(r.alumnoNombre)   like %:txt%" +
    " OR lower(r.alumnoNoControl)  like %:txt%" +
    " OR lower(r.creadorNombre)    like %:txt%" +
    " OR lower(r.boletoFolio)      like %:txt%" +
    " OR lower(r.boletoFolioLargo) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Asistencia> buscarConferenciaIdMinTiempoAsistido (
    @Param("conferenciaId") Long conferenciaId,
    @Param("minTiempoAsistido") Long minTiempoAsistido,
    @Param("txt") String txt,
    Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = :conferenciaId" +
    " AND r.tiempoAsistido >= :minTiempoAsistido" +
    " AND r.fechaUltimaEntrada IS NOT NULL" +
    " AND (lower(r.alumnoNombre)   like %:txt%" +
    " OR lower(r.alumnoNoControl)  like %:txt%" +
    " OR lower(r.creadorNombre)    like %:txt%" +
    " OR lower(r.boletoFolio)      like %:txt%" +
    " OR lower(r.boletoFolioLargo) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Asistencia> buscarConferenciaIdMinTiempoAsistidoPresente (
    @Param("conferenciaId") Long conferenciaId,
    @Param("minTiempoAsistido") Long minTiempoAsistido,
    @Param("txt") String txt,
    Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = :conferenciaId" +
    " AND r.tiempoAsistido >= :minTiempoAsistido" +
    " AND r.fechaUltimaEntrada IS NULL" +
    " AND (lower(r.alumnoNombre)   like %:txt%" +
    " OR lower(r.alumnoNoControl)  like %:txt%" +
    " OR lower(r.creadorNombre)    like %:txt%" +
    " OR lower(r.boletoFolio)      like %:txt%" +
    " OR lower(r.boletoFolioLargo) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Asistencia> buscarConferenciaIdMinTiempoAsistidoAusente (
    @Param("conferenciaId") Long conferenciaId,
    @Param("minTiempoAsistido") Long minTiempoAsistido,
    @Param("txt") String txt,
    Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.congresoId = ?1" +
    " AND r.alumnoId = ?2" +
    " ORDER BY r.id DESC")
  Page<Asistencia> qCongresoIdAlumnoId (
    Long congresoId, Long alumnoId, Pageable pageable);

  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.congresoId = ?1" +
    " AND r.alumnoNoControl = ?2" +
    " ORDER BY r.id DESC")
  Page<Asistencia> qCongresoIdAlumnoNoControl (
    Long congresoId, String alumnoNoControl, Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = ?1" +
    " AND r.alumnoId = ?2")
  Optional<Asistencia> qConferenciaIdAlumnoId (
    Long conferenciaId, Long alumnoId);

  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = ?1" +
    " AND r.alumnoNoControl = ?2")
  Optional<Asistencia> qConferenciaIdAlumnoNoControl (
    Long conferenciaId, String alumnoNoControl);
}
