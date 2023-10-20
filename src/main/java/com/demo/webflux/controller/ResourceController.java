package com.demo.webflux.controller;

import com.demo.webflux.dto.ResourceDto;
import com.demo.webflux.service.mongo.ResourceServiceMongo;
import com.demo.webflux.service.postgres.ResourceServiceSql;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resource")
public class ResourceController {
	private final ResourceServiceMongo serviceMongo;

	@GetMapping("/{id}")
	public Mono<ResourceDto> getResource(@PathVariable String id) {
		return serviceMongo.getResourceById(id);
	}

	@PostMapping
	public Mono<ResourceDto> createResource(@RequestBody ResourceDto dto) {
		return serviceMongo.saveResource(dto);
	}
}
