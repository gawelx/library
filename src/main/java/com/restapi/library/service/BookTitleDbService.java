package com.restapi.library.service;

import com.restapi.library.domain.BookTitle;
import com.restapi.library.repository.BookTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookTitleDbService extends DbService<BookTitle> {

    @Autowired
    public BookTitleDbService(final BookTitleRepository bookTitleRepository) {
        super(bookTitleRepository);
    }

}
