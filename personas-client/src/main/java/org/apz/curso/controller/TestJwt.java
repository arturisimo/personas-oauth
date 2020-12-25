package org.apz.curso.controller;

import java.util.Arrays;
import java.util.Base64;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	//usuario que se autentica para hacer la peticion
	private String user = "user1";
	private String pwd = "user1";
	//eliminar por email
	private String email = "uno@gmail.com";
	
	
	/**
	 * Para listar personas tiene que ser un usuario autenticado
	 * @return
	 */
	@GetMapping(value="test", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Persona[]> get() {
		
		RestTemplate restTemplate = new RestTemplate();
		
		try {
			//Peticion del token al servidor de autenticacion con las credenciales en la cabecera
			HttpHeaders headers = getHeaderServer();
			
			String token = getToken(headers);
			
			// Uso del access token para la autenticacion
			HttpHeaders headerToken = getHeaderToken(token);
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
	@GetMapping(value="test-delete", produces=MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> delete() {
		
		RestTemplate restTemplate = new RestTemplate();
		try {
			
			//Peticion del token al servidor de autenticacion con las credenciales en la cabecera
			HttpHeaders headers = getHeaderServer();
			
			String token = getToken(headers);
			
			// Uso del access token para la autenticacion
			HttpHeaders headerToken = getHeaderToken(token);
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
	 * @return
	 */
	private HttpHeaders getHeaderServer() {
		String credentials = userOauth +":" + pwdOauth;
		String encodedCredentials = new String(Base64.getEncoder().encodeToString(credentials.getBytes()));
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
		headers.add("Authorization", "Basic " + encodedCredentials);
		LOG.info("CAbecera con credenciales para el login en server oauth: [Authorization: Basic {} ] ", encodedCredentials);
		return headers;
	}
	
	/**
	 * Solicitamos el token al servidor oauth
	 * @param response
	 * @return
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 */
	private String getToken(HttpHeaders headers) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		HttpEntity<String> request = new HttpEntity<String>(headers);		
		RestTemplate restTemplate = new RestTemplate();
		
		//Los parametros podian ir en el body en vez de en la url
		String accessTokenUrl = urlServidorOauth + "/token?grant_type=password"
									+ "&username=" + user
									+ "&password=" + pwd;
		
		LOG.info("peticion POST: {}", accessTokenUrl);
		
		//petición POST al servidor de autenticacion
		ResponseEntity<String> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, String.class);
		
		JsonNode node = mapper.readTree(response.getBody());
		return node.path("access_token").asText();
		
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
	
}
