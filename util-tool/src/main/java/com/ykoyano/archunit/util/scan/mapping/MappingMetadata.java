package com.ykoyano.archunit.util.scan.mapping;

import com.tngtech.archunit.core.domain.JavaMethod;
import lombok.Getter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

public class MappingMetadata {

    @Getter
    private String methodType;

    private String[] annotationValue;
    
    private String[] annotationParams;

    public MappingMetadata(final JavaMethod method) {
        if (method.isAnnotatedWith(GetMapping.class)) {
            this.methodType = "GET";
            final var mapping = method.getAnnotationOfType(GetMapping.class);
            this.annotationValue = mapping.value();
            this.annotationParams = mapping.params();
        } else if (method.isAnnotatedWith(PostMapping.class)) {
            this.methodType = "POST";
            final var mapping = method.getAnnotationOfType(PostMapping.class);
            this.annotationValue = mapping.value();
            this.annotationParams = mapping.params();
        } else if (method.isAnnotatedWith(PutMapping.class)) {
            this.methodType = "PUT";
            final var mapping = method.getAnnotationOfType(PutMapping.class);
            this.annotationValue = mapping.value();
            this.annotationParams = mapping.params();
        } else if (method.isAnnotatedWith(DeleteMapping.class)) {
            this.methodType = "DELETE";
            final var mapping = method.getAnnotationOfType(DeleteMapping.class);
            this.annotationValue = mapping.value();
            this.annotationParams = mapping.params();
        }
    }

    public String getAnnotationValue() {
        if (annotationValue != null && annotationValue.length >= 1) {
            if (annotationValue[0].startsWith("/")) {
                return annotationValue[0];
            } else {
                return "/" + annotationValue[0];
            }
        }
        return "";
    }

    public String getAnnotationParams() {
        if (annotationParams != null && annotationParams.length >= 1) {
            return "?" + annotationParams[0];
        }
        return "";
    }
}
