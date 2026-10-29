package com.shop.architecture;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import io.modulith.rules.ModulithRules;
import org.junit.jupiter.api.Test;

class ArchitectureTest {
    @Test
    void modulesDoNotAccessEachOthersInternals() {
        var classes = new ClassFileImporter().importPackages("com.shop");

        ModulithRules.forPackage("com.shop", "ordering", "payments")
            .boundaryRules()
            .internalsShouldNotBeAccessedFromOutside()
            .check(classes);
    }
}
