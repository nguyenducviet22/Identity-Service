package com.ndv.identity_service.domain.dtos.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRoleRequest {

    UUID id;
    String name;
    String description;
    @Builder.Default
    Set<UUID> permissionIds = new HashSet<>();
}
