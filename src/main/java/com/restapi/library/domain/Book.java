package com.restapi.library.domain;

import com.restapi.library.BookStatus;
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
                BookStatus.valueOf(bookDto.getStatus()),
                bookTitle,
                borrowings
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, bookTitle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                status == book.status &&
                bookTitle.equals(book.bookTitle);
    }

}
