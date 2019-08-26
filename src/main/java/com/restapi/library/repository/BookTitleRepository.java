package com.restapi.library.repository;

import com.restapi.library.domain.BookTitle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookTitleRepository extends CrudRepository<BookTitle, Long> {

    @Override
    List<BookTitle> findAll();

}
