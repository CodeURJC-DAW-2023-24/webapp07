package com.daw.webapp07;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Webapp07Application {

	public static void main(String[] args) {
		SpringApplication.run(Webapp07Application.class, args);
	}

	// Customize Swagger
	/*@Bean
	public OpenAPI customOpenAPI(){
		return new OpenAPI()
				.info(new Info()
						.title("SeedVentures Spring Boot 3 API")
						.version("xxx")
						.description("Api documentation for SeedVentures")
				);
	}*/
}
