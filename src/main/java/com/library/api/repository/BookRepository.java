package com.library.api.repository;

import com.library.api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);
    List<Book> findByAvailableTrue();
    List<Book> findByTitleContainingIgnoreCase(String title);
    boolean existsByIsbn(String isbn);

}
