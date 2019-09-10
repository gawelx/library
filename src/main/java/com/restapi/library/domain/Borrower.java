package com.restapi.library.domain;

import com.restapi.library.dto.BorrowerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Borrower {

    @Id
    @NotNull
    private Long id;

    private PersonStatus status;

    private LocalDateTime accountCreationDateTime;

    @NotNull
    @OneToOne(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @MapsId
    private Person person;

    @OneToMany(
            targetEntity = Borrowing.class,
            mappedBy = "borrower"
    )
    private List<Borrowing> borrowings;

    public Borrower(final BorrowerDto borrowerDto, final Person person, final List<Borrowing> borrowings) {
        this(
                borrowerDto.getId(),
                null,
                borrowerDto.getAccountCreationDateTime(),
                person,
                borrowings
        );
        if (person.getBorrower() == null) {
            person.setBorrower(this);
        }
    }

    public void setAccountCreationDateTime(LocalDateTime accountCreationDateTime) {
        this.accountCreationDateTime = accountCreationDateTime;
    }

    public void setStatus(PersonStatus status) {
        this.status = status;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void addBorrowing(Borrowing borrowing) {
        borrowings.add(borrowing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, accountCreationDateTime, person, borrowings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrower borrower = (Borrower) o;
        return id.equals(borrower.id) &&
                status == borrower.status &&
                accountCreationDateTime.equals(borrower.accountCreationDateTime) &&
                person.equals(borrower.person) &&
                borrowings.equals(borrower.borrowings);
    }

    @Override
    public String toString() {
        return "Borrower{" +
                "id=" + id +
                ", status=" + status +
                ", accountCreationDateTime=" + accountCreationDateTime +
                ", person=" + person +
                ", borrowings=" + borrowings +
                '}';
    }

}
