package com.library.managementservice.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateBookRequest(
    @NotBlank String title,
    @NotBlank String author,
    @Min(1) int totalCopies
) {}
