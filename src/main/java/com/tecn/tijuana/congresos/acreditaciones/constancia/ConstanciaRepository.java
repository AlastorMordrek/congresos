package com.tecn.tijuana.congresos.acreditaciones.constancia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConstanciaRepository
  extends JpaRepository<Constancia, Long> {

  @Query("SELECT r FROM Constancia r WHERE r.folio = ?1")
  Page<Constancia> qFolio (String folio, Pageable pageable);


  @Query("SELECT r FROM Constancia r WHERE r.folioLargo = ?1")
  Page<Constancia> qFolioLargo (String folio, Pageable pageable);


  @Query("SELECT r FROM Constancia r WHERE r.congresoId = ?1")
  Page<Constancia> qCongresoId (Long congresoId, Pageable pageable);


  @Query("SELECT r FROM Constancia r WHERE r.alumnoId = ?2")
  Page<Constancia> qAlumnoId (Long alumnoId, Pageable pageable);


  @Query("SELECT r FROM Constancia r WHERE r.alumnoNoControl = ?2")
  Page<Constancia> qAlumnoNoControl (Long alumnoNoControl, Pageable pageable);


  @Query("SELECT r FROM Constancia r WHERE" +
    " r.congresoId = ?1 AND r.alumnoId = ?2")
  Page<Constancia> qCongresoIdAlumnoId (
    Long congresoId, Long alumnoId, Pageable pageable);


  @Query("SELECT r FROM Constancia r WHERE" +
    " r.congresoId = ?1 AND r.alumnoNoControl = ?2")
  Page<Constancia> qCongresoIdAlumnoNoControl (
    Long congresoId, String alumnoNoControl, Pageable pageable);
}
