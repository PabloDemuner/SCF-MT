package com.scfapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.scfapi.config.property.ScfApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(ScfApiProperty.class)
public class SistemaControleFinanceiroApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaControleFinanceiroApiApplication.class, args);
	}

}
