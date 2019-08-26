package com.restapi.library.repository;

import com.restapi.library.domain.Borrower;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowerRepository extends CrudRepository<Borrower, Long> {

    @Override
    List<Borrower> findAll();

    Optional<Borrower> findByPersonFirstNameAndPersonLastName(String firstName, String lastName);

    Optional<Borrower> findByPersonId(Long personId);

}
