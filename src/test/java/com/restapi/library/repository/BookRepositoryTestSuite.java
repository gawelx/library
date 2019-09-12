package com.restapi.library.repository;

import com.restapi.library.domain.Book;
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
import java.util.Optional;

import static com.restapi.library.domain.BookStatus.AVAILABLE;
import static com.restapi.library.domain.BookStatus.BORROWED;
import static com.restapi.library.domain.BookStatus.CANCELED;
import static com.restapi.library.domain.BookStatus.DAMAGED;
import static com.restapi.library.domain.BookStatus.LOST;
import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({
        "/sql/insertPersons.sql",
        "/sql/insertBookTitles.sql",
        "/sql/insertBooks.sql"
})
@Transactional
public class BookRepositoryTestSuite {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testFindAllByStatus() {
        //Given
        Book book1 = bookRepository.getOne(21L);
        Book book2 = bookRepository.getOne(22L);
        Book book3 = bookRepository.getOne(23L);
        Book book4 = bookRepository.getOne(24L);
        Book book5 = bookRepository.getOne(25L);
        Book book6 = bookRepository.getOne(26L);
        Book book7 = bookRepository.getOne(27L);
        Book book8 = bookRepository.getOne(28L);
        Book book9 = bookRepository.getOne(29L);
        Book book10 = bookRepository.getOne(30L);

        //When
        List<Book> retrievedBooksAvailable = bookRepository.findAllByStatus(AVAILABLE);
        List<Book> retrievedBooksBorrowed = bookRepository.findAllByStatus(BORROWED);
        List<Book> retrievedBooksDamaged = bookRepository.findAllByStatus(DAMAGED);
        List<Book> retrievedBooksLost = bookRepository.findAllByStatus(LOST);
        List<Book> retrievedBooksCanceled = bookRepository.findAllByStatus(CANCELED);

        //Then
        assertThat(retrievedBooksAvailable, containsInAnyOrder(
                Hibernate.unproxy(book3),
                Hibernate.unproxy(book5),
                Hibernate.unproxy(book6),
                Hibernate.unproxy(book7),
                Hibernate.unproxy(book8)
        ));
        assertThat(retrievedBooksBorrowed, containsInAnyOrder(
                Hibernate.unproxy(book1),
                Hibernate.unproxy(book2)
        ));
        assertThat(retrievedBooksDamaged, containsInAnyOrder(
                Hibernate.unproxy(book10)
        ));
        assertThat(retrievedBooksLost, containsInAnyOrder(
                Hibernate.unproxy(book4)
        ));
        assertThat(retrievedBooksCanceled, containsInAnyOrder(
                Hibernate.unproxy(book9)
        ));
    }

    @Test
    public void testFindAllByBookTitleId() {
        //Given
        Book book1 = bookRepository.getOne(21L);
        Book book2 = bookRepository.getOne(22L);
        Book book3 = bookRepository.getOne(23L);
        Book book4 = bookRepository.getOne(24L);
        Book book5 = bookRepository.getOne(25L);
        Book book6 = bookRepository.getOne(26L);
        Book book7 = bookRepository.getOne(27L);
        Book book8 = bookRepository.getOne(28L);
        Book book9 = bookRepository.getOne(29L);
        Book book10 = bookRepository.getOne(30L);

        //When
        List<Book> retrievedBooks11L = bookRepository.findAllByBookTitleId(11L);
        List<Book> retrievedBooks12L = bookRepository.findAllByBookTitleId(12L);
        List<Book> retrievedBooks13L = bookRepository.findAllByBookTitleId(13L);

        //Then
        assertThat(retrievedBooks11L, containsInAnyOrder(
                Hibernate.unproxy(book1),
                Hibernate.unproxy(book2),
                Hibernate.unproxy(book3),
                Hibernate.unproxy(book4)
        ));
        assertThat(retrievedBooks12L, containsInAnyOrder(
                Hibernate.unproxy(book5),
                Hibernate.unproxy(book6),
                Hibernate.unproxy(book7)
        ));
        assertThat(retrievedBooks13L, containsInAnyOrder(
                Hibernate.unproxy(book8),
                Hibernate.unproxy(book9),
                Hibernate.unproxy(book10)
        ));
    }

    @Test
    public void testFindAllByBookTitleAuthorsContains() {
        //Given
        Book book1 = bookRepository.getOne(21L);
        Book book2 = bookRepository.getOne(22L);
        Book book3 = bookRepository.getOne(23L);
        Book book4 = bookRepository.getOne(24L);
        Book book5 = bookRepository.getOne(25L);
        Book book6 = bookRepository.getOne(26L);
        Book book7 = bookRepository.getOne(27L);
        Book book8 = bookRepository.getOne(28L);
        Book book9 = bookRepository.getOne(29L);
        Book book10 = bookRepository.getOne(30L);
        Person author1 = personRepository.getOne(1L);
        Person author2 = personRepository.getOne(2L);
        Person author3 = personRepository.getOne(4L);

        //When
        List<Book> retrievedBooksAuthor1 = bookRepository.findAllByBookTitleAuthorsContains(author1);
        List<Book> retrievedBooksAuthor2 = bookRepository.findAllByBookTitleAuthorsContains(author2);
        List<Book> retrievedBooksAuthor3 = bookRepository.findAllByBookTitleAuthorsContains(author3);

        //Then
        assertThat(retrievedBooksAuthor1, containsInAnyOrder(
                Hibernate.unproxy(book1),
                Hibernate.unproxy(book2),
                Hibernate.unproxy(book3),
                Hibernate.unproxy(book4),
                Hibernate.unproxy(book5),
                Hibernate.unproxy(book6),
                Hibernate.unproxy(book7)
        ));
        assertThat(retrievedBooksAuthor2, containsInAnyOrder(
                Hibernate.unproxy(book8),
                Hibernate.unproxy(book9),
                Hibernate.unproxy(book10)
        ));
        assertThat(retrievedBooksAuthor3, containsInAnyOrder(
                Hibernate.unproxy(book5),
                Hibernate.unproxy(book6),
                Hibernate.unproxy(book7)
        ));
    }

    @Test
    public void testFindBookByIdAndStatus() {
        //Given
        Book book1 = bookRepository.getOne(23L);
        Book book2 = bookRepository.getOne(21L);
        Book book3 = bookRepository.getOne(30L);
        Book book4 = bookRepository.getOne(24L);
        Book book5 = bookRepository.getOne(29L);

        //When
        Optional<Book> retrievedBookAvailable = bookRepository.findBookByIdAndStatus(23L, AVAILABLE);
        Optional<Book> retrievedBookBorrowed = bookRepository.findBookByIdAndStatus(21L, BORROWED);
        Optional<Book> retrievedBookDamaged = bookRepository.findBookByIdAndStatus(30L, DAMAGED);
        Optional<Book> retrievedBookLost = bookRepository.findBookByIdAndStatus(24L, LOST);
        Optional<Book> retrievedBookCanceled = bookRepository.findBookByIdAndStatus(29L, CANCELED);
        Optional<Book> retrievedBookEmpty = bookRepository.findBookByIdAndStatus(23L, BORROWED);

        //Then
        assertThat(retrievedBookAvailable, is(optionalWithValue(equalTo(Hibernate.unproxy(book1)))));
        assertThat(retrievedBookBorrowed, is(optionalWithValue(equalTo(Hibernate.unproxy(book2)))));
        assertThat(retrievedBookDamaged, is(optionalWithValue(equalTo(Hibernate.unproxy(book3)))));
        assertThat(retrievedBookLost, is(optionalWithValue(equalTo(Hibernate.unproxy(book4)))));
        assertThat(retrievedBookCanceled, is(optionalWithValue(equalTo(Hibernate.unproxy(book5)))));
        assertThat(retrievedBookEmpty, is(emptyOptional()));
    }

}
