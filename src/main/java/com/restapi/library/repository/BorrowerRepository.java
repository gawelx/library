package com.restapi.library.repository;

import com.restapi.library.domain.Borrower;
import com.restapi.library.domain.PersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    List<Borrower> findAllByStatus(PersonStatus status);

    Optional<Borrower> findByIdAndStatus(Long id, PersonStatus status);

    boolean existsByIdAndStatus(Long id, PersonStatus status);

}
