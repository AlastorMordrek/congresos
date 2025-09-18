package com.tecn.tijuana.congresos.eventos.conferencia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConferenciaRepository
  extends JpaRepository<Conferencia, Long> {

  @Query("SELECT c FROM Conferencia c WHERE c.nombre = ?1")

  Optional<Conferencia> qNombre (
    String nombre);



  @Query("select c from Conferencia c where" +
    " lower(c.nombre)                 like %:txt%"+
    " OR lower(c.resumen)             like %:txt%"+
    " OR lower(c.descripcion)         like %:txt%"+
    " OR lower(c.sala)                like %:txt%"+
    " OR lower(c.staffRequerimientos) like %:txt%")

  Page<Conferencia> buscar (
    @Param("txt") String txt, Pageable pageable);



  @Query("select c from Conferencia c where" +
    " c.congresoId = ?1")

  Page<Conferencia> qCongresoId (
    Long congresoId, Pageable pageable);



  @Query("select c from Conferencia c where" +
    " c.congresoId = ?1" +
    " and c.publicada = TRUE")

  Page<Conferencia> qCongresoIdPublicadas (
    Long congresoId, Pageable pageable);
}
