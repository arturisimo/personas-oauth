package org.apz.curso.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("org.apz.curso.config")
@SpringBootApplication
public class ServerOauthApp {

	public static void main(String[] args) {
		SpringApplication.run(ServerOauthApp.class, args);
	}

}
