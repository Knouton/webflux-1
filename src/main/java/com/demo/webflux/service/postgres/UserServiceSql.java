package com.demo.webflux.service.postgres;

import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.postgres.UserEntity;
import com.demo.webflux.entity.UserRole;
import com.demo.webflux.exception.UserAlreadyExists;
import com.demo.webflux.mapper.sql.UserMapperSql;
import com.demo.webflux.repository.postgres.UserRepositorySql;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceSql {

	private final UserRepositorySql userRepositorySql;
	private final UserMapperSql mapper;
	private final PasswordEncoder passwordEncoder;

	public Mono<UserDto> registerUser(UserDto userDto) {
		UserEntity userEntity = mapper.map(userDto);

		return userRepositorySql.existsByUsername(userEntity.getUsername())
						.flatMap(exists -> (exists) ? Mono.error(new UserAlreadyExists(String.format("User %s already exists",
						                                                                             userEntity.getUsername())))
								: userRepositorySql.save(userEntity.toBuilder()
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
		return userRepositorySql.findById(id).map(mapper::map);
	}

	public  Mono<UserEntity> getUserByUserName(String username) {
		return userRepositorySql.findByUsername(username);
	}
}
