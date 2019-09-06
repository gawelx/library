package com.restapi.library.controller;

import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Borrower;
import com.restapi.library.domain.Person;
import com.restapi.library.dto.PersonDto;
import com.restapi.library.exception.BadRequestException;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.service.BookTitleService;
import com.restapi.library.service.BorrowerService;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/persons")
public class PersonController {

    private final PersonService personService;
    private final BorrowerService borrowerService;
    private final BookTitleService bookTitleService;

    @Autowired
    public PersonController(final PersonService personService, final BorrowerService borrowerService,
                            final BookTitleService bookTitleService) {
        this.personService = personService;
        this.borrowerService = borrowerService;
        this.bookTitleService = bookTitleService;
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
        return new PersonDto(personService.createPerson(new Person(personDto, null, Collections.emptyList())));
    }

    @PutMapping
    public PersonDto updatePerson(@RequestBody PersonDto personDto) {
        Borrower borrower;
        try {
            borrower = borrowerService.getBorrower(personDto.getId());
        } catch (NotFoundException e) {
            borrower = null;
        }
        List<BookTitle> bookTitles;
        try {
            bookTitles = bookTitleService.getAllBookTitlesOfAuthor(personDto.getId());
        } catch (BadRequestException e) {
            bookTitles = Collections.emptyList();
        }
        return new PersonDto(personService.updatePerson(new Person(personDto, borrower, bookTitles)));
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
    }

}
