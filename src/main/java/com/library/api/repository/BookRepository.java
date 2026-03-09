package com.library.api.repository;

import com.library.api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByIsbn(String isbn);

    // Поиск по автору без учета регистра
    List<Book> findByAuthorIgnoreCase(String author);

    // Поиск доступных книг
    List<Book> findByAvailableTrue();

    // Поиск недоступных книг
    List<Book> findByAvailableFalse();  // Добавлено

    // Поиск по названию, содержащему текст
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Проверка существования по ISBN
    boolean existsByIsbn(String isbn);

    // Поиск по году публикации
    List<Book> findByYear(Integer year);

    // Сложный запрос для статистики
    @Query("SELECT b.author, COUNT(b) FROM Book b GROUP BY b.author ORDER BY COUNT(b) DESC")
    List<Object[]> getBookCountByAuthor();  // Добавлено
}