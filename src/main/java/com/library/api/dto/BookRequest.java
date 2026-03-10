package com.library.api.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {

    @NotBlank(message = "Название книги обязательно")
    @Size(min = 1, max = 255, message = "Название должно быть от 1 до 255 символов")
    private String title;

    @NotBlank(message = "Автор обязателен")
    @Size(min = 2, max = 255, message = "Имя автора должно быть от 2 до 255 символов")
    private String author;

    @NotBlank(message = "ISBN обязателен")
    @Pattern(
            regexp = "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$",
            message = "Неверный формат ISBN. Ожидается ISBN-13"
    )
    private String isbn;

    @Min(value = 1450, message = "Год не может быть раньше 1450 (изобретение книгопечатания)")
    @Max(value = 2026, message = "Год не может быть в будущем")
    private Integer year;

    private Boolean available = true;
}
