package com.demo.webflux.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.web.reactive.server.WebTestClient;

public class TestConfig {
	@Bean
	public WebTestClient webTestClient(ApplicationContext applicationContext) {
		return WebTestClient.bindToApplicationContext(applicationContext).build();
	}

	@Bean
	public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
		http.authorizeExchange().anyExchange().permitAll();
		return http.build();
	}
}
