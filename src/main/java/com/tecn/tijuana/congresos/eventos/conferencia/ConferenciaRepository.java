package com.tecn.tijuana.congresos.eventos.conferencia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConferenciaRepository
  extends JpaRepository<Conferencia, Long> {

  @Query("SELECT r FROM Conferencia r WHERE r.nombre = ?1")

  Optional<Conferencia> qNombre (
    String nombre);



  @Query("select r from Conferencia r where" +
    " lower(r.nombre)                 like %:txt%"+
    " OR lower(r.resumen)             like %:txt%"+
    " OR lower(r.descripcion)         like %:txt%"+
    " OR lower(r.sala)                like %:txt%"+
    " OR lower(r.staffRequerimientos) like %:txt%" +
    " ORDER BY r.id DESC")

  Page<Conferencia> buscar (
    @Param("txt") String txt, Pageable pageable);



  @Query("select r from Conferencia r where" +
    " r.congresoId = ?1" +
    " ORDER BY r.id DESC")

  Page<Conferencia> qCongresoId (
    Long congresoId, Pageable pageable);



  @Query("select r from Conferencia r where" +
    " r.congresoId = ?1" +
    " and r.publicada = TRUE" +
    " ORDER BY r.id DESC")

  Page<Conferencia> qCongresoIdPublicadas (
    Long congresoId, Pageable pageable);
}
