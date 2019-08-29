package com.restapi.library.dto;

import com.restapi.library.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookDto {

    private Long id;
    private String status;
    private BookTitleDto bookTitleDto;

    public BookDto(final Book book) {
        this(
                book.getId(),
                book.getStatus().toString(),
                new BookTitleDto(book.getBookTitle())
        );
    }

}
