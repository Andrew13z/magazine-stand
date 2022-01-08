package com.example.magazinestand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MagazineStandApplication {

	private static final Logger logger = LoggerFactory.getLogger(MagazineStandApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MagazineStandApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> logger.info("Hello, World!");
	}

}
