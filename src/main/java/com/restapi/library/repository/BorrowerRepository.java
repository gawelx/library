package com.restapi.library.repository;

import com.restapi.library.domain.Borrower;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface BorrowerRepository extends GenericRepository<Borrower> {

    Optional<Borrower> findByPersonFirstNameAndPersonLastName(String firstName, String lastName);

    Optional<Borrower> findByPersonId(Long personId);

}
