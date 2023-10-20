package com.demo.webflux.security;

import com.demo.webflux.security.model.CustomPrincipal;
import com.demo.webflux.security.model.VerificationResult;
import io.jsonwebtoken.Claims;
import java.security.Principal;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

public class UserAuthenticationBearer {

	public static Mono<Authentication> create(VerificationResult verificationResult) {
		Claims claims = verificationResult.getClaims();
		String subject = claims.getSubject();

		String role = claims.get("role", String.class);
		String username = claims.get("username", String.class);

		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

		String principalId = subject;
		Principal principal = new CustomPrincipal(principalId, username);

		return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorities));
	}
}
