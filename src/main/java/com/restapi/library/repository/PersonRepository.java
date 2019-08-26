package com.restapi.library.repository;

import com.restapi.library.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    @Override
    List<Person> findAll();

    Optional<Person> findByFirstNameAndLastName(String firstName, String lastName);

}
