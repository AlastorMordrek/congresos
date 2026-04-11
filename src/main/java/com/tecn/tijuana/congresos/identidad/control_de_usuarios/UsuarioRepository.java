package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository
  extends JpaRepository<Usuario, Long> {

  @Query("SELECT r FROM Usuario r WHERE r.email = ?1")
  Optional<Usuario> qEmail (String email);

  @Query("SELECT r FROM Usuario r WHERE r.emailInstitucional = ?1")
  Optional<Usuario> qEmailInstitucional (String email);



  @Query("SELECT r FROM Usuario r WHERE r.noControl = ?1")
  Optional<Usuario> qNoControl (String noControl);



  @Query("select r from Usuario r where" +
    " lower(r.email)              like %:txt%"+
    " OR lower(r.nombre)          like %:txt%"+
    " OR lower(r.apellidoPaterno) like %:txt%"+
    " OR lower(r.apellidoMaterno) like %:txt%" +
    " ORDER BY r.id DESC")
  Page<Usuario> buscar (@Param("txt") String txt, Pageable pageable);
}
