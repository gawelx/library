package com.restapi.library.repository;

import com.restapi.library.domain.Penalty;
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
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({
        "/sql/insertPersons.sql",
        "/sql/insertBorrowers.sql",
        "/sql/insertBookTitles.sql",
        "/sql/insertBooks.sql",
        "/sql/insertBorrowings.sql",
        "/sql/insertPenalties.sql"
})
@Transactional
public class PenaltyRepositoryTestSuite {

    @Autowired
    private PenaltyRepository penaltyRepository;

    @Test
    public void testFindAllByPaid() {
        //Given
        Penalty penalty1 = penaltyRepository.getOne(51L);
        Penalty penalty2 = penaltyRepository.getOne(52L);
        Penalty penalty3 = penaltyRepository.getOne(53L);
        Penalty penalty4 = penaltyRepository.getOne(54L);
        Penalty penalty5 = penaltyRepository.getOne(55L);

        //When
        List<Penalty> retrievedPenaltiesPaid = penaltyRepository.findAllByPaid(true);
        List<Penalty> retrievedPenaltiesUnpaid = penaltyRepository.findAllByPaid(false);

        //Then
        assertThat(retrievedPenaltiesPaid, containsInAnyOrder(
                Hibernate.unproxy(penalty1),
                Hibernate.unproxy(penalty3),
                Hibernate.unproxy(penalty4),
                Hibernate.unproxy(penalty5)
        ));
        assertThat(retrievedPenaltiesUnpaid, containsInAnyOrder(
                Hibernate.unproxy(penalty2)
        ));
    }

    @Test
    public void testFindAllByBorrowingId() {
        //Given
        Penalty penalty1 = penaltyRepository.getOne(51L);
        Penalty penalty2 = penaltyRepository.getOne(52L);
        Penalty penalty3 = penaltyRepository.getOne(53L);
        Penalty penalty4 = penaltyRepository.getOne(54L);
        Penalty penalty5 = penaltyRepository.getOne(55L);

        //When
        List<Penalty> retrievedPenaltiesBorrowing43L = penaltyRepository.findAllByBorrowingId(43L);
        List<Penalty> retrievedPenaltiesBorrowing45L = penaltyRepository.findAllByBorrowingId(45L);
        List<Penalty> retrievedPenaltiesBorrowing48L = penaltyRepository.findAllByBorrowingId(48L);

        //Then
        assertThat(retrievedPenaltiesBorrowing43L, containsInAnyOrder(
                Hibernate.unproxy(penalty1)
        ));
        assertThat(retrievedPenaltiesBorrowing45L, containsInAnyOrder(
                Hibernate.unproxy(penalty2),
                Hibernate.unproxy(penalty3)
        ));
        assertThat(retrievedPenaltiesBorrowing48L, containsInAnyOrder(
                Hibernate.unproxy(penalty4),
                Hibernate.unproxy(penalty5)
        ));
    }

    @Test
    public void testFindAllByBorrowingBorrowerId() {
        //Given
        Penalty penalty1 = penaltyRepository.getOne(51L);
        Penalty penalty2 = penaltyRepository.getOne(52L);
        Penalty penalty3 = penaltyRepository.getOne(53L);
        Penalty penalty4 = penaltyRepository.getOne(54L);
        Penalty penalty5 = penaltyRepository.getOne(55L);

        //When
        List<Penalty> retrievedPenaltiesBorrower1L = penaltyRepository.findAllByBorrowingBorrowerId(1L);
        List<Penalty> retrievedPenaltiesBorrower2L = penaltyRepository.findAllByBorrowingBorrowerId(2L);
        List<Penalty> retrievedPenaltiesBorrower3L = penaltyRepository.findAllByBorrowingBorrowerId(3L);

        //Then
        assertThat(retrievedPenaltiesBorrower1L, containsInAnyOrder(
                Hibernate.unproxy(penalty1)
        ));
        assertThat(retrievedPenaltiesBorrower2L, containsInAnyOrder(
                Hibernate.unproxy(penalty2),
                Hibernate.unproxy(penalty3)
        ));
        assertThat(retrievedPenaltiesBorrower3L, containsInAnyOrder(
                Hibernate.unproxy(penalty4),
                Hibernate.unproxy(penalty5)
        ));
    }

    @Test
    public void testFindAllByBorrowingBorrowerIdAndPaid() {
        //Given
        Penalty penalty1 = penaltyRepository.getOne(51L);
        Penalty penalty2 = penaltyRepository.getOne(52L);
        Penalty penalty3 = penaltyRepository.getOne(53L);
        Penalty penalty4 = penaltyRepository.getOne(54L);
        Penalty penalty5 = penaltyRepository.getOne(55L);

        //When
        List<Penalty> retrievedPenaltiesBorrower1LPaid =
                penaltyRepository.findAllByBorrowingBorrowerIdAndPaid(1L, true);
        List<Penalty> retrievedPenaltiesBorrower1LUnpaid =
                penaltyRepository.findAllByBorrowingBorrowerIdAndPaid(1L, false);
        List<Penalty> retrievedPenaltiesBorrower2LPaid =
                penaltyRepository.findAllByBorrowingBorrowerIdAndPaid(2L, true);
        List<Penalty> retrievedPenaltiesBorrower2LUnpaid =
                penaltyRepository.findAllByBorrowingBorrowerIdAndPaid(2L, false);
        List<Penalty> retrievedPenaltiesBorrower3LPaid =
                penaltyRepository.findAllByBorrowingBorrowerIdAndPaid(3L, true);
        List<Penalty> retrievedPenaltiesBorrower3LUnpaid =
                penaltyRepository.findAllByBorrowingBorrowerIdAndPaid(3L, false);

        //Then
        assertThat(retrievedPenaltiesBorrower1LPaid, containsInAnyOrder(Hibernate.unproxy(penalty1)));
        assertThat(retrievedPenaltiesBorrower1LUnpaid, empty());
        assertThat(retrievedPenaltiesBorrower2LPaid, containsInAnyOrder(Hibernate.unproxy(penalty3)));
        assertThat(retrievedPenaltiesBorrower2LUnpaid, containsInAnyOrder(Hibernate.unproxy(penalty2)));
        assertThat(retrievedPenaltiesBorrower3LPaid, containsInAnyOrder(
                Hibernate.unproxy(penalty4),
                Hibernate.unproxy(penalty5)
        ));
        assertThat(retrievedPenaltiesBorrower3LUnpaid, empty());
    }

}
