package com.demo.webflux.controller;

import com.demo.webflux.dto.AuthReqDto;
import com.demo.webflux.dto.AuthRespDto;
import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.UserEntity;
import com.demo.webflux.mapper.UserMapper;
import com.demo.webflux.security.model.CustomPrincipal;
import com.demo.webflux.security.token.SecurityService;
import com.demo.webflux.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final SecurityService securityService;
	private final UserService userService;

	@PostMapping("/register")
	public Mono<UserDto> register(@RequestBody UserDto userDto) {
		return userService.registerUser(userDto);
	}

	@PostMapping("/login")
	public Mono<AuthRespDto> login(@RequestBody AuthReqDto reqDto) {
		return securityService.authenticate(reqDto.getUsername(), reqDto.getPassword())
				.flatMap(tokenDetails -> Mono.just(AuthRespDto.builder()
						                                   .userId(tokenDetails.getUserId())
						                                   .token(tokenDetails.getToken())
						                                   .issueAt(tokenDetails.getIssuedAt())
						                                   .expiresAt(tokenDetails.getExpiresAt())
						                                   .build()));
	}

	@GetMapping("/info")
	public Mono<UserDto> getUserInfo(Authentication authentication) {
		CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();

		return userService.getUserById(customPrincipal.getId());
	}

}
