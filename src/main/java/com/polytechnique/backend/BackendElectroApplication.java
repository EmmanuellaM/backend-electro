package com.polytechnique.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendElectroApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendElectroApplication.class, args);
	}

}