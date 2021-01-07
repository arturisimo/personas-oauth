package org.apz.curso.repository;

import java.util.List;

import org.apz.curso.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol,Integer>{
	
	List<Rol> findByUsuario(Integer id);
	
	
}