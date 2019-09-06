package com.restapi.library.dto;

import com.restapi.library.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookDto {

    private Long id;
    private Integer releaseYear;
    private BigDecimal price;
    private String status;
    private Long bookTitleId;

    public BookDto(final Book book) {
        this(
                book.getId(),
                book.getReleaseYear(),
                book.getPrice(),
                book.getStatus().toString(),
                book.getBookTitle().getId()
        );
    }

}
