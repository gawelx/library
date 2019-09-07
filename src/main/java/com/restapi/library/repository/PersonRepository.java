package com.restapi.library.repository;

import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Person;
import com.restapi.library.domain.PersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByStatus(PersonStatus status);

    Optional<Person> findByIdAndStatus(Long id, PersonStatus status);

    Set<Person> findAllByBookTitlesContains(BookTitle bookTitle);

    @Query
    List<Person> findAllByBookTitlesNotEmpty();

    @Query
    Optional<Person> findByIdAndBookTitlesNotEmpty(Long id);

}
