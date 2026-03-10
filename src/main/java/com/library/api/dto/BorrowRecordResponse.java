package com.library.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecordResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long readerId;
    private String readerName;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private String status;
    private Boolean isOverdue;
}