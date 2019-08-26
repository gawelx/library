package com.restapi.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
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
@Entity(name = "BookTitle")
public class BookTitle {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column
    private String title;

    @NotNull
    @Column(name = "releaseYear")
    private Integer releaseYear;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "authorId",
            foreignKey = @ForeignKey(name = "fk_bookTitle_person")
    )
    private Person author;

    @OneToMany(
            targetEntity = Book.class,
            mappedBy = "bookTitle",
            cascade = CascadeType.ALL
    )
    private List<Book> books;
}
