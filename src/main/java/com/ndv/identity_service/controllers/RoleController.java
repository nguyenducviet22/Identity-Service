package com.ndv.identity_service.controllers;

import com.ndv.identity_service.Services.PermissionService;
import com.ndv.identity_service.Services.RoleService;
import com.ndv.identity_service.domain.dtos.request.ApiResponse;
import com.ndv.identity_service.domain.dtos.request.PermissionRequest;
import com.ndv.identity_service.domain.dtos.request.RoleRequest;
import com.ndv.identity_service.domain.dtos.response.PermissionResponse;
import com.ndv.identity_service.domain.dtos.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) throws Exception {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles(){
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRoles())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable UUID id){
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder().build();
    }
}
