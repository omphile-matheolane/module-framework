# Worked Examples of the Decision Model

These examples apply the evolution decision model to realistic but
fictional components. Each follows the structure of the
[scoring worksheet](../scoring-worksheet.md), records the evidence,
assesses the signals, and explains how the pattern of signals
combined with the drivers produced the outcome.

The purpose is to show what a completed assessment looks like and
how the signals interact in practice. The specific components are
illustrative; the patterns are the things worth attending to.

---

## Examples

| File | Pattern illustrated |
|------|---------------------|
| [`01-payments-extract.md`](01-payments-extract.md) | Extraction of a well-formed module with a genuine driver |
| [`02-notifications-modularise.md`](02-notifications-modularise.md) | Modularisation of a component with weak boundaries in a monolith |
| [`03-fraud-harden.md`](03-fraud-harden.md) | Hardening a module that is close to High on the key signals |
| [`04-search-consolidate.md`](04-search-consolidate.md) | Consolidation of a service whose extraction drivers no longer apply |
| [`05-reporting-stabilise.md`](05-reporting-stabilise.md) | Stabilisation of a component that is serving the organisation well |

---

## How to read these

Each example includes the full worksheet, the signal ratings with
evidence, the driver assessment, and the outcome with reasoning. The
examples are deliberately written at the length a real assessment
would take: long enough to support the decision, short enough to
be worth writing.

Pay attention to the reasoning sections. The signals alone do not
produce the outcome. It is the pattern of signals combined with the
drivers and the current deployment model that produces the
recommendation. Two components with identical signal ratings can
legitimately reach different outcomes depending on context.
