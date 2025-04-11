package com.testtask.knigoed.controller;

import com.testtask.knigoed.dto.BookFilterDto;
import com.testtask.knigoed.model.Book;
import com.testtask.knigoed.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookViewController {
    private final BookService bookService;

    @GetMapping
    public String showBooksPage(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            Model model) {

        BookFilterDto filter = BookFilterDto.builder()
                .title(title)
                .brand(brand)
                .year(year)
                .build();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Book> booksPage = bookService.findBooks(filter, pageable);

        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("totalPages", booksPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "books";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        model.addAttribute("book", book);
        return "edit-book";
    }

}