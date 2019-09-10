package com.restapi.library.domain;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static com.restapi.library.domain.BookStatus.DAMAGED;
import static com.restapi.library.domain.BookStatus.LOST;
import static com.restapi.library.domain.PenaltyCause.BOOK_DAMAGED;
import static com.restapi.library.domain.PenaltyCause.BOOK_LOST;
import static com.restapi.library.domain.PenaltyCause.DEADLINE_EXCEEDED;
import static java.time.temporal.ChronoUnit.DAYS;

@NoArgsConstructor
@Component
public class PenaltiesFactory {

    public List<Penalty> createPenalties(final Borrowing borrowing) {
        List<Penalty> penalties = new ArrayList<>(2);
        if (borrowing.getBook().getStatus() == DAMAGED) {
            penalties.add(createPenalty(
                    BOOK_DAMAGED,
                    borrowing.getBook().getPrice().divide(BigDecimal.valueOf(2), RoundingMode.HALF_DOWN),
                    borrowing
            ));
        } else if (borrowing.getBook().getStatus() == LOST) {
            penalties.add(createPenalty(
                    BOOK_LOST,
                    borrowing.getBook().getPrice(),
                    borrowing
            ));
        }
        int delayDays = countDelayDays(borrowing);
        if ((delayDays) > 0) {
            penalties.add(createPenalty(
                    DEADLINE_EXCEEDED,
                    Penalty.PENALTY_DAILY_RATE.multiply(BigDecimal.valueOf(delayDays)),
                    borrowing
            ));
        }
        return penalties;
    }

    private Penalty createPenalty(final PenaltyCause penaltyCause, final BigDecimal penaltyFee,
                                  final Borrowing borrowing) {
        return new Penalty(
                null,
                LocalDateTime.now(),
                penaltyCause,
                penaltyFee,
                Boolean.FALSE,
                borrowing
        );
    }

    private int countDelayDays(final Borrowing borrowing) {
        LocalDate returnDate = borrowing.getBorrowingDate().plusDays(borrowing.getBorrowingPeriod());
        Period period = returnDate.until(borrowing.getReturnDate());
        int days = (int) DAYS.between(returnDate, borrowing.getReturnDate());
        return Math.max(days, 0);
    }

}
