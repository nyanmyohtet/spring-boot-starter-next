package com.nyanmyohtet.springbootstarter;

import com.nyanmyohtet.springbootstarter.service.ApiService;
import com.nyanmyohtet.springbootstarter.service.AuthService;
import com.nyanmyohtet.springbootstarter.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootStarterApplication implements CommandLineRunner {
	private final Logger LOGGER = LoggerFactory.getLogger(SpringBootStarterApplication.class);

	@Autowired
	ApiService apiService;

	@Autowired
	AuthService authService;

	@Autowired
	JwtUtil jwtUtil;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStarterApplication.class, args);
	}

	@Override
	public void run(String... args) {
		String jwt = jwtUtil.generateJwtAssertion();
		LOGGER.info("jwt: {}", jwt);
		String tokenResponse = authService.fetchAccessToken(jwt).block();
		LOGGER.info("tokenResponse: {}", tokenResponse);
	}

}
