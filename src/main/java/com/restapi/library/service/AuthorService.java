package com.restapi.library.service;

import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Person;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.BookTitleRepository;
import com.restapi.library.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthorService {

    private final PersonRepository personRepository;
    private final BookTitleRepository bookTitleRepository;

    @Autowired
    public AuthorService(final PersonRepository personRepository, final BookTitleRepository bookTitleRepository) {
        this.personRepository = personRepository;
        this.bookTitleRepository = bookTitleRepository;
    }

    public List<Person> getAllAuthors() {
        return personRepository.findAllByBookTitlesNotEmpty();
    }

    public Person getAuthor(final Long id) {
        return personRepository.findByIdAndBookTitlesNotEmpty(id)
                .orElseThrow(() -> new NotFoundException("The author with the id=" + id + " doesn't exist."));
    }

    public Set<Person> getAllAuthorsOfBookTitle(final Long bookTitleId) {
        BookTitle bookTitle = bookTitleRepository.findById(bookTitleId)
                .orElseThrow(() -> new NotFoundException("The book title with the id=" + bookTitleId +
                        " doesn't exist."));
        return personRepository.findAllByBookTitlesContains(bookTitle.getId());
    }

}
