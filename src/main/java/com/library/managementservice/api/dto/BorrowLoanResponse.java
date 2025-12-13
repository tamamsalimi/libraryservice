package com.library.managementservice.api.dto;

import java.time.OffsetDateTime;

public record BorrowLoanResponse(
    Long loanId,
    Long bookId,
    Long memberId,
    OffsetDateTime borrowedAt,
    OffsetDateTime dueDate
) {}
