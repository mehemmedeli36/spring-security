package com.example.starter;

import java.util.UUID;

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
		System.out.println(UUID.randomUUID().toString());
		SpringApplication.run(AuthTestApplication.class, args);
	}

}
