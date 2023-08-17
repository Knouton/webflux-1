package com.demo.webflux.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static reactor.core.publisher.Mono.when;
import com.demo.webflux.dto.ResourceDto;
import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.ResourceEntity;
import com.demo.webflux.entity.UserEntity;
import com.demo.webflux.mapper.ResourceMapper;
import com.demo.webflux.repository.ResourceRepository;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
class ResourceServiceTest {

	@Mock
	ResourceRepository repository;
	@Mock
	ResourceMapper resourceMapper;
	@InjectMocks
	ResourceService service;

	ResourceEntity resourceEntity;
	ResourceDto resourceDto;

	@BeforeEach
	void init() {
		resourceEntity = new ResourceEntity().toBuilder().build();
		resourceEntity.setId(1L);
		resourceEntity.setPath("path");
		resourceEntity.setValue("value");

		resourceDto = new ResourceDto();
		resourceDto.setId(1L);
		resourceDto.setPath("path");
		resourceDto.setValue("value");

		given(resourceMapper.map(any(ResourceEntity.class))).willReturn(resourceDto);
		given(resourceMapper.map(any(ResourceDto.class))).willReturn(resourceEntity);
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