package com.library.managementservice.service;

import com.library.managementservice.api.dto.BorrowLoanResponse;
import com.library.managementservice.api.dto.ReturnLoanResponse;
import com.library.managementservice.config.LibraryRulesConfig;
import com.library.managementservice.entity.Book;
import com.library.managementservice.entity.Loan;
import com.library.managementservice.entity.Member;
import com.library.managementservice.repository.BookRepository;
import com.library.managementservice.repository.LoanRepository;
import com.library.managementservice.repository.MemberRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class LoanService {

    private static final Logger log =
            LoggerFactory.getLogger(LoanService.class);

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;
    private final LibraryRulesConfig rules;

    public LoanService(
            BookRepository bookRepository,
            MemberRepository memberRepository,
            LoanRepository loanRepository,
            LibraryRulesConfig rules
    ) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
        this.rules = rules;
    }

    // ---------------- BORROW ----------------

    @Transactional
    public BorrowLoanResponse borrowBook(Long bookId, Long memberId) {
        OffsetDateTime now = OffsetDateTime.now();

        log.info("Borrow request started: bookId={}, memberId={}", bookId, memberId);

        // 1. Load & lock book
        Book book = bookRepository.findByIdForUpdate(bookId)
                .orElseThrow(() -> {
                    log.warn("Borrow failed - book not found: bookId={}", bookId);
                    return new IllegalArgumentException("Book not found");
                });

        // 2. Load member
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("Borrow failed - member not found: memberId={}", memberId);
                    return new IllegalArgumentException("Member not found");
                });

        // 3. Rule: max active loans
        long activeLoans = loanRepository.countActiveLoans(memberId);
        log.debug("Active loans check: memberId={}, activeLoans={}", memberId, activeLoans);

        if (activeLoans >= rules.getMaxActiveLoansPerMember()) {
            log.warn(
                    "Borrow rejected - max active loans exceeded: memberId={}, activeLoans={}, limit={}",
                    memberId,
                    activeLoans,
                    rules.getMaxActiveLoansPerMember()
            );
            throw new IllegalStateException("Max active loans exceeded");
        }

        // 4. Rule: no overdue loans
        long overdueLoans = loanRepository.countOverdueLoans(memberId, now);
        log.debug("Overdue loans check: memberId={}, overdueLoans={}", memberId, overdueLoans);
        if (overdueLoans > 0) {
            throw new IllegalStateException("Member has overdue loans");
        }

        // 5. Decrease available copies (domain rule)
        book.borrowOne();
        log.debug(
                "Book stock updated: bookId={}, availableCopies={}",
                bookId,
                book.getAvailableCopies()
        );

        // 6. Create loan
        OffsetDateTime dueDate = now.plusDays(rules.getMaxLoanDurationDays());
        Loan loan = new Loan(book, member, now, dueDate);

        // 7. Persist
        loanRepository.save(loan);
        bookRepository.save(book);

        log.info(
                "Borrow success: bookId={}, memberId={}, dueDate={}",
                bookId,
                memberId,
                dueDate
        );
        return new BorrowLoanResponse(
                loan.getId(),
                book.getId(),
                member.getId(),
                loan.getBorrowedAt(),
                loan.getDueDate()
        );
    }

    // ---------------- RETURN ----------------

    @Transactional
    public ReturnLoanResponse returnBook(Long bookId, Long memberId) {
        OffsetDateTime now = OffsetDateTime.now();
        log.info("Return request started: bookId={}, memberId={}", bookId, memberId);

        Loan loan = loanRepository.findActiveLoan(bookId, memberId)
                .orElseThrow(() -> {
                    log.warn(
                            "Return failed - active loan not found: bookId={}, memberId={}",
                            bookId,
                            memberId
                    );
                    return new IllegalArgumentException("Active loan not found");
                });

        // Mark returned
        loan.markReturned(now);

        // Increase available copies
        loan.getBook().returnOne();

        loanRepository.save(loan);

        log.info(
                "Return success: bookId={}, memberId={}, returnedAt={}",
                bookId,
                memberId,
                now
        );
        return new ReturnLoanResponse(
                loan.getId(),
                loan.getReturnedAt(),
                "RETURNED"
        );
    }
}
