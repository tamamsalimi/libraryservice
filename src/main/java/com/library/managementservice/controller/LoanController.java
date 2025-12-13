package com.library.managementservice.controller;

import com.library.managementservice.api.dto.BorrowLoanResponse;
import com.library.managementservice.api.dto.LoanDetailResponse;
import com.library.managementservice.api.dto.ReturnLoanResponse;
import com.library.managementservice.service.LoanService;
;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @PostMapping("/borrow")
    public BorrowLoanResponse borrow(
            @RequestParam Long bookId,
            Authentication authentication
    ) {
        return service.borrowBook(bookId, authentication);
    }

    @PostMapping("/return")
    public ReturnLoanResponse returnBook(
            @RequestParam Long bookId,
            Authentication authentication
    ) {
        return service.returnBook(bookId, authentication);
    }

    @GetMapping("/me")
    public List<LoanDetailResponse> getMyLoans(Authentication authentication) {
        return service.getLoansForCurrentMember(authentication);
    }


}
