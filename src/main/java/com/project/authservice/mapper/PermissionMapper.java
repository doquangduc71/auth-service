package com.project.authservice.mapper;

import com.project.authservice.dto.request.PermissionRequest;
import com.project.authservice.dto.request.UserResquest;
import com.project.authservice.dto.response.PermissionResponse;
import com.project.authservice.dto.response.UserResponse;
import com.project.authservice.entity.Permission;
import com.project.authservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring"
        ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    void updatePermission(@MappingTarget Permission permission, UserResquest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
