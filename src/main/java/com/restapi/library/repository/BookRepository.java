package com.restapi.library.repository;

import com.restapi.library.BookStatus;
import com.restapi.library.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @Override
    List<Book> findAll();

    List<Book> findAllByBookTitleId(Long bookTitleId);

    List<Book> findAllByBookTitleAuthorId(Long authorId);

    List<Book> findAllByBookTitleIdAndStatus(Long bookTitleId, BookStatus status);



}
