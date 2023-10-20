package com.demo.webflux.service.postgres;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.postgres.UserEntity;
import com.demo.webflux.mapper.sql.UserMapperSql;
import com.demo.webflux.repository.postgres.UserRepositorySql;
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
class UserServiceSqlTest {

	@Mock
	UserRepositorySql userRepositorySql;
	@Mock
	UserMapperSql userMapperSql;
	@Mock
	PasswordEncoder passwordEncoder;
	@InjectMocks
	UserServiceSql userServiceSql;

	UserEntity userEntity;
	UserDto userDto;

	@BeforeEach
	void init() {
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setUsername("username");
		userEntity.setPassword("pass");
		userEntity.setEnabled(true);
		userEntity.setFirstName("firstName");
		userEntity.setLastName("lastName");
		userEntity.setCreatedAt(LocalDateTime.now());
		userEntity.setUpdatedAt(LocalDateTime.now());

		userDto = new UserDto();
		userDto.setId("1");
		userDto.setUsername("username");
		userDto.setPassword("pass");
		userDto.setEnabled(true);
		userDto.setFirstName("firstName");
		userDto.setLastName("lastName");
		userDto.setCreatedAt(LocalDateTime.now());
		userDto.setUpdatedAt(LocalDateTime.now());

		given(userMapperSql.map(any(UserEntity.class))).willReturn(userDto);
		given(userMapperSql.map(any(UserDto.class))).willReturn(userEntity);
	}

	@Test
	void registerUser_Success() {
		given(userRepositorySql.save(any(UserEntity.class))).willReturn(Mono.just(userEntity));
		//when(userRepositorySql.save(any(UserEntity.class))).thenReturn(Mono.just(userEntity));
		given(userRepositorySql.existsByUsername(any(String.class))).willReturn(Mono.just(false));

		val result = userServiceSql.registerUser(userDto);

		assertThat(result.block()).isEqualTo(userDto);
	}


	@Test
	void getUserById_Success() {
		given(userRepositorySql.findById(any(Long.class))).willReturn(Mono.just(userEntity));

		val result = userServiceSql.getUserById(1L);

		assertThat(result.block()).isEqualTo(userDto);
	}

	@Test
	void getUserByUserName_Success() {
		given(userRepositorySql.findByUsername(any(String.class))).willReturn(Mono.just(userEntity));

		val result = userServiceSql.getUserByUserName(userEntity.getUsername());

		assertThat(result.block()).isEqualTo(userEntity);
	}
}