package com.restapi.library.domain;

import com.restapi.library.dto.PersonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @OneToOne(
            targetEntity = Borrower.class,
            mappedBy = "person",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private Borrower borrower;

    @OneToMany(
            targetEntity = BookTitle.class,
            mappedBy = "author",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<BookTitle> bookTitles;

    public Person(final PersonDto personDto, final Borrower borrower, final List<BookTitle> bookTitles) {
        this(
                personDto.getId(),
                personDto.getFirstName(),
                personDto.getLastName(),
                borrower,
                bookTitles
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
                firstName.equals(person.firstName) &&
                lastName.equals(person.lastName);
    }

}
