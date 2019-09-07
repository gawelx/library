package com.restapi.library.controller;

import com.restapi.library.dto.BookDto;
import com.restapi.library.dto.BookTitleDto;
import com.restapi.library.dto.PersonDto;
import com.restapi.library.service.AuthorService;
import com.restapi.library.service.BookService;
import com.restapi.library.service.BookTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookTitleService bookTitleService;
    private final BookService bookService;

    @Autowired
    public AuthorController(final AuthorService authorService, final BookTitleService bookTitleService,
                            final BookService bookService) {
        this.authorService = authorService;
        this.bookTitleService = bookTitleService;
        this.bookService = bookService;
    }

    @GetMapping
    public List<PersonDto> getAllAuthors() {
        return authorService.getAllAuthors().stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDto getAuthor(@PathVariable Long id) {
        return new PersonDto(authorService.getAuthor(id));
    }

    @GetMapping("/{id}/bookTitles")
    public List<BookTitleDto> getAuthorsBookTitles(@PathVariable Long id) {
        return bookTitleService.getAllBookTitlesOfAuthor(id).stream()
                .map(BookTitleDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/books")
    public List<BookDto> getAuthorsBooks(@PathVariable Long id) {
        return bookService.getAllBooksOfAuthor(id).stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

}
