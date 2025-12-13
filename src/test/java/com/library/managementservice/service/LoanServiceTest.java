package com.library.managementservice.service;

import com.library.managementservice.config.LibraryRulesConfig;
import com.library.managementservice.entity.Book;
import com.library.managementservice.entity.Loan;
import com.library.managementservice.entity.Member;
import com.library.managementservice.repository.BookRepository;
import com.library.managementservice.repository.LoanRepository;
import com.library.managementservice.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    private BookRepository bookRepository;
    private MemberRepository memberRepository;
    private LoanRepository loanRepository;
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        memberRepository = mock(MemberRepository.class);
        loanRepository = mock(LoanRepository.class);

        LibraryRulesConfig rules = new LibraryRulesConfig();
        rules.setMaxActiveLoansPerMember(2);
        rules.setMaxLoanDurationDays(14);

        loanService = new LoanService(
                bookRepository,
                memberRepository,
                loanRepository,
                rules
        );
    }

    // 1️⃣ Rule: max active loans
    @Test
    void borrowBook_shouldFail_whenMaxActiveLoansExceeded() {
        Book book = new Book("Clean Code", "Robert", "ISBN1", 3);
        Member member = new Member("John", "john@mail.com");

        when(bookRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(loanRepository.countActiveLoans(1L)).thenReturn(2L);

        assertThrows(
                IllegalStateException.class,
                () -> loanService.borrowBook(1L, 1L)
        );
    }

    // 2️⃣ Rule: overdue loan blocks borrowing
    @Test
    void borrowBook_shouldFail_whenMemberHasOverdueLoan() {
        Book book = new Book("Clean Architecture", "Robert", "ISBN2", 2);
        Member member = new Member("Jane", "jane@mail.com");

        when(bookRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(loanRepository.countActiveLoans(1L)).thenReturn(0L);
        when(loanRepository.countOverdueLoans(anyLong(), any(OffsetDateTime.class)))
                .thenReturn(1L);

        assertThrows(
                IllegalStateException.class,
                () -> loanService.borrowBook(1L, 1L)
        );
    }

    // 3️⃣ Happy path
    @Test
    void borrowBook_shouldSucceed_whenRulesSatisfied() {
        Book book = new Book("DDD", "Evans", "ISBN3", 1);
        Member member = new Member("Alex", "alex@mail.com");

        when(bookRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(loanRepository.countActiveLoans(1L)).thenReturn(0L);

        loanService.borrowBook(1L, 1L);

        assertEquals(0, book.getAvailableCopies());
        verify(loanRepository).save(any(Loan.class));
    }
}
