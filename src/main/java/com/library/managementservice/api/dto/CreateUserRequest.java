package com.library.managementservice.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
    @NotBlank String username,   // email
    @NotBlank String password,
    @NotBlank String role,       // ADMIN / MEMBER
    Long memberId                // REQUIRED if role = MEMBER
) {}
