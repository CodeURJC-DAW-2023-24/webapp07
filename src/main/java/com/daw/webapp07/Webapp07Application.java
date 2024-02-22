package com.daw.webapp07;

import com.daw.webapp07.service.DatabaseInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@SpringBootApplication
public class Webapp07Application {

	public static void main(String[] args) {
		SpringApplication.run(Webapp07Application.class, args);
	}

}
