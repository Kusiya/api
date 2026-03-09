package com.library.api.service;

import com.library.api.dto.ReaderRequest;
import com.library.api.dto.ReaderResponse;
import com.library.api.model.Reader;
import com.library.api.repository.ReaderRepository;
import com.library.api.repository.BorrowRecordRepository;
import com.library.api.exception.ResourceNotFoundException;
import com.library.api.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    /**
     * Получить всех читателей
     */
    public List<ReaderResponse> getAllReaders() {
        log.info("Fetching all readers");
        return readerRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получить читателя по ID
     */
    public ReaderResponse getReaderById(Long id) {
        log.info("Fetching reader with id: {}", id);
        Reader reader = findReaderById(id);
        return convertToResponse(reader);
    }

    /**
     * Получить читателя по email
     */
    public ReaderResponse getReaderByEmail(String email) {
        log.info("Fetching reader with email: {}", email);
        Reader reader = readerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Reader not found with email: " + email));
        return convertToResponse(reader);
    }

    /**
     * Создать нового читателя
     */
    @Transactional
    public ReaderResponse createReader(ReaderRequest request) {
        log.info("Creating new reader with email: {}", request.getEmail());

        // Проверяем, нет ли читателя с таким email
        if (readerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Reader with email " + request.getEmail() + " already exists");
        }

        Reader reader = new Reader();
        reader.setFirstName(request.getFirstName());
        reader.setLastName(request.getLastName());
        reader.setEmail(request.getEmail());
        reader.setPhone(request.getPhone());
        reader.setRegistrationDate(LocalDateTime.now());

        Reader savedReader = readerRepository.save(reader);
        log.info("Reader created successfully with id: {}", savedReader.getId());

        return convertToResponse(savedReader);
    }

    /**
     * Обновить читателя
     */
    @Transactional
    public ReaderResponse updateReader(Long id, ReaderRequest request) {
        log.info("Updating reader with id: {}", id);

        Reader reader = findReaderById(id);

        // Проверяем email, если он меняется
        if (!reader.getEmail().equals(request.getEmail()) &&
                readerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Reader with email " + request.getEmail() + " already exists");
        }

        reader.setFirstName(request.getFirstName());
        reader.setLastName(request.getLastName());
        reader.setEmail(request.getEmail());
        reader.setPhone(request.getPhone());

        Reader updatedReader = readerRepository.save(reader);
        log.info("Reader updated successfully with id: {}", updatedReader.getId());

        return convertToResponse(updatedReader);
    }

    /**
     * Удалить читателя
     */
    @Transactional
    public void deleteReader(Long id) {
        log.info("Deleting reader with id: {}", id);

        Reader reader = findReaderById(id);

        // Проверяем, есть ли у читателя активные книги
        long activeBorrows = borrowRecordRepository.countActiveBorrowsByReaderId(id);
        if (activeBorrows > 0) {
            throw new IllegalStateException("Cannot delete reader with active borrows. Return books first.");
        }

        readerRepository.delete(reader);
        log.info("Reader deleted successfully with id: {}", id);
    }

    /**
     * Найти читателей с активными книгами
     */
    public List<ReaderResponse> getReadersWithActiveBorrows() {
        log.info("Fetching readers with active borrows");
        return readerRepository.findReadersWithActiveBorrows()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Конвертировать сущность Reader в DTO ReaderResponse
     */
    private ReaderResponse convertToResponse(Reader reader) {
        long activeBorrows = borrowRecordRepository.countActiveBorrowsByReaderId(reader.getId());

        return ReaderResponse.builder()
                .id(reader.getId())
                .firstName(reader.getFirstName())
                .lastName(reader.getLastName())
                .email(reader.getEmail())
                .phone(reader.getPhone())
                .registrationDate(reader.getRegistrationDate())
                .activeBorrowsCount((int) activeBorrows)
                .build();
    }

    /**
     * Вспомогательный метод для поиска читателя
     */
    private Reader findReaderById(Long id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reader not found with id: " + id));
    }
}
