package com.restapi.library.repository;

import com.restapi.library.domain.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    List<Borrowing> findAllByReturnDateIsNull();

    List<Borrowing> findAllByReturnDateIsNotNull();

    List<Borrowing> findAllByBorrowerId(Long borrowerId);

    List<Borrowing> findAllByBorrowerIdAndReturnDateIsNull(Long borrowerId);

    boolean existsByBorrowerIdAndReturnDateIsNull(Long borrowerId);

    List<Borrowing> findAllByBorrowerIdAndReturnDateIsNotNull(Long borrowerId);

    List<Borrowing> findAllByBookId(Long bookId);

}
