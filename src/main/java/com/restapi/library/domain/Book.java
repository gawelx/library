package com.restapi.library.domain;

import com.restapi.library.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
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

    public Book(final BookDto bookDto, final BookTitle bookTitle) {
        this(
                bookDto.getId(),
                bookDto.getReleaseYear(),
                bookDto.getPrice(),
                bookDto.getStatus() == null ? null : BookStatus.of(bookDto.getStatus()),
                bookTitle
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

    @Override
    public int hashCode() {
        return Objects.hash(id, releaseYear, price, status, bookTitle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id.equals(book.id) &&
                releaseYear.equals(book.releaseYear) &&
                price.compareTo(book.price) == 0 &&
                status == book.status &&
                bookTitle.equals(book.bookTitle);
    }

}
