package com.restapi.library.service;

import com.restapi.library.domain.Book;
import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Borrower;
import com.restapi.library.domain.Borrowing;
import com.restapi.library.domain.Penalty;
import com.restapi.library.domain.Person;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.restapi.library.domain.BookStatus.AVAILABLE;
import static com.restapi.library.domain.BookStatus.DAMAGED;
import static com.restapi.library.domain.BookStatus.LOST;
import static com.restapi.library.domain.PenaltyCause.BOOK_DAMAGED;
import static com.restapi.library.domain.PenaltyCause.BOOK_LOST;
import static com.restapi.library.domain.PenaltyCause.DEADLINE_EXCEEDED;
import static com.restapi.library.domain.PersonStatus.ACTIVE;
import static com.restapi.library.service.CopyService.copyBorrowing;
import static java.lang.Boolean.FALSE;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PenaltiesFactory.class, LocalDateTime.class})
@PowerMockRunnerDelegate(SpringRunner.class)
@SpringBootTest
public class PenaltiesFactoryTestSuite {

    private static Borrowing borrowing1, borrowing2, borrowing3;
    private static Penalty penalty1, penalty2, penalty3;

    @Autowired
    private PenaltiesFactory penaltiesFactory;

    @BeforeClass
    public static void beforeAllTests() {
        Person person = new Person(1L, ACTIVE, "John", "Smith");
        Borrower borrower = new Borrower(1L, ACTIVE, LocalDateTime.now(), person);
        Person author = new Person(2L, ACTIVE, "Jane", "Doe");
        BookTitle bookTitle = new BookTitle(11L, "Gone with the Wind",
                new HashSet<>(Collections.singletonList(author)));
        Book book1 = new Book(21L, 2000, BigDecimal.valueOf(20), LOST, bookTitle);
        Book book2 = new Book(22L, 2000, BigDecimal.valueOf(20), DAMAGED, bookTitle);
        Book book3 = new Book(23L, 2019, BigDecimal.valueOf(20), AVAILABLE, bookTitle);
        LocalDate borrowingDate = LocalDate.now().minus(10, ChronoUnit.DAYS);
        LocalDate returnDate = LocalDate.now();
        borrowing1 = new Borrowing(41L, borrowingDate, 5, returnDate, borrower, book1);
        borrowing2 = new Borrowing(42L, borrowingDate, 15, returnDate, borrower, book2);
        borrowing3 = new Borrowing(43L, borrowingDate, 15, returnDate, borrower, book3);
        LocalDateTime createTime = LocalDateTime.now();
        penalty1 = new Penalty(null, createTime, BOOK_LOST, BigDecimal.valueOf(20), FALSE, borrowing1);
        penalty2 = new Penalty(null, createTime, DEADLINE_EXCEEDED, BigDecimal.valueOf(5), FALSE, borrowing1);
        penalty3 = new Penalty(null, createTime, BOOK_DAMAGED, BigDecimal.valueOf(10), FALSE, borrowing2);
    }

    @Test
    public void should_CreateTwoPenalties_WhenBookIsLostAndReturnedAfterDeadline() {
        //Given
        Borrowing borrowing = copyBorrowing(borrowing1);
        PowerMockito.mockStatic(LocalDateTime.class);
        when(LocalDateTime.now()).thenReturn(penalty1.getCreationTime());

        //When
        List<Penalty> retrievedPenalties = penaltiesFactory.createPenalties(borrowing);

        //Then
        assertThat(retrievedPenalties, containsInAnyOrder(penalty1, penalty2));
    }

    @Test
    public void should_CreateOnePenalty_WhenBookIsDamagedAndReturnedInTime() {
        //Given
        Borrowing borrowing = copyBorrowing(borrowing2);
        PowerMockito.mockStatic(LocalDateTime.class);
        when(LocalDateTime.now()).thenReturn(penalty3.getCreationTime());

        //When
        List<Penalty> retrievedPenalties = penaltiesFactory.createPenalties(borrowing);

        //Then
        assertThat(retrievedPenalties, contains(penalty3));
    }

    @Test
    public void should_ReturnEmptyList_WhenBookIsOKAndReturnedInTime() {
        //Given
        Borrowing borrowing = copyBorrowing(borrowing3);

        //When
        List<Penalty> retrievedPenalties = penaltiesFactory.createPenalties(borrowing);

        //Then
        assertThat(retrievedPenalties, is(empty()));
    }

}
