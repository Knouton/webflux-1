package com.demo.webflux.repository;

import com.demo.webflux.entity.ResourceEntity;
import com.demo.webflux.entity.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ResourceRepository extends R2dbcRepository<ResourceEntity, Long> {
}
