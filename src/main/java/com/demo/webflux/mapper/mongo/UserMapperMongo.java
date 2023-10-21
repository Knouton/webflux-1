package com.demo.webflux.mapper.mongo;

import com.demo.webflux.dto.UserDto;
import com.demo.webflux.entity.mongo.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapperMongo {
	UserDto map(User user);
	@InheritInverseConfiguration
	User map(UserDto userDto);
}
