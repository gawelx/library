package com.restapi.library.controller;

import com.restapi.library.domain.Borrower;
import com.restapi.library.domain.Person;
import com.restapi.library.dto.BorrowerDto;
import com.restapi.library.dto.BorrowingDto;
import com.restapi.library.dto.PenaltyDto;
import com.restapi.library.dto.PersonDto;
import com.restapi.library.service.BorrowerService;
import com.restapi.library.service.BorrowingService;
import com.restapi.library.service.PenaltyService;
import com.restapi.library.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;
    private final PersonService personService;
    private final BorrowingService borrowingService;
    private final PenaltyService penaltyService;

    @Autowired
    public BorrowerController(final BorrowerService borrowerService, final PersonService personService,
                              final BorrowingService borrowingService, final PenaltyService penaltyService) {
        this.borrowerService = borrowerService;
        this.personService = personService;
        this.borrowingService = borrowingService;
        this.penaltyService = penaltyService;
    }

    @GetMapping
    public List<BorrowerDto> getAllBorrowers() {
        return borrowerService.getAllBorrowers().stream()
                .map(BorrowerDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BorrowerDto getBorrower(@PathVariable Long id) {
        return new BorrowerDto(borrowerService.getBorrower(id));
    }

    @GetMapping("/{id}/person")
    public PersonDto getBorrowersPerson(@PathVariable Long id) {
        return new PersonDto(personService.getPerson(id));
    }

    @GetMapping("/{id}/borrowings")
    public List<BorrowingDto> getBorrowersBorrowings(@PathVariable Long id,
                                                     @RequestParam(defaultValue = "all") String category) {
        return borrowingService.getBorrowingsOfBorrower(id, category).stream()
                .map(BorrowingDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/penalties")
    public List<PenaltyDto> getBorrowersPenalties(@PathVariable Long id, @RequestParam(required = false) Boolean paid) {
        return penaltyService.getAllPenaltiesOfBorrower(id, paid).stream()
                .map(PenaltyDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public BorrowerDto createBorrower(@RequestBody BorrowerDto borrowerDto) {
        Person person = personService.getPerson(borrowerDto.getId());
        return new BorrowerDto(borrowerService.createBorrower(new Borrower(borrowerDto, person, Collections.emptyList())));
    }

    @DeleteMapping("/{id}")
    public void deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
    }

}
