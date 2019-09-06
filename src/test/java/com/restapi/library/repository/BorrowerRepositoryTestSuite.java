package com.restapi.library.repository;

import com.restapi.library.domain.Borrower;
import com.restapi.library.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static com.restapi.library.domain.PersonStatus.ACTIVE;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BorrowerRepositoryTestSuite {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Test
    public void testDelete() {
        //Given
        Person person = new Person(
                null,
                ACTIVE,
                "John",
                "Smith",
                null,
                Collections.emptyList());
        Borrower borrower = new Borrower(
                null,
                ACTIVE,
                null,
                person,
                Collections.emptyList()
        );

        try {
            //When
            borrowerRepository.save(borrower);
            int beforeDeletionSize = borrowerRepository.findAll().size();
            personRepository.delete(person);
            int afterDeletionSize = borrowerRepository.findAll().size();

            //Then
//            assertEquals(1, beforeDeletionSize);
//            assertEquals(0, afterDeletionSize);
        } finally {
            //Cleanup
            personRepository.delete(person);
        }
    }

}
