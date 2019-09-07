package com.restapi.library.controller;

import com.restapi.library.domain.Book;
import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Person;
import com.restapi.library.dto.BookDto;
import com.restapi.library.dto.BookTitleDto;
import com.restapi.library.dto.PersonDto;
import com.restapi.library.service.AuthorService;
import com.restapi.library.service.BookService;
import com.restapi.library.service.BookTitleService;
import com.restapi.library.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/bookTitles")
public class BookTitleController {

    private final BookTitleService bookTitleService;
    private final BookService bookService;
    private final AuthorService authorService;
    private final PersonService personService;

    @Autowired
    public BookTitleController(final BookTitleService bookTitleService, final BookService bookService,
                               final AuthorService authorService, final PersonService personService) {
        this.bookTitleService = bookTitleService;
        this.bookService = bookService;
        this.authorService = authorService;
        this.personService = personService;
    }

    @GetMapping
    public List<BookTitleDto> getAllBookTitles() {
        return bookTitleService.getAllBookTitles().stream()
                .map(BookTitleDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookTitleDto getBookTitle(@PathVariable Long id) {
        return new BookTitleDto(bookTitleService.getBookTitle(id));
    }

    @GetMapping("/{id}/authors")
    public List<PersonDto> getBookTitlesAuthors(@PathVariable Long id) {
        return authorService.getAllAuthorsOfBookTitle(id).stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/books")
    public List<BookDto> getBookTitlesBooks(@PathVariable Long id) {
        return bookService.getAllBooksOfBookTitle(id).stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public BookTitleDto createBookTitle(@RequestBody BookTitleDto bookTitleDto) {
        BookTitle bookTitle = new BookTitle(bookTitleDto, Collections.emptySet(), Collections.emptyList());
        return new BookTitleDto(bookTitleService.createBookTitle(bookTitle));
    }

    @PostMapping("{id}/authors/{authorId}")
    public List<PersonDto> addAuthor(@PathVariable Long id, @PathVariable Long authorId) {
        BookTitle bookTitle = bookTitleService.getBookTitle(id);
        Person author = personService.getPerson(authorId);
        return bookTitleService.addAuthor(bookTitle, author).getAuthors().stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }

    @PutMapping
    public BookTitleDto updateBookTitle(@RequestBody BookTitleDto bookTitleDto) {
        Set<Person> authors = authorService.getAllAuthorsOfBookTitle(bookTitleDto.getId());
        List<Book> books = bookService.getAllBooksOfBookTitle(bookTitleDto.getId());
        BookTitle bookTitle = new BookTitle(bookTitleDto, authors, books);
        return new BookTitleDto(bookTitleService.updateBookTitle(bookTitle));
    }

    @DeleteMapping("{id}/authors/{authorId}")
    public List<PersonDto> removeAuthor(@PathVariable Long id, @PathVariable Long authorId) {
        BookTitle bookTitle = bookTitleService.getBookTitle(id);
        Person author = authorService.getAuthor(authorId);
        return bookTitleService.removeAuthor(bookTitle, author).getAuthors().stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }

}
