# Module Structure: Payments

**Module**: Payments

**Last updated**: 2026-03-15

**Owning team**: Payments Team

---

## Purpose

This document captures the internal structure of the Payments
module: its public surface, its internal organisation, and the
conventions that keep the boundary defensible.

For the business capability and dependencies of the module, see
the [domain map](01-domain-map.md). For the contracts the module
exposes, see the [contract catalog](03-contract-catalog.md).

---

## Package layout

```
com.example.payments                    (public surface)
├── PaymentService.java                 (facade interface)
├── PaymentRecord.java                  (read model)
├── events/
│   ├── PaymentAuthorised.java
│   ├── PaymentCaptured.java
│   └── PaymentRefunded.java
├── domain/                             (package-private)
│   ├── AuthorisationEngine.java
│   ├── CaptureEngine.java
│   ├── RefundEngine.java
│   ├── ReconciliationEngine.java
│   └── ...
├── persistence/                        (package-private)
│   ├── PaymentAuthorisationRepository.java
│   ├── PaymentRecordRepository.java
│   └── ...
├── integration/                        (package-private)
│   ├── StripeAdapter.java
│   ├── AdyenAdapter.java
│   ├── PayGateAdapter.java
│   └── ...
└── config/                             (package-private)
    ├── PaymentsConfiguration.java
    └── PaymentsModuleConfig.java
```

The public surface is `com.example.payments` itself. Everything
below that package is package-private. Consumers interact with
the module through `PaymentService`, the domain events, and the
`PaymentRecord` read model. Nothing else is reachable from
outside the module.

---

## Public surface

### PaymentService

The primary entry point. A single facade interface that covers
the operations consumers need:

```java
package com.example.payments;

public interface PaymentService {

    AuthorisationResult authorise(AuthorisationRequest request);

    CaptureResult capture(CaptureRequest request);

    RefundResult refund(RefundRequest request);

    PaymentRecord lookup(PaymentId paymentId);

    List<PaymentRecord> lookupForOrder(OrderId orderId);
}
```

The request and response types are immutable records defined in
the same public package. Internal types (entity classes,
persistence objects, integration DTOs) do not leak across the
surface.

### Domain events

Three events are published when relevant state changes occur.
Consumers subscribe through the internal event bus:

- `PaymentAuthorised`: published when a payment is successfully
  authorised against a provider
- `PaymentCaptured`: published when funds are captured (may be
  at authorisation for immediate-capture flows, or later for
  delayed-capture flows)
- `PaymentRefunded`: published when a refund is successfully
  processed

Each event carries enough information for consumers to react
without needing to call back into Payments. The event payloads
are versioned; schema changes follow the system-wide versioning
policy documented in the contract catalog.

### PaymentRecord

A read-only view of a payment's current state. Exposed for
reporting, customer service lookups, and reconciliation.
Immutable. Contains only the fields that consumers should
legitimately need; internal state is not exposed.

---

## Internal architecture

The Payments module uses a layered internal architecture
because the domain logic is rich enough to warrant it. The layers
are:

**Domain** (`com.example.payments.domain`): the business logic.
Authorisation rules, capture rules, refund policies,
reconciliation rules. Depends only on itself and on shared types.

**Persistence** (`com.example.payments.persistence`): the
repositories that read and write domain state to the database.
Depends on the domain layer.

**Integration** (`com.example.payments.integration`): the
adapters that interact with external payment providers. Depends
on the domain layer.

**Configuration** (`com.example.payments.config`): Spring
configuration classes that wire the layers together.

The direction of dependencies is enforced by architecture tests.
Domain does not depend on persistence or integration. Persistence
and integration both depend on domain. Configuration depends on
all three.

---

## Spring scoping

The module configures its own Spring component scanning to
prevent accidental leakage across the module boundary:

```java
@Configuration
@ComponentScan(basePackages = "com.example.payments")
public class PaymentsModuleConfig {
    // explicit bean definitions for cases where component scanning
    // is not the right mechanism
}
```

The main application-level `@SpringBootApplication` does not
scan under `com.example.payments` directly. Instead, it imports
`PaymentsModuleConfig`. This prevents beans inside the module
from being picked up by other modules' component scanning.

The pattern is documented in ADR-0035 (Spring scoping for
modules).

---

## How the module is kept consistent over time

### Architecture tests

See [governance](04-governance.md) for the architecture tests
that enforce the module's boundaries. The key rules for Payments
are:

- Nothing outside `com.example.payments` depends on anything in
  `com.example.payments.domain`, `com.example.payments.persistence`,
  or `com.example.payments.integration`
- The domain layer does not depend on the persistence or
  integration layers
- No cyclic dependencies between Payments and any other module
- Payments depends only on modules it explicitly declares

All of these run on every build.

### Code review

Changes to the public surface (the `com.example.payments`
package itself, excluding subpackages) require a specific label
on the pull request and review by two members of the Payments
Team. This is enforced through CODEOWNERS.

Internal changes require standard review but do not need the
additional sign-off.

### Contract testing

All external consumers of `PaymentService` and the payment
events have Pact tests. Any change that would break those tests
fails the build before merge.

---

## Exceptions and known deviations

Each deviation from the standard pattern is documented:

**StripeAdapter exposes a public method for webhook verification**.
This is a deliberate exception to the "nothing public outside the
surface" rule, because Stripe's webhook verification happens at
the HTTP layer (before any facade call can occur) and the module
boundary is crossed by the webhook handler rather than by an
internal caller. The handler lives in
`com.example.payments.integration` and is invoked through the
application's HTTP routing, not through the PaymentService
facade. Architecture tests include an explicit exemption for
this class.

**PaymentRecord exposes internal identifiers**. Consumers
(particularly Reporting) need to be able to correlate payment
records across events and read lookups. The `paymentId` field is
exposed for this purpose. This is not considered a leak because
the identifier is part of the public contract; the internal
storage details (which tables hold what) remain hidden.

There are no other known deviations.

---

## Notes on the upcoming extraction

The Payments module is scheduled for extraction to an
independent service (see ADR-0042). The internal structure
documented here will migrate largely intact; the layered
architecture translates directly to a service, with the main
changes being:

- `PaymentService` becomes a REST API and an RPC interface
- Domain events are published to an external message bus rather
  than the internal event bus
- `PaymentRecord` lookups are served through the API rather than
  through an in-process facade
- The integration adapters gain retry, circuit breaker, and
  timeout behaviour appropriate to network calls

The extraction plan is in [`08-extraction-plan.md`](08-extraction-plan.md).
