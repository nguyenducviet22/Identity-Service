package com.ndv.identity_service.Services;

import com.ndv.identity_service.domain.dtos.request.RoleRequest;
import com.ndv.identity_service.domain.dtos.response.RoleResponse;
import com.ndv.identity_service.domain.entities.Permission;
import com.ndv.identity_service.domain.entities.Role;
import com.ndv.identity_service.mappers.RoleMapper;
import com.ndv.identity_service.repositories.PermissionRepository;
import com.ndv.identity_service.repositories.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionService permissionService;

    public RoleResponse createRole(RoleRequest request) throws Exception {
        if (roleRepository.existsByName(request.getName())) {
            throw new Exception("Role existed!");
        }
        Role newRole = roleMapper.toEntity(request);
        Set<UUID> permissionIds = request.getPermissionIds();
        List<Permission> permissions = permissionService.getPermissionByIds(permissionIds);
        newRole.setPermissions(new HashSet<>(permissions));
        Role savedRole = roleRepository.save(newRole);
        return roleMapper.toDto(savedRole);
    }

    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(role -> roleMapper.toDto(role))
                .toList();
    }

    public void deleteRole(UUID role) {
        roleRepository.deleteById(role);
    }

    public List<Role> getRoleByIds(Set<UUID> roleIds) {
        List<Role> foundRoles = roleRepository.findAllById(roleIds);
        if (foundRoles.size() != roleIds.size()) throw new EntityNotFoundException("Not all specified roles exist!");
        return foundRoles;
    }
}
