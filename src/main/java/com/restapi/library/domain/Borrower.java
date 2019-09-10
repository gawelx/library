package com.restapi.library.domain;

import com.restapi.library.dto.BorrowerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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

    public Borrower(final BorrowerDto borrowerDto, final Person person) {
        this(
                borrowerDto.getId(),
                null,
                borrowerDto.getAccountCreationDateTime(),
                person
        );
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

    @Override
    public int hashCode() {
        return Objects.hash(id, status, accountCreationDateTime, person);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrower borrower = (Borrower) o;
        return id.equals(borrower.id) &&
                status == borrower.status &&
                accountCreationDateTime.equals(borrower.accountCreationDateTime) &&
                person.equals(borrower.person);
    }

}
