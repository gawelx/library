package com.restapi.library.domain;

import com.restapi.library.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column
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

}
