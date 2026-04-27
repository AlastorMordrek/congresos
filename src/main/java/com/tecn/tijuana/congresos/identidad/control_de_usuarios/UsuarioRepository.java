package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository
  extends JpaRepository<Usuario, Long> {

  @Query("select r from Usuario r ORDER BY r.id DESC")
  Page<Usuario> q (Pageable pageable);



  @Query("SELECT r FROM Usuario r WHERE r.email = ?1")
  Optional<Usuario> qEmail (String email);



  @Query("SELECT r FROM Usuario r WHERE r.emailInstitucional = ?1")
  Optional<Usuario> qEmailInstitucional (String email);



  @Query("SELECT r FROM Usuario r WHERE r.noControl = ?1")
  Optional<Usuario> qNoControl (String noControl);



  @Query("select r from Usuario r where" +
    " lower(r.email)                  like %:txt%"+
    " OR lower(r.nombre)              like %:txt%"+
    " OR lower(r.apellidoPaterno)     like %:txt%"+
    " OR lower(r.apellidoMaterno)     like %:txt%"+
    " OR lower(r.telPref)             like %:txt%"+
    " OR lower(r.telSuf)              like %:txt%"+
    " OR lower(r.noControl)           like %:txt%"+
    " OR lower(r.codigoCarrera)       like %:txt%"+
    " OR lower(r.grupo)               like %:txt%"+
    " OR lower(r.curp)                like %:txt%"+
    " OR lower(r.emailInstitucional)  like %:txt%"+
    " OR lower(r.staffResponsabilidades) like %:txt%" +
    " ORDER BY r.id DESC")
  Page<Usuario> buscar (@Param("txt") String txt, Pageable pageable);



  // Variante con filtros opcionales.
  // El patron (:param IS NULL OR r.campo = :param) hace que si el parametro
  // es null, la condicion siempre sea verdadera y no filtre por ese campo.
  // Se usa esta estrategia porque la cantidad de combinaciones posibles
  // hace impractico crear una consulta para cada combinacion.
  @Query("SELECT r FROM Usuario r WHERE" +
    " (:txt IS NULL OR (" +
    "   lower(r.email)                     like %:txt%" +
    "   OR lower(r.nombre)                 like %:txt%" +
    "   OR lower(r.apellidoPaterno)        like %:txt%" +
    "   OR lower(r.apellidoMaterno)        like %:txt%" +
    "   OR lower(r.telPref)                like %:txt%" +
    "   OR lower(r.telSuf)                 like %:txt%" +
    "   OR lower(r.noControl)              like %:txt%" +
    "   OR lower(r.codigoCarrera)          like %:txt%" +
    "   OR lower(r.grupo)                  like %:txt%" +
    "   OR lower(r.curp)                   like %:txt%" +
    "   OR lower(r.emailInstitucional)     like %:txt%" +
    "   OR lower(r.staffResponsabilidades) like %:txt%" +
    " ))" +
    " AND (:rol              IS NULL OR r.rol              = :rol)" +
    " AND (:bloqueado        IS NULL OR r.bloqueado        = :bloqueado)" +
    " AND (:externo          IS NULL OR r.externo          = :externo)" +
    " AND (:staffAutorizado  IS NULL OR r.staffAutorizado  = :staffAutorizado)" +
    " AND (:staffCustodio    IS NULL OR r.staffCustodio    = :staffCustodio)" +
    " AND (:staffAlumnos     IS NULL OR r.staffAlumnos     = :staffAlumnos)" +
    " AND (:staffInscripciones IS NULL OR r.staffInscripciones = :staffInscripciones)" +
    " ORDER BY r.id DESC")
  Page<Usuario> qFiltrado (
    @Param("txt")               String txt,
    @Param("rol")               Rol rol,
    @Param("bloqueado")         Boolean bloqueado,
    @Param("externo")           Boolean externo,
    @Param("staffAutorizado")   Boolean staffAutorizado,
    @Param("staffCustodio")     Boolean staffCustodio,
    @Param("staffAlumnos")      Boolean staffAlumnos,
    @Param("staffInscripciones") Boolean staffInscripciones,
    Pageable pageable);
}
