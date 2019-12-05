package org.apz.curso.controller;

import java.io.IOException;
import java.util.Arrays;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apz.curso.model.Persona;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class TestJwt {
	
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
		
		//Peticion del token al servidor de autenticacion con las credenciales en la cabecera
		HttpHeaders headers = getHeaderServer();
		
		String token = getToken(headers);
		System.out.println("--"+token);
		
		// Uso del access token para la autenticacion
		HttpHeaders headerToken = getHeaderToken(token);
		HttpEntity<String> entity = new HttpEntity<>(headerToken);

		return restTemplate.exchange(urlServidorRecursos, HttpMethod.GET, entity, Persona[].class);
		
	}
	
	/**
	 * Solo puede eliminar si tiene ROL_ADMIN. si no devuelve un error 403 Access is denied
	 * @return
	 */
	@GetMapping(value="test-delete", produces=MediaType.TEXT_PLAIN_VALUE)
	public String delete() {
		
		RestTemplate restTemplate = new RestTemplate();
		try {
			
			//Peticion del token al servidor de autenticacion con las credenciales en la cabecera
			HttpHeaders headers = getHeaderServer();
			
			String token = getToken(headers);
			System.out.println("--"+token);
			
			// Uso del access token para la autenticacion
			HttpHeaders headerToken = getHeaderToken(token);
			HttpEntity<String> entity = new HttpEntity<>(headerToken);
	
			ResponseEntity<String> data = restTemplate.exchange(urlServidorRecursos + "/"+ email, HttpMethod.DELETE, entity, String.class);
			System.out.println(data);
			return data.getBody();
		
		} catch (Exception e) {
			return e.getMessage();
		}
		
	}
	
	
	/**
	 * @return
	 */
	private HttpHeaders getHeaderServer() {
		String credentials = userOauth +":" + pwdOauth;
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
		headers.add("Authorization", "Basic " + encodedCredentials);
		return headers;
	}
	
	/**
	 * Solicitamos el token al servidor oauth
	 * @param response
	 * @return
	 */
	private String getToken(HttpHeaders headers) {
		ObjectMapper mapper = new ObjectMapper();
		HttpEntity<String> request = new HttpEntity<String>(headers);		
		ResponseEntity<String> response = null;
		RestTemplate restTemplate = new RestTemplate();
		JsonNode node;
		
		//Los parametros podian ir en el body en vez de en la url
		String accessTokenUrl = urlServidorOauth + "/token?grant_type=password"
									+ "&username=" + user
									+ "&password=" + pwd;
		
		try {
		
			//petición POST al servidor de autenticacion
			response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, String.class);
		
			node = mapper.readTree(response.getBody());
			return node.path("access_token").asText();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Header con el access token para la peticion de personas
	 * @param token
	 * @return
	 */
	private HttpHeaders getHeaderToken(String token) {
		HttpHeaders headers1 = new HttpHeaders();
		headers1.add("Authorization", "Bearer " + token);
		return headers1;
	}
	
}
