package com.library.api.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReaderRequest {

    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 2, max = 100, message = "Фамилия должна быть от 2 до 100 символов")
    private String lastName;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Неверный формат телефона")
    private String phone;
}
