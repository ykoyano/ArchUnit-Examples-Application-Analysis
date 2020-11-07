package com.ykoyano.archunit.repository;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExternalApiUrl {

    String value();
}
