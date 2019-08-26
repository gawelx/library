package com.restapi.library.repository;

import com.restapi.library.BookStatus;
import com.restapi.library.domain.Borrowing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowingRepository extends CrudRepository<Borrowing, Long> {

    @Override
    List<Borrowing> findAll();

    List<Borrowing> findAllByBorrowerId(Long borrowerId);

    List<Borrowing> findAllByBorrowerIdAndReturnDateIsNull(Long borrowerId);

    List<Borrowing> findAllByBorrowingDateBefore(LocalDate date);

    List<Borrowing> findAllByBorrowerIdAndPenaltyFeeIsNotNull(Long borrowerId);

    List<Borrowing> findAllByBookId(Long bookId);

    List<Borrowing> findAllByPenaltyFeeIsNotNull();

}
