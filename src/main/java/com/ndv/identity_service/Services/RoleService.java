package com.ndv.identity_service.Services;

import com.ndv.identity_service.domain.dtos.request.RoleRequest;
import com.ndv.identity_service.domain.dtos.response.RoleResponse;
import com.ndv.identity_service.domain.entities.Permission;
import com.ndv.identity_service.domain.entities.Role;
import com.ndv.identity_service.mappers.RoleMapper;
import com.ndv.identity_service.repositories.PermissionRepository;
import com.ndv.identity_service.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;

    public RoleResponse createRole(RoleRequest request){
        Role newRole = roleMapper.toEntity(request);
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        newRole.setPermissions(new HashSet<>(permissions));
        Role savedRole = roleRepository.save(newRole);
        return roleMapper.toDto(savedRole);
    }

    public List<RoleResponse> getAllRoles(){
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(role -> roleMapper.toDto(role))
                .toList();
    }

    public void deleteRole(String role){
        roleRepository.deleteById(role);
    }
}
