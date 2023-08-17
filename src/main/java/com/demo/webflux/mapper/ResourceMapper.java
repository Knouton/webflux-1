package com.demo.webflux.mapper;

import com.demo.webflux.dto.ResourceDto;
import com.demo.webflux.entity.ResourceEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

	ResourceDto map (ResourceEntity entity);

	@InheritInverseConfiguration
	ResourceEntity map(ResourceDto dto);
}
