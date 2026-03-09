package com.library.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder  // Паттерн Builder для удобного создания объектов
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer publicationYear;
    private String description;
    private Boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}