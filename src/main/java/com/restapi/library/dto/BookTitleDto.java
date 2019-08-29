package com.restapi.library.dto;

import com.restapi.library.domain.BookTitle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookTitleDto {

    private Long id;
    private String title;
    private Integer releaseYear;
    private PersonDto author;

    public BookTitleDto(final BookTitle bookTitle) {
        this(
                bookTitle.getId(),
                bookTitle.getTitle(),
                bookTitle.getReleaseYear(),
                new PersonDto(bookTitle.getAuthor())
        );
    }

}
