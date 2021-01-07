package org.apz.curso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apz.curso.config.User;
import org.apz.curso.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestJwt {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestJwt.class);
	
	@Value("${server.oauth}")
	private String urlServidorOauth;
	
	@Value("${server.personas}")
	private String urlServidorRecursos;
	
	@Value("${server.oauth.user}")
	private String userOauth;
	
	@Value("${server.oauth.pwd}")
	private String pwdOauth;
	
	//eliminar por email
	private String email = "uno@gmail.com";
	
	
	/**
	 * Para listar personas tiene que ser un usuario autenticado
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Persona[]> get(Authentication auth) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			User user = (User)auth.getPrincipal();
			
			// Uso del access token para la autenticacion
			HttpHeaders headerToken = getHeaderToken(user.getToken());
			HttpEntity<String> entity = new HttpEntity<>(headerToken);
	
			LOG.info("GET recurso securizado: {}", urlServidorRecursos);
			
			return restTemplate.exchange(urlServidorRecursos, HttpMethod.GET, entity, Persona[].class);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return new ResponseEntity<>(new Persona[] {}, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Solo puede eliminar si tiene ROL_ADMIN. si no devuelve un error 403 Access is denied
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value="/", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> delete(Authentication auth) {
		
		RestTemplate restTemplate = new RestTemplate();
		try {
			
			User user = (User)auth.getPrincipal();
			
			// Uso del access token para la autenticacion
			HttpHeaders headerToken = getHeaderToken(user.getPassword());
			HttpEntity<String> entity = new HttpEntity<>(headerToken);
			
			String deleteUrl = urlServidorRecursos + "/"+ email;
			
			LOG.info("DELETE recurso securizado: {}", deleteUrl);
			
			return restTemplate.exchange(deleteUrl, HttpMethod.DELETE, entity, String.class);
		
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	/**
	 * Header con el access token para la peticion de personas
	 * @param token
	 * @return
	 */
	private HttpHeaders getHeaderToken(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		LOG.info("Cabecera con JWT: [Authorization: Bearer {}]", token);
		return headers;
	}
	
	@GetMapping(value="/error", produces=MediaType.APPLICATION_JSON_VALUE)
	public String error(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
		return "error";
	}
	
	@ExceptionHandler(Exception.class)
	public String exception(Model model, Exception e) {
		model.addAttribute("errorMessage", e.getMessage());
		return "error";
	}
	
	
}
