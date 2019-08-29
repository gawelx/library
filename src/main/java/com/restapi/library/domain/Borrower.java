package com.restapi.library.domain;

import com.restapi.library.dto.BorrowerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
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
    private Long id;

    private LocalDateTime accountCreationDateTime;

    @NotNull
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "personId",
            foreignKey = @ForeignKey(name = "fk_borrower_person")
    )
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
                borrowerDto.getAccountCreationDateTime(),
                person,
                borrowings
        );
    }

    @PrePersist
    protected void onCreate() {
        accountCreationDateTime = LocalDateTime.now();
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountCreationDateTime, person);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrower borrower = (Borrower) o;
        return Objects.equals(accountCreationDateTime, borrower.accountCreationDateTime) &&
                person.equals(borrower.person);
    }

}
