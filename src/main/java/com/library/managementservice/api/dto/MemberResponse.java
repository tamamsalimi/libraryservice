package com.library.managementservice.api.dto;

public record MemberResponse(
    Long id,
    String name,
    String email,
    long activeLoans,
    String password
) {}
