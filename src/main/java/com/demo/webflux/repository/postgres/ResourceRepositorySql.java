package com.demo.webflux.repository.postgres;

import com.demo.webflux.entity.postgres.ResourceEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepositorySql extends R2dbcRepository<ResourceEntity, Long> {
}
