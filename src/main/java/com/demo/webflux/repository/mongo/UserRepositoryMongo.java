package com.demo.webflux.repository.mongo;

import com.demo.webflux.entity.mongo.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepositoryMongo extends ReactiveMongoRepository<User, String> {

	Mono<User> findUserByUsername(String name);
	Mono<Boolean> existsByUsername(String name);
}
