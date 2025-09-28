package com.example.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.example"})
@ComponentScan(basePackages = {"com.example"})
@EnableJpaRepositories(basePackages = {"com.example"})
@SpringBootApplication
public class AuthTestApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthTestApplication.class, args);
	}

}
