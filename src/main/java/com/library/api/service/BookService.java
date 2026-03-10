package com.library.api.service;

import com.library.api.dto.BookRequest;
import com.library.api.dto.BookResponse;
import com.library.api.model.Book;
import com.library.api.repository.BookRepository;
import com.library.api.exception.ResourceNotFoundException;
import com.library.api.exception.BadRequestException;
import com.library.api.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service  // Отмечает, что это сервисный компонент Spring
@RequiredArgsConstructor  // Lombok: создает конструктор для всех final полей
@Slf4j  // Lombok: добавляет логгер (log.info, log.error)
@Transactional(readOnly = true)  // Все методы по умолчанию только для чтения
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Получить все книги
     */
    public List<BookResponse> getAllBooks() {
        log.info("Fetching all books from database");
        return bookRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получить книгу по ID
     */
    public BookResponse getBookById(Long id) {
        log.info("Fetching book with id: {}", id);
        Book book = findBookById(id);
        return convertToResponse(book);
    }

    /**
     * Получить книгу по ISBN
     */
    public BookResponse getBookByIsbn(String isbn) {
        log.info("Fetching book with ISBN: {}", isbn);
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new ResourceNotFoundException("Book not found with ISBN: " + isbn);
        }
        return convertToResponse(book);
    }

    /**
     * Создать новую книгу
     */
    @Transactional  // Этот метод изменяет данные, поэтому не readOnly
    public BookResponse createBook(BookRequest request) {
        log.info("Creating new book with ISBN: {}", request.getIsbn());

        // Проверяем, нет ли книги с таким ISBN
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Book with ISBN " + request.getIsbn() + " already exists");
        }

        // Создаем новую книгу
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setYear(request.getYear());
        book.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);

        // Сохраняем в базу
        Book savedBook = bookRepository.save(book);
        log.info("Book created successfully with id: {}", savedBook.getId());

        return convertToResponse(savedBook);
    }

    /**
     * Обновить существующую книгу
     */
    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        log.info("Updating book with id: {}", id);

        // Находим книгу
        Book book = findBookById(id);

        // Проверяем ISBN, если он меняется
        if (!book.getIsbn().equals(request.getIsbn()) &&
                bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Book with ISBN " + request.getIsbn() + " already exists");
        }

        // Обновляем поля
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setYear(request.getYear());
        book.setAvailable(request.getAvailable() != null ? request.getAvailable() : book.getAvailable());

        // Сохраняем изменения
        Book updatedBook = bookRepository.save(book);
        log.info("Book updated successfully with id: {}", updatedBook.getId());

        return convertToResponse(updatedBook);
    }

    /**
     * Удалить книгу
     */
    @Transactional
    public void deleteBook(Long id) {
        log.info("Deleting book with id: {}", id);

        Book book = findBookById(id);
        bookRepository.delete(book);

        log.info("Book deleted successfully with id: {}", id);
    }

    /**
     * Поиск книг по автору
     */
    public List<BookResponse> getBooksByAuthor(String author) {
        log.info("Searching books by author: {}", author);
        return bookRepository.findByAuthorIgnoreCase(author)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Поиск книг по названию
     */
    public List<BookResponse> searchBooksByTitle(String title) {
        log.info("Searching books by title containing: {}", title);
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получить доступные книги
     */
    public List<BookResponse> getAvailableBooks() {
        log.info("Fetching available books");
        return bookRepository.findByAvailableTrue()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получить недоступные книги
     */
    public List<BookResponse> getUnavailableBooks() {
        log.info("Fetching unavailable books");
        return bookRepository.findByAvailableFalse()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Выдать книгу читателю (обновляет статус available)
     */
    @Transactional
    public BookResponse borrowBook(Long id) {
        log.info("Borrowing book with id: {}", id);

        Book book = findBookById(id);

        if (!book.getAvailable()) {
            throw new BadRequestException("Book with id " + id + " is already borrowed");
        }

        book.setAvailable(false);
        Book updatedBook = bookRepository.save(book);
        log.info("Book with id {} marked as borrowed", id);

        return convertToResponse(updatedBook);
    }

    /**
     * Вернуть книгу (обновляет статус available)
     */
    @Transactional
    public BookResponse returnBook(Long id) {
        log.info("Returning book with id: {}", id);

        Book book = findBookById(id);

        if (book.getAvailable()) {
            throw new BadRequestException("Book with id " + id + " is already available");
        }

        book.setAvailable(true);
        Book updatedBook = bookRepository.save(book);
        log.info("Book with id {} marked as returned", id);

        return convertToResponse(updatedBook);
    }

    /**
     * Получить статистику по книгам
     */
    public List<Object[]> getBooksStatistics() {
        log.info("Fetching books statistics");
        return bookRepository.getBookCountByAuthor();
    }

    /**
     * Конвертировать сущность Book в DTO BookResponse
     */
    private BookResponse convertToResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .year(book.getYear())
                .available(book.getAvailable())
                .createdAt(book.getCreatedAt().atStartOfDay())
                .updatedAt(book.getUpdatedAt().atStartOfDay())
                .build();
    }

    /**
     * Вспомогательный метод для поиска книги с обработкой ошибки
     */
    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
}