package com.ykoyano.archunit.service;

import com.ykoyano.archunit.application.dto.BookDto;
import com.ykoyano.archunit.application.dto.BookGetResponse;
import com.ykoyano.archunit.application.dto.RecommendedBookDto;
import com.ykoyano.archunit.repository.BookRepository;
import com.ykoyano.archunit.repository.RecommendedBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final RecommendedBookRepository recommendedBookRepository;

    public BookGetResponse getBooksByAuthor(final String author) {
        final var books = bookRepository.findBooksByAuthor(author);
        return BookGetResponse.builder()
            .books(books.stream().map(this::getBookDto).collect(Collectors.toList()))
            .recommendedBookDto(getRecommendedBookDto(author))
            .build();
    }

    private BookDto getBookDto(com.ykoyano.archunit.domain.Book book) {
        return new BookDto(book.getTitle(), book.getAuthor());
    }

    private RecommendedBookDto getRecommendedBookDto(String author) {
        final var recommendedBook = recommendedBookRepository.findRecommendedBook(author);
        return new RecommendedBookDto(
            recommendedBook.getTitle(),
            recommendedBook.getAuthor(),
            recommendedBook.getReason()
        );
    }
}
