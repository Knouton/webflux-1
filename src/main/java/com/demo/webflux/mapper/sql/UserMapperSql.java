package com.demo.webflux.mapper.sql;

import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.postgres.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapperSql {
	UserDto map (UserEntity userEntity);

	@InheritInverseConfiguration
	UserEntity map(UserDto dto);
}
