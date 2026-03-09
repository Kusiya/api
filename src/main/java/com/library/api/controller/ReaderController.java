package com.library.api.controller;

import com.library.api.dto.ReaderRequest;
import com.library.api.dto.ReaderResponse;
import com.library.api.service.ReaderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ReaderController {

    private final ReaderService readerService;

    @GetMapping
    public ResponseEntity<List<ReaderResponse>> getAllReaders() {
        log.info("REST request to get all readers");
        List<ReaderResponse> readers = readerService.getAllReaders();
        return ResponseEntity.ok(readers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderResponse> getReaderById(@PathVariable Long id) {
        log.info("REST request to get reader with id: {}", id);
        ReaderResponse reader = readerService.getReaderById(id);
        return ResponseEntity.ok(reader);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ReaderResponse> getReaderByEmail(@PathVariable String email) {
        log.info("REST request to get reader with email: {}", email);
        ReaderResponse reader = readerService.getReaderByEmail(email);
        return ResponseEntity.ok(reader);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ReaderResponse>> getReadersWithActiveBorrows() {
        log.info("REST request to get readers with active borrows");
        List<ReaderResponse> readers = readerService.getReadersWithActiveBorrows();
        return ResponseEntity.ok(readers);
    }

    @PostMapping
    public ResponseEntity<ReaderResponse> createReader(@Valid @RequestBody ReaderRequest request) {
        log.info("REST request to create new reader: {}", request);
        ReaderResponse createdReader = readerService.createReader(request);
        return new ResponseEntity<>(createdReader, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReaderResponse> updateReader(
            @PathVariable Long id,
            @Valid @RequestBody ReaderRequest request) {
        log.info("REST request to update reader with id: {}", id);
        ReaderResponse updatedReader = readerService.updateReader(id, request);
        return ResponseEntity.ok(updatedReader);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) {
        log.info("REST request to delete reader with id: {}", id);
        readerService.deleteReader(id);
        return ResponseEntity.noContent().build();
    }
}