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
}
