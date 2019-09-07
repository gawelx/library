package com.restapi.library.service;

import com.restapi.library.domain.Penalty;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.PenaltyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;

    @Autowired
    public PenaltyService(final PenaltyRepository penaltyRepository) {
        this.penaltyRepository = penaltyRepository;
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
        return penaltyRepository.findAllByBorrowingId(borrowingId);
    }

    public List<Penalty> getAllPenaltiesOfBorrower(final Long borrowerId, final Boolean paid) {
        if (paid == null) {
            return penaltyRepository.findAllByBorrowingBorrowerId(borrowerId);
        }
        return penaltyRepository.findAllByBorrowingBorrowerIdAndPaid(borrowerId, paid);
    }

    public Penalty markPenaltyPaid(final Long id) {
        Penalty persistedPenalty = penaltyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The penalty with the id=" + id + " doesn't exist."));
        persistedPenalty.setPaid(Boolean.TRUE);
        return penaltyRepository.save(persistedPenalty);
    }

}
