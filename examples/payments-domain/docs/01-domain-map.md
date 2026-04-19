# Domain Map: Payments Domain

**System**: Marketplace Core (monolith)

**Last updated**: 2026-03-15

**Maintained by**: Payments Team, reviewed by Commerce Platform
group

---

## Purpose

This map documents the payments portion of the marketplace's
domain. It is a working document that reflects the current state,
not the intended state. The broader system map covers other
domains (orders, inventory, catalog, notifications, reporting)
and is maintained separately.

---

## Review cadence

Quarterly review by the Payments Team with Commerce Platform
input. Last review: 2026-03-15. Next scheduled review: 2026-06-15.

---

## Modules in the payments domain

### Module: Payments

**Business capability**: Authorises, captures, and refunds
payments for marketplace orders. Reconciles with external
payment providers. Produces financial records for downstream
accounting and reporting.

**Owning team**: Payments Team (5 engineers)

**Status**: Active. Currently being extracted to an independent
service (see ADR-0042).

**Public surface**:
- `PaymentService` facade (Java interface)
- `PaymentAuthorised`, `PaymentCaptured`, `PaymentRefunded`
  domain events (published to the internal event bus)
- `PaymentRecord` read model (immutable, exposed through the
  facade)

**Data owned**:
- `payment_authorisations`
- `payment_captures`
- `payment_refunds`
- `payment_records`
- `provider_reconciliation_log`
- `payment_audit_trail`
- Seven further tables holding internal state that other
  modules have no business reading

**Dependencies**:

| Depends on | Type | Notes |
|-----------|------|-------|
| Provider integrations (external) | Sync HTTP | Stripe, Adyen, PayGate |
| Orders | Async event subscription | Consumes `OrderPlaced` events |
| Fraud | Sync | Scores transactions before authorisation |

**Consumed by**:

| Consumer | Type | Notes |
|----------|------|-------|
| Orders | Sync facade calls | Authorises payment for order confirmation |
| Refunds UI (admin) | Sync facade calls | Admin-initiated refund flows |
| Reporting | Async event subscription | Consumes payment events for financial reports |
| Notifications | Async event subscription | Consumes events to send customer communications |

**Known issues**:
- One remaining read dependency from Reporting against
  `provider_reconciliation_log` was resolved in February 2026.
  All reads now go through the facade.
- The `refund_reason_code` enum on the public contract has not
  been standardised across consumers. Refunds UI and
  Notifications interpret a couple of the codes differently.
  Flagged for Q2 2026.

---

### Module: Fraud

**Business capability**: Scores transactions and flags suspicious
activity. Provides a synchronous risk score to Payments at
authorisation time and an asynchronous review queue for offline
analysis.

**Owning team**: Risk Team (4 engineers)

**Status**: Active. Recently hardened (ADR-0051, Q2 2026 work in
progress).

**Public surface**:
- `FraudService` facade
- `RiskFlagged`, `RiskCleared` domain events

**Data owned**: Eight tables covering risk events, scoring
history, review queues, and analyst overrides. No cross-module
foreign keys remain.

**Dependencies**:

| Depends on | Type | Notes |
|-----------|------|-------|
| External fraud providers | Sync HTTP | For enrichment data |

**Consumed by**:

| Consumer | Type | Notes |
|----------|------|-------|
| Payments | Sync facade calls | Risk score at authorisation |
| Reporting | Async event subscription | Regulatory reporting |

**Known issues**: Two older architecture test suppressions from
before modularisation, being addressed as part of the hardening
work in Q2 2026.

---

### Module: Refunds UI

**Business capability**: Admin-facing tools for initiating and
reviewing refunds.

**Owning team**: Internal Tools Team (3 engineers)

**Status**: Active, stable.

**Public surface**: Web UI; no programmatic public surface
consumed by other modules.

**Data owned**: Session state and UI preferences only. No domain
data.

**Dependencies**:

| Depends on | Type | Notes |
|-----------|------|-------|
| Payments | Sync facade calls | Read and refund operations |
| Orders | Sync facade calls | Order lookup |

**Consumed by**: End users only (admins).

---

## Dependency overview

```
graph LR
    orders -->|places payment| payments
    fraud -->|risk score| payments
    refundsUI -->|refund ops| payments
    refundsUI -->|order lookup| orders
    payments -->|events| reporting
    payments -->|events| notifications
    fraud -->|events| reporting
    payments -->|HTTP| external_providers
    fraud -->|HTTP| external_fraud_providers
```

---

## Changes since last review

| Date | Change | Reason |
|------|--------|--------|
| 2026-02-10 | Removed last direct read of `provider_reconciliation_log` by Reporting | Hardening prerequisite to extraction |
| 2026-03-15 | Extraction of Payments scheduled (ADR-0042) | Regulatory expansion into new geography |

---

## Notes

The extraction of Payments is in progress (ADR-0042, target
completion Q3 2026). When the extraction completes, this map will
be updated to reflect the new deployment model. The Payments
module as documented here continues to exist inside the monolith
until migration completes; the "module" and "service"
designations will be distinct in the map for the duration of the
migration.

Fraud was considered as an extraction candidate during the Q1
2026 review. The assessment (see
[`06-evolution-assessment.md`](06-evolution-assessment.md) in the
decision model examples for a fraud module example) concluded
that hardening was the appropriate outcome rather than
extraction. That decision will be revisited in Q1 2027.
