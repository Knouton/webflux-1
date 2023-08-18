package com.demo.webflux.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.demo.webflux.config.JwtTest;
import com.demo.webflux.dto.ResourceDto;
import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.UserEntity;
import com.demo.webflux.entity.UserRole;
import com.demo.webflux.repository.ResourceRepository;
import com.demo.webflux.service.ResourceService;
import com.demo.webflux.service.UserService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ResourceControllerTest {

	private final static String TEST_USER = "test_user";
	private final static String PASSWORD = "test_user1";

	@Autowired
	private WebTestClient webTestClient;
	@MockBean
	private ResourceService resourceService;

	@MockBean
	private UserService userService;

	@MockBean
	private ResourceRepository repository;

	ResourceDto dto;
	UserEntity userEntity;
	UserDto userDto;

	@BeforeEach
	void init() {
		dto = new ResourceDto(1L, "value", "path");

		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setUsername("test");
		userEntity.setRole(UserRole.USER);
		userEntity.setEnabled(true);

		userDto = new UserDto();
		userDto.setId(1L);
		userDto.setUsername("test");
		userDto.setRole(UserRole.USER);
		userDto.setEnabled(true);
	}

	@Test
	//@WithMockUser(authorities = USER)
	void getResourceByJwt_Success() {
		when(userService.getUserById(any(Long.class))).thenReturn(Mono.just(userDto));
		when(resourceService.getResourceById(any(Long.class))).thenReturn(Mono.just(dto));

		webTestClient
				//mutateWith(mockJwt().jwt(jwt -> jwt.claims(claims -> claims.remove("scope"))))
				.get()
				.uri("/api/resources/{id}", Collections.singletonMap("id", dto.getId()))
				.headers(http -> http.setBearerAuth(JwtTest.generateJwt(userEntity)))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(System.out::println)
				.jsonPath("$.id").isEqualTo(dto.getId())
				.jsonPath("$.value").isEqualTo(dto.getValue())
				.jsonPath("$.path").isEqualTo(dto.getPath());
	}

	@Test
	void getResource_UnSuccess() {

	}

	@Test
	void createResourceByJwt_Success() {
	}

	@Test
	void createResource_UnSuccess() {

	}
}