package com.restapi.library.service;

import com.restapi.library.controller.BorrowingAction;
import com.restapi.library.controller.ProblemType;
import com.restapi.library.domain.Borrowing;
import com.restapi.library.domain.PenaltiesFactory;
import com.restapi.library.exception.BadRequestException;
import com.restapi.library.exception.ConflictException;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.BookRepository;
import com.restapi.library.repository.BorrowerRepository;
import com.restapi.library.repository.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.restapi.library.domain.BookStatus.BORROWED;
import static com.restapi.library.domain.PersonStatus.ACTIVE;

@Service
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;
    private final PenaltiesFactory penaltiesFactory;

    @Autowired
    public BorrowingService(final BorrowingRepository borrowingRepository, final BorrowerRepository borrowerRepository,
                            final BookRepository bookRepository, final PenaltiesFactory penaltiesFactory) {
        this.borrowingRepository = borrowingRepository;
        this.borrowerRepository = borrowerRepository;
        this.bookRepository = bookRepository;
        this.penaltiesFactory = penaltiesFactory;
    }

    public List<Borrowing> getAllBorrowings(final String category) {
        switch (category) {
            case "all":
                return borrowingRepository.findAll();
            case "pending":
                return borrowingRepository.findAllByReturnDateIsNull();
            case "closed":
                return borrowingRepository.findAllByReturnDateIsNotNull();
        }
        throw new BadRequestException("'" + category + "' is not valid borrowing category.");
    }

    public Borrowing getBorrowing(final Long id) {
        return borrowingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("The borrowing with the id=" + id + " doesn't exist."));
    }

    public List<Borrowing> getBorrowingsOfBorrower(final Long borrowerId, final String category) {
        if (!borrowerRepository.existsByIdAndStatus(borrowerId, ACTIVE)) {
            throw new NotFoundException("The borrower with the id=" + borrowerId + " doesn't exist.");
        }
        switch (category) {
            case "all":
                return borrowingRepository.findAllByBorrowerId(borrowerId);
            case "pending":
                return borrowingRepository.findAllByBorrowerIdAndReturnDateIsNull(borrowerId);
            case "closed":
                return borrowingRepository.findAllByBorrowerIdAndReturnDateIsNotNull(borrowerId);
        }
        throw new BadRequestException("'" + category + "' is not valid borrowing category.");
    }

    public List<Borrowing> getBorrowingsOfBook(final Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new NotFoundException("The book with the id=" + bookId + " doesn't exist.");
        }
        return borrowingRepository.findAllByBookId(bookId);
    }

    public Borrowing createBorrowing(final Borrowing borrowing) {
        borrowing.clearId();
        borrowing.getBorrower().addBorrowing(borrowing);
        borrowing.getBook().addBorrowing(borrowing);
        borrowing.getBook().setStatus(BORROWED);
        return borrowingRepository.save(borrowing);
    }

    public Borrowing updateBorrowing(final Borrowing borrowing, final BorrowingAction action,
                                     final String problemType) {
        Borrowing persistedBorrowing = borrowingRepository.findById(borrowing.getId())
                .orElseThrow(() -> new NotFoundException("The borrowing with the id=" + borrowing.getId() +
                        " doesn't exist."));
        if (persistedBorrowing.isBookReturned()) {
            throw new ConflictException("The book has been already returned. Update is impossible.");
        }
        Borrowing updatedBorrowing = null;
        switch (action) {
            case BORROWING_PERIOD_UPDATE:
                updatedBorrowing = updateBorrowingPeriod(persistedBorrowing, borrowing.getBorrowingPeriod());
                break;
            case BOOK_RETURN:
                updatedBorrowing = returnBook(persistedBorrowing, ProblemType.of(problemType));
                break;
        }
        return updatedBorrowing;
    }

    private Borrowing updateBorrowingPeriod(final Borrowing borrowing, final Integer newPeriod) {
        if (isPeriodToShort(borrowing.getBorrowingDate(), newPeriod)) {
            throw new ConflictException("New borrowing period is to short.");
        }
        borrowing.setBorrowingPeriod(newPeriod);
        return borrowingRepository.save(borrowing);
    }

    private Borrowing returnBook(final Borrowing borrowing, final ProblemType problemType) {
        borrowing.setReturnDate(LocalDate.now());
        borrowing.getBook().setStatus(problemType.getBookStatus());
        borrowing.setPenalties(penaltiesFactory.createPenalties(borrowing));
        return borrowingRepository.save(borrowing);
    }

    private boolean isPeriodToShort(final LocalDate borrowingDate, final Integer period) {
        return borrowingDate.plusDays(period).isBefore(LocalDate.now());
    }

}
