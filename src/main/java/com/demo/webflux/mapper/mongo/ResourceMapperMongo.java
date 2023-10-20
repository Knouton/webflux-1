package com.demo.webflux.mapper.mongo;

import com.demo.webflux.dto.ResourceDto;
import com.demo.webflux.entity.mongo.Resource;
import com.demo.webflux.entity.postgres.ResourceEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceMapperMongo {
	ResourceDto map (Resource entity);

	@InheritInverseConfiguration
	Resource map(ResourceDto dto);
}
