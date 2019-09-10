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
import javax.validation.constraints.NotNull;
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

    public BookTitle(final BookTitleDto bookTitleDto, final Set<Person> authors) {
        this(
                bookTitleDto.getId(),
                bookTitleDto.getTitle(),
                authors
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
        return Objects.hash(id, title, authors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookTitle bookTitle = (BookTitle) o;
        return id.equals(bookTitle.id) &&
                title.equals(bookTitle.title) &&
                authors.equals(bookTitle.authors);
    }

}
