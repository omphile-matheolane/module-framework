# Evolution Assessment: Payments

This is the completed evolution assessment that led to the
decision to extract Payments as an independent service. It is
the payments-specific instance of the
[scoring worksheet](../../../decision-model/scoring-worksheet.md).

The assessment was conducted on 2026-03-15 by two assessors
working independently, then reconciled in a joint session.

---

## Component under assessment

**Component name**: Payments

**Current deployment model**: Module inside a modular monolith

**Owning team**: Payments Team (5 engineers)

**Assessors**: T. Lead (Payments Team), C. Architect (Commerce
Platform)

**Date of assessment**: 2026-03-15

**Previous assessment**: 2025-06-10 (recommended harden;
hardening completed Q3 2025, verified Q4 2025)

---

## Signal assessments

### 1. Code Isolation: **High**

**Evidence**:
- ArchUnit tests pass on CI build 4821 (2026-03-14) with zero
  suppressions
- Public surface is the `PaymentService` facade plus the three
  domain events plus the `PaymentRecord` read model
- Dependency graph (from static analysis): no module imports
  from `com.example.payments.domain..`, `persistence..`, or
  `integration..`
- The exception is the deliberate `StripeWebhookHandler`
  exemption, which is documented and does not involve consumer
  modules reaching into internals

**Notes**: The hardening work in Q3 2025 closed the remaining
two suppressions (Reporting reading from `PaymentRecordRepository`
directly). The boundary is clean.

---

### 2. Data Ownership: **High**

**Evidence**:
- Payments owns 13 tables. No other module has write access.
- The last remaining read dependency (Reporting reading
  `provider_reconciliation_log` for monthly financial reports)
  was routed through the Payments facade in February 2026
- No cross-module foreign keys involve Payments tables
- Schema access control in production confirms write permissions
  are scoped to the payments service account only
- Validation: a mock migration of Payments tables to a separate
  database was run in the staging environment in February 2026
  and completed without breaking any other module

**Notes**: The data separation is real, not notional.

---

### 3. Contract Stability: **High**

**Evidence**:
- No breaking changes to `PaymentService` or payment events in
  the last 14 months (longer than the 12-month threshold)
- All five consumers covered by Pact tests or schema validation
  tests
- Versioning policy applied consistently: v2.0 was introduced
  with a 90-day deprecation period, v1 was retired on schedule
- Two purely additive changes in the last year (v2.1 added
  `lookupForOrder`, and one event got a new optional field),
  both handled without breaking anyone

**Notes**: The contract has been genuinely stable.

---

### 4. Team Autonomy: **Medium**

**Evidence**:
- The Payments Team ships its own changes without cross-team
  code review
- Deployments are constrained by the monolith's weekly release
  train
- Emergency patches in the last quarter (one, for a Stripe API
  compatibility issue) required release coordination with three
  other teams
- The team's deployment cadence is limited by the shared
  pipeline; they would deploy more frequently if they could

**Notes**: The boundary problem here is deployment coupling
rather than code coupling. This is exactly the kind of constraint
extraction addresses.

---

### 5. Operational Impact: **High**

**Evidence**:
- One incident attributable to payments in the last quarter
  (a Stripe provider outage, not a module defect)
- Latency within SLO consistently: p99 under 400ms for
  authorisation across the last 90 days
- On-call volume for payments is modest: two pages in the last
  quarter, both related to the Stripe outage above

**Notes**: The module is operationally quiet.

---

## Summary

| Signal | Rating |
|--------|--------|
| Code Isolation | High |
| Data Ownership | High |
| Contract Stability | High |
| Team Autonomy | Medium |
| Operational Impact | High |

---

## Driver assessment

- [x] Team autonomy needs that the current structure constrains
- [x] Regulatory requirements demanding separation
- [ ] Independent scaling requirements
- [ ] Technology divergence
- [ ] Cost reduction
- [ ] Operational burden disproportionate to business value
- [x] Frequent change concentrated in this component
- [ ] Multiple teams working on this component regularly

**Driver summary**:

The business is expanding into a new geography (Nigeria) with
regulatory requirements that are easier to satisfy with
infrastructure separation. Specifically, local payment data
residency rules and PCI-DSS level 1 certification are both
simpler to demonstrate for an isolated service than for a shared
monolithic deployment.

On top of the regulatory driver, the Payments Team is constrained
by the monolith's weekly release train. The team is capable of
shipping more frequently than the current pipeline supports, and
the pace of payment-related work is increasing as new payment
methods and new markets come online.

The regulatory driver on its own would be enough to justify
extraction. The team autonomy benefit is additional.

---

## Outcome

**Recommended outcome**: Extract to a service

**Reasoning**:

The three signals that gate extraction (Code Isolation, Data
Ownership, Contract Stability) are all High, which is the
necessary precondition. Team Autonomy is Medium, but the Medium
rating reflects the current deployment-level constraint rather
than a code-level issue, and extraction is precisely what would
resolve it. Operational Impact is High (healthy), which means
extraction is not inheriting operational problems.

Two drivers apply. The regulatory driver (Nigeria expansion) is
a hard requirement with a fixed timeline. The team autonomy
driver is additional but real.

Alternative outcomes considered:

**Stabilise**: would ignore the regulatory driver and is not
viable on that basis alone.

**Harden**: the module is already hardened (Q3 2025). There is
nothing further to harden. The remaining weakness is Team
Autonomy, which hardening cannot address.

**Modularise**: the module is already modularised.

**Consolidate**: the component is not extracted, so
consolidation does not apply.

Extract is the outcome. The module has been prepared
deliberately over the last 18 months for this move, and the
drivers that would justify it have now materialised.

---

## Implementation plan

**Next actions**:

1. Record the decision as ADR-0042 (complete, see
   [`07-adr-extract.md`](07-adr-extract.md))
2. Develop the detailed extraction plan (complete, see
   [`08-extraction-plan.md`](08-extraction-plan.md))
3. Phase 1: Set up independent deployment pipeline and
   infrastructure
4. Phase 2: Implement the service with the existing module as
   the source
5. Phase 3: Migrate consumers one at a time behind a feature flag
6. Phase 4: Retire the in-process path
7. Phase 5: Confirm regulatory compliance

**Estimated effort**: Two quarters of focused work by the
Payments Team, with platform support.

**Dependencies**:
- Platform team for deployment infrastructure
- Security team for compliance sign-off
- Consumer teams (Orders, Refunds UI, Reporting, Notifications,
  Audit) for migration coordination

**Target completion**: End of Q3 2026.

---

## Reassessment trigger

**Scheduled reassessment date**: Q1 2027 (six months after target
completion)

**Events that would trigger earlier reassessment**:
- Extraction does not deliver the regulatory or autonomy benefits
  within 90 days of completion
- Operational cost of the separate service exceeds twice the
  projected figure
- Significant change in consumer patterns during the migration
  (e.g. a new consumer coming online with different requirements)
- Material change in regulatory requirements

---

## Sign-off

**Assessor A**: T. Lead (Payments Team)

**Assessor B**: C. Architect (Commerce Platform)

**Decision recorded as ADR**: ADR-0042
([`07-adr-extract.md`](07-adr-extract.md))

---

## Notes from the reconciliation session

The two assessors had independent ratings that agreed on four of
the five signals. The disagreement was on Team Autonomy: T. Lead
rated Low based on the team's felt experience of being
constrained, C. Architect rated Medium based on the evidence
that routine deployments do ship independently.

The reconciliation settled on Medium. The evidence showed that
standard work does ship without cross-team coordination; the
constraint is specifically around deployment cadence and
emergency response. Low would overstate the problem. The Medium
rating combined with the deployment-related driver captured the
situation more accurately than either initial rating alone.

This disagreement was useful. It surfaced a distinction that
would not have come out of a single-assessor session: the
difference between code-level autonomy (High) and
deployment-level autonomy (Medium). The extraction addresses
the second without needing to address the first, which is the
right shape for the work.
