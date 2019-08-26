package com.restapi.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Borrowing {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "borrowingDate")
    private LocalDate borrowingDate;

    @NotNull
    @Column(name = "borrowingPeriod")
    private Integer borrowingPeriod;

    @Column(name = "returnDate")
    private LocalDate returnDate;

    @Column(name = "penaltyFee")
    private BigDecimal penaltyFee;

}
