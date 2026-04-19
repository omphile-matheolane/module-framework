# ADR-0042: Extract Payments to an Independent Service

**Status**: Accepted

**Date**: 2026-03-15

**Deciders**: T. Lead (Payments Team), C. Architect (Commerce
Platform), Head of Engineering, Head of Compliance

**Related ADRs**:
- ADR-0014 (Modularisation of payments inside the monolith)
- ADR-0041 (Q3 2025 hardening of Payments)

---

## Context

The Payments module has been in production as a bounded module
within the marketplace monolith for three years. It was
modularised in 2024 (ADR-0014) and hardened in Q3 2025
(ADR-0041) in preparation for a potential future extraction. The
Q1 2026 evolution assessment (see
[`06-evolution-assessment.md`](06-evolution-assessment.md))
confirms that the module is ready for extraction and that two
genuine drivers now apply.

The business has committed to expanding into Nigeria in 2026.
The regulatory environment there includes payment data residency
requirements and an expectation of PCI-DSS level 1
certification at the infrastructure boundary. Both of those are
easier to demonstrate for an isolated service than for a shared
monolithic deployment. The compliance organisation has
indicated that they can certify either way, but that the path
with an isolated service carries less ongoing audit burden.

Separately, the Payments Team has been constrained by the
monolith's weekly release train. The team is capable of shipping
more frequently and has been paced by the shared pipeline rather
than by its own capacity. This has been manageable historically
but becomes problematic as the pace of payment-related work
increases with new methods (buy-now-pay-later, local mobile money
in Nigeria) and new markets coming online.

The assessment signals support extraction:

- Code Isolation: High (zero architecture test suppressions)
- Data Ownership: High (all 13 tables owned exclusively, no
  cross-module foreign keys, mock migration validated in staging)
- Contract Stability: High (no breaking changes in 14 months,
  all consumers covered by Pact or schema validation)
- Team Autonomy: Medium (deployment-level constraint)
- Operational Impact: High (healthy, one provider-caused
  incident in the last quarter)

The regulatory driver is sufficient on its own. The team
autonomy driver is additional.

---

## Decision

Extract the Payments module from the monolith and deploy it as
an independent service. The new service will:

- Have its own deployment pipeline, monitoring, alerting, and
  on-call rotation
- Run in isolated infrastructure that meets PCI-DSS level 1
  requirements independently of the monolith
- Support payment data residency in the new geography by running
  a regional instance with data stored locally
- Expose its functionality through a REST API and publish domain
  events to the shared Kafka cluster
- Retire the in-process `PaymentService` facade once all
  consumers have migrated

---

## Alternatives considered

### Alternative 1: Stabilise

Leave the module as-is and pursue compliance through the
monolith. Rejected because the audit burden at the monolith
level is greater than the audit burden at the service level,
and the team autonomy constraint would remain unaddressed.

### Alternative 2: Harden further

Continue hardening without extracting. Rejected because the
module is already hardened and there is nothing further to
strengthen at the boundary. The remaining constraint is
deployment coupling, which hardening does not address.

### Alternative 3: Extract only the Nigeria-specific portion

Create a separate service for Nigerian payments while keeping
the rest of Payments in the monolith. Rejected because it would
create two systems to maintain, would fragment the payments
contract, and would not address the team autonomy constraint.
The team unanimously preferred a single extracted service.

### Alternative 4: Extract later (after Nigerian launch)

Launch Nigeria on the monolith, then extract afterwards.
Rejected because the compliance work is materially cheaper if
done at extraction time, and retrofitting isolation after
launch would duplicate effort. The extraction is scheduled to
precede the Nigerian launch.

---

## Consequences

### Positive consequences

**Regulatory posture**: PCI-DSS level 1 certification and
payment data residency become clean architectural facts rather
than ongoing audit exercises. The compliance organisation
expects this to reduce audit cost by roughly 40% annually.

**Team autonomy**: the Payments Team gains full control over
their deployment cadence. They expect to move from weekly to
daily deployments, with corresponding improvement in
delivery pace.

**Technology flexibility**: the service can adopt technology
choices appropriate to its specific needs (for example, a
smaller JVM footprint for the Nigerian region) without
affecting the monolith.

**Operational isolation**: a payment-specific incident no
longer has any path to degrade the monolith as a whole. The
reverse is also true.

### Negative consequences or trade-offs

**Operational overhead**: running an independent service adds
infrastructure, monitoring, and on-call costs. Estimated at
roughly 15% of one engineer's capacity ongoing, spread across
the team.

**Latency budget pressure**: what is currently an in-process
call from Orders to Payments will become a network call. The
expected additional latency is roughly 5-10ms, which is within
the current SLO headroom but will need monitoring.

**Fraud call still synchronous**: the Payments-to-Fraud call
remains synchronous. Post-extraction, this will be a
service-to-service call rather than an in-process one. The
latency impact is manageable but real. Fraud's extraction (if
ever pursued) would further extend this path.

**Migration coordination**: five consumers need to migrate from
the in-process facade to the REST API. This coordination is
non-trivial and will slow the migration compared to a hard
cutover.

### Neutral consequences

**New consumer-driven concerns**: the Pact test infrastructure
that already exists for the in-process contract extends
naturally to the REST contract. Consumer migration can happen
alongside the existing test coverage rather than requiring new
testing infrastructure.

**Event delivery moves to Kafka**: payment events currently
flow through the internal event bus. After extraction, they
will flow through Kafka. The event schemas do not change, but
consumers will need to update their subscription mechanism.
This is a coordinated change, not an emergency.

---

## Implementation notes

The implementation is detailed in
[`08-extraction-plan.md`](08-extraction-plan.md). The high-level
shape:

**Phase 1** (Q2 2026): Deployment infrastructure, service
skeleton, parallel running with in-process path.

**Phase 2** (Q2/Q3 2026): Consumer migration, one consumer per
sprint, behind a feature flag.

**Phase 3** (Q3 2026): Retire the in-process path, validate
compliance, decommission the shared infrastructure.

**Next actions** (immediate):

1. Confirm infrastructure plan with Platform team
2. Kick off Phase 1 work
3. Brief each consumer team on the migration plan
4. Schedule the first compliance checkpoint for end of Phase 1

**Dependencies**:
- Platform team for deployment infrastructure
- Security team for compliance sign-off
- All five consumer teams for migration

**Rollback criteria**:

The extraction will be considered unsuccessful and a consolidation
decision revisited if, within 90 days of Phase 3 completion:

- The regulatory benefits have not materialised (compliance
  determines that the isolated posture does not reduce audit
  burden as expected)
- The team autonomy benefits have not materialised (deployment
  cadence has not improved, or has improved but not in ways
  that matter for delivery)
- The operational cost exceeds twice the projected figure
- Latency or reliability degrades significantly and cannot be
  recovered

The specific rollback approach would follow the
[consolidate outcome guide](../../../decision-model/outcomes/consolidate.md).

---

## References

- Evolution assessment: [`06-evolution-assessment.md`](06-evolution-assessment.md)
- Extraction plan: [`08-extraction-plan.md`](08-extraction-plan.md)
- Contract catalog entry: [`03-contract-catalog.md`](03-contract-catalog.md)
- Module structure documentation: [`02-module-structure.md`](02-module-structure.md)
- Prior hardening work: ADR-0041
- Original modularisation: ADR-0014
- Nigeria regulatory assessment: (internal document, not
  included in this example)
