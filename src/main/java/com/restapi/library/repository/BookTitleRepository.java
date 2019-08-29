package com.restapi.library.repository;

import com.restapi.library.domain.BookTitle;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface BookTitleRepository extends GenericRepository<BookTitle> {

}
