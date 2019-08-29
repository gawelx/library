package com.restapi.library.dto;

import com.restapi.library.domain.Borrower;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BorrowerDto {

    private Long id;
    private LocalDateTime accountCreationDateTime;
    private PersonDto personDto;

    public BorrowerDto(final Borrower borrower) {
        this(
                borrower.getId(),
                borrower.getAccountCreationDateTime(),
                new PersonDto(borrower.getPerson())
        );
    }

}
