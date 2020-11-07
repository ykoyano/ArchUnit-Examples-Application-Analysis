package com.ykoyano.archunit.util.scan.archunit;

import com.tngtech.archunit.core.domain.JavaMethod;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ApiDependencyResolver {

    private final JavaMethod controllerMethod;

    public ApiDependencyResolver( final JavaMethod controllerMethod) {
        this.controllerMethod = controllerMethod;
    }

    /**
     * Returns the correspondence between each method of the Controller class and
     * which external API each method depends on, based on the assumption of the order of method calls,
     * such as "ControllerClass -> ServiceClass -> RepositoryClass"
     */
    public Set<JavaMethod> resolve() {
        // Get the Service class methods called by the methods of the Controller class
        final var serviceMethods = extractDependMethodsByLayer(controllerMethod);

        if (serviceMethods.isEmpty()) {
            return Set.of();
        }

        // It is assumed that the methods of the Controller class only call one method of the Service class
        final var serviceMethod = serviceMethods.stream().findFirst().get();

        // Get the method of the Repository class that the Service class method is calling
        final var repositoryMethods = new RecursiveMethodDependencyResolver(
            serviceMethod,
            Layer.SERVICE,
            Layer.REPOSITORY
        ).resolve();

        return repositoryMethods;
    }


    private Set<JavaMethod> extractDependMethodsByLayer(
         final JavaMethod method
    ) {
        return method.getMethodCallsFromSelf().stream().filter(methodCall ->
            methodCall.getTargetOwner().isAnnotatedWith(Layer.SERVICE.getMarkerAnnotation())
        ).map(v ->
            v.getTarget().resolve()
        ).flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }
}
