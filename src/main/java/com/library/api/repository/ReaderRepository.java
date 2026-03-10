package com.library.api.repository;

import com.library.api.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {

    // Поиск по email (точное совпадение)
    Optional<Reader> findByEmail(String email);

    // Проверка существования по email
    boolean existsByEmail(String email);

    // Поиск по фамилии
    List<Reader> findByLastNameIgnoreCase(String lastName);

    // Поиск по имени и фамилии
    List<Reader> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    // Поиск читателей с активными книгами
    @Query("SELECT DISTINCT r FROM Reader r JOIN r.borrowRecords br WHERE br.status = 'ACTIVE'")
    List<Reader> findReadersWithActiveBorrows();

    // Поиск читателей с просроченными книгами
    @Query("SELECT DISTINCT r FROM Reader r JOIN r.borrowRecords br WHERE br.status = 'OVERDUE'")
    List<Reader> findReadersWithOverdueBooks();

    // Поиск читателей, зарегистрированных после определенной даты
    @Query("SELECT r FROM Reader r WHERE r.registrationDate > :date")
    List<Reader> findReadersRegisteredAfter(@Param("date") java.time.LocalDateTime date);
}
