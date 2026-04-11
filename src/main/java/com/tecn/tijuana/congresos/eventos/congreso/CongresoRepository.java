package com.tecn.tijuana.congresos.eventos.congreso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CongresoRepository extends JpaRepository<Congreso, Long> {

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
    " r.publicado = TRUE" +
    " and r.fechaFin >= CURRENT_DATE" +
    " ORDER BY r.id DESC")
  Page<Congreso> publicadosProximos (Pageable pageable);
}
