package com.ykoyano.archunit.repository;

import com.ykoyano.archunit.domain.RecommendedBook;
import com.ykoyano.archunit.external.RecommendedBookEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestOperations;

@Repository
@RequiredArgsConstructor
public class RecommendedBookRepository {

    private static final String EXTERNAL_API_URL = "https://api.book/recommend/search?={bookId}";

    @Autowired
    private final RestOperations restOperations;

    @ExternalApiUrl(EXTERNAL_API_URL)
    public RecommendedBook findRecommendedBook(final String author) {
        final var recommendedBook = restOperations.getForObject(EXTERNAL_API_URL, RecommendedBookEntity.class, author);
        return new RecommendedBook(
            recommendedBook.getTitle(),
            recommendedBook.getAuthor(),
            recommendedBook.getReason()
        );
    }
}
