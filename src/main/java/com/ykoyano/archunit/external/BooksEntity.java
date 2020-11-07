package com.ykoyano.archunit.external;

import lombok.Value;

import java.util.List;

@Value
public class BooksEntity {
    List<BookEntity> books;
}
