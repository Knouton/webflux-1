package com.demo.webflux.mapper;

import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserDto map (UserEntity userEntity);

	@InheritInverseConfiguration
	UserEntity map(UserDto dto);
}
