package com.demo.webflux.security.token;

import com.demo.webflux.entity.UserEntity;
import com.demo.webflux.exception.AuthException;
import com.demo.webflux.security.model.TokenDetails;
import com.demo.webflux.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityService {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.expiration}")
	private Integer expirationInSeconds;
	@Value("${jwt.issuer}")
	private String issuer;

	public Mono<TokenDetails> authenticate(String username, String password) {
		return userService.getUserByUserName(username)
				.flatMap(user -> {
					if(!user.isEnabled()) {
						return Mono.error(new AuthException("Account disable: "+ username, "USER_ACCOUNT_DISABLE"));
					}
					if(!passwordEncoder.matches(password, user.getPassword())) {
						return Mono.error(new AuthException("Wrong password: " + username, "WRONG_PASSWORD"));
					}

					return Mono.just(generateToken(user).toBuilder()
							                 .userId(user.getId())
							                 .build());
				})
				.switchIfEmpty(Mono.error(new AuthException("Invalid username: " + username, "INVALID_USERNAME")));
	}

	private TokenDetails generateToken(UserEntity user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", user.getRole());
		claims.put("username", user.getUsername());

		return generateToken(claims, user.getId().toString());
	}

	private TokenDetails generateToken(Map<String, Object> claims, String subject) {
		Long expirationTimeMillis = expirationInSeconds * 1000L;
		Date expirationDate = new Date(new Date().getTime() + expirationTimeMillis);

		return generateToken(expirationDate, claims, subject);
	}
	private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject) {
		Date createDate = new Date();
		String token = Jwts.builder()
				.setClaims(claims)
				.setIssuer(issuer)
				.setSubject(subject)
				.setIssuedAt(createDate)
				.setId(UUID.randomUUID().toString())
				.setExpiration(expirationDate)
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();

		return TokenDetails.builder()
				.token(token)
				.issuedAt(createDate)
				.expiresAt(expirationDate)
				.build();
	}
	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(this.secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
