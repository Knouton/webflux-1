package com.demo.webflux.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.UserEntity;
import com.demo.webflux.mapper.UserMapper;
import com.demo.webflux.repository.UserRepository;
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
class UserServiceTest {

	@Mock
	UserRepository userRepository;
	@Mock
	UserMapper userMapper;
	@Mock
	PasswordEncoder passwordEncoder;
	@InjectMocks
	UserService userService;

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
		userDto.setId(1L);
		userDto.setUsername("username");
		userDto.setPassword("pass");
		userDto.setEnabled(true);
		userDto.setFirstName("firstName");
		userDto.setLastName("lastName");
		userDto.setCreatedAt(LocalDateTime.now());
		userDto.setUpdatedAt(LocalDateTime.now());

		given(userMapper.map(any(UserEntity.class))).willReturn(userDto);
		given(userMapper.map(any(UserDto.class))).willReturn(userEntity);
	}

	@Test
	void registerUser_Success() {
		given(userRepository.save(any(UserEntity.class))).willReturn(Mono.just(userEntity));
		//when(userRepository.save(any(UserEntity.class))).thenReturn(Mono.just(userEntity));
		given(userRepository.existsByUsername(any(String.class))).willReturn(Mono.just(false));

		val result = userService.registerUser(userDto);

		assertThat(result.block()).isEqualTo(userDto);
	}


	@Test
	void getUserById_Success() {
		given(userRepository.findById(any(Long.class))).willReturn(Mono.just(userEntity));

		val result = userService.getUserById(1L);

		assertThat(result.block()).isEqualTo(userDto);
	}

	@Test
	void getUserByUserName_Success() {
		given(userRepository.findByUsername(any(String.class))).willReturn(Mono.just(userEntity));

		val result = userService.getUserByUserName(userEntity.getUsername());

		assertThat(result.block()).isEqualTo(userEntity);
	}
}