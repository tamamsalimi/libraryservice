package com.library.managementservice.api.error;

import java.time.OffsetDateTime;

public record ApiErrorResponse(
    String message,
    int status,
    OffsetDateTime timestamp
) {}
