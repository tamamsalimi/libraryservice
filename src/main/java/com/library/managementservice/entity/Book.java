package com.library.managementservice.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "books",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_books_isbn", columnNames = "isbn")
        },
        indexes = {
                @Index(name = "idx_books_title", columnList = "title"),
                @Index(name = "idx_books_author", columnList = "author")
        }
)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(length = 120, nullable = false)
    private String author;

    @Column(length = 20, nullable = false)
    private String isbn;

    @Column(name = "total_copies", nullable = false)
    private int totalCopies;

    @Column(name = "available_copies", nullable = false)
    private int availableCopies;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Book() {
        // JPA only
    }

    public Book(String title, String author, String isbn, int totalCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    // -------- getters --------

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    // -------- domain behavior --------

    public void borrowOne() {
        if (availableCopies <= 0) {
            throw new IllegalStateException("No available copies");
        }
        this.availableCopies--;
    }

    public void returnOne() {
        if (availableCopies < totalCopies) {
            this.availableCopies++;
        }
    }

    public void update(String title, String author, int totalCopies) {
        if (totalCopies < this.totalCopies - this.availableCopies) {
            throw new IllegalStateException("Total copies cannot be less than borrowed copies");
        }

        this.title = title;
        this.author = author;

        int borrowed = this.totalCopies - this.availableCopies;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies - borrowed;
    }

}
