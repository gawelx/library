package com.restapi.library.domain;

import com.restapi.library.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Integer releaseYear;

    @NotNull
    private BigDecimal price;

    private BookStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "bookTitleId",
            foreignKey = @ForeignKey(name = "fk_book_bookTitle")
    )
    private BookTitle bookTitle;

    @OneToMany(
            targetEntity = Borrowing.class,
            mappedBy = "book"
    )
    private List<Borrowing> borrowings;

    public Book(final BookDto bookDto, final BookTitle bookTitle, List<Borrowing> borrowings) {
        this(
                bookDto.getId(),
                bookDto.getReleaseYear(),
                bookDto.getPrice(),
                bookDto.getStatus() == null ? null : BookStatus.of(bookDto.getStatus()),
                bookTitle,
                borrowings
        );
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public void clearId() {
        this.id = null;
    }

    public void addBorrowing(Borrowing borrowing) {
        borrowings.add(borrowing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, releaseYear, status, bookTitle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                releaseYear.equals(book.releaseYear) &&
                status == book.status &&
                bookTitle.equals(book.bookTitle);
    }

}
