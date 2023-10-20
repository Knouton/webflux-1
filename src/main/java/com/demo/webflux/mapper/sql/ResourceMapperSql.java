package com.demo.webflux.mapper.sql;

import com.demo.webflux.dto.ResourceDto;
import com.demo.webflux.entity.postgres.ResourceEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceMapperSql {

	ResourceDto map (ResourceEntity entity);
	@InheritInverseConfiguration
	ResourceEntity map(ResourceDto dto);

}
