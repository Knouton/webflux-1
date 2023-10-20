package com.demo.webflux.service.postgres;

import com.demo.webflux.dto.ResourceDto;
import com.demo.webflux.entity.postgres.ResourceEntity;
import com.demo.webflux.mapper.sql.ResourceMapperSql;
import com.demo.webflux.repository.postgres.ResourceRepositorySql;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class ResourceServiceSql {
	private final ResourceRepositorySql repository;
	private final ResourceMapperSql mapper;

	public Mono<ResourceDto> saveResource(ResourceDto dto) {
		ResourceEntity entity = mapper.map(dto);

		return repository.save(entity).map(mapper::map);

	}
	public Mono<ResourceDto> getResourceById(Long id) {
		return repository.findById(id)
				.map(mapper::map);
	}


}
