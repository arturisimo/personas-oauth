package org.apz.curso.config;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationProvider.class);
	
	
	@Value("${server.oauth}")
	private String urlServidorOauth;
	
	@Value("${server.oauth.user}")
	private String userOauth;
	
	@Value("${server.oauth.pwd}")
	private String pwdOauth;
	
    @Value("${application.jwtSigningKey}")
    private String jwtSigningKey;
	
	
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	
    	String username = authentication.getName();
    	
    	try {
    		
    		String password = authentication.getCredentials().toString();
            
            HttpHeaders headers = getHeaderServer();
    		
    		String token = getToken(headers, username, password);
    		
    		Claims body = Jwts.parser()
                    .setSigningKey(jwtSigningKey.getBytes(Charset.forName("UTF-8") ))
                    .parseClaimsJws(token)
                    .getBody();
    		
    		List<String> roles = (List<String>)body.get("authorities");
    		
    		User usuario = new User(username, token, roles);
    		
    		return new UsernamePasswordAuthenticationToken(usuario, usuario.getToken(), usuario.getAuthorities());
    		
		} catch (Exception e) {
			LOG.info(e.getMessage());
			throw new AuthorizationException(username, e);
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
	private String getToken(HttpHeaders headers, String username, String password) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		HttpEntity<String> request = new HttpEntity<String>(headers);		
		RestTemplate restTemplate = new RestTemplate();
		
		//Los parametros podian ir en el body en vez de en la url
		String accessTokenUrl = urlServidorOauth + "/token?grant_type=password"
									+ "&username=" + username
									+ "&password=" + password;
		
		LOG.info("peticion POST: {}", accessTokenUrl);
		
		//petición POST al servidor de autenticacion
		ResponseEntity<String> response = restTemplate.exchange(accessTokenUrl, HttpMethod.POST, request, String.class);
		
		JsonNode node = mapper.readTree(response.getBody());
		return node.path("access_token").asText();
		
	}
	
	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
}