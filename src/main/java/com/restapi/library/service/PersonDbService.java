package com.restapi.library.service;

import com.restapi.library.domain.Person;
import com.restapi.library.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonDbService extends DbService<Person> {

    @Autowired
    public PersonDbService(final PersonRepository personRepository) {
        super(personRepository);
    }

}
