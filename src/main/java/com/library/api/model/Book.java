package com.library.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity //JPA сущность
@Table(name = "books") // Имя таблицы в БД
@Data // get, set, toString, equals, hashCode
@NoArgsConstructor //генерирует конструктор без аргументов
@AllArgsConstructor //генерирует конструктор с всеми аргументами
public class Book {

    @Id // Первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Автоинкремент
    private Long id;

    //Настройка полей
    @Column(name = "title", nullable = false) //not null
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "available")
    private Boolean available = true;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    /*
     Вызывается перед сохранением сущности в базу данных
     Устанавливает даты создания и обновления
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    /*
     Вызывается перед обновлением сущности
     Обновляет дату последнего изменения
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}
