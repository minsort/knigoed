package com.testtask.knigoed.controller;

import com.testtask.knigoed.model.Book;
import com.testtask.knigoed.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable UUID id) {
        log.info("Получение книги по ID: {}", id);
        Optional<Book> book = bookService.findById(id);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        log.info("Создание новой книги: {}", book);
        Book createdBook = bookService.create(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable UUID id, @RequestBody Book book) {
        log.info("Обновление книги с ID {}: {}", id, book);
        try {
            Book updatedBook = bookService.update(id, book);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            log.error("Ошибка при обновлении книги: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        log.info("Удаление книги с ID: {}", id);
        try {
            bookService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Ошибка при удалении книги: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
