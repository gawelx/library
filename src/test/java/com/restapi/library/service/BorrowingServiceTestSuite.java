package com.restapi.library.service;

import com.restapi.library.domain.Book;
import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Borrower;
import com.restapi.library.domain.Borrowing;
import com.restapi.library.domain.Person;
import com.restapi.library.exception.BadRequestException;
import com.restapi.library.exception.ConflictException;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.BookRepository;
import com.restapi.library.repository.BorrowerRepository;
import com.restapi.library.repository.BorrowingRepository;
import com.restapi.library.repository.PenaltyRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.restapi.library.controller.BorrowingAction.BOOK_RETURN;
import static com.restapi.library.controller.BorrowingAction.BORROWING_PERIOD_UPDATE;
import static com.restapi.library.domain.BookStatus.AVAILABLE;
import static com.restapi.library.domain.BookStatus.BORROWED;
import static com.restapi.library.domain.PersonStatus.ACTIVE;
import static com.restapi.library.service.CopyService.copyBook;
import static com.restapi.library.service.CopyService.copyBookTitle;
import static com.restapi.library.service.CopyService.copyBorrower;
import static com.restapi.library.service.CopyService.copyBorrowing;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BorrowingServiceTestSuite {

    private static Borrowing borrowing1, borrowing2, borrowing3;
    private static Book book2;

    @InjectMocks
    private BorrowingService borrowingService;
    @Mock
    private BorrowingRepository borrowingRepository;
    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private PenaltyRepository penaltyRepository;
    @Mock
    private PenaltiesFactory penaltiesFactory;

    @BeforeClass
    public static void beforeAllTests() {
        Person person = new Person(1L, ACTIVE, "John", "Smith");
        Borrower borrower = new Borrower(1L, ACTIVE, LocalDateTime.now(), person);
        Person author = new Person(2L, ACTIVE, "Jane", "Doe");
        BookTitle bookTitle = new BookTitle(11L, "Gone with the Wind",
                new HashSet<>(Collections.singletonList(author)));
        Book book1 = new Book(21L, 2000, BigDecimal.valueOf(20), AVAILABLE, bookTitle);
        book2 = new Book(22L, 2019, BigDecimal.valueOf(35), BORROWED, bookTitle);
        borrowing1 = new Borrowing(
                41L,
                LocalDate.now().minus(15L, ChronoUnit.DAYS),
                30,
                LocalDate.now(),
                borrower,
                book1
        );
        borrowing2 = new Borrowing(
                42L,
                LocalDate.of(2019, 6, 1),
                30,
                LocalDate.of(2019, 7, 10),
                borrower,
                book2
        );
        borrowing3 = new Borrowing(
                43L,
                LocalDate.now(),
                30,
                null,
                borrower,
                book2
        );
    }

    @Test
    public void should_ReturnAllBorrowings_IfCategoryIsAll() {
        //Given
        List<Borrowing> borrowings = Stream.of(borrowing1, borrowing2, borrowing3)
                .map(CopyService::copyBorrowing)
                .collect(Collectors.toList());
        when(borrowingRepository.findAll()).thenReturn(borrowings);

        //When
        List<Borrowing> retrievedBorrowings = borrowingService.getAllBorrowings("all");

        //Then
        assertThat(retrievedBorrowings, containsInAnyOrder(borrowing1, borrowing2, borrowing3));
    }

    @Test
    public void should_ReturnPendingBorrowings_IfCategoryIsPending() {
        //Given
        List<Borrowing> borrowings = Stream.of(borrowing3)
                .map(CopyService::copyBorrowing)
                .collect(Collectors.toList());
        when(borrowingRepository.findAllByReturnDateIsNull()).thenReturn(borrowings);

        //When
        List<Borrowing> retrievedBorrowings = borrowingService.getAllBorrowings("pending");

        //Then
        assertThat(retrievedBorrowings, containsInAnyOrder(borrowing3));
    }

    @Test
    public void should_ReturnClosedBorrowings_IfCategoryIsClosed() {
        //Given
        List<Borrowing> borrowings = Stream.of(borrowing1, borrowing2)
                .map(CopyService::copyBorrowing)
                .collect(Collectors.toList());
        when(borrowingRepository.findAllByReturnDateIsNotNull()).thenReturn(borrowings);

        //When
        List<Borrowing> retrievedBorrowings = borrowingService.getAllBorrowings("closed");

        //Then
        assertThat(retrievedBorrowings, containsInAnyOrder(borrowing1, borrowing2));
    }

    @Test
    public void should_ThrowBadRequestException_IfCategoryIsInvalid() {
        //Given

        //When
        Exception thrownException = null;
        try {
            borrowingService.getAllBorrowings("xxx");
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(BadRequestException.class)));
    }

    @Test
    public void should_ReturnBorrowing_IfBorrowingsIdIsValid() {
        //Given
        Borrowing borrowing = copyBorrowing(borrowing1);
        when(borrowingRepository.findById(41L)).thenReturn(Optional.of(borrowing));

        //When
        Borrowing retrievedBorrowing = borrowingService.getBorrowing(41L);

        //Then
        assertThat(retrievedBorrowing, is(equalTo(borrowing1)));
    }

    @Test
    public void should_ThrowNotFoundException_IfBorrowingsIdIsInvalid() {
        //Given
        when(borrowingRepository.findById(41L)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            borrowingService.getBorrowing(41L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_ReturnAllBorrowersBorrowings_IfRequestedBorrowersIdIsValidAndCategoryIsAll() {
        //Given
        List<Borrowing> borrowings = Stream.of(borrowing1, borrowing2, borrowing3)
                .map(CopyService::copyBorrowing)
                .collect(Collectors.toList());
        when(borrowerRepository.existsByIdAndStatus(1L, ACTIVE)).thenReturn(true);
        when(borrowingRepository.findAllByBorrowerId(1L)).thenReturn(borrowings);

        //When
        List<Borrowing> retrievedBorrowings = borrowingService.getBorrowingsOfBorrower(1L, "all");

        //Then
        assertThat(retrievedBorrowings, containsInAnyOrder(borrowing1, borrowing2, borrowing3));
    }

    @Test
    public void should_ReturnPendingBorrowersBorrowings_IfRequestedBorrowersIdIsValidAndCategoryIsPending() {
        //Given
        List<Borrowing> borrowings = Stream.of(borrowing3)
                .map(CopyService::copyBorrowing)
                .collect(Collectors.toList());
        when(borrowerRepository.existsByIdAndStatus(1L, ACTIVE)).thenReturn(true);
        when(borrowingRepository.findAllByBorrowerIdAndReturnDateIsNull(1L)).thenReturn(borrowings);

        //When
        List<Borrowing> retrievedBorrowings = borrowingService.getBorrowingsOfBorrower(1L, "pending");

        //Then
        assertThat(retrievedBorrowings, containsInAnyOrder(borrowing3));
    }

    @Test
    public void should_ReturnClosedBorrowersBorrowings_IfRequestedBorrowersIdIsValidAndCategoryIsClosed() {
        //Given
        List<Borrowing> borrowings = Stream.of(borrowing1, borrowing2)
                .map(CopyService::copyBorrowing)
                .collect(Collectors.toList());
        when(borrowerRepository.existsByIdAndStatus(1L, ACTIVE)).thenReturn(true);
        when(borrowingRepository.findAllByBorrowerIdAndReturnDateIsNotNull(1L)).thenReturn(borrowings);

        //When
        List<Borrowing> retrievedBorrowings = borrowingService.getBorrowingsOfBorrower(1L, "closed");

        //Then
        assertThat(retrievedBorrowings, containsInAnyOrder(borrowing1, borrowing2));
    }

    @Test
    public void should_ThrowBadRequestException_IfRequestedBorrowersIdIsValidAndCategoryIsInvalid() {
        //Given
        when(borrowerRepository.existsByIdAndStatus(1L, ACTIVE)).thenReturn(true);

        //When
        Exception thrownException = null;
        try {
            borrowingService.getBorrowingsOfBorrower(1L, "xxx");
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(BadRequestException.class)));
    }

    @Test
    public void should_ThrowNotFoundException_IfRequestedBorrowersIdIsInvalid() {
        //Given
        when(borrowerRepository.existsByIdAndStatus(1L, ACTIVE)).thenReturn(false);

        //When
        Exception thrownException = null;
        try {
            borrowingService.getBorrowingsOfBorrower(1L, "all");
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_ReturnBooksBorrowings_IfRequestedBooksIdIsValid() {
        //Given
        List<Borrowing> borrowings = Stream.of(borrowing2, borrowing3)
                .map(CopyService::copyBorrowing)
                .collect(Collectors.toList());
        when(bookRepository.existsById(21L)).thenReturn(true);
        when(borrowingRepository.findAllByBookId(21L)).thenReturn(borrowings);

        //When
        List<Borrowing> retrievedBorrowings = borrowingService.getBorrowingsOfBook(21L);

        //Then
        assertThat(retrievedBorrowings, containsInAnyOrder(borrowing2, borrowing3));
    }

    @Test
    public void should_ThrowNotFoundException_IfRequestedBooksIdIsInvalid() {
        //Given
        when(bookRepository.existsById(21L)).thenReturn(false);

        //When
        Exception thrownException = null;
        try {
            borrowingService.getBorrowingsOfBook(21L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_CreateBorrowing() {
        //Given
        Borrowing newBorrowing = new Borrowing(
                49L,
                null,
                borrowing3.getBorrowingPeriod(),
                LocalDate.now(),
                copyBorrower(borrowing3.getBorrower()),
                new Book(
                        book2.getId(),
                        book2.getReleaseYear(),
                        book2.getPrice(),
                        AVAILABLE,
                        copyBookTitle(book2.getBookTitle())
                )
        );
        Borrowing newBorrowingWithId = copyBorrowing(borrowing3);
        when(borrowingRepository.save(newBorrowing)).thenReturn(newBorrowingWithId);

        //When
        Borrowing retrievedBorrowing = borrowingService.createBorrowing(newBorrowing);

        //Then
        assertThat(retrievedBorrowing, is(equalTo(borrowing3)));
    }

    @Test
    public void should_UpdateBorrowingsPeriod_IfUpdatedBorrowingsIdIsValidAndBorrowingIsPendingAndBorrowingActionIsPeriodUpdate() {
        //Given
        Borrowing changes = new Borrowing(
                borrowing3.getId(),
                null,
                borrowing3.getBorrowingPeriod(),
                null,
                null,
                null
        );
        Borrowing persistedBorrowing = new Borrowing(
                borrowing3.getId(),
                borrowing3.getBorrowingDate(),
                7,
                borrowing3.getReturnDate(),
                copyBorrower(borrowing3.getBorrower()),
                copyBook(borrowing3.getBook())
        );
        when(borrowingRepository.findById(43L)).thenReturn(Optional.of(persistedBorrowing));
        when(borrowingRepository.save(persistedBorrowing)).thenReturn(persistedBorrowing);

        //When
        Borrowing retrievedBorrowing = borrowingService.updateBorrowing(changes, BORROWING_PERIOD_UPDATE, null);

        //Then
        assertThat(retrievedBorrowing, is(equalTo(borrowing3)));
    }

    @Test
    public void should_UpdateBorrowingsReturnDateAndBooksStatus_IfUpdatedBorrowingsIdIsValidAndBorrowingIsPendingAndBorrowingActionIsReturnBook() {
        //Given
        Borrowing changes = new Borrowing(
                borrowing1.getId(),
                null,
                null,
                null,
                null,
                null
        );
        Borrowing persistedBorrowing = new Borrowing(
                borrowing1.getId(),
                borrowing1.getBorrowingDate(),
                borrowing1.getBorrowingPeriod(),
                null,
                copyBorrower(borrowing1.getBorrower()),
                new Book(
                        borrowing1.getBook().getId(),
                        borrowing1.getBook().getReleaseYear(),
                        borrowing1.getBook().getPrice(),
                        BORROWED,
                        copyBookTitle(borrowing1.getBook().getBookTitle())
                )
        );
        when(borrowingRepository.findById(41L)).thenReturn(Optional.of(persistedBorrowing));
        when(penaltiesFactory.createPenalties(persistedBorrowing)).thenReturn(Collections.emptyList());
        when(borrowingRepository.save(persistedBorrowing)).thenReturn(persistedBorrowing);

        //When
        Borrowing retrievedBorrowing = borrowingService.updateBorrowing(changes, BOOK_RETURN, "none");

        //Then
        assertThat(retrievedBorrowing, is(equalTo(borrowing1)));
        verify(penaltyRepository).saveAll(Collections.emptyList());
    }

    @Test
    public void should_ThrowNotFoundException_IfUpdatedBorrowingsIdIsInvalid() {
        //Given
        Borrowing changes = copyBorrowing(borrowing1);
        when(borrowingRepository.findById(41L)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            borrowingService.updateBorrowing(changes, null, null);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_ThrowConflictException_IfUpdatedBorrowingIsClosed() {
        //Given
        Borrowing changes = new Borrowing(
                borrowing2.getId(),
                null,
                null,
                null,
                null,
                null
        );
        Borrowing persistedBorrowing = copyBorrowing(borrowing2);
        when(borrowingRepository.findById(42L)).thenReturn(Optional.of(persistedBorrowing));

        //When
        Exception thrownException = null;
        try {
            borrowingService.updateBorrowing(changes, null, null);
        } catch (Exception e) {
            thrownException = e;
        }
        //Then
        assertThat(thrownException, is(instanceOf(ConflictException.class)));
    }

    @Test
    public void should_ThrowConflictException_IfUpdatedBorrowingsPeriodIsToShortAndBorrowingActionIsPeriodUpdate() {
        //Given
        Borrowing changes = new Borrowing(
                borrowing1.getId(),
                null,
                7,
                null,
                null,
                null
        );
        Borrowing persistedBorrowing = copyBorrowing(borrowing1);
        when(borrowingRepository.findById(41L)).thenReturn(Optional.of(persistedBorrowing));

        //When
        Exception thrownException = null;
        try {
            borrowingService.updateBorrowing(changes, BORROWING_PERIOD_UPDATE, null);
        } catch (Exception e) {
            thrownException = e;
        }
        //Then
        assertThat(thrownException, is(instanceOf(ConflictException.class)));
    }

    @Test
    public void should_ThrowBadRequestException_IfProblemTypeIsInvalidAndBorrowingActionIsBookReturn() {
        //Given
        Borrowing changes = new Borrowing(
                borrowing3.getId(),
                null,
                null,
                null,
                null,
                null
        );
        Borrowing persistedBorrowing = copyBorrowing(borrowing3);
        when(borrowingRepository.findById(43L)).thenReturn(Optional.of(persistedBorrowing));

        //When
        Exception thrownException = null;
        try {
            borrowingService.updateBorrowing(changes, BOOK_RETURN, "xxx");
        } catch (Exception e) {
            thrownException = e;
        }
        //Then
        assertThat(thrownException, is(instanceOf(BadRequestException.class)));
    }

}
