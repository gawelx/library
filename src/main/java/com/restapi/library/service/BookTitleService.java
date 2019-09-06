package com.restapi.library.service;

import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Person;
import com.restapi.library.exception.BadRequestException;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.BookTitleRepository;
import com.restapi.library.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookTitleService {

    private final BookTitleRepository bookTitleRepository;
    private final PersonRepository personRepository;

    @Autowired
    public BookTitleService(final BookTitleRepository bookTitleRepository, final PersonRepository personRepository) {
        this.bookTitleRepository = bookTitleRepository;
        this.personRepository = personRepository;
    }

    public List<BookTitle> getAllBookTitles() {
        return bookTitleRepository.findAll();
    }

    public BookTitle getBookTitle(final Long id) {
        return bookTitleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The book title with the id=" + id + " doesn't exist."));
    }

    public List<BookTitle> getAllBookTitlesOfAuthor(final Long authorId) {
        Person author = personRepository.findByIdAndBookTitlesNotEmpty(authorId)
                .orElseThrow(() -> new BadRequestException("The author with the id=" + authorId + " doesn't exist."));
        return bookTitleRepository.findAllByAuthorsContains(author);
    }

    public BookTitle createBookTitle(final BookTitle bookTitle) {
        bookTitle.clearId();
        return bookTitleRepository.save(bookTitle);
    }

    public BookTitle addAuthor(final BookTitle bookTitle, final Person author) {
        bookTitle.addAuthor(author);
        return bookTitleRepository.save(bookTitle);
    }

    public BookTitle updateBookTitle(final BookTitle bookTitle) {
        return bookTitleRepository.save(bookTitle);
    }

    public BookTitle removeAuthor(final BookTitle bookTitle, final Person author) {
        bookTitle.getAuthors().remove(author);
        return bookTitleRepository.save(bookTitle);
    }

}
