package com.cursor.pruebacursor;

import com.cursor.pruebacursor.config.OpenAIConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(OpenAIConfig.class)
public class PruebacursorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PruebacursorApplication.class, args);
	}

}
