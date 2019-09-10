package com.restapi.library.repository;

import com.restapi.library.domain.BookTitle;
import com.restapi.library.domain.Person;
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
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({
        "/sql/insertPersons.sql",
        "/sql/insertBookTitles.sql"
})
@Transactional
public class BookTitleRepositoryTestSuite {

    @Autowired
    private BookTitleRepository bookTitleRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testFindAllByAuthorContains() {
        //Given
        BookTitle bookTitle1 = bookTitleRepository.getOne(11L);
        BookTitle bookTitle2 = bookTitleRepository.getOne(12L);
        Person author = personRepository.getOne(1L);

        //When
        List<BookTitle> retrievedBookTitles = bookTitleRepository.findAllByAuthorsContains(author);

        //Then
        assertThat(retrievedBookTitles, containsInAnyOrder(
                Hibernate.unproxy(bookTitle1),
                Hibernate.unproxy(bookTitle2)
        ));
    }

}
