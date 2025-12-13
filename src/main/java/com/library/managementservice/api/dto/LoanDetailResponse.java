package com.library.managementservice.api.dto;

import java.time.OffsetDateTime;

public record LoanDetailResponse(
    Long loanId,
    Long bookId,
    String bookTitle,
    OffsetDateTime borrowedAt,
    OffsetDateTime dueDate,
    OffsetDateTime returnedAt
) {}
