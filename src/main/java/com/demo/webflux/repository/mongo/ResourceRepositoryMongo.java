package com.demo.webflux.repository.mongo;

import com.demo.webflux.entity.mongo.Resource;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ResourceRepositoryMongo extends ReactiveMongoRepository<Resource, String> {
}
