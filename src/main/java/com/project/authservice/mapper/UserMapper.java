package com.project.authservice.mapper;

import com.project.authservice.dto.request.UserResquest;
import com.project.authservice.dto.response.UserResponse;
import com.project.authservice.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring"
        ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {
    @Mapping(target = "roles",ignore = true)
    User toUser(UserResquest request);
    @Mapping(target = "roles",ignore = true)
    void updateUser(@MappingTarget User user, UserResquest request);

    UserResponse toUserResponse(User user);
}
