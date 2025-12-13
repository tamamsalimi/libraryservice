package com.library.managementservice.controller;

import com.library.managementservice.api.dto.BorrowLoanResponse;
import com.library.managementservice.api.dto.ReturnLoanResponse;
import com.library.managementservice.service.LoanService;
;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        @RequestParam Long memberId
    ) {
        return service.borrowBook(bookId, memberId);
    }

    @PostMapping("/return")
    public ReturnLoanResponse returnBook(
        @RequestParam Long bookId,
        @RequestParam Long memberId
    ) {
       return service.returnBook(bookId, memberId);
    }
}
