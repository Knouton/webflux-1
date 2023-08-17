package com.demo.webflux.security.model;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerificationResult {
	private Claims claims;
	private String token;
}
