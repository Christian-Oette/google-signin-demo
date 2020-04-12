package com.christianoette.signin.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@PropertySource(value = "google-client.properties")
@EnableWebSecurity
public class GoogleSignInDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoogleSignInDemoApplication.class, args);
	}

}
