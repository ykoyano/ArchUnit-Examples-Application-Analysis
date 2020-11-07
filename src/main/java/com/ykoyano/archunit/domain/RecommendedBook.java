package com.ykoyano.archunit.domain;

import lombok.Value;

@Value
public class RecommendedBook {

    String title;

    String author;

    String reason;
}
