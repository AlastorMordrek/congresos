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



  @Query("select r from Conferencia r where" +
    " r.congresoId = :congresoId" +
    " and(" +
    " lower(r.nombre)                 like %:txt%" +
    " OR lower(r.resumen)             like %:txt%" +
    " OR lower(r.descripcion)         like %:txt%" +
    " OR lower(r.sala)                like %:txt%" +
    " OR lower(r.staffRequerimientos) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Conferencia> buscarCongresoId (
    @Param("congresoId") Long congresoId,
    @Param("txt") String txt,
    Pageable pageable);



  @Query("select r from Conferencia r where" +
    " r.congresoId = ?1" +
    " and r.publicada = ?2" +
    " ORDER BY r.id DESC")
  Page<Conferencia> qCongresoIdPublicada (
    Long congresoId, boolean publicada, Pageable pageable);

  @Query("select r from Conferencia r where" +
    " r.congresoId = :congresoId" +
    " and r.publicada = :publicada" +
    " and(" +
    " lower(r.nombre)                 like %:txt%" +
    " OR lower(r.resumen)             like %:txt%" +
    " OR lower(r.descripcion)         like %:txt%" +
    " OR lower(r.sala)                like %:txt%" +
    " OR lower(r.staffRequerimientos) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Conferencia> buscarCongresoIdPublicada (
    @Param("congresoId") Long congresoId,
    @Param("txt") String txt,
    @Param("publicada") boolean publicada,
    Pageable pageable);



  @Query("select r from Conferencia r where" +
    " r.congresoId = ?1" +
    " and r.cancelada = ?2" +
    " ORDER BY r.id DESC")
  Page<Conferencia> qCongresoIdCancelada (
    Long congresoId, boolean cancelada, Pageable pageable);

  @Query("select r from Conferencia r where" +
    " r.congresoId = :congresoId" +
    " and r.cancelada = :cancelada" +
    " and(" +
    " lower(r.nombre)                 like %:txt%" +
    " OR lower(r.resumen)             like %:txt%" +
    " OR lower(r.descripcion)         like %:txt%" +
    " OR lower(r.sala)                like %:txt%" +
    " OR lower(r.staffRequerimientos) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Conferencia> buscarCongresoIdCancelada (
    @Param("congresoId") Long congresoId,
    @Param("txt") String txt,
    @Param("cancelada") boolean cancelada,
    Pageable pageable);



  @Query("select r from Conferencia r where" +
    " r.congresoId = ?1" +
    " and r.publicada = ?2" +
    " and r.cancelada = ?3" +
    " ORDER BY r.id DESC")
  Page<Conferencia> qCongresoIdPublicadaCancelada (
    Long congresoId, boolean publicada, boolean cancelada,
    Pageable pageable);

  @Query("select r from Conferencia r where" +
    " r.congresoId = :congresoId" +
    " and r.publicada = :publicada" +
    " and r.cancelada = :cancelada" +
    " and(" +
    " lower(r.nombre)                 like %:txt%" +
    " OR lower(r.resumen)             like %:txt%" +
    " OR lower(r.descripcion)         like %:txt%" +
    " OR lower(r.sala)                like %:txt%" +
    " OR lower(r.staffRequerimientos) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Conferencia> buscarCongresoIdPublicadaCancelada (
    @Param("congresoId") Long congresoId,
    @Param("txt") String txt,
    @Param("publicada") boolean publicada,
    @Param("cancelada") boolean cancelada,
    Pageable pageable);
}
