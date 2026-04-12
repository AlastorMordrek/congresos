package com.tecn.tijuana.congresos.eventos.congreso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CongresoRepository extends JpaRepository<Congreso, Long> {

  @Query("select r from Congreso r ORDER BY r.id DESC")
  Page<Congreso> q (Pageable pageable);



  @Query("SELECT r FROM Congreso r WHERE r.nombre = ?1")
  Optional<Congreso> encontrarPorNombre (String nombre);



  @Query("select r from Congreso r where" +
    " lower(r.nombre)                 like %:txt%"+
    " OR lower(r.resumen)             like %:txt%"+
    " OR lower(r.descripcion)         like %:txt%"+
    " OR lower(r.direccion)           like %:txt%"+
    " OR lower(r.staffRequerimientos) like %:txt%" +
    " ORDER BY r.id DESC")
  Page<Congreso> buscar (@Param("txt") String txt, Pageable pageable);



  @Query("select r from Congreso r where r.publicado = TRUE" +
    " ORDER BY r.id DESC")
  Page<Congreso> publicados (Pageable pageable);

  @Query("SELECT r FROM Congreso r WHERE r.publicado = TRUE" +
    " AND (:txt IS NULL OR (" +
    "   lower(r.nombre)                 like %:txt%" +
    "   OR lower(r.resumen)             like %:txt%" +
    "   OR lower(r.descripcion)         like %:txt%" +
    "   OR lower(r.direccion)           like %:txt%" +
    "   OR lower(r.staffRequerimientos) like %:txt%" +
    " ))" +
    " AND (CAST(:fechaFinMin AS timestamp) IS NULL" +
    "   OR r.fechaFin >= :fechaFinMin)" +
    " AND (:gratuito IS NULL OR r.gratuito = :gratuito)" +
    " ORDER BY r.id DESC")
  Page<Congreso> qPublicadosFiltrado (
    @Param("txt")         String txt,
    @Param("fechaFinMin") LocalDateTime fechaFinMin,
    @Param("gratuito")    Boolean gratuito,
    Pageable pageable);

  @Query("select r from Congreso r where" +
    " r.publicado = TRUE and("+
    " lower(r.nombre)                 like %:txt%"+
    " OR lower(r.resumen)             like %:txt%"+
    " OR lower(r.descripcion)         like %:txt%"+
    " OR lower(r.direccion)           like %:txt%"+
    " OR lower(r.staffRequerimientos) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Congreso> buscarPublicados (
    @Param("txt") String txt, Pageable pageable);



  @Query("select r from Congreso r where r.organizadorId = ?1" +
    " ORDER BY r.id DESC")
  Page<Congreso> porOrganizador (Long idOrg, Pageable pageable);

  @Query("select r from Congreso r where" +
    " r.organizadorId = ?1 and("+
    " lower(r.nombre)                 like %:txt%"+
    " OR lower(r.resumen)             like %:txt%"+
    " OR lower(r.descripcion)         like %:txt%"+
    " OR lower(r.direccion)           like %:txt%"+
    " OR lower(r.staffRequerimientos) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Congreso> buscarPorOrganizador (
    Long idOrg, @Param("txt") String txt, Pageable pageable);



  @Query("select r from Congreso r where" +
    " r.organizadorId = ?1" +
    " and r.publicado = ?2" +
    " ORDER BY r.id DESC")
  Page<Congreso> porOrganizadorPublicado (
    Long idOrg, boolean publicado, Pageable pageable);

  @Query("select r from Congreso r where" +
    " r.organizadorId = :idOrg" +
    " and r.publicado = :publicado" +
    " and(" +
    " lower(r.nombre)                 like %:txt%" +
    " OR lower(r.resumen)             like %:txt%" +
    " OR lower(r.descripcion)         like %:txt%" +
    " OR lower(r.direccion)           like %:txt%" +
    " OR lower(r.staffRequerimientos) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Congreso> buscarPorOrganizadorPublicado (
    @Param("idOrg") Long idOrg,
    @Param("txt") String txt,
    @Param("publicado") boolean publicado,
    Pageable pageable);



  @Query("select r from Congreso r where" +
    " r.organizadorId = ?1" +
    " and r.cancelado = ?2" +
    " ORDER BY r.id DESC")
  Page<Congreso> porOrganizadorCancelado (
    Long idOrg, boolean cancelado, Pageable pageable);

  @Query("select r from Congreso r where" +
    " r.organizadorId = :idOrg" +
    " and r.cancelado = :cancelado" +
    " and(" +
    " lower(r.nombre)                 like %:txt%" +
    " OR lower(r.resumen)             like %:txt%" +
    " OR lower(r.descripcion)         like %:txt%" +
    " OR lower(r.direccion)           like %:txt%" +
    " OR lower(r.staffRequerimientos) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Congreso> buscarPorOrganizadorCancelado (
    @Param("idOrg") Long idOrg,
    @Param("txt") String txt,
    @Param("cancelado") boolean cancelado,
    Pageable pageable);



  @Query("select r from Congreso r where" +
    " r.organizadorId = ?1" +
    " and r.publicado = ?2" +
    " and r.cancelado = ?3" +
    " ORDER BY r.id DESC")
  Page<Congreso> porOrganizadorPublicadoCancelado (
    Long idOrg, boolean publicado, boolean cancelado, Pageable pageable);

  @Query("select r from Congreso r where" +
    " r.organizadorId = :idOrg" +
    " and r.publicado = :publicado" +
    " and r.cancelado = :cancelado" +
    " and(" +
    " lower(r.nombre)                 like %:txt%" +
    " OR lower(r.resumen)             like %:txt%" +
    " OR lower(r.descripcion)         like %:txt%" +
    " OR lower(r.direccion)           like %:txt%" +
    " OR lower(r.staffRequerimientos) like %:txt%)" +
    " ORDER BY r.id DESC")
  Page<Congreso> buscarPorOrganizadorPublicadoCancelado (
    @Param("idOrg") Long idOrg,
    @Param("txt") String txt,
    @Param("publicado") boolean publicado,
    @Param("cancelado") boolean cancelado,
    Pageable pageable);



  @Query("select r from Congreso r where" +
    " r.publicado = TRUE" +
    " and r.fechaFin >= CURRENT_DATE" +
    " ORDER BY r.id DESC")
  Page<Congreso> publicadosProximos (Pageable pageable);
}
