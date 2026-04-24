package com.exactpromos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ExactPromosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExactPromosApplication.class, args);
	}

}
