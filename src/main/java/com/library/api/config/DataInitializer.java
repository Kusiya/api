package com.library.api.config;

import com.library.api.model.Book;
import com.library.api.model.Reader;
import com.library.api.repository.BookRepository;
import com.library.api.repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component  // Spring-компонент
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing test data...");

        // Добавляем книги, только если база пуста
        if (bookRepository.count() == 0) {
            log.info("Adding sample books...");

            Book book1 = new Book();
            book1.setTitle("Война и мир");
            book1.setAuthor("Лев Толстой");
            book1.setIsbn("978-5-17-090501-4");
            book1.setYear(1869);
            book1.setAvailable(true);
            bookRepository.save(book1);

            Book book2 = new Book();
            book2.setTitle("Преступление и наказание");
            book2.setAuthor("Фёдор Достоевский");
            book2.setIsbn("978-5-17-090501-1");
            book2.setYear(1866);
            book2.setAvailable(true);
            bookRepository.save(book2);

            Book book3 = new Book();
            book3.setTitle("Мастер и Маргарита");
            book3.setAuthor("Михаил Булгаков");
            book3.setIsbn("978-5-17-090501-8");
            book3.setYear(1967);
            book3.setAvailable(true);
            bookRepository.save(book3);

            log.info("Sample books added");
        }

        // Добавляем читателей
        if (readerRepository.count() == 0) {
            log.info("Adding sample readers...");

            Reader reader1 = new Reader();
            reader1.setFirstName("Иван");
            reader1.setLastName("Петров");
            reader1.setEmail("ivan.petr@email.com");
            reader1.setPhone("+79161232772");
            reader1.setRegistrationDate(LocalDateTime.now());
            readerRepository.save(reader1);

            Reader reader2 = new Reader();
            reader2.setFirstName("Мария");
            reader2.setLastName("Иванова");
            reader2.setEmail("maria.ivan@email.com");
            reader2.setPhone("+79167654884");
            reader2.setRegistrationDate(LocalDateTime.now());
            readerRepository.save(reader2);

            log.info("Sample readers added");
        }

        log.info("Data initialization completed");
    }
}
