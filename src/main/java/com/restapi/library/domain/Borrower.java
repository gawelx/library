package com.restapi.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Borrower {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "accountCreationDate")
    private LocalDate accountCreationDate;

    @NotNull
    @OneToOne
    @JoinColumn(
            name = "personId",
            foreignKey = @ForeignKey(name = "fk_borrower_person")
    )
    private Person person;

    @OneToMany (
            targetEntity = Borrowing.class,
            mappedBy = "borrower"
    )
    private List<Borrowing> borrowings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrower borrower = (Borrower) o;
        return Objects.equals(id, borrower.id) &&
                Objects.equals(accountCreationDate, borrower.accountCreationDate) &&
                person.equals(borrower.person) &&
                Objects.equals(borrowings, borrower.borrowings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountCreationDate, person, borrowings);
    }

}
