package com.demo.webflux.service.mongo;

import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.UserRole;
import com.demo.webflux.entity.mongo.User;
import com.demo.webflux.exception.UserAlreadyExists;
import com.demo.webflux.mapper.mongo.UserMapperMongo;
import com.demo.webflux.repository.mongo.UserRepositoryMongo;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceMongo {

	UserRepositoryMongo repository;
	UserMapperMongo mapper;
	private final PasswordEncoder passwordEncoder;

	public Mono<UserDto> registerUser(UserDto userDto) {
		User userEntity = mapper.map(userDto);

		return repository.existsByUsername(userEntity.getUsername())
				.flatMap(exists -> (exists) ? Mono.error(new UserAlreadyExists(String.format("User %s already exists",
				                                                                             userEntity.getUsername())))
						: repository.save(userEntity.toBuilder()
								                         .password(passwordEncoder.encode(userEntity.getPassword()))
								                         .role(UserRole.USER)
								                         .enabled(true)
								                         .createdAt(LocalDateTime.now())
								                         .updatedAt(LocalDateTime.now())
								                         .build())
						.doOnSuccess(user -> log.info("In registerUser — user: {} created", user)))
				.map(mapper::map);
	}

	public Mono<UserDto> getUserById(String id) {
		return repository.findById(id).map(mapper::map);
	}

	public  Mono<User> getUserByUserName(String username) {
		return repository.findUserByUsername(username);
	}
}
