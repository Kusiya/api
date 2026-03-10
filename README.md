# Library API

RESTful API для управления библиотекой. Проект разработан на Java Spring Boot для демонстрации навыков backend-разработки.

## Технологический стек

- **Java 17** - язык программирования
- **Spring Boot 3** - основной фреймворк
- **Spring Data JPA** - работа с базой данных
- **PostgreSQL** - база данных
- **Lombok** - уменьшение шаблонного кода
- **Maven** - сборка проекта
- **REST API** - архитектурный стиль

## Функциональность

### Книги (Books)
- Получение списка всех книг
- Получение книги по ID
- Получение книги по ISBN
- Поиск книг по автору
- Поиск книг по названию
- Фильтрация доступных/недоступных книг
- Создание новой книги
- Обновление информации о книге
- Удаление книги
- Выдача книги читателю
- Возврат книги

### Читатели (Readers)
- Регистрация читателя
- Получение списка читателей
- Получение информации о читателе
- Обновление данных читателя
- Удаление читателя

### Выдача книг (Borrow Records) - в разработке
- История выдач
- Отслеживание просрочек
- Статистика

## Установка и запуск

### Требования
- JDK 17 или выше
- PostgreSQL 12 или выше
- Maven 3.6 или выше
- Git (опционально)

### Пошаговая инструкция

1. **Клонировать репозиторий**
   ```bash
   git clone https://github.com/Kusiya/api.git
2. **Создать базу данных в PostgreSQL**
   ```sql
   CREATE DATABASE library_bd;
   ```
   В пакете config находится класс DataInitializer, который заполняет БД, если она пустая.
   Необходимо создать в PostgreSQL таблицы books, readers, borrow_records (обязательно в БД library_bd):
   ```sql
    CREATE TABLE books(
	id SERIAL PRIMARY KEY,
	title VARCHAR(100) NOT NULL,
	author VARCHAR(100) NOT NULL,
	isbn VARCHAR(20) UNIQUE NOT NULL,
	year INTEGER NOT NULL,
	available BOOLEAN DEFAULT TRUE
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
   );
   
   CREATE TABLE readers (
   id SERIAL PRIMARY KEY,
   first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    registration_date DATE NOT NULL DEFAULT CURRENT_DATE,
    is_active BOOLEAN DEFAULT TRUE, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
   );
   
   CREATE TABLE borrow_records (
	  id SERIAL PRIMARY KEY,
    book_id INTEGER NOT NULL,
    reader_id INTEGER NOT NULL,
    borrow_date DATE NOT NULL DEFAULT CURRENT_DATE,
    due_date DATE NOT NULL DEFAULT (CURRENT_DATE + INTERVAL '14 days'),
    return_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    fine_amount DECIMAL(10, 2) DEFAULT 0.00,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_borrow_records_book 
        FOREIGN KEY (book_id) 
        REFERENCES books(id) 
        ON DELETE RESTRICT,
    CONSTRAINT fk_borrow_records_reader 
        FOREIGN KEY (reader_id) 
        REFERENCES readers(id) 
        ON DELETE RESTRICT,
    CONSTRAINT valid_status 
        CHECK (status IN ('ACTIVE', 'RETURNED', 'OVERDUE')),
    CONSTRAINT valid_return_date 
        CHECK (return_date IS NULL OR return_date >= borrow_date),
    CONSTRAINT valid_fine 
        CHECK (fine_amount >= 0)
        );
      
4. **Настроить подключение к БД**

   Откройте файл src/main/resources/application.properties и укажите свой пароль:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/library_db
   spring.datasource.username=postgres
   spring.datasource.password=your_password_here
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
6. **Собрать проект**
   ```bash
   mvn clean install
8. **Запустить приложение**
   ```bash
   mvn spring-boot:run
    ```
   Или запустите через IntelliJ IDEA:
   Откройте класс LibraryApiApplication.java ->
   Нажмите зеленую стрелку;
10. **Проверить работу**
    Откройте браузер и перейдите по адресу:
    ```text
    http://localhost:8080/api/books //получить все книги
    http://localhost:8080/api/readers //получить всех читателей
    http://localhost:8080/api/books/1/borrow  //выдать книгу(в разработке)
    http://localhost:8080/api/books/1/return //вернуть книгу(в разработке)
 11. **Контакты**
     Автор: [Гальцова Виктория]
     Email: [kerrin2025@gmail.com]
     GitHub: @Kusiya
  
