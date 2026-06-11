package pl.kmalski.verification;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class ArchitectureTest {

    private final JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("pl.kmalski.verification");

    @Test
    void shouldRespectLayeredArchitecture() {
        var architecture = layeredArchitecture()
                .consideringAllDependencies()

                .layer("Domain")
                .definedBy("..domain..")

                .layer("Application")
                .definedBy("..application..")

                .layer("Infrastructure")
                .definedBy("..infrastructure..")

                .whereLayer("Domain")
                .mayOnlyBeAccessedByLayers("Application", "Infrastructure")

                .whereLayer("Application")
                .mayOnlyBeAccessedByLayers("Infrastructure")

                .whereLayer("Infrastructure")
                .mayNotBeAccessedByAnyLayer();

        architecture.check(importedClasses);
    }

}
