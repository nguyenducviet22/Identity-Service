package com.ndv.identity_service.mappers;

import com.ndv.identity_service.domain.dtos.request.PermissionRequest;
import com.ndv.identity_service.domain.dtos.request.RoleRequest;
import com.ndv.identity_service.domain.dtos.response.PermissionResponse;
import com.ndv.identity_service.domain.dtos.response.RoleResponse;
import com.ndv.identity_service.domain.entities.Permission;
import com.ndv.identity_service.domain.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleRequest request);
    RoleResponse toDto(Role role);
}
