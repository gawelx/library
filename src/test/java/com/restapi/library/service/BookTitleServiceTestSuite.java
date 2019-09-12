package com.restapi.library.service;

import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Person;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.restapi.library.domain.PersonStatus.ACTIVE;
import static com.restapi.library.domain.PersonStatus.DELETED;
import static com.restapi.library.service.CopyService.copyBookTitle;
import static com.restapi.library.service.CopyService.copyPerson;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookTitleServiceTestSuite {

    private static BookTitle bookTitle1, bookTitle2, bookTitle3;
    private static Person author1, author2, author3;

    @InjectMocks
    private BookTitleService bookTitleService;
    @Mock
    private BookTitleRepository bookTitleRepository;
    @Mock
    private PersonRepository personRepository;

    @BeforeClass
    public static void beforeAllTests() {
        author1 = new Person(1L, ACTIVE, "John", "Smith");
        author2 = new Person(2L, DELETED, "Jane", "Doe");
        author3 = new Person(3L, ACTIVE, "Jim", "Beam");
        bookTitle1 = new BookTitle(11L, "Gone with the Wind", new HashSet<>(Collections.singletonList(author1)));
        bookTitle2 = new BookTitle(12L, "The Witcher", new HashSet<>(Collections.singletonList(author3)));
        bookTitle3 = new BookTitle(13L, "Harry Potter", new HashSet<>(Arrays.asList(author1, author2)));
    }

    @Test
    public void should_ReturnAllBookTitles() {
        //Given
        List<BookTitle> bookTitles = Stream.of(bookTitle1, bookTitle2, bookTitle3)
                .map(CopyService::copyBookTitle)
                .collect(Collectors.toList());
        when(bookTitleRepository.findAll()).thenReturn(bookTitles);

        //When
        List<BookTitle> retrievedBookTitles = bookTitleService.getAllBookTitles();

        //Then
        assertThat(retrievedBookTitles, containsInAnyOrder(bookTitle1, bookTitle2, bookTitle3));
    }

    @Test
    public void should_ReturnBookTitle_IfRequestedBookTitlesIdIsValid() {
        //Given
        BookTitle bookTitle = copyBookTitle(bookTitle1);
        when(bookTitleRepository.findById(11L)).thenReturn(Optional.of(bookTitle));

        //When
        BookTitle retrievedBookTitle = bookTitleService.getBookTitle(11L);

        //Then
        assertThat(retrievedBookTitle, is(equalTo(bookTitle1)));
    }

    @Test
    public void should_ThrowNotFoundException_IfRequestedBookTitlesIdIsInvalid() {
        //Given
        when(bookTitleRepository.findById(11L)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            bookTitleService.getBookTitle(11L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_ReturnAllAuthorsBookTitles_IfRequestedAuthorsIdIsValid() {
        //Given
        Person author = copyPerson(author1);
        List<BookTitle> bookTitles = Stream.of(bookTitle1, bookTitle3)
                .map(CopyService::copyBookTitle)
                .collect(Collectors.toList());
        when(personRepository.findByIdAndBookTitlesNotEmpty(1L)).thenReturn(Optional.of(author));
        when(bookTitleRepository.findAllByAuthorsContains(author)).thenReturn(bookTitles);

        //When
        List<BookTitle> retrievedBookTitles = bookTitleService.getAllBookTitlesOfAuthor(1L);

        //Then
        assertThat(retrievedBookTitles, containsInAnyOrder(bookTitle1, bookTitle3));
    }

    @Test
    public void should_ThrowNotFoundException_IfRequestedAuthorsIdIsInvalid() {
        //Given
        when(personRepository.findByIdAndBookTitlesNotEmpty(1L)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            bookTitleService.getAllBookTitlesOfAuthor(1L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
        verify(personRepository, only()).findByIdAndBookTitlesNotEmpty(1L);
    }

    @Test
    public void should_CreateBookTitle() {
        //Given
        BookTitle newBookTitle = new BookTitle(5L, bookTitle1.getTitle(), bookTitle1.getAuthors().stream()
                .map(CopyService::copyPerson)
                .collect(Collectors.toSet()));
        BookTitle createdBookTitle = copyBookTitle(bookTitle1);
        when(bookTitleRepository.save(newBookTitle)).thenReturn(createdBookTitle);

        //When
        BookTitle retrievedBookTitle = bookTitleService.createBookTitle(newBookTitle);

        //Then
        assertThat(retrievedBookTitle, is(equalTo(bookTitle1)));
    }

    @Test
    public void should_AddBookTitlesAuthor_IfAuthorIsNotBookTitlesAuthorAlready() {
        //Given
        BookTitle bookTitle = copyBookTitle(bookTitle1);
        Person newAuthor = copyPerson(author2);
        when(bookTitleRepository.save(bookTitle)).thenReturn(bookTitle);

        //When
        BookTitle retrievedBookTitle = bookTitleService.addAuthor(bookTitle, newAuthor);

        //Then
        assertThat(retrievedBookTitle.getId(), is(bookTitle1.getId()));
        assertThat(retrievedBookTitle.getTitle(), is(bookTitle1.getTitle()));
        assertThat(retrievedBookTitle.getAuthors(), containsInAnyOrder(author1, author2));
    }

    @Test
    public void should_DoNothing_IfAuthorAlreadyIsBookTitlesAuthor() {
        //Given
        BookTitle bookTitle = copyBookTitle(bookTitle1);
        Person newAuthor = copyPerson(author1);
        when(bookTitleRepository.save(bookTitle)).thenReturn(bookTitle);

        //When
        BookTitle retrievedBookTitle = bookTitleService.addAuthor(bookTitle, newAuthor);

        //Then
        assertThat(retrievedBookTitle, is(equalTo(bookTitle1)));
    }

    @Test
    public void should_UpdateBookTitle_IfUpdatedBookTitlesIdIsValid() {
        //Given
        BookTitle bookTitle = copyBookTitle(bookTitle1);
        when(bookTitleRepository.existsById(11L)).thenReturn(true);
        when(bookTitleRepository.save(bookTitle)).thenReturn(bookTitle);

        //When
        BookTitle retrievedBookTitle = bookTitleService.updateBookTitle(bookTitle);

        //Then
        assertThat(retrievedBookTitle, is(equalTo(bookTitle1)));
    }

    @Test
    public void should_ThrowNotFoundException_IfUpdatedBookTitlesIdIsInvalid() {
        //Given
        BookTitle bookTitle = copyBookTitle(bookTitle1);
        when(bookTitleRepository.existsById(11L)).thenReturn(false);

        //When
        Exception thrownException = null;
        try {
            bookTitleService.updateBookTitle(bookTitle);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
        verify(bookTitleRepository, only()).existsById(11L);
    }

    @Test
    public void should_RemoveBookTitlesAuthor_IfAuthorIsBookTitlesAuthor() {
        //Given
        BookTitle bookTitle = copyBookTitle(bookTitle3);
        Person author = copyPerson(author2);
        when(bookTitleRepository.save(bookTitle)).thenReturn(bookTitle);

        //When
        BookTitle retrievedBookTitle = bookTitleService.removeAuthor(bookTitle, author);

        //Then
        assertThat(retrievedBookTitle.getAuthors(), contains(author1));
    }

    @Test
    public void should_DoNothing_IfAuthorIsNotBookTitlesAuthor() {
        //Given
        BookTitle bookTitle = copyBookTitle(bookTitle3);
        Person author = copyPerson(author3);
        when(bookTitleRepository.save(bookTitle)).thenReturn(bookTitle);

        //When
        BookTitle retrievedBookTitle = bookTitleService.removeAuthor(bookTitle, author);

        //Then
        assertThat(retrievedBookTitle, is(equalTo(bookTitle3)));
    }

}
