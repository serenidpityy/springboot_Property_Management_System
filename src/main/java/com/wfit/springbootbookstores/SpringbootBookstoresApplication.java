package com.wfit.springbootbookstores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.wfit.springbootbookstores.repository")
public class SpringbootBookstoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBookstoresApplication.class, args);
	}
}
