package com.restapi.library.repository;

import com.restapi.library.domain.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

    List<Penalty> findAllByPaid(Boolean paid);

    List<Penalty> findAllByBorrowingId(Long id);

    List<Penalty> findAllByBorrowingBorrowerId(Long borrowerId);

    List<Penalty> findAllByBorrowingBorrowerIdAndPaid(Long borrowerId, Boolean paid);

    boolean existsByBorrowingBorrowerIdAndPaid(Long borrowerId, Boolean paid);

}
