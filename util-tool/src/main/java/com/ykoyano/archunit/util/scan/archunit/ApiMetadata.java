package com.ykoyano.archunit.util.scan.archunit;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * A class that retains metadata such as API paths and methods corresponding to methods of the Controller class
 */
@EqualsAndHashCode
public class ApiMetadata {


    @Getter
    private final String methodType;


    @Getter
    private final String path;

    public ApiMetadata(
         final String methodType,
         final String path
    ) {
        this.methodType = methodType;
        this.path = path;
    }

    @Override
    public String toString() {
        return methodType + "(" + path + ")";
    }
}
