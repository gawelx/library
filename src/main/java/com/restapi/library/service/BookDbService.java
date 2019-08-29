package com.restapi.library.service;

import com.restapi.library.domain.Book;
import com.restapi.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookDbService extends DbService<Book> {

    @Autowired
    public BookDbService(final BookRepository bookRepository) {
        super(bookRepository);
    }

}
