package com.ykoyano.archunit.repository;

import com.ykoyano.archunit.domain.Book;
import com.ykoyano.archunit.external.BooksEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private static final String EXTERNAL_API_URL = "https://api.book/search?author={author}";

    @Autowired
    private final RestOperations restOperations;

    @ExternalApiUrl(EXTERNAL_API_URL)
    public List<Book> findBooksByAuthor(final String author) {
        final var books = restOperations.getForObject(EXTERNAL_API_URL, BooksEntity.class, author);
        return books.getBooks().stream().map(bookEntity ->
            new Book(
                bookEntity.getId(),
                bookEntity.getTitle(),
                bookEntity.getAuthor()
            )
        ).collect(Collectors.toList());
    }
}
