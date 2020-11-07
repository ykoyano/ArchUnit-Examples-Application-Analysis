package com.ykoyano.archunit.util.scan.archunit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

@Getter
@AllArgsConstructor
public enum Layer {
    SERVICE(Service.class),
    REPOSITORY(Repository.class);
    private final Class<? extends Annotation> markerAnnotation;
}
