# Contract Catalog: Payments Domain

**System**: Marketplace Core (payments portion)

**Last updated**: 2026-03-15

**Maintained by**: Payments Team

---

## Purpose

This is the payments portion of the broader marketplace contract
catalog. It documents the contracts Payments exposes to other
modules and the external contracts it depends on.

For the broader system-wide catalog, see
`../../contract-catalog.md` (not included in this example).

---

## Contracts

### Contract: PaymentService (in-process Java interface)

**Owning module**: Payments

**Current version**: v2.1

**Communication pattern**: In-process Java interface

**Specification location**:
`com.example.payments.PaymentService` with Javadoc; supplementary
OpenAPI spec at `docs/payments/payment-service.yaml` (used during
extraction to define the equivalent HTTP API)

**Consumers**:

| Consumer | Version consumed | Contract test coverage |
|----------|------------------|------------------------|
| Orders | v2.1 | Pact tests |
| Refunds UI | v2.1 | Pact tests |
| Admin tools | v2.0 (migration to v2.1 in progress) | Pact tests |

**Previous versions still supported**:

| Version | Deprecation date | Retirement date |
|---------|------------------|-----------------|
| v2.0 | 2026-01-15 | 2026-06-30 |
| v1.0 | 2025-06-01 | 2025-12-31 (retired) |

**Versioning notes**: The v1 to v2 transition introduced the
`AuthRequest` sealed type pattern, consolidating the two
authorise overloads into a single operation. The v2.0 to v2.1
transition was purely additive (added the `lookupForOrder`
operation). Breaking changes have been rare.

**Stability**: Stable. No breaking changes in the last 14 months.
High on the Contract Stability signal.

---

### Contract: Payment domain events

**Owning module**: Payments

**Current version**: event schema v2

**Communication pattern**: Asynchronous events published to the
internal event bus; will migrate to Kafka topics during
extraction.

**Specification location**:
`docs/payments/event-schemas/` (JSON schemas for
`PaymentAuthorised`, `PaymentCaptured`, `PaymentRefunded`)

**Consumers**:

| Consumer | Version consumed | Contract test coverage |
|----------|------------------|------------------------|
| Reporting | v2 | Schema validation tests |
| Notifications | v2 | Schema validation tests |
| Audit | v2 | Schema validation tests |

**Previous versions still supported**:

| Version | Deprecation date | Retirement date |
|---------|------------------|-----------------|
| v1 | 2025-04-01 | 2025-09-30 (retired) |

**Versioning notes**: Event schemas are versioned through the
`schema_version` field in the event envelope. Consumers
negotiate on that field. Additive changes (new optional fields)
do not require a version bump; any change to required fields or
semantics does.

**Stability**: Stable. Events have been unchanged since the v2
migration.

---

### Contract: Stripe integration (external dependency)

**Owning module**: External (Stripe)

**Current version**: Stripe API v2024-11-20 (pinned)

**Communication pattern**: Synchronous HTTPS

**Specification location**: Stripe's public API documentation

**Consumers**: Payments (via `StripeAdapter`)

**Versioning notes**: Stripe's API version is pinned explicitly
via the `Stripe-Version` header. Version upgrades are treated as
significant changes and require an ADR.

**Stability**: External; stability is out of our control, though
Stripe's deprecation cadence is well-established.

---

### Contract: Adyen integration (external dependency)

**Owning module**: External (Adyen)

**Current version**: Adyen Checkout API v71

**Communication pattern**: Synchronous HTTPS

**Specification location**: Adyen's public API documentation

**Consumers**: Payments (via `AdyenAdapter`)

**Versioning notes**: Adyen API version pinned in configuration;
upgrades treated as significant changes requiring an ADR.

**Stability**: External; Adyen's deprecation policy is
documented and predictable.

---

### Contract: PayGate integration (external dependency)

**Owning module**: External (PayGate)

**Current version**: PayGate PayWeb3 API

**Communication pattern**: Synchronous HTTPS

**Specification location**: PayGate developer documentation

**Consumers**: Payments (via `PayGateAdapter`)

**Versioning notes**: PayGate's versioning is less predictable
than Stripe's or Adyen's. The adapter includes a compatibility
layer that absorbs minor schema changes. Significant changes
require rework; each such change in the last two years has been
tracked as a dedicated ADR.

**Stability**: External; less predictable than Stripe or Adyen.
PayGate serves the South African market specifically and has
been included despite the operational overhead because the local
market coverage is worth the cost.

---

### Contract: Orders domain events (consumed by Payments)

**Owning module**: Orders

**Current version**: v3

**Communication pattern**: Asynchronous events from the internal
event bus

**Specification location**: `docs/orders/event-schemas/`

**Consumers (Payments as a consumer)**: Payments subscribes to
`OrderPlaced` and `OrderCancelled` events.

**Versioning notes**: Payments is on v3. Consumer-driven Pact
tests exist to confirm that Orders continues to produce events
in the shape Payments expects.

**Stability**: Stable. This contract has been unchanged for 18
months.

---

### Contract: FraudService (consumed by Payments)

**Owning module**: Fraud

**Current version**: v1.4

**Communication pattern**: In-process Java interface

**Specification location**: `com.example.fraud.FraudService`
with Javadoc

**Consumers (Payments as a consumer)**: Payments calls
`FraudService.score(ScoreRequest)` at authorisation time.

**Versioning notes**: The Fraud team has been consistent with
versioning. v1.3 to v1.4 was purely additive.

**Stability**: Stable. Medium on Contract Stability (one breaking
change in the last year, handled through proper versioning; see
ADR-0047).

---

## System-wide policies

The payments domain follows the marketplace-wide contract
policies. The relevant points:

**Versioning**: Semantic versioning for in-process interfaces.
Event schemas use a `schema_version` field in the envelope. REST
APIs (where they exist, as on the Payments service after
extraction) use URL-based versioning.

**Breaking change handling**: Minimum 90-day deprecation notice.
Old versions supported for at least 180 days after deprecation
announcement. No breaking change lands without an ADR.

**Contract testing**: Pact tests required for all consumers of
in-process facades. Schema validation tests required for event
consumers. Both run on every build.

**Documentation**: Contracts live in the same repository as the
code. Changes to the contract and changes to the code must land
in the same pull request.

---

## Change log

| Date | Contract | Change | Reason |
|------|----------|--------|--------|
| 2026-03-02 | PaymentService | v2.1 added `lookupForOrder` | Simplifies callers needing order-level payment history |
| 2026-01-20 | PayGate | Adapter compatibility update | PayGate schema revision |
| 2025-12-15 | Payment events | v1 retired | All consumers migrated to v2 |
| 2025-09-30 | Payment events | v1 deprecated | Migration to v2 complete among active consumers |
| 2025-06-01 | PaymentService | v2.0 introduced | Consolidation of authorise operations via sealed AuthRequest |
