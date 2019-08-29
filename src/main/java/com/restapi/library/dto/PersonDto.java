package com.restapi.library.dto;

import com.restapi.library.domain.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PersonDto {

    private Long id;
    private String firstName;
    private String lastName;

    public PersonDto(final Person person) {
        this(person.getId(), person.getFirstName(), person.getLastName());
    }

}
