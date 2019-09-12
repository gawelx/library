package com.restapi.library.service;

import com.restapi.library.domain.Book;
import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Person;
import com.restapi.library.exception.ConflictException;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.BookRepository;
import com.restapi.library.repository.BookTitleRepository;
import com.restapi.library.repository.PersonRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.restapi.library.domain.BookStatus.AVAILABLE;
import static com.restapi.library.domain.BookStatus.BORROWED;
import static com.restapi.library.domain.BookStatus.LOST;
import static com.restapi.library.domain.PersonStatus.ACTIVE;
import static com.restapi.library.service.CopyService.copyBook;
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
public class BookServiceTestSuite {

    private static Book book1, book2, book3;
    private static Person author;

    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookTitleRepository bookTitleRepository;
    @Mock
    private PersonRepository personRepository;

    @BeforeClass
    public static void beforeAllTests() {
        author = new Person(1L, ACTIVE, "John", "Smith");
        BookTitle bookTitle = new BookTitle(11L, "Gone with the Wind",
                new HashSet<>(Collections.singletonList(author)));
        book1 = new Book(21L, 2000, BigDecimal.valueOf(25), AVAILABLE, bookTitle);
        book2 = new Book(22L, 2005, BigDecimal.valueOf(32), BORROWED, bookTitle);
        book3 = new Book(23L, 2019, BigDecimal.valueOf(50), LOST, bookTitle);
    }

    @Test
    public void should_ReturnAllBooks_WhenRequestedBookStatusIsNull() {
        //Given
        List<Book> books = Stream.of(book1, book2, book3)
                .map(CopyService::copyBook)
                .collect(Collectors.toList());
        when(bookRepository.findAll()).thenReturn(books);

        //When
        List<Book> retrievedBooks = bookService.getAllBooks(null);

        //Then
        assertThat(retrievedBooks, containsInAnyOrder(book1, book2, book3));
    }

    @Test
    public void should_ReturnBooksWithRequiredStatus_WhenRequestedBookStatusIsNotNull() {
        //Given
        List<Book> books = Stream.of(book1)
                .map(CopyService::copyBook)
                .collect(Collectors.toList());
        when(bookRepository.findAllByStatus(AVAILABLE)).thenReturn(books);

        //When
        List<Book> retrievedBooks = bookService.getAllBooks(AVAILABLE);

        //Then
        assertThat(retrievedBooks, contains(book1));
    }

    @Test
    public void should_ReturnBook_IfRequestedBooksIdIsValid() {
        //Given
        Book book = copyBook(book1);
        when(bookRepository.findById(21L)).thenReturn(Optional.of(book));

        //When
        Book retrievedBook = bookService.getBook(21L);

        //Then
        assertThat(retrievedBook, is(equalTo(book1)));
    }

    @Test
    public void should_ThrowNotFoundException_IfRequestedBooksIdIsInvalid() {
        //Given
        when(bookRepository.findById(21L)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            bookService.getBook(21L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_ReturnAvailableBook_IfRequestedBooksIdIsValidAndBookIsAvailable() {
        //Given
        Book book = copyBook(book1);
        when(bookRepository.findBookByIdAndStatus(21L, AVAILABLE)).thenReturn(Optional.of(book));

        //When
        Book retrievedBook = bookService.getAvailableBook(21L);

        //Then
        assertThat(retrievedBook, is(equalTo(book1)));
    }

    @Test
    public void should_ThrowConflictException_IfRequestedBooksIdIsInvalidOrBookIsNotAvailable() {
        //Given
        when(bookRepository.findBookByIdAndStatus(21L, AVAILABLE)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            bookService.getAvailableBook(21L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(ConflictException.class)));
    }

    @Test
    public void should_ReturnAllBookTitlesBooks_WhenRequestedBookTitlesIdIsValid() {
        //Given
        List<Book> books = Stream.of(book1, book2, book3)
                .map(CopyService::copyBook)
                .collect(Collectors.toList());
        when(bookTitleRepository.existsById(11L)).thenReturn(true);
        when(bookRepository.findAllByBookTitleId(11L)).thenReturn(books);

        //When
        List<Book> retrievedBooks = bookService.getAllBooksOfBookTitle(11L);

        //Then
        assertThat(retrievedBooks, containsInAnyOrder(book1, book2, book3));
    }

    @Test
    public void should_ThrowNotFoundException_IfRequestedBookTitlesIdIsInvalid() {
        //Given
        when(bookTitleRepository.existsById(11L)).thenReturn(false);

        //When
        Exception thrownException = null;
        try {
            bookService.getAllBooksOfBookTitle(11L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
        verify(bookTitleRepository, only()).existsById(11L);
    }

    @Test
    public void should_ReturnAllAuthorsBooks_WhenRequestedAuthorsIdIsValid() {
        //Given
        List<Book> books = Stream.of(book1, book2, book3)
                .map(CopyService::copyBook)
                .collect(Collectors.toList());
        Person author = copyPerson(BookServiceTestSuite.author);
        when(personRepository.findByIdAndBookTitlesNotEmpty(1L)).thenReturn(Optional.of(author));
        when(bookRepository.findAllByBookTitleAuthorsContains(author)).thenReturn(books);

        //When
        List<Book> retrievedBooks = bookService.getAllBooksOfAuthor(1L);

        //Then
        assertThat(retrievedBooks, containsInAnyOrder(book1, book2, book3));
    }

    @Test
    public void should_ThrowNotFoundException_IfRequestedAuthorsIdIsInvalid() {
        //Given
        when(personRepository.findByIdAndBookTitlesNotEmpty(1L)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            bookService.getAllBooksOfAuthor(1L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
        verify(personRepository, only()).findByIdAndBookTitlesNotEmpty(1L);
    }

    @Test
    public void should_CreateAvailableBook() {
        //Given
        Book newBook = new Book(2L, book1.getReleaseYear(), book1.getPrice(), LOST,
                copyBookTitle(book1.getBookTitle()));
        Book newBookWithId = copyBook(book1);
        when(bookRepository.save(newBook)).thenReturn(newBookWithId);

        //When
        Book retrievedBook = bookService.createBook(newBook);

        //Then
        assertThat(retrievedBook, is(equalTo(book1)));
    }

    @Test
    public void should_UpdateBooksStatus_IfUpdatedBooksStatusPropertyIsNotNull() {
        //Given
        Book updatedBook = new Book(21L, null, null, book1.getStatus(), null);
        Book persistedBook = new Book(21L, book1.getReleaseYear(), book1.getPrice(), BORROWED,
                copyBookTitle(book1.getBookTitle()));
        when(bookRepository.findById(21L)).thenReturn(Optional.of(persistedBook));
        when(bookRepository.save(persistedBook)).thenReturn(persistedBook);

        //When
        Book retrievedBook = bookService.updateBook(updatedBook);

        //Then
        assertThat(retrievedBook, is(equalTo(book1)));
    }

    @Test
    public void should_UpdateBooksPrice_IfUpdatedBooksPricePropertyIsNotNull() {
        //Given
        Book updatedBook = new Book(21L, null, book1.getPrice(), null, null);
        Book persistedBook = new Book(21L, book1.getReleaseYear(), BigDecimal.valueOf(30), book1.getStatus(),
                copyBookTitle(book1.getBookTitle()));
        when(bookRepository.findById(21L)).thenReturn(Optional.of(persistedBook));
        when(bookRepository.save(persistedBook)).thenReturn(persistedBook);

        //When
        Book retrievedBook = bookService.updateBook(updatedBook);

        //Then
        assertThat(retrievedBook, is(equalTo(book1)));
    }

    @Test
    public void should_UpdateBooksReleaseYear_IfUpdatedBooksReleaseYearPropertyIsNotNull() {
        //Given
        Book updatedBook = new Book(21L, book1.getReleaseYear(), null, null, null);
        Book persistedBook = new Book(21L, 3000, book1.getPrice(), book1.getStatus(),
                copyBookTitle(book1.getBookTitle()));
        when(bookRepository.findById(21L)).thenReturn(Optional.of(persistedBook));
        when(bookRepository.save(persistedBook)).thenReturn(persistedBook);

        //When
        Book retrievedBook = bookService.updateBook(updatedBook);

        //Then
        assertThat(retrievedBook, is(equalTo(book1)));
    }

    @Test
    public void should_DoNothingWithBooksStatusAndPriceAndReleaseYear_IfUpdatedBooksStatusAndPriceAndReleaseYearPropertiesAreNull() {
        //Given
        Book updatedBook = new Book(21L, null, null, null, null);
        Book persistedBook = copyBook(book1);
        when(bookRepository.findById(21L)).thenReturn(Optional.of(persistedBook));
        when(bookRepository.save(persistedBook)).thenReturn(persistedBook);

        //When
        Book retrievedBook = bookService.updateBook(updatedBook);

        //Then
        assertThat(retrievedBook, is(equalTo(book1)));
    }

    @Test
    public void should_ThrowNotFoundException_IfUpdatedBooksIdIsNotValid() {
        //Given
        Book updatedBook = copyBook(book1);
        when(bookRepository.findById(21L)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            bookService.updateBook(updatedBook);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
        verify(bookRepository, only()).findById(21L);
    }

}
