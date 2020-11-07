package com.ykoyano.archunit.util.scan.mapping;

import com.tngtech.archunit.core.domain.JavaMethod;
import com.ykoyano.archunit.util.scan.archunit.ApiMetadata;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Builder Class of ApiMetadata
 * <p>
 * Obtain the path of the API corresponding to the methods of the Controller class
 * from the values of the RequestMapping annotation given to the Controller
 * and the annotation given to the methods
 */
public class ApiMetadataBuilder {

    private final JavaMethod method;

    public ApiMetadataBuilder(final JavaMethod method) {
        this.method = method;
    }

    public ApiMetadata build() {
        final var owner = method.getOwner();
        final var requestMapping = owner.getAnnotationOfType(RequestMapping.class);
        final var metadata = new MappingMetadata(method);
        final var rootPath = requestMapping.value();
        final var childPath = metadata.getAnnotationValue() + metadata.getAnnotationParams();
        return new ApiMetadata(rootPath[0] + childPath, metadata.getMethodType());
    }
}
