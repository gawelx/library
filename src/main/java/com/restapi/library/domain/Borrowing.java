package com.restapi.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Borrowing {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private LocalDate borrowingDate;

    @NotNull
    private Integer borrowingPeriod;

    private LocalDate returnDate;

    private BigDecimal penaltyFee;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "borrowerId",
            foreignKey = @ForeignKey(name = "fk_borrowing_borrower")
    )
    private Borrower borrower;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "bookId",
            foreignKey = @ForeignKey(name = "fk_borrowing_book")
    )
    private Book book;

    @Override
    public int hashCode() {
        return Objects.hash(id, borrowingDate, borrower, book);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrowing borrowing = (Borrowing) o;
        return Objects.equals(id, borrowing.id) &&
                borrowingDate.equals(borrowing.borrowingDate) &&
                borrower.equals(borrowing.borrower) &&
                book.equals(borrowing.book);
    }

}
