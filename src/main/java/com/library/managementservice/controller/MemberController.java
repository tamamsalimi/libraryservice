package com.library.managementservice.controller;

import com.library.managementservice.api.dto.CreateMemberRequest;
import com.library.managementservice.api.dto.LoanDetailResponse;
import com.library.managementservice.api.dto.MemberResponse;
import com.library.managementservice.service.MemberService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse create(@RequestBody @Valid CreateMemberRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public MemberResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/{id}/loans")
    public List<LoanDetailResponse> getLoans(@PathVariable Long id) {
        return service.getLoansByMember(id);
    }
}
