package com.demo.webflux.service.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.mongo.User;
import com.demo.webflux.entity.postgres.UserEntity;
import com.demo.webflux.mapper.mongo.UserMapperMongo;
import com.demo.webflux.repository.mongo.UserRepositoryMongo;
import java.time.LocalDateTime;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
public class UserServiceMongoTest {

	@Mock
	UserRepositoryMongo repositoryMongo;
	@Mock
	UserMapperMongo mapperMongo;
	@Mock
	PasswordEncoder passwordEncoder;
	@InjectMocks
	UserServiceMongo serviceMongo;

	User user;
	UserDto userDto;

	@BeforeEach
	void init() {
		user = new User();
		user.setId("1");
		user.setUsername("username");
		user.setPassword("pass");
		user.setEnabled(true);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());

		userDto = new UserDto();
		userDto.setId("1");
		userDto.setUsername("username");
		userDto.setPassword("pass");
		userDto.setEnabled(true);
		userDto.setFirstName("firstName");
		userDto.setLastName("lastName");
		userDto.setCreatedAt(LocalDateTime.now());
		userDto.setUpdatedAt(LocalDateTime.now());

		given(mapperMongo.map(any(User.class))).willReturn(userDto);
		given(mapperMongo.map(any(UserDto.class))).willReturn(user);
	}

	@Test
	void registerUser_Success() {
		given(repositoryMongo.save(any(User.class))).willReturn(Mono.just(user));
		//when(userRepositorySql.save(any(UserEntity.class))).thenReturn(Mono.just(userEntity));
		given(repositoryMongo.existsByUsername(any(String.class))).willReturn(Mono.just(false));

		val result = serviceMongo.registerUser(userDto);

		assertThat(result.block()).isEqualTo(userDto);
	}


	@Test
	void getUserById_Success() {
		given(repositoryMongo.findById(any(String.class))).willReturn(Mono.just(user));

		val result = serviceMongo.getUserById("1");

		assertThat(result.block()).isEqualTo(userDto);
	}

	@Test
	void getUserByUserName_Success() {
		given(repositoryMongo.findUserByUsername(any(String.class))).willReturn(Mono.just(user));

		val result = serviceMongo.getUserByUserName(user.getUsername());

		assertThat(result.block()).isEqualTo(user);
	}
}
