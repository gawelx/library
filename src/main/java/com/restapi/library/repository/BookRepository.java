package com.restapi.library.repository;

import com.restapi.library.domain.Book;
import com.restapi.library.domain.BookStatus;
import com.restapi.library.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByStatus(BookStatus status);

    List<Book> findAllByBookTitleId(Long bookTitleId);

    List<Book> findAllByBookTitleAuthorsContains(Person author);

    Optional<Book> findBookByIdAndStatus(Long id, BookStatus status);

}
