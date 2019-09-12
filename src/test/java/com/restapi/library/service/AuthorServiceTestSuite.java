package com.restapi.library.service;

import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Person;
import com.restapi.library.domain.PersonStatus;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.BookTitleRepository;
import com.restapi.library.repository.PersonRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.restapi.library.service.CopyService.copyBookTitle;
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
public class AuthorServiceTestSuite {

    private static Person person1, person2, person3;
    private static BookTitle bookTitle;

    @InjectMocks
    private AuthorService authorService;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private BookTitleRepository bookTitleRepository;

    @BeforeClass
    public static void beforeAllTests() {
        person1 = new Person(1L, PersonStatus.ACTIVE, "John", "Smith");
        person2 = new Person(2L, PersonStatus.ACTIVE, "Jane", "Doe");
        person3 = new Person(3L, PersonStatus.ACTIVE, "Jim", "Beam");
        bookTitle = new BookTitle(10L, "Gone with the Wind", new HashSet<>(Arrays.asList(person1, person2)));
    }

    @Test
    public void should_ReturnAllAuthors() {
        //Given
        List<Person> authors = Stream.of(person1, person2, person3)
                .map(CopyService::copyPerson)
                .collect(Collectors.toList());
        when(personRepository.findAllByBookTitlesNotEmpty()).thenReturn(authors);

        //When
        List<Person> retrievedAuthors = authorService.getAllAuthors();

        //Then
        assertThat(retrievedAuthors, containsInAnyOrder(person1, person2, person3));
    }

    @Test
    public void should_ReturnAuthor_IfRequestedAuthorIdIsValid() {
        //Given
        Person person = copyPerson(person1);
        when(personRepository.findByIdAndBookTitlesNotEmpty(1L)).thenReturn(Optional.of(person));

        //When
        Person retrievedAuthor = authorService.getAuthor(1L);

        //Then
        assertThat(retrievedAuthor, is(equalTo(person1)));
    }

    @Test
    public void should_ThrowNotFoundException_IfRequestedAuthorIdIsInvalid() {
        //Given
        when(personRepository.findByIdAndBookTitlesNotEmpty(1L)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            authorService.getAuthor(1L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_ReturnAllAuthorsOfBookTitle_IfRequestedBookTitleIsValid() {
        //Given
        Set<Person> authors = Stream.of(person1, person2)
                .map(CopyService::copyPerson)
                .collect(Collectors.toSet());
        BookTitle bookTitle = copyBookTitle(AuthorServiceTestSuite.bookTitle);
        when(bookTitleRepository.findById(10L)).thenReturn(Optional.of(bookTitle));
        when(personRepository.findAllByBookTitlesContains(10L)).thenReturn(authors);

        //When
        Set<Person> retrievedAuthors = authorService.getAllAuthorsOfBookTitle(10L);

        //Then
        assertThat(retrievedAuthors, containsInAnyOrder(person1, person2));
    }

    @Test
    public void should_ThrowNotFoundException_IfRequestedBookTitleIsInvalid() {
        //Given
        when(bookTitleRepository.findById(11L)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            authorService.getAllAuthorsOfBookTitle(11L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
        verify(bookTitleRepository, only()).findById(11L);
    }

}