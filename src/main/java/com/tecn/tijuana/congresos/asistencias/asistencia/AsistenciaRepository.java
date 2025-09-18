package com.tecn.tijuana.congresos.asistencias.asistencia;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AsistenciaRepository
  extends JpaRepository<Asistencia, Long> {

  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.boletoFolio = ?1")

  Page<Asistencia> qBoletoFolio (
    String boletoFolio, Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.boletoFolioLargo = ?1")

  Page<Asistencia> qBoletoFolioLargo (
    String boletoFolio, Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.conferenciaId = ?1")

  Page<Asistencia> qConferenciaId (
    Long conferenciaId, Pageable pageable);



  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.congresoId = ?1" +
    " AND r.alumnoId = ?2")

  Page<Asistencia> qCongresoIdAlumnoId (
    Long congresoId, Long alumnoId, Pageable pageable);

  @Query("SELECT r FROM Asistencia r WHERE" +
    " r.congresoId = ?1" +
    " AND r.alumnoNoControl = ?2")

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
