package com.demo.webflux.controller;

import com.demo.webflux.dto.AuthEvent;
import com.demo.webflux.dto.AuthReqDto;
import com.demo.webflux.dto.AuthRespDto;
import com.demo.webflux.dto.UserDto;
import com.demo.webflux.security.model.CustomPrincipal;
import com.demo.webflux.security.token.SecurityService;
import com.demo.webflux.service.adapter.AuthEventService;
import com.demo.webflux.service.mongo.UserServiceMongo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

	private final SecurityService securityService;
	private final UserServiceMongo userServiceMongo;
	private final AuthEventService authEventService;

	@PostMapping("/register")
	public Mono<UserDto> register(@RequestBody UserDto userDto) {
		log.info("register user: {}", userDto.getUsername());

		Mono<UserDto> registerUser = userServiceMongo.registerUser(userDto);

		log.info("Successful register user: {}", userDto.getUsername());
		authEventService.sendAuthEvent(
				Mono.just(new AuthEvent("user " + userDto.getUsername() + " successful created")));

		return registerUser;
	}

	@PostMapping("/login")
	public Mono<AuthRespDto> login(@RequestBody AuthReqDto reqDto) {
		authEventService.sendAuthEvent(
				Mono.just(new AuthEvent("unknown user try to login" + reqDto.getUsername())));

		log.info("login user: {}", reqDto.getUsername());
		Mono<AuthRespDto> respDto = securityService.authenticate(reqDto.getUsername(), reqDto.getPassword())
				.flatMap(tokenDetails -> Mono.just(AuthRespDto.builder()
						                                   .userId(tokenDetails.getUserId())
						                                   .token(tokenDetails.getToken())
						                                   .issueAt(tokenDetails.getIssuedAt())
						                                   .expiresAt(tokenDetails.getExpiresAt())
						                                   .build()));
		log.info("Successful login user: {}", reqDto.getUsername());

		authEventService.sendAuthEvent(
				Mono.just(new AuthEvent("user " + reqDto.getUsername() + " successful login")));
		return respDto;
	}

	@GetMapping("/info")
	public Mono<UserDto> getUserInfo(Authentication authentication) {
		CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();

		return userServiceMongo.getUserById(customPrincipal.getId());
	}

}
