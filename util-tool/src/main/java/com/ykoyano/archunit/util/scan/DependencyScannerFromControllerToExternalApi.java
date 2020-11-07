package com.ykoyano.archunit.util.scan;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import com.ykoyano.archunit.util.scan.archunit.DependencyScannerFromControllerToApi;
import com.ykoyano.archunit.util.scan.report.CsvWriter;

public final class DependencyScannerFromControllerToExternalApi {

    private static final JavaClasses importedClasses = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("com.ykoyano.archunit");

    public static void main(final String[] args) {
        final var scanner = new DependencyScannerFromControllerToApi();

        ArchRuleDefinition.methods()
            .that()
            .areDeclaredInClassesThat()
            .haveNameMatching("^.*Controller$")
            .should(scanner)
            .check(importedClasses);

        final var csvWriter = new CsvWriter(scanner.getDependMapperMethods());
        csvWriter.outputBackendToDatabase();
    }
}
