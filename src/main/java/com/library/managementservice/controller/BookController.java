package com.library.managementservice.controller;

import com.library.managementservice.api.dto.BookResponse;
import com.library.managementservice.api.dto.CreateBookRequest;
import com.library.managementservice.service.BookService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@RequestBody @Valid CreateBookRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return service.getById(id);
    }
}
