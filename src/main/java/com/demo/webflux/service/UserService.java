package com.demo.webflux.service;

import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.ResourceEntity;
import com.demo.webflux.entity.UserEntity;
import com.demo.webflux.entity.UserRole;
import com.demo.webflux.exception.UserAlreadyExists;
import com.demo.webflux.mapper.UserMapper;
import com.demo.webflux.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper mapper;
	private final PasswordEncoder passwordEncoder;

	public Mono<UserDto> registerUser(UserDto userDto) {
		UserEntity userEntity = mapper.map(userDto);

		return userRepository.existsByUsername(userEntity.getUsername())
						.flatMap(exists -> (exists) ? Mono.error(new UserAlreadyExists(String.format("User %s already exists",
						                                                                             userEntity.getUsername())))
								: userRepository.save(userEntity.toBuilder()
				                           .password(passwordEncoder.encode(userEntity.getPassword()))
				                           .role(UserRole.USER)
				                           .enabled(true)
				                           .createdAt(LocalDateTime.now())
				                           .updatedAt(LocalDateTime.now())
				                           .build())
				.doOnSuccess(user -> log.info("In registerUser — user: {} created", user)))
				.map(mapper::map);
	}

	public Mono<UserDto> getUserById(Long id) {
		return userRepository.findById(id).map(mapper::map);
	}

	public  Mono<UserEntity> getUserByUserName(String username) {
		return userRepository.findByUsername(username);

	}
}
