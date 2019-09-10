package com.restapi.library.repository;

import com.restapi.library.domain.Borrower;
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

import static com.restapi.library.domain.PersonStatus.ACTIVE;
import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({
        "/sql/insertPersons.sql",
        "/sql/insertBorrowers.sql"
})
@Transactional
public class BorrowerRepositoryTestSuite {

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Test
    public void testFindAllByStatus() {
        //Given
        Borrower borrower1 = borrowerRepository.getOne(1L);
        Borrower borrower2 = borrowerRepository.getOne(2L);

        //When
        List<Borrower> retrievedBorrowers = borrowerRepository.findAllByStatus(ACTIVE);

        //Then
        assertThat(retrievedBorrowers, containsInAnyOrder(
                Hibernate.unproxy(borrower1),
                Hibernate.unproxy(borrower2)
        ));
    }

    @Test
    public void testFindByIdAndStatus() {
        //Given
        Borrower borrower1 = borrowerRepository.getOne(1L);

        //When
        Optional<Borrower> retrievedBorrower1 = borrowerRepository.findByIdAndStatus(1L, ACTIVE);
        Optional<Borrower> retrievedBorrower2 = borrowerRepository.findByIdAndStatus(3L, ACTIVE);

        //Then
        assertThat(retrievedBorrower1, is(optionalWithValue(equalTo(Hibernate.unproxy(borrower1)))));
        assertThat(retrievedBorrower2, is(emptyOptional()));
    }

    @Test
    public void testExistsByIdAndStatus() {
        //Given

        //When
        boolean result1 = borrowerRepository.existsByIdAndStatus(1L, ACTIVE);
        boolean result2 = borrowerRepository.existsByIdAndStatus(3L, ACTIVE);
        boolean result3 = borrowerRepository.existsByIdAndStatus(5L, ACTIVE);

        //Then
        assertTrue(result1);
        assertFalse(result2);
        assertFalse(result3);
    }

}
