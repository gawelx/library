package com.restapi.library.dto;

import com.restapi.library.domain.Borrowing;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BorrowingDto {

    private Long id;
    private LocalDate borrowingDate;
    private Integer borrowingPeriod;
    private LocalDate returnDate;
    private BigDecimal penaltyFee;
    private BorrowerDto borrowerDto;
    private BookDto bookDto;

    public BorrowingDto(final Borrowing borrowing) {
        this(
                borrowing.getId(),
                borrowing.getBorrowingDate(),
                borrowing.getBorrowingPeriod(),
                borrowing.getReturnDate(),
                borrowing.getPenaltyFee(),
                new BorrowerDto(borrowing.getBorrower()),
                new BookDto(borrowing.getBook())
        );
    }


}
