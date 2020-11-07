package com.ykoyano.archunit.util.scan.archunit;

import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.ykoyano.archunit.util.scan.mapping.ApiMetadataBuilder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Set;

/**
 * The Class implemented for the sake of convenience to understand dependencies
 * Note that it is not used to impose constraints, which is the original purpose of the class
 */
public class DependencyScannerFromControllerToApi extends ArchCondition<JavaMethod> {

    /**
     * Map that keeps a correspondence between each method of Controller class and which external API it depends on
     */
    @Getter
    private final HashMap<ApiMetadata, Set<JavaMethod>> dependMapperMethods = new HashMap<>();

    public DependencyScannerFromControllerToApi() {
        super("");
    }

    /**
     * Implemented for convenience to understand the dependencies.
     * Note that it is not used to inspect the constraints, which is the purpose of the original class
     */
    @Override
    public void check(
        final JavaMethod item,
        final ConditionEvents events
    ) {
        resolve(item);
        // We don't test for architectural dependencies, so we always return true
        events.add(new SimpleConditionEvent(item, true, ""));
    }

    /**
     * Add a mapping to dependMapperMethods to determine which external API each method in the Controller class depends on
     */
    private void resolve(final JavaMethod item) {
        final var apiPath = new ApiMetadataBuilder(item).build();
        dependMapperMethods.put(apiPath, new ApiDependencyResolver(item).resolve());
    }
}

