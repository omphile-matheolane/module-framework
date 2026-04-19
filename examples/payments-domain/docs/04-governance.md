# Governance: Payments Domain

**System**: Marketplace Core (payments portion)

**Last updated**: 2026-03-15

**Maintained by**: Payments Team

---

## Purpose

This document captures the governance posture for the payments
domain: who owns what, what architectural rules are enforced, and
where the architectural decisions have been recorded. It is the
operational view of the [Unify discipline](../../../disciplines/04-unify/)
applied to payments specifically.

---

## Ownership

### Ownership registry extract (payments domain)

| Module | Owning team | Primary contact | Escalation contact | Last confirmed |
|--------|-------------|-----------------|--------------------|----------------|
| Payments | Payments Team | T. Lead | Head of Commerce Platform | 2026-03-01 |
| Fraud | Risk Team | R. Lead | Head of Risk | 2026-02-20 |
| Refunds UI | Internal Tools Team | I. Lead | Head of Engineering Ops | 2026-01-10 |

### Ownership responsibilities

Payments Team specifically:

- Maintains the integrity of the Payments module, including its
  internal quality, its adherence to the public surface, and
  its compliance with financial regulations
- Reviews any change to the public Payments contract (all
  changes to `com.example.payments` outside subpackages)
- Manages the evolution of the module, including the current
  extraction to an independent service
- Communicates with consumers (Orders, Refunds UI, Reporting,
  Notifications) about upcoming changes that might affect them
- Responds to payments-related incidents as primary on-call

### Decision rights

| Decision type | Authority | Consulted |
|---------------|-----------|-----------|
| Changes to the `PaymentService` public interface | Payments Team with Commerce Platform architect review | Consumer teams |
| Changes to payment event schemas | Payments Team | Reporting, Notifications, Audit |
| Provider integration choices | Payments Team | Head of Commerce Platform, Finance, Security |
| Internal implementation | Payments Team | |
| Extraction or deployment model changes | Payments Team with architecture review | Commerce Platform group |
| Regulatory compliance posture | Payments Team jointly with Compliance | Legal, Security |

---

## Architecture tests

The following tests run on every build. They are implemented in
ArchUnit and live alongside the payments module source.

### Test 1: Payments internals are not accessed externally

```java
@ArchTest
static final ArchRule payments_internals_are_not_accessed_externally =
    noClasses()
        .that().resideOutsideOfPackage("com.example.payments..")
        .and().doNotHaveFullyQualifiedName(
            "com.example.payments.integration.StripeWebhookHandler")  // exemption
        .should().dependOnClassesThat()
        .resideInAnyPackage(
            "com.example.payments.domain..",
            "com.example.payments.persistence..",
            "com.example.payments.integration..",
            "com.example.payments.config..")
        .because("payments internals must only be accessed through the payments public surface");
```

Current status: passing. Exemption for `StripeWebhookHandler` is
documented in [module structure](02-module-structure.md) and
reviewed annually.

### Test 2: Domain does not depend on infrastructure

```java
@ArchTest
static final ArchRule payments_domain_does_not_depend_on_infrastructure =
    noClasses()
        .that().resideInAPackage("com.example.payments.domain..")
        .should().dependOnClassesThat()
        .resideInAnyPackage(
            "com.example.payments.persistence..",
            "com.example.payments.integration..")
        .because("payments domain logic must not depend on infrastructure implementation details");
```

Current status: passing.

### Test 3: No cyclic dependencies

```java
@ArchTest
static final ArchRule modules_are_acyclic =
    slices()
        .matching("com.example.(*)..")
        .should().beFreeOfCycles()
        .because("cyclic dependencies between modules indicate boundary problems");
```

Current status: passing.

### Test 4: Payments depends only on declared modules

```java
@ArchTest
static final ArchRule payments_depends_only_on_declared_modules =
    classes()
        .that().resideInAPackage("com.example.payments..")
        .should().onlyDependOnClassesThat()
        .resideInAnyPackage(
            "com.example.payments..",
            "com.example.fraud",                 // FraudService facade only
            "com.example.shared..",
            "com.example.common..",
            "java..", "javax..", "jakarta..",
            "org.springframework..",
            "com.fasterxml.jackson..",
            "com.stripe..",
            "com.adyen..",
            "com.paygate..")
        .because("payments should only depend on explicitly declared modules");
```

Note that the declaration allows `com.example.fraud` (the public
package of Fraud) but not `com.example.fraud.domain` or similar
internals. Payments calls Fraud through its facade only.

Current status: passing.

### Test 5: Public surface is narrow

```java
@ArchTest
static final ArchRule payments_public_surface_is_narrow =
    classes()
        .that().resideInAPackage("com.example.payments")
        .and().arePublic()
        .should().haveSimpleNameEndingWith("Service")
        .orShould().haveSimpleNameEndingWith("Request")
        .orShould().haveSimpleNameEndingWith("Result")
        .orShould().haveSimpleNameEndingWith("Record")
        .orShould().haveSimpleName("PaymentId")
        .orShould().haveSimpleName("PaymentStatus")
        .because("the payments public surface is limited to services, request/result/record types, and explicitly named value types");
```

Current status: passing.

### Suppression list

There are no active suppressions on payments architecture tests.
The two suppressions that existed before the Q3 2025 hardening
work (for `PaymentRecordRepository` being accessed externally
by Reporting) were resolved during that hardening.

---

## Architecture decision records

The ADRs specific to payments are maintained in
`docs/adrs/payments/`. Summary of the currently relevant ones:

| ADR | Title | Status | Summary |
|-----|-------|--------|---------|
| ADR-0014 | Modularisation of payments inside the monolith | Accepted, implemented | Original modularisation of payments (2024) |
| ADR-0023 | Spring scoping for modules | Accepted | Module-level `@ComponentScan` pattern |
| ADR-0027 | Payment event schema versioning | Accepted | Event versioning policy, including `schema_version` envelope field |
| ADR-0035 | Payment adapter pattern | Accepted | Provider adapter pattern and its extension points |
| ADR-0039 | v1 to v2 PaymentService migration | Accepted, implemented | The consolidation of authorise overloads into sealed AuthRequest |
| ADR-0041 | Q3 2025 hardening of Payments | Accepted, implemented | Hardening work that preceded the extraction decision |
| ADR-0042 | Extraction of Payments to an independent service | Accepted, in progress | The current extraction decision (see [`07-adr-extract.md`](07-adr-extract.md)) |

---

## Integration with the development process

### Pull request requirements

Changes touching the payments module must:

- Pass all architecture tests (enforced by CI)
- Pass all contract tests (enforced by CI)
- Update the contract catalog if the public contract is changing
- Update the domain map if module relationships are changing
- Link to the relevant ADR if the change implements an
  architectural decision
- Get review from at least two Payments Team members for public
  surface changes (enforced through CODEOWNERS)

### Deployment gates

The monolith's deployment pipeline enforces:

- All architecture tests pass
- All contract tests pass
- Schema migrations are compatible with the previous release
  (for rolling deployment)
- Observability instrumentation is present (module identifier on
  logs, tracing configured)

Once the extraction completes, the Payments service will have
its own deployment pipeline with equivalent gates plus the
additional checks appropriate to a network boundary.

---

## Maintenance of this document

Updates happen:

- When ownership changes
- When an architecture test is added, modified, or suppressed
- When an ADR is accepted that affects the payments domain
- Quarterly, as part of the broader governance review

The annual sign-off from the Commerce Platform architect
confirms that the governance posture recorded here matches what
is actually in place.

Next scheduled review: 2026-06-15.
