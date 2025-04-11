package com.testtask.knigoed.service;

import com.testtask.knigoed.dto.BookFilterDto;
import com.testtask.knigoed.model.Book;
import com.testtask.knigoed.repository.BookRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Найти книгу по ID
     *
     * @param id UUID книги
     * @return Optional<Book> (может быть пустым, если книга не найдена)
     */
    public Optional<Book> findById(UUID id) {
        return bookRepository.findById(id);
    }

    /**
     * Добавить новую книгу
     *
     * @param book Данные книги
     * @return Сохраненная книга (с заполненным ID)
     */
    @Transactional
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Обновить существующую книгу
     *
     * @param id       ID книги для обновления
     * @param bookData Новые данные книги
     * @return Обновленная книга
     * @throws RuntimeException если книга не найдена
     */
    @Transactional
    public Book update(UUID id, Book bookData) {
        return bookRepository.findById(id).map(existingBook -> {
            if (bookData.getVendorCode() != null) {
                existingBook.setVendorCode(bookData.getVendorCode());
            }
            if (bookData.getTitle() != null) {
                existingBook.setTitle(bookData.getTitle());
            }
            if (bookData.getYear() != null) {
                existingBook.setYear(bookData.getYear());
            }
            if (bookData.getBrand() != null) {
                existingBook.setBrand(bookData.getBrand());
            }
            if (bookData.getStock() != null) {
                existingBook.setStock(bookData.getStock());
            }
            if (bookData.getPrice() != null) {
                existingBook.setPrice(bookData.getPrice());
            }
            return bookRepository.save(existingBook);
        }).orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    /**
     * Удалить книгу по ID
     *
     * @param id UUID книги для удаления
     */
    @Transactional
    public void delete(UUID id) {
        bookRepository.deleteById(id);
    }

    /**
     * Найти книги
     *
     * @param filter   объект для фильтрации
     * @param pageable объект для пагинации
     * @return Optional<Book> (может быть пустым, если книга не найдена)
     */
    public Page<Book> findBooks(BookFilterDto filter, Pageable pageable) {
        return bookRepository.findAll(buildSpecification(filter), pageable);
    }

    public Specification<Book> buildSpecification(BookFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getTitle() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + filter.getTitle().toLowerCase() + "%"));
            }

            if (filter.getBrand() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("brand"), filter.getBrand()));
            }

            if (filter.getYear() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("year"), filter.getYear()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}