package org.apz.curso.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;




@Entity
@Table(name="usuarios")
public class Usuario implements Serializable {
	
	private static final long serialVersionUID = -5890583493090274031L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "enabled", nullable = false)
	private boolean enabled;
	
	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "usuario", nullable = false, unique = true)
	private String usuario;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="id_usuario", nullable = false, insertable = true, updatable = false)
	private List<Rol> roles;
	
	public Usuario() {
		super();
	}
	
	public Usuario(int id, boolean enabled, String password, String usuario) {
		super();
		this.id = id;
		this.enabled = enabled;
		this.password = password;
		this.usuario = usuario;
	}

	public Usuario(String usuario) {
		this.usuario = usuario;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<Rol> getRoles() {
		return this.roles;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}
	

}