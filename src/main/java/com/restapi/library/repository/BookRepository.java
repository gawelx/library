package com.restapi.library.repository;

import com.restapi.library.domain.Book;
import com.restapi.library.domain.BookStatus;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface BookRepository extends GenericRepository<Book> {

    List<Book> findAllByBookTitleId(Long bookTitleId);

    List<Book> findAllByBookTitleAuthorId(Long authorId);

    List<Book> findAllByBookTitleIdAndStatus(Long bookTitleId, BookStatus status);


}
