package com.restapi.library.repository;

import com.restapi.library.domain.Borrowing;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({
        "/sql/insertPersons.sql",
        "/sql/insertBorrowers.sql",
        "/sql/insertBookTitles.sql",
        "/sql/insertBooks.sql",
        "/sql/insertBorrowings.sql"
})
@Transactional
public class BorrowingRepositoryTestSuite {

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Test
    public void testFindAllByReturnDateIsNull() {
        //Given
        Borrowing borrowing1 = borrowingRepository.getOne(46L);
        Borrowing borrowing2 = borrowingRepository.getOne(47L);

        //When
        List<Borrowing> retrievedBorrowingsPending = borrowingRepository.findAllByReturnDateIsNull();

        //Then
        assertThat(retrievedBorrowingsPending, containsInAnyOrder(
                Hibernate.unproxy(borrowing1),
                Hibernate.unproxy(borrowing2)
        ));
    }

    @Test
    public void testFindAllByReturnDateIsNotNull() {
        //Given
        Borrowing borrowing1 = borrowingRepository.getOne(41L);
        Borrowing borrowing2 = borrowingRepository.getOne(42L);
        Borrowing borrowing3 = borrowingRepository.getOne(43L);
        Borrowing borrowing4 = borrowingRepository.getOne(44L);
        Borrowing borrowing5 = borrowingRepository.getOne(45L);
        Borrowing borrowing6 = borrowingRepository.getOne(48L);

        //When
        List<Borrowing> retrievedBorrowingsClosed = borrowingRepository.findAllByReturnDateIsNotNull();

        //Then
        assertThat(retrievedBorrowingsClosed, containsInAnyOrder(
                Hibernate.unproxy(borrowing1),
                Hibernate.unproxy(borrowing2),
                Hibernate.unproxy(borrowing3),
                Hibernate.unproxy(borrowing4),
                Hibernate.unproxy(borrowing5),
                Hibernate.unproxy(borrowing6)
        ));
    }

    @Test
    public void testFindAllByBorrowerId() {
        //Given
        Borrowing borrowing1 = borrowingRepository.getOne(41L);
        Borrowing borrowing2 = borrowingRepository.getOne(42L);
        Borrowing borrowing3 = borrowingRepository.getOne(43L);
        Borrowing borrowing4 = borrowingRepository.getOne(44L);
        Borrowing borrowing5 = borrowingRepository.getOne(45L);
        Borrowing borrowing6 = borrowingRepository.getOne(46L);
        Borrowing borrowing7 = borrowingRepository.getOne(47L);
        Borrowing borrowing8 = borrowingRepository.getOne(48L);

        //When
        List<Borrowing> retrievedBorrowingsBorrower1L = borrowingRepository.findAllByBorrowerId(1L);
        List<Borrowing> retrievedBorrowingsBorrower2L = borrowingRepository.findAllByBorrowerId(2L);
        List<Borrowing> retrievedBorrowingsBorrower3L = borrowingRepository.findAllByBorrowerId(3L);

        //Then
        assertThat(retrievedBorrowingsBorrower1L, containsInAnyOrder(
                Hibernate.unproxy(borrowing1),
                Hibernate.unproxy(borrowing2),
                Hibernate.unproxy(borrowing3),
                Hibernate.unproxy(borrowing7)
        ));
        assertThat(retrievedBorrowingsBorrower2L, containsInAnyOrder(
                Hibernate.unproxy(borrowing4),
                Hibernate.unproxy(borrowing5),
                Hibernate.unproxy(borrowing6)
        ));
        assertThat(retrievedBorrowingsBorrower3L, containsInAnyOrder(
                Hibernate.unproxy(borrowing8)
        ));
    }

    @Test
    public void testFindAllByBorrowerIdAndReturnDateIsNull() {
        //Given
        Borrowing borrowing1 = borrowingRepository.getOne(46L);
        Borrowing borrowing2 = borrowingRepository.getOne(47L);

        //When
        List<Borrowing> retrievedBorrowingsBorrower1L = borrowingRepository.findAllByBorrowerIdAndReturnDateIsNull(1L);
        List<Borrowing> retrievedBorrowingsBorrower2L = borrowingRepository.findAllByBorrowerIdAndReturnDateIsNull(2L);
        List<Borrowing> retrievedBorrowingsBorrower3L = borrowingRepository.findAllByBorrowerIdAndReturnDateIsNull(3L);

        //Then
        assertThat(retrievedBorrowingsBorrower1L, containsInAnyOrder(
                Hibernate.unproxy(borrowing2)
        ));
        assertThat(retrievedBorrowingsBorrower2L, containsInAnyOrder(
                Hibernate.unproxy(borrowing1)
        ));
        assertThat(retrievedBorrowingsBorrower3L, empty());
    }

    @Test
    public void testExistsByBorrowerIdAndReturnDateIsNull() {
        //Given

        //When
        boolean resultBorrower1L = borrowingRepository.existsByBorrowerIdAndReturnDateIsNull(1L);
        boolean resultBorrower2L = borrowingRepository.existsByBorrowerIdAndReturnDateIsNull(2L);
        boolean resultBorrower3L = borrowingRepository.existsByBorrowerIdAndReturnDateIsNull(3L);

        //Then
        assertTrue(resultBorrower1L);
        assertTrue(resultBorrower2L);
        assertFalse(resultBorrower3L);
    }

    @Test
    public void testFindAllByBorrowerIdAndReturnDateIsNotNull() {
        //Given
        Borrowing borrowing1 = borrowingRepository.getOne(41L);
        Borrowing borrowing2 = borrowingRepository.getOne(42L);
        Borrowing borrowing3 = borrowingRepository.getOne(43L);
        Borrowing borrowing4 = borrowingRepository.getOne(44L);
        Borrowing borrowing5 = borrowingRepository.getOne(45L);
        Borrowing borrowing6 = borrowingRepository.getOne(48L);

        //When
        List<Borrowing> retrievedBorrowingsBorrower1L = borrowingRepository.findAllByBorrowerIdAndReturnDateIsNotNull(1L);
        List<Borrowing> retrievedBorrowingsBorrower2L = borrowingRepository.findAllByBorrowerIdAndReturnDateIsNotNull(2L);
        List<Borrowing> retrievedBorrowingsBorrower3L = borrowingRepository.findAllByBorrowerIdAndReturnDateIsNotNull(3L);

        //Then
        assertThat(retrievedBorrowingsBorrower1L, containsInAnyOrder(
                Hibernate.unproxy(borrowing1),
                Hibernate.unproxy(borrowing2),
                Hibernate.unproxy(borrowing3)
        ));
        assertThat(retrievedBorrowingsBorrower2L, containsInAnyOrder(
                Hibernate.unproxy(borrowing4),
                Hibernate.unproxy(borrowing5)
        ));
        assertThat(retrievedBorrowingsBorrower3L, containsInAnyOrder(
                Hibernate.unproxy(borrowing6)
        ));
    }

    @Test
    public void testFindAllByBookId() {
        //Given
        Borrowing borrowing1 = borrowingRepository.getOne(42L);
        Borrowing borrowing2 = borrowingRepository.getOne(44L);

        //When
        List<Borrowing> retrievedBorrowingsBook26L = borrowingRepository.findAllByBookId(26L);
        List<Borrowing> retrievedBorrowingsBook27L = borrowingRepository.findAllByBookId(27L);

        //Then
        assertThat(retrievedBorrowingsBook26L, containsInAnyOrder(
                Hibernate.unproxy(borrowing1),
                Hibernate.unproxy(borrowing2)
        ));
        assertThat(retrievedBorrowingsBook27L, empty());
    }

}
