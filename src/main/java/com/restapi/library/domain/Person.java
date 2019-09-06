package com.restapi.library.domain;

import com.restapi.library.dto.PersonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

import static com.restapi.library.domain.PersonStatus.ACTIVE;

@NamedQuery(
        name = "Person.FindAllByBookTitlesNotEmpty",
        query = "select distinct p from BookTitle bt left join Person p"
)
@NamedQuery(
        name = "Person.FindByIdAndBookTitlesNotEmpty",
        query = "select distinct p from BookTitle bt left join Person p where p.id = :id"
)

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    private PersonStatus status;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @OneToOne(
            targetEntity = Borrower.class,
            mappedBy = "person"
    )
    private Borrower borrower;

    @ManyToMany(
            targetEntity = BookTitle.class,
            mappedBy = "authors"
    )
    private List<BookTitle> bookTitles;

    public Person(final PersonDto personDto, final Borrower borrower, final List<BookTitle> bookTitles) {
        this(
                personDto.getId(),
                ACTIVE,
                personDto.getFirstName(),
                personDto.getLastName(),
                borrower,
                bookTitles
        );
    }

    public void setStatus(PersonStatus status) {
        this.status = status;
    }

    public void clearId() {
        id = null;
    }

    public boolean isBorrower() {
        return borrower != null;
    }

    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

    public void removeBorrower() {
        borrower = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, firstName, lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
                Objects.equals(status, person.status) &&
                firstName.equals(person.firstName) &&
                lastName.equals(person.lastName);
    }

}
