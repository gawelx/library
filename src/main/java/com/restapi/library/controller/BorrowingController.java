package com.restapi.library.controller;

import com.restapi.library.domain.Book;
import com.restapi.library.domain.Borrower;
import com.restapi.library.domain.Borrowing;
import com.restapi.library.dto.BookDto;
import com.restapi.library.dto.BookTitleDto;
import com.restapi.library.dto.BorrowerDto;
import com.restapi.library.dto.BorrowingDto;
import com.restapi.library.dto.PenaltyDto;
import com.restapi.library.dto.PersonDto;
import com.restapi.library.service.AuthorService;
import com.restapi.library.service.BookService;
import com.restapi.library.service.BookTitleService;
import com.restapi.library.service.BorrowerService;
import com.restapi.library.service.BorrowingService;
import com.restapi.library.service.PenaltyService;
import com.restapi.library.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;
    private final BookService bookService;
    private final BorrowerService borrowerService;
    private final PersonService personService;
    private final BookTitleService bookTitleService;
    private final AuthorService authorService;
    private final PenaltyService penaltyService;

    @Autowired
    public BorrowingController(final BorrowingService borrowingService, final BookService bookService,
                               final BorrowerService borrowerService, final PersonService personService,
                               final BookTitleService bookTitleService, final AuthorService authorService,
                               final PenaltyService penaltyService) {
        this.borrowingService = borrowingService;
        this.bookService = bookService;
        this.borrowerService = borrowerService;
        this.personService = personService;
        this.bookTitleService = bookTitleService;
        this.authorService = authorService;
        this.penaltyService = penaltyService;
    }

    @GetMapping
    public List<BorrowingDto> getAllBorrowings(@RequestParam(defaultValue = "all") String category) {
        return borrowingService.getAllBorrowings(category).stream()
                .map(BorrowingDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BorrowingDto getBorrowing(@PathVariable Long id) {
        return new BorrowingDto(borrowingService.getBorrowing(id));
    }

    @GetMapping("/{id}/borrower")
    public BorrowerDto getBorrowingsBorrower(@PathVariable Long id) {
        Borrowing borrowing = borrowingService.getBorrowing(id);
        return new BorrowerDto(borrowerService.getBorrower(borrowing.getBorrower().getId()));
    }

    @GetMapping("/{id}/borrower/person")
    public PersonDto getBorrowingsPerson(@PathVariable Long id) {
        Borrowing borrowing = borrowingService.getBorrowing(id);
        return new PersonDto(personService.getPerson(borrowing.getBorrower().getPerson().getId()));
    }

    @GetMapping("{id}/book")
    public BookDto getBorrowingsBook(@PathVariable Long id) {
        Borrowing borrowing = borrowingService.getBorrowing(id);
        return new BookDto(bookService.getBook(borrowing.getBook().getId()));
    }

    @GetMapping("{id}/book/bookTitle")
    public BookTitleDto getBorrowingsBookTitle(@PathVariable Long id) {
        Borrowing borrowing = borrowingService.getBorrowing(id);
        return new BookTitleDto(bookTitleService.getBookTitle(borrowing.getBook().getBookTitle().getId()));
    }

    @GetMapping("{id}/book/bookTitle/authors")
    public List<PersonDto> getBorrowingsBookAuthors(@PathVariable Long id) {
        Borrowing borrowing = borrowingService.getBorrowing(id);
        return authorService.getAllAuthorsOfBookTitle(borrowing.getBook().getBookTitle().getId()).stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/penalties")
    public List<PenaltyDto> getBorrowingsPenalties(@PathVariable Long id) {
        return penaltyService.getAllPenaltiesOfBorrowing(id).stream()
                .map(PenaltyDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public BorrowingDto createBorrowing(@RequestBody BorrowingDto borrowingDto) {
        Borrower borrower = borrowerService.getBorrower(borrowingDto.getBorrowerId());
        Book book = bookService.getAvailableBook(borrowingDto.getBookId());
        return new BorrowingDto(borrowingService.createBorrowing(
                new Borrowing(borrowingDto, borrower, book, Collections.emptyList())
        ));
    }

    @PutMapping
    public BorrowingDto updateBorrowing(@RequestBody BorrowingDto borrowingDto, @RequestParam String action,
                                        @RequestParam(defaultValue = "none") String problemType) {
        Borrowing borrowing = new Borrowing(borrowingDto, null, null, null);
        return new BorrowingDto(
                borrowingService.updateBorrowing(borrowing, BorrowingAction.of(action), problemType)
        );
    }

}