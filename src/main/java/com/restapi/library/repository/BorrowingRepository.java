package com.restapi.library.repository;

import com.restapi.library.domain.Borrowing;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Transactional
@Repository
public interface BorrowingRepository extends GenericRepository<Borrowing> {

    List<Borrowing> findAllByBorrowerId(Long borrowerId);

    List<Borrowing> findAllByBorrowerIdAndReturnDateIsNull(Long borrowerId);

    List<Borrowing> findAllByBorrowingDateBefore(LocalDate date);

    List<Borrowing> findAllByBorrowerIdAndPenaltyFeeIsNotNull(Long borrowerId);

    List<Borrowing> findAllByBookId(Long bookId);

    List<Borrowing> findAllByPenaltyFeeIsNotNull();

}
