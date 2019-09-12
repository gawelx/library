package com.restapi.library.repository;

import com.restapi.library.domain.Person;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.restapi.library.domain.PersonStatus.ACTIVE;
import static com.restapi.library.domain.PersonStatus.DELETED;
import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({
        "/sql/insertPersons.sql",
        "/sql/insertBookTitles.sql"
})
@Transactional
public class PersonRepositoryTestSuite {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testFindAllByStatus() {
        //Given
        Person person1 = personRepository.getOne(1L);
        Person person2 = personRepository.getOne(2L);
        Person person3 = personRepository.getOne(3L);
        Person person4 = personRepository.getOne(4L);

        //When
        List<Person> retrievedPersonsActive = personRepository.findAllByStatus(ACTIVE);
        List<Person> retrievedPersonsDeleted = personRepository.findAllByStatus(DELETED);

        //Then
        assertThat(retrievedPersonsActive, containsInAnyOrder(
                Hibernate.unproxy(person1),
                Hibernate.unproxy(person2)
        ));
        assertThat(retrievedPersonsDeleted, containsInAnyOrder(
                Hibernate.unproxy(person3),
                Hibernate.unproxy(person4)
        ));
    }

    @Test
    public void testFindByIdAndStatus() {
        //Given
        Person person1 = personRepository.getOne(1L);

        //When
        Optional<Person> retrievedPerson1 = personRepository.findByIdAndStatus(1L, ACTIVE);
        Optional<Person> retrievedPerson2 = personRepository.findByIdAndStatus(3L, ACTIVE);

        //Then
        assertThat(retrievedPerson1, is(optionalWithValue(equalTo(Hibernate.unproxy(person1)))));
        assertThat(retrievedPerson2, is(emptyOptional()));
    }

    @Test
    public void testFindAllByBookTitlesContains() {
        //Given
        Person person1 = personRepository.getOne(1L);
        Person person2 = personRepository.getOne(4L);

        //When
        Set<Person> retrievedPersons = personRepository.findAllByBookTitlesContains(12L);

        //Then
        assertThat(retrievedPersons, containsInAnyOrder(
                Hibernate.unproxy(person1),
                Hibernate.unproxy(person2)
        ));
    }

    @Test
    public void testFindAllByBookTitlesNotEmpty() {
        //Given
        Person person1 = personRepository.getOne(1L);
        Person person2 = personRepository.getOne(2L);
        Person person3 = personRepository.getOne(4L);

        //When
        List<Person> retrievedPersons = personRepository.findAllByBookTitlesNotEmpty();

        //Then
        assertThat(retrievedPersons, containsInAnyOrder(
                Hibernate.unproxy(person1),
                Hibernate.unproxy(person2),
                Hibernate.unproxy(person3)
        ));
    }

    @Test
    public void testFindByIdAndBookTitlesNotEmpty() {
        //Given
        Person person1 = personRepository.getOne(1L);

        //When
        Optional<Person> retrievedPerson1 = personRepository.findByIdAndBookTitlesNotEmpty(1L);
        Optional<Person> retrievedPerson2 = personRepository.findByIdAndBookTitlesNotEmpty(3L);

        //Then
        assertThat(retrievedPerson1, is(optionalWithValue(equalTo(Hibernate.unproxy(person1)))));
        assertThat(retrievedPerson2, is(emptyOptional()));
    }

}
