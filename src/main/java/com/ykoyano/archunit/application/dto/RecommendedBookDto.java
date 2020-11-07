package com.ykoyano.archunit.application.dto;

import lombok.Value;

@Value
public class RecommendedBookDto {

    String title;

    String author;

    String reason;
}
