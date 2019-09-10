package com.restapi.library.domain;

import com.restapi.library.dto.BookTitleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class BookTitle {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String title;

    @ManyToMany
    @JoinTable(
            name = "bookTitle_author",
            joinColumns = @JoinColumn(
                    name = "bookTitleId",
                    foreignKey = @ForeignKey(name = "fk_bta_bookTitle")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "authorId",
                    foreignKey = @ForeignKey(name = "fk_bta_author")
            )
    )
    private Set<Person> authors;

    @OneToMany(
            targetEntity = Book.class,
            mappedBy = "bookTitle"
    )
    private List<Book> books;

    public BookTitle(final BookTitleDto bookTitleDto, final Set<Person> authors, final List<Book> books) {
        this(
                bookTitleDto.getId(),
                bookTitleDto.getTitle(),
                authors,
                books
        );
    }

    public void clearId() {
        id = null;
    }

    public void addAuthor(Person author) {
        authors.add(author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, authors, books);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookTitle bookTitle = (BookTitle) o;
        return id.equals(bookTitle.id) &&
                title.equals(bookTitle.title) &&
                authors.equals(bookTitle.authors) &&
                books.equals(bookTitle.books);
    }

}
