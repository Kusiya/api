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
public class ReaderResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDateTime registrationDate;
    private Integer activeBorrowsCount;  // Количество книг на руках
}