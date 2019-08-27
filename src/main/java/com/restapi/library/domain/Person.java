package com.restapi.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    @Column(name = "firstName")
    private String firstName;

    @NotNull
    @Column(name = "lastName")
    private String lastName;

    @OneToOne(
            targetEntity = Borrower.class,
            mappedBy = "person",
            cascade = CascadeType.REMOVE
    )
    private Borrower borrower;

    @OneToMany(
            targetEntity = BookTitle.class,
            mappedBy = "author"//,
//            cascade = CascadeType.REMOVE
    )
    private List<BookTitle> bookTitles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
                firstName.equals(person.firstName) &&
                lastName.equals(person.lastName);// &&
//                Objects.equals(borrower, person.borrower) &&
//                Objects.equals(bookTitles, person.bookTitles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);//, borrower, bookTitles);
    }

}
