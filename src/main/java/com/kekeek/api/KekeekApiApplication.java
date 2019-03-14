package com.kekeek.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KekeekApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KekeekApiApplication.class, args);
	}

}
