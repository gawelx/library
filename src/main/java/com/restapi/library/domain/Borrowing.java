package com.restapi.library.domain;

import com.restapi.library.dto.BorrowingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
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

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "borrowerId",
            foreignKey = @ForeignKey(name = "fk_borrowing_borrower")
    )
    private Borrower borrower;

    @NotNull
    @ManyToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinColumn(
            name = "bookId",
            foreignKey = @ForeignKey(name = "fk_borrowing_book")
    )
    private Book book;

    public Borrowing(final BorrowingDto borrowingDto, final Borrower borrower, final Book book) {
        this(
                borrowingDto.getId(),
                borrowingDto.getBorrowingDate(),
                borrowingDto.getBorrowingPeriod(),
                borrowingDto.getReturnDate(),
                borrower,
                book
        );
    }

    @PrePersist
    public void onCreate() {
        borrowingDate = LocalDate.now();
        returnDate = null;
    }

    public void clearId() {
        id = null;
    }

    public void setBorrowingPeriod(Integer borrowingPeriod) {
        this.borrowingPeriod = borrowingPeriod;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isBookReturned() {
        return returnDate != null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, borrowingDate, borrowingPeriod, returnDate, borrower, book);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrowing borrowing = (Borrowing) o;
        return id.equals(borrowing.id) &&
                borrowingDate.equals(borrowing.borrowingDate) &&
                borrowingPeriod.equals(borrowing.borrowingPeriod) &&
                Objects.equals(returnDate, borrowing.returnDate) &&
                borrower.equals(borrowing.borrower) &&
                book.equals(borrowing.book);
    }

}
