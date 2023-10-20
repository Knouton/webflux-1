package com.demo.webflux.service.postgres;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static reactor.core.publisher.Mono.when;
import com.demo.webflux.dto.ResourceDto;
import com.demo.webflux.entity.postgres.ResourceEntity;
import com.demo.webflux.mapper.sql.ResourceMapperSql;
import com.demo.webflux.repository.postgres.ResourceRepositorySql;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
class ResourceServiceSqlTest {

	@Mock
	ResourceRepositorySql repository;
	@Mock
	ResourceMapperSql resourceMapperSql;
	@InjectMocks
	ResourceServiceSql service;

	ResourceEntity resourceEntity;
	ResourceDto resourceDto;

	@BeforeEach
	void init() {
		resourceEntity = new ResourceEntity().toBuilder().build();
		resourceEntity.setId(1L);
		resourceEntity.setPath("path");
		resourceEntity.setValue("value");

		resourceDto = new ResourceDto();
		resourceDto.setId("1");
		resourceDto.setPath("path");
		resourceDto.setValue("value");

		given(resourceMapperSql.map(any(ResourceEntity.class))).willReturn(resourceDto);
		given(resourceMapperSql.map(any(ResourceDto.class))).willReturn(resourceEntity);
	}

	@Test
	void saveResource_Success() {
		given(repository.save(any(ResourceEntity.class))).willReturn(Mono.just(resourceEntity));

		val result = service.saveResource(resourceDto);

		assertThat(result.block()).isEqualTo(resourceDto);
	}

	@Test
	void getResource_Success() {
		given(repository.findById(any(Long.class))).willReturn(Mono.just(resourceEntity));

		val result = service.getResourceById(1L);

		assertThat(result.block()).isEqualTo(resourceDto);

	}
}