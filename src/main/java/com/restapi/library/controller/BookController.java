package com.restapi.library.controller;

import com.restapi.library.domain.Book;
import com.restapi.library.domain.BookStatus;
import com.restapi.library.domain.BookTitle;
import com.restapi.library.dto.BookDto;
import com.restapi.library.dto.BookTitleDto;
import com.restapi.library.dto.BorrowingDto;
import com.restapi.library.dto.PersonDto;
import com.restapi.library.service.AuthorService;
import com.restapi.library.service.BookService;
import com.restapi.library.service.BookTitleService;
import com.restapi.library.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/books")
public class BookController {

    private final BookService bookService;
    private final BookTitleService bookTitleService;
    private final BorrowingService borrowingService;
    private final AuthorService authorService;

    @Autowired
    public BookController(final BookService bookService, final BookTitleService bookTitleService,
                          final BorrowingService borrowingService, final AuthorService authorService) {
        this.bookService = bookService;
        this.bookTitleService = bookTitleService;
        this.borrowingService = borrowingService;
        this.authorService = authorService;
    }

    @GetMapping
    public List<BookDto> getAllBooks(@RequestParam(required = false) String status) {
        BookStatus bookStatus = status == null ? null : BookStatus.of(status);
        return bookService.getAllBooks(bookStatus).stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable Long id) {
        return new BookDto(bookService.getBook(id));
    }

    @GetMapping("{id}/bookTitle")
    public BookTitleDto getBooksBookTitle(@PathVariable Long id) {
        Book book = bookService.getBook(id);
        return new BookTitleDto(bookTitleService.getBookTitle(book.getBookTitle().getId()));
    }

    @GetMapping("{id}/bookTitle/authors")
    public List<PersonDto> getBooksAuthors(@PathVariable Long id) {
        Book book = bookService.getBook(id);
        return authorService.getAllAuthorsOfBookTitle(book.getBookTitle().getId()).stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}/borrowings")
    public List<BorrowingDto> getBooksBorrowings(@PathVariable Long id) {
        return borrowingService.getBorrowingsOfBook(id).stream()
                .map(BorrowingDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public BookDto createBook(@RequestBody BookDto bookDto) {
        BookTitle bookTitle = bookTitleService.getBookTitle(bookDto.getBookTitleId());
        return new BookDto(bookService.createBook(new Book(bookDto, bookTitle)));
    }

    @PutMapping
    public BookDto updateBook(@RequestBody BookDto bookDto) {
        return new BookDto(bookService.updateBook(new Book(bookDto, null)));
    }

}
