package com.restapi.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.List;

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
            cascade = CascadeType.ALL
    )
    private Borrower borrower;

    @OneToMany(
            targetEntity = BookTitle.class,
            mappedBy = "author",
            cascade = CascadeType.ALL
    )
    private List<BookTitle> bookTitles;

}
