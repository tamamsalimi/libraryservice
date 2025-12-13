package com.library.managementservice.service;

import com.library.managementservice.api.dto.BookResponse;
import com.library.managementservice.api.dto.CreateBookRequest;
import com.library.managementservice.entity.Book;
import com.library.managementservice.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BookResponse create(CreateBookRequest request) {
        Book book = new Book(
                request.title(),
                request.author(),
                request.isbn(),
                request.totalCopies()
        );
        if (repository.existsByIsbn(request.isbn())) {
            throw new IllegalStateException("ISBN already exists");
        }
        repository.save(book);
        return toResponse(book);
    }

    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        return toResponse(book);
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getTotalCopies(),
                book.getAvailableCopies()
        );
    }
}
