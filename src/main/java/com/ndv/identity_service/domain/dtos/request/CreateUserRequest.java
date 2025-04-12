package com.ndv.identity_service.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "Username is required!")
    @Size(min = 2, max = 20, message = "Username must be between {min} and {max} chars!")
    private String username;
    @Size(min = 8, message = "Password must be at least {min} chars!")
    private String password;
    @NotNull(message = "First name is required!")
    private String firstName;
    @NotNull(message = "Last name is required!")
    private String lastName;
    private LocalDate dob;
}
