package com.library.api.controller;  // ВАЖНО: правильный пакет

import com.library.api.dto.BookRequest;
import com.library.api.dto.BookResponse;
import com.library.api.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // ЭТА АННОТАЦИЯ ОБЯЗАТЕЛЬНА!
@RequestMapping("/api/books")  // ЭТА АННОТАЦИЯ ТОЖЕ ОБЯЗАТЕЛЬНА!
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        log.info("REST request to get all books");
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        log.info("REST request to get book with id: {}", id);
        BookResponse book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookResponse>> getAvailableBooks() {
        log.info("REST request to get available books");
        List<BookResponse> books = bookService.getAvailableBooks();
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        log.info("REST request to create new book");
        BookResponse createdBook = bookService.createBook(request);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }
}
