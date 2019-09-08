package com.restapi.library.service;

import com.restapi.library.domain.Borrower;
import com.restapi.library.exception.ConflictException;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.restapi.library.domain.PersonStatus.ACTIVE;
import static com.restapi.library.domain.PersonStatus.DELETED;

@Service
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    @Autowired
    public BorrowerService(final BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    public List<Borrower> getAllBorrowers() {
        return borrowerRepository.findAllByStatus(ACTIVE);
    }

    public Borrower getBorrower(final Long id) {
        return borrowerRepository.findByIdAndStatus(id, ACTIVE)
                .orElseThrow(() -> new NotFoundException("The borrower with the id=" + id + " doesn't exist."));
    }

    public Borrower createBorrower(final Borrower borrower) {
        if (borrowerRepository.existsByIdAndStatus(borrower.getId(), ACTIVE)) {
            throw new ConflictException("The borrower with the id=" + borrower.getId() + " already exists.");
        }
        borrower.setStatus(ACTIVE);
        borrower.setAccountCreationDateTime(LocalDateTime.now());
        return borrowerRepository.save(borrower);
    }

    public void deleteBorrower(final Long id) {
        borrowerRepository.findByIdAndStatus(id, ACTIVE).ifPresent(borrower -> {
            borrower.setStatus(DELETED);
            borrower.getPerson().removeBorrower();
            borrowerRepository.save(borrower);
        });
    }

}
