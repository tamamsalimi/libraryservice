package com.library.managementservice.service;

import com.library.managementservice.api.dto.CreateMemberRequest;
import com.library.managementservice.api.dto.CreateUserResponse;
import com.library.managementservice.api.dto.LoanDetailResponse;
import com.library.managementservice.api.dto.MemberResponse;
import com.library.managementservice.entity.Member;
import com.library.managementservice.entity.User;
import com.library.managementservice.repository.LoanRepository;
import com.library.managementservice.repository.MemberRepository;
import com.library.managementservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private static final Logger log =
            LoggerFactory.getLogger(MemberService.class);


    private final MemberRepository repository;

    private final LoanRepository loanRepository;

    private final UserService userService;

    public MemberService(MemberRepository repository, LoanRepository loanRepository, UserService userService) {
        this.repository = repository;
        this.loanRepository = loanRepository;
        this.userService = userService;
    }

    @Transactional
    public MemberResponse create(CreateMemberRequest request) {
        log.info("Create member requested with email={}", request.email());
        if (repository.existsByEmail(request.email())) {
            log.warn("Create member failed: email already exists, email={}", request.email());
            throw new IllegalStateException("Email already exists");
        }

        Member member = new Member(
                request.name(),
                request.email()
        );
        repository.save(member);
        log.info("Member created successfully, memberId={}", member.getId());
        CreateUserResponse userResponse = userService.createUserForMember(member);
        log.info("User created for member, memberId={}, username={}",
                member.getId(),
                member.getEmail()
        );

        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                0L,
                userResponse.initialPassword() // ⚠️ hanya return sekali
        );
    }

    @Transactional(readOnly = true)
    public MemberResponse getById(Long id) {
        log.debug("Get member by id requested, memberId={}", id);
        Member member = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Member not found, memberId={}", id);
                    return new IllegalArgumentException("Member not found");
                });
        long activeLoans = loanRepository.countActiveLoans(id);
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                activeLoans,
                ""
        );
    }


    @Transactional(readOnly = true)
    public List<LoanDetailResponse> getLoansByMember(Long memberId) {
        log.debug("Get active loans for member, memberId={}", memberId);
        repository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("Member not found when fetching loans, memberId={}", memberId);
                    return new IllegalArgumentException("Member not found");
                });

        List<LoanDetailResponse> loans =
                loanRepository.findLoanHistoryByMember(memberId)
                        .stream()
                        .map(l -> new LoanDetailResponse(
                                l.getId(),
                                l.getBook().getId(),
                                l.getBook().getTitle(),
                                l.getBorrowedAt(),
                                l.getDueDate(),
                                l.getReturnedAt()
                        ))
                        .toList();

        log.info("Active loans fetched, memberId={}, count={}", memberId, loans.size());
        return loans;
    }


}
