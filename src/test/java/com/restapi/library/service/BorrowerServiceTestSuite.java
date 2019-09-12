package com.restapi.library.service;

import com.restapi.library.domain.Borrower;
import com.restapi.library.domain.Person;
import com.restapi.library.exception.ConflictException;
import com.restapi.library.exception.NotFoundException;
import com.restapi.library.repository.BorrowerRepository;
import com.restapi.library.repository.BorrowingRepository;
import com.restapi.library.repository.PenaltyRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.restapi.library.domain.PersonStatus.ACTIVE;
import static com.restapi.library.domain.PersonStatus.DELETED;
import static com.restapi.library.service.CopyService.copyBorrower;
import static org.exparity.hamcrest.date.LocalDateTimeMatchers.after;
import static org.exparity.hamcrest.date.LocalDateTimeMatchers.within;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BorrowerServiceTestSuite {

    private static Borrower borrower1, borrower2, borrower3;

    @InjectMocks
    private BorrowerService borrowerService;
    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private PenaltyRepository penaltyRepository;
    @Mock
    private BorrowingRepository borrowingRepository;

    @BeforeClass
    public static void beforeAllTests() {
        Person person1 = new Person(1L, ACTIVE, "John", "Smith");
        Person person2 = new Person(2L, ACTIVE, "Jane", "Doe");
        Person person3 = new Person(3L, ACTIVE, "Jim", "Beam");
        borrower1 = new Borrower(person1.getId(), ACTIVE, LocalDateTime.now(), person1);
        borrower2 = new Borrower(person2.getId(), ACTIVE, LocalDateTime.now(), person2);
        borrower3 = new Borrower(person3.getId(), ACTIVE, LocalDateTime.now(), person3);
    }

    @Test
    public void should_ReturnAllBorrowers() {
        //Given
        List<Borrower> borrowers = Stream.of(borrower1, borrower2, borrower3)
                .map(CopyService::copyBorrower)
                .collect(Collectors.toList());
        when(borrowerRepository.findAllByStatus(ACTIVE)).thenReturn(borrowers);

        //When
        List<Borrower> retrievedBorrowers = borrowerService.getAllBorrowers();

        //Then
        assertThat(retrievedBorrowers, containsInAnyOrder(borrower1, borrower2, borrower3));
    }

    @Test
    public void should_ReturnBorrower_IfRequestedBorrowerIdIsValid() {
        //Given
        Borrower borrower = copyBorrower(borrower1);
        when(borrowerRepository.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(borrower));

        //When
        Borrower retrievedBorrower = borrowerService.getBorrower(1L);

        //Then
        assertThat(retrievedBorrower, is(equalTo(borrower1)));
    }

    @Test
    public void should_ThrowNotFoundException_IfRequestedBorrowerIdIsInvalid() {
        //Given
        when(borrowerRepository.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            borrowerService.getBorrower(1L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_CreateBorrower_IfBorrowerWithTheSameIdDoesntExist() {
        //Given
        Borrower newBorrower = copyBorrower(borrower1);
        when(borrowerRepository.existsByIdAndStatus(1L, ACTIVE)).thenReturn(false);
        when(borrowerRepository.save(newBorrower)).thenReturn(newBorrower);

        //When
        Borrower returnedBorrower = borrowerService.createBorrower(newBorrower);

        //Then
        assertThat(returnedBorrower.getId(), is(borrower1.getId()));
        assertThat(returnedBorrower.getStatus(), is(ACTIVE));
        assertThat(returnedBorrower.getPerson(), is(equalTo(borrower1.getPerson())));
        assertThat(returnedBorrower.getAccountCreationDateTime(), is(after(borrower1.getAccountCreationDateTime())));
        assertThat(returnedBorrower.getAccountCreationDateTime(), is(within(1, ChronoUnit.MINUTES, borrower1.getAccountCreationDateTime())));
    }

    @Test
    public void should_ThrowConflictException_IfBorrowerWithTheSameIdExists() {
        //Given
        Borrower newBorrower = copyBorrower(borrower1);
        when(borrowerRepository.existsByIdAndStatus(1L, ACTIVE)).thenReturn(true);

        //When
        Exception thrownException = null;
        try {
            borrowerService.createBorrower(newBorrower);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(ConflictException.class)));
        verify(borrowerRepository, only()).existsByIdAndStatus(1L, ACTIVE);
    }

    @Test
    public void should_deleteBorrower_IfDeletedBorrowerExistsAndThereAreNoUnpaidBorrowersPenaltiesAndThereAreNoPendingBorrowersBorrowings() {
        //Given
        Borrower deletedBorrower = copyBorrower(borrower1);
        when(borrowerRepository.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(deletedBorrower));
        when(penaltyRepository.existsByBorrowingBorrowerIdAndPaid(1L, false)).thenReturn(false);
        when(borrowingRepository.existsByBorrowerIdAndReturnDateIsNull(1L)).thenReturn(false);

        //When
        borrowerService.deleteBorrower(1L);

        //Then
        assertThat(deletedBorrower.getStatus(), is(DELETED));
        verify(borrowerRepository).save(deletedBorrower);
    }

    @Test
    public void should_ThrowConflictException_IfDeletedBorrowerExistsAndThereAreUnpaidBorrowersPenalties() {
        //Given
        Borrower deletedBorrower = copyBorrower(borrower1);
        when(borrowerRepository.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(deletedBorrower));
        when(penaltyRepository.existsByBorrowingBorrowerIdAndPaid(1L, false)).thenReturn(true);

        //When
        Exception thrownException = null;
        try {
            borrowerService.deleteBorrower(1L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(ConflictException.class)));
        verify(borrowerRepository, never()).save(deletedBorrower);
    }

    @Test
    public void should_ThrowConflictException_IfDeletedBorrowerExistsAndThereArePendingBorrowersBorrowings() {
        //Given
        Borrower deletedBorrower = copyBorrower(borrower1);
        when(borrowerRepository.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.of(deletedBorrower));
        when(penaltyRepository.existsByBorrowingBorrowerIdAndPaid(1L, false)).thenReturn(false);
        when(borrowingRepository.existsByBorrowerIdAndReturnDateIsNull(1L)).thenReturn(true);

        //When
        Exception thrownException = null;
        try {
            borrowerService.deleteBorrower(1L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(ConflictException.class)));
        verify(borrowerRepository, never()).save(deletedBorrower);
    }

    @Test
    public void should_DoNothing_IfDeletedBorrowerDoesntExists() {
        //Given
        when(borrowerRepository.findByIdAndStatus(1L, ACTIVE)).thenReturn(Optional.empty());

        //When
        borrowerService.deleteBorrower(1L);

        //Then
        verify(borrowerRepository, only()).findByIdAndStatus(1L, ACTIVE);
    }

}
