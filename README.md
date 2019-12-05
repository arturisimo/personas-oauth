### JWT + Oauth2

Se delega el proceso de autenticacion en un tercero, un **servidor de autenticación** (facebbook, google) utiliza un token **JWT JSON Web Token**. 

JWT Esta compuesto de

* Cabecera: metadata de encriptacion (metodo de encriptacio)
* Payload: JSON encriptado con info del usuario y el authority (fecha...)
* Firma: Proveedor de confianza

El servidor de autenticación provee el token al cliente. El cliente hace la llamada al **servidor de recurso** (microservicio) con ese token en vez del usuario/password. El servidor comprueba los datos a partir de la firma y el método de encriptación (usualmente de clave simétrica)


**Servidor de autenticacion**

	@EnableAuthorizationServer
	
	http://localhost:6000/oauth
		
**Servidor de recursos**
	
	@EnableResourceServer	

	http://localhost:5000/lista
	
**Cliente**

	http://localhost:8000/test -> 	GET:http://localhost:5000/lista
  	http://localhost:8000/test-delete  ->  DELETE:http://localhost:5000/lista/uno@gmail.com

