package com.demo.webflux.service.mongo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import com.demo.webflux.dto.ResourceDto;
import com.demo.webflux.entity.mongo.Resource;
import com.demo.webflux.entity.postgres.ResourceEntity;
import com.demo.webflux.mapper.mongo.ResourceMapperMongo;
import com.demo.webflux.repository.mongo.ResourceRepositoryMongo;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
public class ResourceServiceMongoTest {

	@Mock
	ResourceRepositoryMongo repository;
	@Mock
	ResourceMapperMongo resourceMapperMongo;
	@InjectMocks
	ResourceServiceMongo service;

	Resource resource;
	ResourceDto resourceDto;

	@BeforeEach
	void init() {
		resource = new Resource().toBuilder().build();
		resource.setId("1");
		resource.setPath("path");
		resource.setValue("value");

		resourceDto = new ResourceDto();
		resourceDto.setId("1");
		resourceDto.setPath("path");
		resourceDto.setValue("value");

		given(resourceMapperMongo.map(any(Resource.class))).willReturn(resourceDto);
		given(resourceMapperMongo.map(any(ResourceDto.class))).willReturn(resource);
	}

	@Test
	void saveResource_Success() {
		given(repository.save(any(Resource.class))).willReturn(Mono.just(resource));

		val result = service.saveResource(resourceDto);

		assertThat(result.block()).isEqualTo(resourceDto);
	}

	@Test
	void getResource_Success() {
		given(repository.findById(any(String.class))).willReturn(Mono.just(resource));

		val result = service.getResourceById("1");

		assertThat(result.block()).isEqualTo(resourceDto);

	}
}
