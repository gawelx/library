package com.restapi.library.service;

import com.restapi.library.domain.Book;
import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Borrower;
import com.restapi.library.domain.Borrowing;
import com.restapi.library.domain.Penalty;
import com.restapi.library.domain.Person;

import java.util.stream.Collectors;

public class CopyService {

    public static Penalty copyPenalty(final Penalty penalty) {
        return new Penalty(
                penalty.getId(),
                penalty.getCreationTime(),
                penalty.getPenaltyCause(),
                penalty.getPenaltyFee(),
                penalty.getPaid(),
                copyBorrowing(penalty.getBorrowing())
        );
    }

    public static Borrowing copyBorrowing(final Borrowing borrowing) {
        return new Borrowing(
                borrowing.getId(),
                borrowing.getBorrowingDate(),
                borrowing.getBorrowingPeriod(),
                borrowing.getReturnDate(),
                copyBorrower(borrowing.getBorrower()),
                copyBook(borrowing.getBook())
        );
    }

    public static Borrower copyBorrower(final Borrower borrower) {
        return new Borrower(
                borrower.getId(),
                borrower.getStatus(),
                borrower.getAccountCreationDateTime(),
                copyPerson(borrower.getPerson())
        );
    }

    public static Book copyBook(final Book book) {
        return new Book(
                book.getId(),
                book.getReleaseYear(),
                book.getPrice(),
                book.getStatus(),
                copyBookTitle(book.getBookTitle())
        );
    }

    public static Person copyPerson(final Person person) {
        return new Person(
                person.getId(),
                person.getStatus(),
                person.getFirstName(),
                person.getLastName()
        );
    }

    public static BookTitle copyBookTitle(final BookTitle bookTitle) {
        return new BookTitle(
                bookTitle.getId(),
                bookTitle.getTitle(),
                bookTitle.getAuthors().stream()
                        .map(CopyService::copyPerson)
                        .collect(Collectors.toSet())
        );
    }


}
