package com.library.managementservice.api.dto;

public record CreateUserResponse(
    Long userId,
    String username,
    String role,
    Long memberId,
    String initialPassword // nullable
) {}
