package com.tecn.tijuana.congresos.eventos.congreso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CongresoRepository extends JpaRepository<Congreso, Long> {

  @Query("SELECT c FROM Congreso c WHERE c.nombre = ?1")
  Optional<Congreso> encontrarPorNombre (String nombre);



  @Query("select c from Congreso c where" +
    " lower(c.nombre)                 like %:txt%"+
    " OR lower(c.resumen)             like %:txt%"+
    " OR lower(c.descripcion)         like %:txt%"+
    " OR lower(c.direccion)           like %:txt%"+
    " OR lower(c.staffRequerimientos) like %:txt%")
  Page<Congreso> buscar (@Param("txt") String txt, Pageable pageable);



  @Query("select c from Congreso c where c.publicado = TRUE")
  Page<Congreso> publicados (Pageable pageable);

  @Query("select c from Congreso c where" +
    " c.publicado = TRUE and("+
    " lower(c.nombre)                 like %:txt%"+
    " OR lower(c.resumen)             like %:txt%"+
    " OR lower(c.descripcion)         like %:txt%"+
    " OR lower(c.direccion)           like %:txt%"+
    " OR lower(c.staffRequerimientos) like %:txt%)")
  Page<Congreso> buscarPublicados (
    @Param("txt") String txt, Pageable pageable);



  @Query("select c from Congreso c where c.organizadorId = ?1")
  Page<Congreso> porOrganizador (Long idOrg, Pageable pageable);

  @Query("select c from Congreso c where" +
    " c.organizadorId = ?1 and("+
    " lower(c.nombre)                 like %:txt%"+
    " OR lower(c.resumen)             like %:txt%"+
    " OR lower(c.descripcion)         like %:txt%"+
    " OR lower(c.direccion)           like %:txt%"+
    " OR lower(c.staffRequerimientos) like %:txt%)")
  Page<Congreso> buscarPorOrganizador (
    Long idOrg, @Param("txt") String txt, Pageable pageable);



  @Query("select c from Congreso c where" +
    " c.publicado = TRUE" +
    " and c.fechaFin >= CURRENT_DATE")
  Page<Congreso> publicadosProximos (Pageable pageable);
}
