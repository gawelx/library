package com.restapi.library.controller;

import com.restapi.library.domain.Person;
import com.restapi.library.dto.PersonDto;
import com.restapi.library.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(final PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<PersonDto> getPersons() {
        return personService.getAllPersons().stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDto getPerson(@PathVariable Long id) {
        return new PersonDto(personService.getPerson(id));
    }

    @PostMapping
    public PersonDto createPerson(@RequestBody PersonDto personDto) {
        return new PersonDto(personService.createPerson(new Person(personDto)));
    }

    @PutMapping
    public PersonDto updatePerson(@RequestBody PersonDto personDto) {
        return new PersonDto(personService.updatePerson(new Person(personDto)));
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
    }

}
