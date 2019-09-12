package com.restapi.library.service;

import com.restapi.library.domain.Book;
import com.restapi.library.domain.BookStatus;
import com.restapi.library.domain.Person;
import com.restapi.library.exception.ConflictException;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.BookRepository;
import com.restapi.library.repository.BookTitleRepository;
import com.restapi.library.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.restapi.library.domain.BookStatus.AVAILABLE;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookTitleRepository bookTitleRepository;
    private final PersonRepository personRepository;

    @Autowired
    public BookService(final BookRepository bookRepository, final BookTitleRepository bookTitleRepository,
                       final PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.bookTitleRepository = bookTitleRepository;
        this.personRepository = personRepository;
    }

    public List<Book> getAllBooks(final BookStatus status) {
        if (status == null) {
            return bookRepository.findAll();
        }
        return bookRepository.findAllByStatus(status);
    }

    public Book getBook(final Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The book with the id=" + id + " doesn't exist."));
    }

    public Book getAvailableBook(final Long id) {
        return bookRepository.findBookByIdAndStatus(id, AVAILABLE)
                .orElseThrow(() -> new ConflictException("The book with the id=" + id + " is not available."));
    }

    public List<Book> getAllBooksOfBookTitle(final Long bookTitleId) {
        if (!bookTitleRepository.existsById(bookTitleId)) {
            throw new NotFoundException("The book title with the id=" + bookTitleId + " doesn't exist.");
        }
        return bookRepository.findAllByBookTitleId(bookTitleId);
    }

    public List<Book> getAllBooksOfAuthor(final Long authorId) {
        Person author = personRepository.findByIdAndBookTitlesNotEmpty(authorId)
                .orElseThrow(() -> new NotFoundException("The author with the id=" + authorId + " doesn't exist."));
        return bookRepository.findAllByBookTitleAuthorsContains(author);
    }

    public Book createBook(final Book book) {
        book.clearId();
        book.setStatus(AVAILABLE);
        return bookRepository.save(book);
    }

    public Book updateBook(final Book book) {
        Book persistedBook = bookRepository.findById(book.getId())
                .orElseThrow(() -> new NotFoundException("The book with the id=" + book.getId() + " doesn't exist."));
        if (book.getStatus() != null) {
            persistedBook.setStatus(book.getStatus());
        }
        if (book.getPrice() != null) {
            persistedBook.setPrice(book.getPrice());
        }
        if (book.getReleaseYear() != null) {
            persistedBook.setReleaseYear(book.getReleaseYear());
        }
        return bookRepository.save(persistedBook);
    }

}
