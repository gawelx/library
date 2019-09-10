package com.restapi.library.domain;

import com.restapi.library.dto.PersonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.restapi.library.domain.PersonStatus.ACTIVE;

@NamedQuery(
        name = "Person.findAllByBookTitlesContains",
        query = "select distinct p from BookTitle bt inner join bt.authors p where bt.id = :bookTitleId"
)
@NamedQuery(
        name = "Person.findAllByBookTitlesNotEmpty",
        query = "select distinct p from BookTitle bt inner join bt.authors p"
)
@NamedQuery(
        name = "Person.findByIdAndBookTitlesNotEmpty",
        query = "select distinct p from BookTitle bt inner join bt.authors p where p.id = :id"
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

    public Person(final PersonDto personDto) {
        this(
                personDto.getId(),
                ACTIVE,
                personDto.getFirstName(),
                personDto.getLastName()
        );
    }

    public void setStatus(PersonStatus status) {
        this.status = status;
    }

    public void clearId() {
        id = null;
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
        return id.equals(person.id) &&
                status == person.status &&
                firstName.equals(person.firstName) &&
                lastName.equals(person.lastName);
    }

}
