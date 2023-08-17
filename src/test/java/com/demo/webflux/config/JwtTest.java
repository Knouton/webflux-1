package com.demo.webflux.config;

import com.demo.webflux.entity.UserEntity;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtTest {

	private static final String TOKEN = "b5f59337a612a2a7dc07328f3e7d1a04722967c7f06df20a499a7d3f91ff2a7e";

	public static String generateJwt(UserEntity userEntity) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", userEntity.getRole());
		claims.put("username", userEntity.getUsername());

		Long expirationTimeMillis = 3600 * 1000L;
		Date expirationDate = new Date(new Date().getTime() + expirationTimeMillis);

		return Jwts.builder()
				.setClaims(claims)
				.setIssuer("issuer")
				.setSubject(userEntity.getId().toString())
				.setIssuedAt(new Date())
				.setId(UUID.randomUUID().toString())
				.setExpiration(expirationDate)
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	private static Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(TOKEN);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
