package com.demo.webflux.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import com.demo.webflux.config.JwtTest;
import com.demo.webflux.dto.AuthReqDto;
import com.demo.webflux.dto.AuthRespDto;
import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.mongo.User;
import com.demo.webflux.entity.postgres.UserEntity;
import com.demo.webflux.entity.UserRole;
import com.demo.webflux.security.model.TokenDetails;
import com.demo.webflux.security.token.SecurityService;
import com.demo.webflux.service.mongo.UserServiceMongo;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class AuthControllerTest {
	private final static String TEST_USER = "test_user";
	private final static String PASSWORD = "test_user1";
	@Autowired
	private WebTestClient webTestClient;
	@MockBean
	private UserServiceMongo serviceMongo;
	@MockBean
	private SecurityService securityService;


	User userEntity;
	UserDto userDto;
	AuthReqDto reqDto;
	AuthRespDto respDto;
	TokenDetails tokenDetails;
	@BeforeEach
	void init() {
		userEntity = new User();
		userEntity.setId("");
		userEntity.setUsername("test");
		userEntity.setPassword("100");
		userEntity.setRole(UserRole.USER);
		userEntity.setEnabled(true);

		userDto = new UserDto();
		userDto.setId("1");
		userDto.setUsername("test");
		userDto.setPassword("100");
		userDto.setFirstName("firstName");
		userDto.setLastName("lastName");
		userDto.setEnabled(true);

		reqDto = new AuthReqDto();
		reqDto.setUsername("100");
		reqDto.setPassword("100");

		respDto = new AuthRespDto();
		respDto.setUserId("1");
		respDto.setToken("token");
		respDto.setIssueAt(new Date());
		Long expirationTimeMillis = 50000 * 1000L;
		Date expirationDate = new Date(new Date().getTime() + expirationTimeMillis);
		respDto.setExpiresAt(expirationDate);

		tokenDetails = new TokenDetails();
		tokenDetails.setUserId("1");
		tokenDetails.setToken(JwtTest.generateJwt(userEntity));
		tokenDetails.setIssuedAt(new Date());
		tokenDetails.setExpiresAt(expirationDate);
	}
	@Test
	void register_Success() {
		given(serviceMongo.registerUser(any(UserDto.class))).willReturn(Mono.just(userDto));

		webTestClient
				.post()
				.uri("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(userDto), UserDto.class)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(System.out::println)
				.jsonPath("$.id").isEqualTo(userDto.getId())
				.jsonPath("$.username").isEqualTo(userDto.getUsername())
				.jsonPath("$.first_name").isEqualTo(userDto.getFirstName())
				.jsonPath("$.last_name").isEqualTo(userDto.getLastName());
	}

	@Test
	void login_Success() {
		tokenDetails.setToken("token");
		given(securityService.authenticate(any(String.class), any(String.class))).willReturn(Mono.just(tokenDetails));

		webTestClient
				.post()
				.uri("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(reqDto), AuthReqDto.class)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(System.out::println)
				.jsonPath("$.user_id").isEqualTo(respDto.getUserId())
				.jsonPath("$.token").isEqualTo(respDto.getToken());
	}

	@Test
	void getUserInfoByJwt_Success() {
		given(serviceMongo.getUserById(any(String.class))).willReturn(Mono.just(userDto));
		given(securityService.authenticate(any(String.class), any(String.class))).willReturn(Mono.just(tokenDetails));
		String token = tokenDetails.getToken();

		webTestClient
				.get()
				.uri("/api/auth/info")
				.headers(http -> http.setBearerAuth(token))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.consumeWith(System.out::println)
				.jsonPath("$.id").isEqualTo(userDto.getId())
				.jsonPath("$.username").isEqualTo(userDto.getUsername());
	}

	@Test
	void getUserInfo_UnSuccess() {
		webTestClient
				.get()
				.uri("/api/auth/info")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isUnauthorized()
				.expectBody()
				.consumeWith(System.out::println);
	}
}