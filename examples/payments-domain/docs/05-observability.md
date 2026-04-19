# Observability: Payments Domain

**System**: Marketplace Core (payments portion)

**Last updated**: 2026-03-15

**Maintained by**: Payments Team

---

## Purpose

This document captures how observability is structured for the
Payments module: what is instrumented, where the data goes, what
dashboards exist, and how observability data feeds into
architectural decisions.

It is the practical view of the [Leverage discipline](../../../disciplines/05-leverage/)
applied to payments specifically.

---

## What is instrumented

### Structured logs

Every log line emitted from the Payments module includes the
following fields:

```json
{
  "timestamp": "2026-03-15T14:32:11.482Z",
  "level": "INFO",
  "module": "payments",
  "component": "authorisation",
  "trace_id": "a1b2c3d4e5f6",
  "span_id": "9e8f7d6c",
  "message": "payment authorised",
  "payment_id": "pay_01HX9...",
  "order_id": "ord_01HX8...",
  "amount_cents": 4500,
  "currency": "ZAR",
  "provider": "stripe"
}
```

The `module` field is always `payments`. The `component` field
is one of `authorisation`, `capture`, `refund`, `reconciliation`,
`integration`. Trace and span identifiers come from OpenTelemetry.

Sensitive fields (card data, CVV, full authentication responses)
are never logged. The redaction is enforced at the logging
framework level, not left to individual log statements.

### Metrics

The following metrics are emitted from the Payments module:

**Latency metrics** (histograms with module, component, and
operation labels):
- `payments_operation_duration_ms` for each of `authorise`,
  `capture`, `refund`, `lookup`
- `payments_provider_call_duration_ms` for calls to external
  providers, tagged with the provider name

**Error metrics** (counters):
- `payments_operation_errors_total` with labels for operation,
  error category, and whether the error was recoverable
- `payments_provider_errors_total` with provider and error
  category labels

**Throughput metrics** (counters):
- `payments_operations_total` with operation and outcome labels
  (success, failure, partial)

**Business metrics** (for the team to understand demand, not
strictly architectural):
- `payments_value_cents_total` with currency labels
- `payments_active_authorisations` (gauge)

### Distributed tracing

The module participates in distributed tracing through
OpenTelemetry. Every operation creates spans with:

- Span name in `payments.<operation>` format (e.g.
  `payments.authorise`)
- `module` attribute set to `payments`
- `component` attribute set to the internal component
- Provider details on integration spans
- Payment and order identifiers as attributes for correlation

Sampling is configured to capture 100% of traces where errors
occur, 100% of traces with latency above p99 targets, and 10% of
successful traces at target latency. This preserves visibility
into the tail while keeping trace volume manageable.

---

## Dashboards

### Payments module health

A single dashboard showing the module's overall health:

- Authorisation success rate (last 1h, 24h, 7d)
- Latency percentiles (p50, p95, p99) for each operation
- Error rate by error category
- Provider-level success rate and latency (one row per provider)
- Volume by currency and payment method

Reviewed in the weekly Payments Team meeting. Deviations from
targets are discussed and acted on.

### Payment interactions

A dashboard showing how the Payments module interacts with the
rest of the system:

- Call volume from Orders to Payments (authorisation path)
- Event consumption rate from Payments by each subscriber
  (Reporting, Notifications, Audit)
- Latency of Payments -> Fraud calls (upstream dependency)
- Latency of Payments -> Provider calls (external dependencies)

This dashboard is particularly useful for architectural
decisions because it shows the module's runtime coupling with
its neighbours.

### Reconciliation

A dashboard specific to the reconciliation process:

- Reconciliation lag (time between provider settlement and our
  reconciliation record)
- Unmatched transactions by age
- Reconciliation error rate by provider

This is operational rather than architectural, but it surfaces
data integrity issues that sometimes have architectural
implications.

---

## What the data has surfaced

Concrete examples of how observability data has informed
architectural decisions for payments:

**Pre-hardening (Q2 2025)**: Tracing revealed that Reporting was
reading directly from the `provider_reconciliation_log` table
rather than going through the Payments facade. This was not
apparent from reading the code alone; the query was in a
different module entirely. The discovery drove the hardening
work in Q3 2025.

**During the hardening (Q3 2025)**: Latency data showed that the
Payments -> Fraud call was the single largest contributor to
authorisation latency, accounting for around 40% of the total.
This is still the case post-hardening, and it is recorded as a
factor in the extraction decision (the extracted service will
need to maintain the synchronous call to Fraud, and the latency
budget will need to account for the additional network hop).

**Pre-extraction assessment (Q1 2026)**: The Operational Impact
assessment was supported by observability data showing zero
payments-attributable incidents in the last quarter, consistent
p99 latency within SLO, and modest on-call volume. Without this
data, the Operational Impact rating would have been based on
impression.

---

## Retention

- Logs: 30 days at full fidelity, 90 days for error-level logs
- Metrics: 13 months at 1-minute resolution
- Traces: 7 days for routine traces, 90 days for traces flagged
  as interesting (errors, latency outliers)

The retention is longer than pure operational needs would
require, deliberately, because architectural decisions operate
on timescales that pure operational retention does not cover.
The ability to go back a year and look at trends is what makes
the evolution assessment defensible.

---

## Alerting

The alerts that fire to the Payments Team on-call:

- Authorisation success rate below 98% for 5 minutes
- Authorisation p99 latency above 800ms for 5 minutes
- Any provider showing error rate above 1% for 5 minutes
- Reconciliation lag above 2 hours

These are operational alerts rather than architectural ones.
Architectural concerns (boundary violations, contract drift,
dependency changes) surface in the dashboards and the quarterly
reviews rather than as real-time alerts.

---

## How this feeds MODULE

The observability data feeds into the other disciplines in
specific ways:

**Map**: the Payments interactions dashboard confirms whether
the documented dependencies match runtime behaviour. When they
diverge, the domain map is updated or the behaviour is
investigated.

**Organise**: traces occasionally reveal calls that bypass the
public surface through reflection or dynamic proxies. These are
investigated and either closed or documented as exemptions.

**Design**: latency data on contract interactions (Payments ->
Fraud, Orders -> Payments) reveals whether the contracts are
performing as expected. Chatty interactions or consistent
latency violations prompt contract-level discussions.

**Unify**: observability data can surface violations that
compile-time checks miss. The test suite cannot catch
reflection-based access to internals; the traces can.

**Evaluate**: observability provides the Operational Impact
signal and contributes evidence for Contract Stability (through
error rates on contract calls). Without this data, evolution
assessments would be much more speculative.

---

## Post-extraction considerations

Once the extraction completes, the observability posture will
need to adjust:

- Logs continue to carry the `module: payments` field, but new
  service-level metadata (service instance, region) will be added
- Metrics shift from being aggregated at the monolith level to
  being aggregated at the service level
- Tracing becomes more important, because what was previously an
  in-process call is now a network call that can fail in new ways
- New alerts will be needed for service-level concerns (instance
  health, cross-service latency, message queue backlogs for the
  event bus migration)

The extraction plan includes the observability adjustments as
part of the Phase 2 (deployment separation) work.
