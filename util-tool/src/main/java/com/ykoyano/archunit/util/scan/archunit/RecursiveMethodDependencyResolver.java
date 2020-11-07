package com.ykoyano.archunit.util.scan.archunit;

import com.tngtech.archunit.core.domain.JavaConstructorCall;
import com.tngtech.archunit.core.domain.JavaMethod;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RecursiveMethodDependencyResolver {
    
    private final HashSet<JavaMethod> calledMethods;
    
    private final JavaMethod controllerMethod;
    
    private final Layer currentLayer;
    
    private final Layer nextLayer;

    private static final int MAX_DEPTH = 5;

    public RecursiveMethodDependencyResolver(
         final JavaMethod controllerMethod,
         final Layer currentLayer,
         final Layer nextLayer
    ) {
        this.controllerMethod = controllerMethod;
        this.currentLayer = currentLayer;
        this.nextLayer = nextLayer;
        this.calledMethods = new HashSet<>();
    }

    /**
     * Recursively fetch the methods of the nextLayer class that are called by the methods of the currentLayer class.
     */
    public Set<JavaMethod> resolve() {
        collectCalledMethods(this.controllerMethod, 0);
        return this.calledMethods;
    }

    private void collectCalledMethods(
         final JavaMethod method,
         final int depth
    ) {
        // Censored at a reasonable depth to prevent recursive calls from becoming StackOverflow.
        if (depth > MAX_DEPTH) {
            return;
        }

        // Get the method of the nextLayer class that method calls directly.
        calledMethods.addAll(extractDependMethodsByLayer(method, nextLayer));

        // If method is calling another method of the same class, look at the methods it is calling in addition
        extractDependMethodsByLayer(method, currentLayer).forEach(javaMethod -> {
            collectCalledMethods(javaMethod, depth + 1);
        });

        // If an anonymous class has been created in method, check the methods called by the anonymous class in addition to the anonymous class
        method.getConstructorCallsFromSelf().stream().filter(constructorCall -> {
            // Extract the anonymous class created in the method
            return constructorCall.getTargetOwner().getFullName().contains(method.getOwner().getSimpleName());
        }).forEach(constructorCall -> {
            // Get the methods of the nextLayer class that the anonymous class is calling.
            final var methods = extractDependMethodsByLayer(constructorCall, nextLayer);
            calledMethods.addAll(methods);

            // Considering that an anonymous class calls another method of the currentLayer class or creates yet another anonymous class
            extractDependMethodsBySimpleName(constructorCall, method.getOwner().getSimpleName())
                .forEach(javaMethod -> collectCalledMethods(javaMethod, depth + 1));
        });
    }

    
    private Set<JavaMethod> extractDependMethodsByLayer(
         final JavaMethod method,
         final Layer layer
    ) {
        return method.getMethodCallsFromSelf().stream().filter(methodCall ->
            methodCall.getTargetOwner().isAnnotatedWith(layer.getMarkerAnnotation())
        ).map(methodCall ->
            methodCall.getTarget().resolve()
        ).flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    
    private Set<JavaMethod> extractDependMethodsByLayer(
         final JavaConstructorCall method,
         final Layer layer
    ) {
        return method.getTargetOwner().getMethodCallsFromSelf().stream().filter(methodCall ->
            methodCall.getTargetOwner().isAnnotatedWith(layer.getMarkerAnnotation())
        ).map(methodCall ->
            methodCall.getTarget().resolve()
        )
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    
    private Set<JavaMethod> extractDependMethodsBySimpleName(
         final JavaConstructorCall method,
         final String ownerSimpleName
    ) {
        return method.getTargetOwner().getMethodCallsFromSelf().stream().filter(methodCall ->
            methodCall.getTargetOwner().getFullName().contains(ownerSimpleName)
        ).map(methodCall ->
            methodCall.getTarget().resolve()
        )
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }
}
