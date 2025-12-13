package com.library.managementservice.api.dto;

import java.time.OffsetDateTime;

public record ReturnLoanResponse(
    Long loanId,
    OffsetDateTime returnedAt,
    String status
) {}
