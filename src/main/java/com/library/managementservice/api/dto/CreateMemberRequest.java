package com.library.managementservice.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateMemberRequest(
    @NotBlank String name,
    @Email @NotBlank String email
) {}
