package com.ndv.identity_service.Services;

import com.ndv.identity_service.domain.dtos.request.PermissionRequest;
import com.ndv.identity_service.domain.dtos.response.PermissionResponse;
import com.ndv.identity_service.domain.entities.Permission;
import com.ndv.identity_service.mappers.PermissionMapper;
import com.ndv.identity_service.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest request) {
        Permission newPermission = permissionMapper.toEntity(request);
        Permission savedPermission = permissionRepository.save(newPermission);
        return permissionMapper.toDto(savedPermission);
    }

    public List<PermissionResponse> getAllPermissions(){
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(permission -> permissionMapper.toDto(permission))
                .toList();
    }

    public void deletePermission(String permission){
        permissionRepository.deleteById(permission);
    }
}
