package com.ykoyano.archunit.application.controller;

import com.ykoyano.archunit.application.dto.BookGetResponse;
import com.ykoyano.archunit.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/books", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class BookController {

    private final BookService usecase;

    @GetMapping(value = "{author}")
    public ResponseEntity<BookGetResponse> getBooksByAuthor(
        @PathVariable final String author) {
        final var body = usecase.getBooksByAuthor(author);
        return ResponseEntity.ok().body(body);
    }
}
