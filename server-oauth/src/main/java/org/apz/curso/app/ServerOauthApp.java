package org.apz.curso.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"org.apz.curso.service","org.apz.curso.config"})
@EntityScan(basePackages = {"org.apz.curso.model"})
@EnableJpaRepositories(basePackages = {"org.apz.curso.repository"})
@SpringBootApplication
public class ServerOauthApp {

	public static void main(String[] args) {
		SpringApplication.run(ServerOauthApp.class, args);
	}

}
