package com.restapi.library.repository;

import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface BookTitleRepository extends JpaRepository<BookTitle, Long> {

    List<BookTitle> findAllByAuthorsContains(Person author);

}
