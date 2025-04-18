package com.ndv.identity_service.domain.dtos.request;

import com.ndv.identity_service.domain.entities.Role;
import com.ndv.identity_service.validators.DOBConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {

    @NotBlank(message = "Username is required!")
    @Size(min = 2, max = 20, message = "Username must be between {min} and {max} chars!")
    String username;
    @Size(min = 8, message = "Password must be at least {min} chars!")
    String password;
    @NotNull(message = "First name is required!")
    String firstName;
    @NotNull(message = "Last name is required!")
    String lastName;
    @DOBConstraint(min = 18, message = "Age must be at least {min} years old!")
    LocalDate dob;
    @Builder.Default
    Set<UUID> roleIds = new HashSet<>();
}
