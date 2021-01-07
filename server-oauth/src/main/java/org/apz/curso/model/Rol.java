package org.apz.curso.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="roles")
@NamedQuery(name="Rol.findByUsuario", query="select r From Rol r Where r.usuario.id=?1")
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String authority;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="id_usuario", nullable = false, insertable = false, updatable = false)
	private Usuario usuario;

	public Rol() {
	}
	
	public Rol(int id, String authority, Usuario usuario) {
		super();
		this.id = id;
		this.authority = authority;
		this.usuario = usuario;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}