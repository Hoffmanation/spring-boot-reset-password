package com.resetPasswordDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({ "com.resetPasswordDemo" })
@EntityScan({ "com.resetPasswordDemo"})
@EnableJpaRepositories({ "com.resetPasswordDemo"})
public class resetPasswordDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(resetPasswordDemoApplication .class, args);
	}
}
