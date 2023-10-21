package com.demo.webflux.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.demo.webflux.config.JwtTest;
import com.demo.webflux.dto.ResourceDto;
import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.mongo.User;
import com.demo.webflux.entity.postgres.UserEntity;
import com.demo.webflux.entity.UserRole;
import com.demo.webflux.security.model.TokenDetails;
import com.demo.webflux.security.token.SecurityService;
import com.demo.webflux.service.mongo.ResourceServiceMongo;
import com.demo.webflux.service.mongo.UserServiceMongo;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
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
	ResourceServiceMongo resourceServiceMongo;

	@MockBean
	private UserServiceMongo userServiceMongo;
	@MockBean
	private SecurityService securityService;

	ResourceDto resourceDto;
	User user;
	UserDto userDto;
	TokenDetails tokenDetails;

	@BeforeEach
	void init() {
		resourceDto = new ResourceDto("1", "value", "path");

		user = new User();
		user.setId("1");
		user.setUsername("test");
		user.setRole(UserRole.USER);
		user.setEnabled(true);

		userDto = new UserDto();
		userDto.setId("1");
		userDto.setUsername("test");
		userDto.setPassword("100");
		userDto.setFirstName("firstName");
		userDto.setLastName("lastName");
		userDto.setEnabled(true);

		Long expirationTimeMillis = 50000 * 1000L;
		Date expirationDate = new Date(new Date().getTime() + expirationTimeMillis);
		tokenDetails = new TokenDetails();
		tokenDetails.setUserId("1");
		tokenDetails.setToken(JwtTest.generateJwt(user));
		tokenDetails.setIssuedAt(new Date());
		tokenDetails.setExpiresAt(expirationDate);
	}

	@Test
	void getResourceByJwt_Success() {
		given(userServiceMongo.getUserById(any(String.class))).willReturn(Mono.just(userDto));
		given(resourceServiceMongo.getResourceById(any(String.class))).willReturn(Mono.just(resourceDto));
		given(securityService.authenticate(any(String.class), any(String.class))).willReturn(Mono.just(tokenDetails));

		webTestClient
				.get()
				.uri("/resource/1")
				.headers(http -> http.setBearerAuth(tokenDetails.getToken()))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(System.out::println)
				.jsonPath("$.id").isEqualTo(resourceDto.getId())
				.jsonPath("$.value").isEqualTo(resourceDto.getValue())
				.jsonPath("$.path").isEqualTo(resourceDto.getPath());
	}

	@Test
	void getResource_UnSuccess() {
		webTestClient
				.get()
				.uri("/resource/1")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isUnauthorized()
				.expectBody()
				.consumeWith(System.out::println);
	}

	@Test
	void createResourceByJwt_Success() {
		given(userServiceMongo.getUserById(any(String.class))).willReturn(Mono.just(userDto));
		given(resourceServiceMongo.saveResource(any(ResourceDto.class))).willReturn(Mono.just(resourceDto));
		given(securityService.authenticate(any(String.class), any(String.class))).willReturn(Mono.just(tokenDetails));

		webTestClient
				.post()
				.uri("/resource")
				.headers(http -> http.setBearerAuth(tokenDetails.getToken()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(resourceDto), ResourceDto.class)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(System.out::println)
				.jsonPath("$.id").isEqualTo(resourceDto.getId())
				.jsonPath("$.value").isEqualTo(resourceDto.getValue())
				.jsonPath("$.path").isEqualTo(resourceDto.getPath());
	}

	@Test
	void createResource_UnSuccess() {
		webTestClient
				.post()
				.uri("/resource")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(resourceDto), ResourceDto.class)
				.exchange()
				.expectStatus().isUnauthorized()
				.expectBody()
				.consumeWith(System.out::println);
	}
}