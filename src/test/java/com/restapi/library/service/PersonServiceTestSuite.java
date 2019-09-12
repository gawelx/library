package com.restapi.library.service;

import com.restapi.library.domain.Person;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.PersonRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.restapi.library.domain.PersonStatus.ACTIVE;
import static com.restapi.library.domain.PersonStatus.DELETED;
import static com.restapi.library.service.CopyService.copyPerson;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTestSuite {

    private static Person person1, person2, person3;

    @InjectMocks
    private PersonService personService;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private BorrowerService borrowerService;

    @BeforeClass
    public static void beforeAllTests() {
        person1 = new Person(1L, ACTIVE, "John", "Smith");
        person2 = new Person(2L, ACTIVE, "Jane", "Doe");
        person3 = new Person(3L, ACTIVE, "Jim", "Beam");
    }

    @Test
    public void should_ReturnAllPersons() {
        //Given
        List<Person> persons = Stream.of(person1, person2, person3)
                .map(CopyService::copyPerson)
                .collect(Collectors.toList());
        when(personRepository.findAllByStatus(ACTIVE)).thenReturn(persons);

        //When
        List<Person> retrievedPersons = personService.getAllPersons();

        //Then
        assertThat(retrievedPersons, containsInAnyOrder(person1, person2, person3));
    }

    @Test
    public void should_ReturnPerson_IfRequestedPersonIdIsValid() {
        //Given
        Person person = copyPerson(person1);
        when(personRepository.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(person));

        //When
        Person retrievedPerson = personService.getPerson(1L);

        //Then
        assertThat(retrievedPerson, is(equalTo(person1)));
    }

    @Test
    public void should_ThrowNotFoundException_IfRequestedPersonIdIsInvalid() {
        //Given
        when(personRepository.findByIdAndStatus(5L, ACTIVE)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            personService.getPerson(5L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_CreatePerson() {
        //Given
        Person newPerson = new Person(5L, person1.getStatus(), person1.getFirstName(), person1.getLastName());
        Person newPersonWithId = copyPerson(person1);
        when(personRepository.save(newPerson)).thenReturn(newPersonWithId);

        //When
        Person retrievedPerson = personService.createPerson(newPerson);

        //Then
        assertThat(retrievedPerson, is(equalTo(person1)));
    }

    @Test
    public void should_UpdatePerson_IdUpdatedPersonIdIsValid() {
        //Given
        Person newPerson = copyPerson(person1);
        when(personRepository.save(newPerson)).thenReturn(newPerson);

        //When
        Person retrievedPerson = personService.updatePerson(newPerson);

        //Then
        assertThat(retrievedPerson, is(equalTo(person1)));
    }

    @Test
    public void should_ThrowNotFoundException_IfUpdatedPersonIdIsInvalid() {
        //Given
        Person newPerson = new Person(null, person1.getStatus(), person1.getFirstName(), person1.getLastName());

        //When
        Exception thrownException = null;
        try {
            personService.updatePerson(newPerson);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_DeletePerson_IfDeletedPersonIdIsValid() {
        //Given
        Person deletedPerson = copyPerson(person1);
        when(personRepository.findById(1L)).thenReturn(Optional.of(deletedPerson));

        //When
        personService.deletePerson(1L);

        //Then
        assertThat(deletedPerson.getStatus(), is(DELETED));
        verify(borrowerService).deleteBorrower(1L);
        verify(personRepository).save(deletedPerson);
    }

    @Test
    public void should_DoNothing_IfDeletedPersonIdIsInvalid() {
        //Given
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        //When
        personService.deletePerson(1L);

        //Then
        verify(personRepository, only()).findById(1L);
    }

}
