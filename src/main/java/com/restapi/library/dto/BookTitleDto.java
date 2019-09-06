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

    public BookTitleDto(final BookTitle bookTitle) {
        this(
                bookTitle.getId(),
                bookTitle.getTitle()
        );
    }

}
