package com.restapi.library.domain;

import com.restapi.library.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "BookItem")
public class BookItem {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private BookStatus status;

    //link to Book

}
