package com.demo.webflux.service.mongo;

import com.demo.webflux.dto.ResourceDto;
import com.demo.webflux.entity.mongo.Resource;
import com.demo.webflux.mapper.mongo.ResourceMapperMongo;
import com.demo.webflux.repository.mongo.ResourceRepositoryMongo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ResourceServiceMongo {
    private final ResourceRepositoryMongo repository;
	private final ResourceMapperMongo mapper;

	public Mono<ResourceDto> saveResource(ResourceDto dto) {
		Resource entity = mapper.map(dto);

		return repository.save(entity)
				.map(mapper::map);

	}

	public Mono<ResourceDto> getResourceById(String id) {
		return repository.findById(id)
				.map(mapper::map);
	}

}
