package com.restapi.library.repository;

import com.restapi.library.domain.Person;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface PersonRepository extends GenericRepository<Person> {

    Optional<Person> findByFirstNameAndLastName(String firstName, String lastName);

}
