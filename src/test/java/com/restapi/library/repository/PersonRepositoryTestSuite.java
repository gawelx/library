package com.restapi.library.repository;

import com.restapi.library.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonRepositoryTestSuite {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testDelete() {
        //Given
        Person person = new Person(
                null,
                "John",
                "Smith",
                null,
                Collections.emptyList());

        //When
        person = personRepository.save(person);
        int beforeDeletionSize = personRepository.findAll().size();
        personRepository.delete(person);
        int afterDeletionSize = personRepository.findAll().size();

        //Then
        assertEquals(1, beforeDeletionSize);
        assertEquals(0, afterDeletionSize);
    }

    @Test
    public void testSave() {
        //Given
        Person person = new Person(
                null,
                "John",
                "Smith",
                null,
                Collections.emptyList());

        //When
        person = personRepository.save(person);
        List<Person> retrievedPersons = personRepository.findAll();

        //Then
        try {
            assertEquals(1, retrievedPersons.size());
            assertTrue(retrievedPersons.contains(person));
        } finally {
            personRepository.delete(person);
        }
    }

    @Test
    public void testFindAll() {
        //Given
        Person person1 = new Person(
                null,
                "John",
                "Smith",
                null,
                Collections.emptyList());
        Person person2 = new Person(
                null,
                "Jane",
                "Doe",
                null,
                Collections.emptyList());

        //When
        person1 = personRepository.save(person1);
        person2 = personRepository.save(person2);
        List<Person> retrievedPersons = personRepository.findAll();

        //Then
        try {
            assertEquals(2, retrievedPersons.size());
            assertTrue(retrievedPersons.contains(person1));
            assertTrue(retrievedPersons.contains(person2));
        } finally {
            //CleanUp
            personRepository.deleteAll();
        }
    }

    @Test
    public void testFindById() {
        //Given
        Person person = new Person(
                null,
                "John",
                "Smith",
                null,
                Collections.emptyList());

        //When
        person = personRepository.save(person);
        Optional<Person> retrievedPerson = personRepository.findById(person.getId());

        //Then
        try {
            assertTrue(retrievedPerson.isPresent());
            assertEquals(person, retrievedPerson.get());
        } finally {
            //CleanUp
            personRepository.delete(person);
        }
    }

    @Test
    public void testFindByFirstNameAndLastName() {
        //Given
        Person person1 = new Person(
                null,
                "John",
                "Smith",
                null,
                Collections.emptyList());
        Person person2 = new Person(
                null,
                "Jane",
                "Doe",
                null,
                Collections.emptyList());

        //When
        personRepository.save(person1);
        person2 = personRepository.save(person2);
        Optional<Person> retrievedPerson = personRepository.findByFirstNameAndLastName(person2.getFirstName(),
                person2.getLastName());
        //Then
        try {
            assertTrue(retrievedPerson.isPresent());
            assertEquals(person2, retrievedPerson.get());
        } finally {
            //CleanUp
            personRepository.deleteAll();
        }
    }

}
