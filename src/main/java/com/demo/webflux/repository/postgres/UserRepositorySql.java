package com.demo.webflux.repository.postgres;

import com.demo.webflux.entity.postgres.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepositorySql extends R2dbcRepository<UserEntity, Long> {

	Mono<UserEntity> findByUsername(String username);

	Mono<Boolean> existsByUsername(String username);
}
