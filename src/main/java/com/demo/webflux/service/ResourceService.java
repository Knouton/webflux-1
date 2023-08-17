package com.demo.webflux.service;

import com.demo.webflux.dto.ResourceDto;
import com.demo.webflux.entity.ResourceEntity;
import com.demo.webflux.mapper.ResourceMapper;
import com.demo.webflux.repository.ResourceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class ResourceService {
	private final ResourceRepository repository;
	private final ResourceMapper mapper;

	public Mono<ResourceDto> saveResource(ResourceDto dto) {
		ResourceEntity entity = mapper.map(dto);

		return repository.save(entity).map(mapper::map);

	}
	public Mono<ResourceDto> getResourceById(Long id) {
		return repository.findById(id)
				.map(mapper::map);
	}


}
