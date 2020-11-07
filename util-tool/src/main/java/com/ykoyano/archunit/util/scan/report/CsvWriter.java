package com.ykoyano.archunit.util.scan.report;

import com.tngtech.archunit.core.domain.JavaMethod;
import com.ykoyano.archunit.repository.ExternalApiUrl;
import com.ykoyano.archunit.util.scan.archunit.ApiMetadata;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class CsvWriter {

    private final HashMap<ApiMetadata, Set<JavaMethod>> dependencies;

    private static final String OUTPUT_FILE_PATH = "build/scan/";

    public CsvWriter(
         final HashMap<ApiMetadata, Set<JavaMethod>> dependencies
    ) {
        this.dependencies = dependencies;
        final var outputFile = new File(OUTPUT_FILE_PATH);
        outputFile.mkdir();
    }

    public void outputBackendToDatabase() {
        final var rows = dependencies.entrySet().stream().map(entry -> {
            final var apiMetadata = entry.getKey();
            return entry.getValue().stream().map(method ->
                apiMetadata + "," + method.getName() + "," + method.getAnnotationOfType(ExternalApiUrl.class).value()
            ).collect(Collectors.toList());
        })
            .flatMap(Collection::stream)
            .sorted()
            .collect(Collectors.toList());

        try (final var fileWriter = new FileWriter(OUTPUT_FILE_PATH + "Controller-to-ExternalApiUrl.csv");
            final var printWriter = new PrintWriter(fileWriter)) {
            final var csvHeader = "END_POINT(Controller),REPOSITORY_METHOD,END_POINT(ExternalApiUrl)";
            printWriter.println(csvHeader);
            rows.forEach(printWriter::println);
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to create Controller-to-ExternalApiUrl.csv", e);
        }
    }
}
