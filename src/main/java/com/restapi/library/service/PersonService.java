package com.restapi.library.service;

import com.restapi.library.domain.Person;
import com.restapi.library.exception.BadRequestException;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.restapi.library.domain.PersonStatus.ACTIVE;
import static com.restapi.library.domain.PersonStatus.DELETED;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAllByStatus(ACTIVE);
    }

    public Person getPerson(final Long id) {
        return personRepository.findByIdAndStatus(id, ACTIVE)
                .orElseThrow(() -> new NotFoundException("The person with the id=" + id + " doesn't exist."));
    }

    public Person createPerson(final Person person) {
        person.clearId();
        return personRepository.save(person);
    }

    public Person updatePerson(final Person person) {
        if (person.getId() == null) {
            throw new BadRequestException("The person id must be specified.");
        }
        return personRepository.save(person);
    }

    public void deletePerson(final Long id) {
        personRepository.findById(id).ifPresent(person -> {
            person.setStatus(DELETED);
            if (person.isBorrower()) {
                person.getBorrower().setStatus(DELETED);
            }
            personRepository.save(person);
        });
    }

}
