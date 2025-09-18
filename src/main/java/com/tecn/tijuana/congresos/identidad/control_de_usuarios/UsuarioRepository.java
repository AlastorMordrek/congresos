package com.tecn.tijuana.congresos.identidad.control_de_usuarios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository
  extends JpaRepository<Usuario, Long> {

  @Query("SELECT s FROM Usuario s WHERE s.email = ?1")
  Optional<Usuario> qEmail (String email);



  @Query("SELECT s FROM Usuario s WHERE s.noControl = ?1")
  Optional<Usuario> qNoControl (String noControl);



  @Query("select p from Usuario p where" +
    " lower(p.email)              like %:txt%"+
    " OR lower(p.nombre)          like %:txt%"+
    " OR lower(p.apellidoPaterno) like %:txt%"+
    " OR lower(p.apellidoMaterno) like %:txt%")
  Page<Usuario> buscar (@Param("txt") String txt, Pageable pageable);
}
