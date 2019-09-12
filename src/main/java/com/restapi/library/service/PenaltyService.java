package com.restapi.library.service;

import com.restapi.library.domain.Penalty;
import com.restapi.library.exception.ConflictException;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.BorrowerRepository;
import com.restapi.library.repository.BorrowingRepository;
import com.restapi.library.repository.PenaltyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.restapi.library.domain.PersonStatus.ACTIVE;

@Service
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final BorrowingRepository borrowingRepository;
    private final BorrowerRepository borrowerRepository;

    @Autowired
    public PenaltyService(final PenaltyRepository penaltyRepository, final BorrowingRepository borrowingRepository,
                          final BorrowerRepository borrowerRepository) {
        this.penaltyRepository = penaltyRepository;
        this.borrowingRepository = borrowingRepository;
        this.borrowerRepository = borrowerRepository;
    }

    public List<Penalty> getAllPenalties(Boolean paid) {
        if (paid == null) {
            return penaltyRepository.findAll();
        }
        return penaltyRepository.findAllByPaid(paid);
    }

    public Penalty getPenalty(final Long id) {
        return penaltyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The penalty with the id=" + id + " doesn't exist."));
    }

    public List<Penalty> getAllPenaltiesOfBorrowing(final Long borrowingId) {
        if (!borrowingRepository.existsById(borrowingId)) {
            throw new NotFoundException("The borrowing with the id=" + borrowingId + " doesn't exist.");
        }
        return penaltyRepository.findAllByBorrowingId(borrowingId);
    }

    public List<Penalty> getAllPenaltiesOfBorrower(final Long borrowerId, final Boolean paid) {
        if (!borrowerRepository.existsByIdAndStatus(borrowerId, ACTIVE)) {
            throw new NotFoundException("The borrower with the id=" + borrowerId + " doesn't exist.");
        }
        if (paid == null) {
            return penaltyRepository.findAllByBorrowingBorrowerId(borrowerId);
        }
        return penaltyRepository.findAllByBorrowingBorrowerIdAndPaid(borrowerId, paid);
    }

    public Penalty markPenaltyPaid(final Long id) {
        Penalty persistedPenalty = penaltyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The penalty with the id=" + id + " doesn't exist."));
        if (persistedPenalty.getPaid()) {
            throw new ConflictException("The penalty has already been paid.");
        }
        persistedPenalty.setPaid(Boolean.TRUE);
        return penaltyRepository.save(persistedPenalty);
    }

}
