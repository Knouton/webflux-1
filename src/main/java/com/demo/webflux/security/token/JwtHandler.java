package com.demo.webflux.security.token;

import com.demo.webflux.exception.AuthException;
import com.demo.webflux.exception.UnauthorizedException;
import com.demo.webflux.security.model.VerificationResult;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class JwtHandler {

	private final String secret;

	public Mono<VerificationResult> check(String accessToken) {

		return Mono.just(verify(accessToken))
				.onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
	}

	private VerificationResult verify(String token) {
		Claims claims = getClaimsFromToken(token);
		final Date expirationDate = claims.getExpiration();

		if (expirationDate.before(new Date())) {
			throw new AuthException("Token expired", "TOKEN_EXPIRED");
		}

		return new VerificationResult(claims, token);
	}

	private Claims getClaimsFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}


	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(this.secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
