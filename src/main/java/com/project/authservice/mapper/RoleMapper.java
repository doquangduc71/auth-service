package com.project.authservice.mapper;

import com.project.authservice.dto.request.PermissionRequest;
import com.project.authservice.dto.request.RoleRequest;
import com.project.authservice.dto.request.UserResquest;
import com.project.authservice.dto.response.PermissionResponse;
import com.project.authservice.dto.response.RoleResponse;
import com.project.authservice.entity.Permission;
import com.project.authservice.entity.Role;
import org.mapstruct.*;

@Mapper(componentModel = "spring"
        ,nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        ,nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
