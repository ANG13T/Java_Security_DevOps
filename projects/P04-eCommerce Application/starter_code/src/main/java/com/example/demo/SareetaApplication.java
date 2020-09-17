package com.example.demo;

import com.example.demo.logging.CsvLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableJpaRepositories("com.example.demo.model.persistence.repositories")
@EntityScan("com.example.demo.model.persistence")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SareetaApplication {

	@Bean
	public CsvLogger getLogger() {
		System.out.println("initialized logger");
		return new CsvLogger();
	}

	@Bean
	public BCryptPasswordEncoder bcryptPasswordEncoder(){
		System.out.println("initialized bcryptPasswordEncoder");
		return new BCryptPasswordEncoder();
	}
	public static void main(String[] args) {
		SpringApplication.run(SareetaApplication.class, args);
	}



}
