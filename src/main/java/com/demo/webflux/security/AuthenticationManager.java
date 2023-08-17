package com.demo.webflux.security;

import com.demo.webflux.dto.UserDto;
import com.demo.webflux.exception.UnauthorizedException;
import com.demo.webflux.security.model.CustomPrincipal;
import com.demo.webflux.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
	private final UserService userService;

	@Override
	public Mono<Authentication> authenticate(final Authentication authentication) {
		CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

		return userService.getUserById(principal.getId())
				.filter(UserDto::isEnabled)
				.switchIfEmpty(Mono.error(new UnauthorizedException("User disabled: " + principal.getName())))
				.map(user -> authentication);
	}
}
