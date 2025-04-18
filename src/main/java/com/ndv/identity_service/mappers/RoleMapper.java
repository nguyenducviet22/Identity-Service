package com.ndv.identity_service.mappers;

import com.ndv.identity_service.domain.dtos.request.CreateRoleRequest;
import com.ndv.identity_service.domain.dtos.request.UpdateRoleRequest;
import com.ndv.identity_service.domain.dtos.response.RoleResponse;
import com.ndv.identity_service.domain.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(CreateRoleRequest request);
    RoleResponse toDto(Role role);

    void updateRole(@MappingTarget Role role, UpdateRoleRequest request);

}
