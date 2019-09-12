package com.restapi.library.service;

import com.restapi.library.domain.Book;
import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Borrower;
import com.restapi.library.domain.Borrowing;
import com.restapi.library.domain.Penalty;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.restapi.library.domain.BookStatus.DAMAGED;
import static com.restapi.library.domain.BookStatus.LOST;
import static com.restapi.library.domain.PenaltyCause.BOOK_DAMAGED;
import static com.restapi.library.domain.PenaltyCause.BOOK_LOST;
import static com.restapi.library.domain.PenaltyCause.DEADLINE_EXCEEDED;
import static com.restapi.library.domain.PersonStatus.ACTIVE;
import static com.restapi.library.service.CopyService.copyBorrowing;
import static com.restapi.library.service.CopyService.copyPenalty;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PenaltyServiceTestSuite {

    private static Penalty penalty1, penalty2, penalty3;

    @InjectMocks
    private PenaltyService penaltyService;
    @Mock
    private PenaltyRepository penaltyRepository;
    @Mock
    private BorrowingRepository borrowingRepository;
    @Mock
    private BorrowerRepository borrowerRepository;

    @BeforeClass
    public static void beforeAllTests() {
        Person person = new Person(1L, ACTIVE, "John", "Smith");
        Borrower borrower = new Borrower(1L, ACTIVE, LocalDateTime.now(), person);
        Person author = new Person(2L, ACTIVE, "Jane", "Doe");
        BookTitle bookTitle = new BookTitle(11L, "Gone with the Wind",
                new HashSet<>(Collections.singletonList(author)));
        Book book1 = new Book(21L, 2000, BigDecimal.valueOf(20), LOST, bookTitle);
        Book book2 = new Book(22L, 2000, BigDecimal.valueOf(20), DAMAGED, bookTitle);
        LocalDate borrowingDate = LocalDate.of(2019, 6, 1);
        LocalDate returnDate = LocalDate.of(2019, 6, 30);
        Borrowing borrowing1 = new Borrowing(41L, borrowingDate, 20, returnDate, borrower, book1);
        Borrowing borrowing2 = new Borrowing(42L, borrowingDate, 30, returnDate, borrower, book2);
        LocalDateTime createTime = LocalDateTime.of(2019, 6, 30, 8, 54, 23);
        penalty1 = new Penalty(51L, createTime, BOOK_LOST, BigDecimal.valueOf(20), FALSE, borrowing1);
        penalty2 = new Penalty(52L, createTime, DEADLINE_EXCEEDED, BigDecimal.valueOf(9), TRUE, borrowing1);
        penalty3 = new Penalty(53L, createTime, BOOK_DAMAGED, BigDecimal.valueOf(10), FALSE, borrowing2);
    }

    @Test
    public void should_ReturnAllPenalties_WhenPaidIsNull() {
        //Given
        List<Penalty> penalties = Stream.of(penalty1, penalty2, penalty3)
                .map(CopyService::copyPenalty)
                .collect(Collectors.toList());
        when(penaltyRepository.findAll()).thenReturn(penalties);

        //When
        List<Penalty> retrievedPenalties = penaltyService.getAllPenalties(null);

        //Then
        assertThat(retrievedPenalties, containsInAnyOrder(penalty1, penalty2, penalty3));
    }

    @Test
    public void should_ReturnUnpaidPenalties_WhenPaidIsFalse() {
        //Given
        List<Penalty> penalties = Stream.of(penalty1, penalty3)
                .map(CopyService::copyPenalty)
                .collect(Collectors.toList());
        when(penaltyRepository.findAllByPaid(false)).thenReturn(penalties);

        //When
        List<Penalty> retrievedPenalties = penaltyService.getAllPenalties(false);

        //Then
        assertThat(retrievedPenalties, containsInAnyOrder(penalty1, penalty3));
    }

    @Test
    public void should_ReturnPaidPenalties_WhenPaidIsTrue() {
        //Given
        List<Penalty> penalties = Stream.of(penalty2)
                .map(CopyService::copyPenalty)
                .collect(Collectors.toList());
        when(penaltyRepository.findAllByPaid(true)).thenReturn(penalties);

        //When
        List<Penalty> retrievedPenalties = penaltyService.getAllPenalties(true);

        //Then
        assertThat(retrievedPenalties, containsInAnyOrder(penalty2));
    }

    @Test
    public void should_ReturnPenalty_WhenRequestedPenaltyIdIsValid() {
        //Given
        Penalty penalty = copyPenalty(penalty1);
        when(penaltyRepository.findById(51L)).thenReturn(Optional.of(penalty));

        //When
        Penalty retrievedPenalty = penaltyService.getPenalty(51L);

        //Then
        assertThat(retrievedPenalty, is(equalTo(penalty1)));
    }

    @Test
    public void should_ThrowNotFoundException_WhenRequestedPenaltyIdIsInvalid() {
        //Given
        when(penaltyRepository.findById(51L)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            penaltyService.getPenalty(51L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_ReturnBorrowingsPenalties_WhenRequestedBorrowingsIdIsValid() {
        //Given
        List<Penalty> penalties = Stream.of(penalty1, penalty2)
                .map(CopyService::copyPenalty)
                .collect(Collectors.toList());
        when(borrowingRepository.existsById(41L)).thenReturn(true);
        when(penaltyRepository.findAllByBorrowingId(41L)).thenReturn(penalties);

        //When
        List<Penalty> retrievedPenalties = penaltyService.getAllPenaltiesOfBorrowing(41L);

        //Then
        assertThat(retrievedPenalties, containsInAnyOrder(penalty1, penalty2));
    }

    @Test
    public void should_ThrowNotFoundException_WhenRequestedBorrowingsIdIsInvalid() {
        //Given
        when(borrowingRepository.existsById(41L)).thenReturn(false);

        //When
        Exception thrownException = null;
        try {
            penaltyService.getAllPenaltiesOfBorrowing(41L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_ReturnAllBorrowersPenalties_WhenRequestedBorrowersIdIsValidAndPaidIsNull() {
        //Given
        List<Penalty> penalties = Stream.of(penalty1, penalty2, penalty3)
                .map(CopyService::copyPenalty)
                .collect(Collectors.toList());
        when(borrowerRepository.existsByIdAndStatus(1L, ACTIVE)).thenReturn(true);
        when(penaltyRepository.findAllByBorrowingBorrowerId(1L)).thenReturn(penalties);

        //When
        List<Penalty> retrievedPenalties = penaltyService.getAllPenaltiesOfBorrower(1L, null);

        //Then
        assertThat(retrievedPenalties, containsInAnyOrder(penalty1, penalty2, penalty3));
    }

    @Test
    public void should_ReturnPaidBorrowersPenalties_WhenRequestedBorrowersIdIsValidAndPaidIsTrue() {
        //Given
        List<Penalty> penalties = Stream.of(penalty2)
                .map(CopyService::copyPenalty)
                .collect(Collectors.toList());
        when(borrowerRepository.existsByIdAndStatus(1L, ACTIVE)).thenReturn(true);
        when(penaltyRepository.findAllByBorrowingBorrowerIdAndPaid(1L, true)).thenReturn(penalties);

        //When
        List<Penalty> retrievedPenalties = penaltyService.getAllPenaltiesOfBorrower(1L, true);

        //Then
        assertThat(retrievedPenalties, contains(penalty2));
    }

    @Test
    public void should_ReturnUnpaidBorrowersPenalties_WhenRequestedBorrowersIdIsValidAndPaidIsFalse() {
        //Given
        List<Penalty> penalties = Stream.of(penalty1, penalty3)
                .map(CopyService::copyPenalty)
                .collect(Collectors.toList());
        when(borrowerRepository.existsByIdAndStatus(1L, ACTIVE)).thenReturn(true);
        when(penaltyRepository.findAllByBorrowingBorrowerIdAndPaid(1L, false)).thenReturn(penalties);

        //When
        List<Penalty> retrievedPenalties = penaltyService.getAllPenaltiesOfBorrower(1L, false);

        //Then
        assertThat(retrievedPenalties, containsInAnyOrder(penalty1, penalty3));
    }

    @Test
    public void should_ThrowNotFoundException_WhenRequestedBorrowersIdIsInvalid() {
        //Given
        when(borrowerRepository.existsByIdAndStatus(1L, ACTIVE)).thenReturn(false);

        //When
        Exception thrownException = null;
        try {
            penaltyService.getAllPenaltiesOfBorrower(1L, null);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

    @Test
    public void should_MarkPenaltyAsPaid_WhenUpdatedPenaltiesIdIsValidAndPenaltyIsUnpaid() {
        //Given
        Penalty persistedPenalty = new Penalty(
                penalty2.getId(),
                penalty2.getCreationTime(),
                penalty2.getPenaltyCause(),
                penalty2.getPenaltyFee(),
                FALSE,
                copyBorrowing(penalty2.getBorrowing())
        );
        when(penaltyRepository.findById(52L)).thenReturn(Optional.of(persistedPenalty));
        when(penaltyRepository.save(persistedPenalty)).thenReturn(persistedPenalty);

        //When
        Penalty retrievedPenalty = penaltyService.markPenaltyPaid(52L);

        //Then
        assertThat(retrievedPenalty, is(equalTo(penalty2)));
    }

    @Test
    public void should_ThrowConflictException_WhenUpdatedPenaltiesIdIsValidAndPenaltyIsPaid() {
        //Given
        Penalty persistedPenalty = copyPenalty(penalty2);
        when(penaltyRepository.findById(52L)).thenReturn(Optional.of(persistedPenalty));

        //When
        Exception thrownException = null;
        try {
            penaltyService.markPenaltyPaid(52L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(ConflictException.class)));
    }

    @Test
    public void should_ThrowNotFoundException_WhenUpdatedPenaltiesIdIsInvalid() {
        //Given
        when(penaltyRepository.findById(52L)).thenReturn(Optional.empty());

        //When
        Exception thrownException = null;
        try {
            penaltyService.markPenaltyPaid(52L);
        } catch (Exception e) {
            thrownException = e;
        }

        //Then
        assertThat(thrownException, is(instanceOf(NotFoundException.class)));
    }

}
