package com.library.managementservice.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(
    name = "loans",
    indexes = {
        @Index(
            name = "idx_loans_member_active",
            columnList = "member_id",
            unique = false
        ),
        @Index(
            name = "idx_loans_book_active",
            columnList = "book_id",
            unique = false
        )
    }
)
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "borrowed_at", nullable = false)
    private OffsetDateTime borrowedAt;

    @Column(name = "due_date", nullable = false)
    private OffsetDateTime dueDate;

    @Column(name = "returned_at")
    private OffsetDateTime returnedAt;

    protected Loan() {
        // JPA only
    }

    public Loan(Book book, Member member, OffsetDateTime borrowedAt, OffsetDateTime dueDate) {
        this.book = book;
        this.member = member;
        this.borrowedAt = borrowedAt;
        this.dueDate = dueDate;
    }

    // -------- getters --------

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public OffsetDateTime getBorrowedAt() {
        return borrowedAt;
    }

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public OffsetDateTime getReturnedAt() {
        return returnedAt;
    }

    // -------- domain helpers --------

    public boolean isReturned() {
        return returnedAt != null;
    }

    public boolean isOverdue(OffsetDateTime now) {
        return returnedAt == null && dueDate.isBefore(now);
    }

    public void markReturned(OffsetDateTime returnedAt) {
        if (this.returnedAt != null) {
            throw new IllegalStateException("Loan already returned");
        }
        this.returnedAt = returnedAt;
    }
}
