package com.library.api.repository;

import com.library.api.model.BorrowRecord;
import com.library.api.model.BorrowRecord.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    // Найти активные записи для книги
    List<BorrowRecord> findByBookIdAndStatus(Long bookId, BorrowStatus status);

    // Найти все записи для читателя
    List<BorrowRecord> findByReaderId(Long readerId);

    // Найти активные записи для читателя
    List<BorrowRecord> findByReaderIdAndStatus(Long readerId, BorrowStatus status);

    // Найти просроченные записи
    @Query("SELECT br FROM BorrowRecord br WHERE br.status = 'ACTIVE' AND br.dueDate < :currentDate")
    List<BorrowRecord> findOverdueRecords(@Param("currentDate") LocalDateTime currentDate);

    // Проверить, есть ли у читателя активная запись на книгу
    boolean existsByBookIdAndReaderIdAndStatus(Long bookId, Long readerId, BorrowStatus status);

    // Получить количество активных записей для читателя
    @Query("SELECT COUNT(br) FROM BorrowRecord br WHERE br.reader.id = :readerId AND br.status = 'ACTIVE'")
    long countActiveBorrowsByReaderId(@Param("readerId") Long readerId);

    // Обновить статус просроченных записей
    @Modifying
    @Query("UPDATE BorrowRecord br SET br.status = 'OVERDUE' WHERE br.status = 'ACTIVE' AND br.dueDate < :currentDate")
    int updateOverdueStatus(@Param("currentDate") LocalDateTime currentDate);
}
