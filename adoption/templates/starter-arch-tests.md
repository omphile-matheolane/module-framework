# Starter Architecture Tests

This file collects a starter set of architectural rules that tend
to be useful across most systems. The examples use ArchUnit for
Java, which is the ecosystem with the most mature tooling. The
underlying rules translate directly to equivalents in other
ecosystems: NetArchTest and ArchUnitNET for .NET, import-linter
for Python, dependency-cruiser or ts-arch for TypeScript, and
custom linters built around Go's `internal` package mechanism.

The rules here are not meant to be a complete governance suite.
They are the rules that tend to matter most when a team is first
introducing architectural testing, and that a team should be able
to defend quickly when asked why they are enforced.

Adapt the package names and module names to your own system. Pay
attention to what each rule is actually checking rather than
copying verbatim.

---

## 1. Modules do not access each other's internals

The single most important rule. Consumers reach into another
module only through its public surface.

```java
@AnalyzeClasses(packages = "com.example", importOptions = ImportOption.DoNotIncludeTests.class)
public class ModuleBoundaryTests {

    @ArchTest
    static final ArchRule payments_internals_are_not_accessed_externally =
        noClasses()
            .that().resideOutsideOfPackage("com.example.payments..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                "com.example.payments.domain..",
                "com.example.payments.persistence..",
                "com.example.payments.integration..")
            .because("payments internals must only be accessed through the payments public surface");

    @ArchTest
    static final ArchRule orders_internals_are_not_accessed_externally =
        noClasses()
            .that().resideOutsideOfPackage("com.example.orders..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                "com.example.orders.domain..",
                "com.example.orders.persistence..",
                "com.example.orders.integration..")
            .because("orders internals must only be accessed through the orders public surface");
}
```

Add one rule per module. The rule is the same shape each time.

---

## 2. No cyclic dependencies between modules

Cyclic dependencies are a strong signal that module boundaries
are not well-formed. Catch them at build time.

```java
@ArchTest
static final ArchRule modules_are_acyclic =
    slices()
        .matching("com.example.(*)..")
        .should().beFreeOfCycles()
        .because("cyclic dependencies between modules indicate boundary problems");
```

If there are known cycles that cannot be resolved immediately,
suppress them explicitly with a list of exceptions rather than
disabling the rule. The list is visible debt and tends to shrink
faster than invisible debt.

---

## 3. Modules depend only on modules they explicitly declare

This rule is the architectural equivalent of explicit imports. It
makes the module-level dependency graph match what is declared
rather than what happens to compile.

```java
@ArchTest
static final ArchRule payments_depends_only_on_declared_modules =
    classes()
        .that().resideInAPackage("com.example.payments..")
        .should().onlyDependOnClassesThat()
        .resideInAnyPackage(
            "com.example.payments..",     // itself
            "com.example.shared..",        // shared types module
            "com.example.common..",        // common utilities
            "java..", "javax..", "jakarta..",
            "org.springframework..",
            "com.fasterxml.jackson..")
        .because("payments should only depend on explicitly declared modules and common infrastructure");
```

Adjust the allowed packages to match your system's actual
dependencies. The rule should be tight enough that accidentally
importing from an unrelated module fails the build.

---

## 4. Shared types live in a designated module

A common source of coupling is shared types scattered across
modules. Consolidating them into a single shared module makes the
sharing explicit and visible.

```java
@ArchTest
static final ArchRule shared_types_live_in_shared_module =
    classes()
        .that().haveSimpleNameEndingWith("Dto")
        .or().haveSimpleNameEndingWith("Event")
        .or().haveSimpleNameEndingWith("Command")
        .should().resideInAPackage("com.example.shared..")
        .because("shared data transfer types must live in the shared module, not scattered across domain modules");
```

This rule is more opinionated than the others and may need
adjustment. Some teams allow DTOs to live in the module that owns
them, which is also reasonable. The important thing is that the
rule matches your team's convention.

---

## 5. Public surfaces are narrow

Prevent accidental expansion of the public surface by requiring
that public classes in the module root remain a small, curated
set.

```java
@ArchTest
static final ArchRule payments_public_surface_is_narrow =
    classes()
        .that().resideInAPackage("com.example.payments")
        .and().arePublic()
        .should().haveSimpleNameEndingWith("Service")
        .orShould().haveSimpleNameEndingWith("Event")
        .orShould().haveSimpleNameEndingWith("Config")
        .because("the payments public surface is limited to services, events, and configuration");
```

The specific naming conventions are for you to decide. The rule
makes those conventions enforceable rather than aspirational.

---

## 6. Domain layer does not depend on infrastructure

For modules that use a layered internal architecture, enforce the
direction of dependencies. Domain logic should not depend on
persistence or integration code.

```java
@ArchTest
static final ArchRule domain_does_not_depend_on_infrastructure =
    noClasses()
        .that().resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage("..persistence..", "..integration..")
        .because("domain logic must not depend on infrastructure implementation details");
```

Apply this selectively. Modules with simple logic may not need
layered internal architecture and do not need this rule. Modules
with rich domain logic benefit from enforcing the direction of
dependencies.

---

## 7. Spring component scanning stays inside module boundaries

For Spring-based systems specifically: prevent one module's
component scanning from picking up classes in another module.

```java
@ArchTest
static final ArchRule modules_have_self_contained_component_scan =
    classes()
        .that().areAnnotatedWith(SpringBootApplication.class)
        .or().areAnnotatedWith(ComponentScan.class)
        .should().resideInAPackage("com.example")
        .because("component scanning should happen at the application root, not inside modules");
```

This addresses the failure mode where `@ComponentScan` in a
module picks up beans from other modules and silently couples
them.

---

## Suppressions

When introducing these rules into an existing codebase, there will
be violations. Suppress them explicitly rather than weakening the
rules.

```java
@ArchTest
static final ArchRule payments_internals_are_not_accessed_externally =
    noClasses()
        .that().resideOutsideOfPackage("com.example.payments..")
        .and().doNotHaveFullyQualifiedName("com.example.reporting.LegacyReportBuilder")
        .and().doNotHaveFullyQualifiedName("com.example.admin.LegacyAdminPanel")
        .should().dependOnClassesThat()
        .resideInAnyPackage(
            "com.example.payments.domain..",
            "com.example.payments.persistence..",
            "com.example.payments.integration..")
        .because("payments internals must only be accessed through the payments public surface");
```

Maintain a suppression list file that tracks each exception with
a reason and, ideally, an expected resolution date. Review it
quarterly. The list should shrink over time.

---

## Putting it together

A reasonable starting configuration for a new system:

- Rule 1 (module boundaries) for every module, from day one
- Rule 2 (no cycles) from day one
- Rule 3 (explicit dependencies) for modules where the dependency
  graph is settled
- Rule 4 (shared types) if the team has a convention around shared
  types
- Rule 5 (narrow surfaces) for each module, tightened over time
- Rule 6 (domain independence) for modules with layered internal
  architecture
- Rule 7 (Spring scanning) for Spring-based systems

For existing systems: start with Rule 1 for the two or three
modules that cause the most friction, tolerate a suppression list,
and expand coverage as the list shrinks.
