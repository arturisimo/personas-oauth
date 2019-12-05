package org.apz.curso.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages="org.apz.curso.controller")
@SpringBootApplication
public class PersonaClientApp {

	public static void main(String[] args) {
		SpringApplication.run(PersonaClientApp.class, args);
	}

}
