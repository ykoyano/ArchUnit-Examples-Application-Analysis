package com.ykoyano.archunit.application.dto;

import lombok.Builder;

import java.util.List;

@Builder
public class BookGetResponse {
    private final List<BookDto> books;

    RecommendedBookDto recommendedBookDto;
}
