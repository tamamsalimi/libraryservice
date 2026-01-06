package com.library.managementservice.repository;

import com.library.managementservice.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("""
        select count(l)
        from Loan l
        where l.member.id = :memberId
          and l.returnedAt is null
    """)
    long countActiveLoans(@Param("memberId") Long memberId);

    @Query("""
        select count(l)
        from Loan l
        where l.member.id = :memberId
          and l.returnedAt is null
          and l.dueDate < :now
    """)
    long countOverdueLoans(
        @Param("memberId") Long memberId,
        @Param("now") OffsetDateTime now
    );

    @Query("""
        select l
        from Loan l
        where l.book.id = :bookId
          and l.member.id = :memberId
          and l.returnedAt is null
    """)
    Optional<Loan> findActiveLoan(
        @Param("bookId") Long bookId,
        @Param("memberId") Long memberId
    );

    @Query("""
        select l
        from Loan l
        where l.member.id = :memberId
          and l.returnedAt is null
    """)
    List<Loan> findActiveLoansByMember(@Param("memberId") Long memberId);

    @Query("""
    select l
    from Loan l
    where l.member.id = :memberId
    order by l.borrowedAt desc
""")
    List<Loan> findLoanHistoryByMember(@Param("memberId") Long memberId);

}
