package com.restapi.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column
    private String title;

    @Column(name = "releaseYear")
    private Integer releaseYear;

    //link to Person as the author

}
