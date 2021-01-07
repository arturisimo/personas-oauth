package org.apz.curso.repository;

import org.apz.curso.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer>{

	Usuario findByUsuario(String nombreUsuario);
	
	
}