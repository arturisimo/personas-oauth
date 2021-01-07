package org.apz.curso.config;

import org.springframework.security.core.AuthenticationException;

public class AuthorizationException extends AuthenticationException {

	private static final long serialVersionUID = 1L;
	
	private final String userName;
	
	public AuthorizationException(String userName, Throwable t) {
		super("Authorization Exception", t);
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

}
