# Extraction Plan: Payments

This plan implements the decision recorded in ADR-0042
([`07-adr-extract.md`](07-adr-extract.md)). It is the working
document for the Payments Team during the extraction.

**Owner**: Payments Team (T. Lead accountable)

**Target completion**: End of Q3 2026

**Last updated**: 2026-03-15

---

## Summary

The Payments module will be extracted from the monolith as an
independent service over two quarters. Work proceeds in three
phases: infrastructure and parallel running, consumer migration,
and in-process retirement. Each phase has entry and exit
criteria, and the plan can be paused or rolled back at the phase
boundaries if the extraction is not delivering as expected.

---

## Phase 1: Infrastructure and parallel running

**Target timeframe**: Q2 2026 (approximately 10 weeks)

**Objective**: Stand up the payments service running in parallel
with the in-process path, processing no production traffic, fully
instrumented.

### Work items

1. **Deployment infrastructure**
   - Provision isolated compute and network infrastructure
     meeting PCI-DSS level 1 requirements
   - Configure the deployment pipeline (CI, CD, artifact
     registry, environment promotion)
   - Establish the Nigerian region in the target architecture

2. **Service skeleton**
   - Scaffold the service project with the same internal
     structure as the existing module (see
     [`02-module-structure.md`](02-module-structure.md))
   - Migrate the domain and persistence layers largely
     unchanged
   - Wrap the facade in an HTTP API using the OpenAPI spec
     already referenced in the contract catalog
   - Migrate the provider integrations, adding retry, circuit
     breaker, and timeout behaviour appropriate to network
     calls

3. **Data infrastructure**
   - Provision the service's database, separate from the
     monolith's database
   - Plan the data migration strategy (dump-and-restore for
     initial state; logical replication for the transition
     period to keep both sides in sync)

4. **Event infrastructure**
   - Configure Kafka topics for the payment events
   - Implement the event publisher in the service
   - Consumers will continue to receive events from the
     in-process event bus until migrated in Phase 2

5. **Observability**
   - Extend the dashboards described in
     [`05-observability.md`](05-observability.md) to cover the
     service
   - Add service-specific signals: instance health, cross-service
     latency, Kafka publisher health
   - Configure alerting for the service

6. **Compliance**
   - Work with Security and Compliance to validate the isolated
     posture meets PCI-DSS level 1 requirements
   - Validate payment data residency for the Nigerian region

### Exit criteria

- Service is deployed in the production environment
- Service processes synthetic traffic successfully
- Both the service and the in-process path handle the full set
  of payment operations identically for the same inputs
- Dashboards show the service operating within SLO
- Compliance has provided sign-off on the infrastructure posture

### Phase 1 checkpoint

At the end of Phase 1, the team reviews whether to proceed.
Reasons to pause or reconsider include:

- Compliance has identified concerns that cannot be resolved
- The operational cost of running the service is materially
  higher than projected
- Latency or reliability in the service is worse than in the
  monolith

If these concerns apply, the decision is revisited (possibly
back to the evolution assessment) before Phase 2 begins.

---

## Phase 2: Consumer migration

**Target timeframe**: Q2/Q3 2026 (approximately 8 weeks)

**Objective**: Migrate all consumers from the in-process path to
the service. Both paths remain available throughout Phase 2 so
that individual consumers can be rolled back if issues emerge.

### Migration order

Consumers are migrated in order of complexity and risk. Simpler
consumers go first to build confidence; the most consequential
consumer (Orders) goes later, once the service's stability is
established.

1. **Audit** (week 1-2). Lowest risk. Consumes events for
   archival; no synchronous path. Migrating means switching the
   event subscription from the internal bus to Kafka.

2. **Notifications** (week 2-3). Low risk. Consumes events for
   customer communications. Delivery can tolerate brief delays
   without business impact.

3. **Reporting** (week 3-5). Medium risk. Consumes events for
   financial reports and makes synchronous lookups through the
   facade for monthly reconciliation. Both paths migrate in
   this step.

4. **Refunds UI** (week 5-6). Medium risk. Makes synchronous
   facade calls for admin-initiated refunds. Relatively low
   volume; admin-facing so any issues are detected quickly.

5. **Orders** (week 6-8). Highest risk. Payment authorisation
   is on the critical path for order placement. Requires the
   most careful migration.

For each consumer:

- Feature flag controls which path (in-process or service) is
  used
- Migration happens at configurable percentage rollouts (10%,
  50%, 100%)
- Observability dashboards compare the two paths during rollout
- Each percentage step gets 24 hours of observation before
  advancing

### Exit criteria

- All consumers are at 100% on the service path
- No consumer has rolled back in the last two weeks
- Observability data shows comparable or better performance on
  the service path
- Any identified issues have been addressed and verified

### Phase 2 checkpoint

At the end of Phase 2, the team confirms the migration is stable
before proceeding to in-process retirement. Reasons to pause:

- Any consumer is still oscillating between paths
- Latency or error rates are materially worse on the service
  path
- Operational issues are occurring that were not anticipated

---

## Phase 3: In-process retirement

**Target timeframe**: Q3 2026 (approximately 4 weeks)

**Objective**: Remove the in-process payments code from the
monolith, validate compliance, and decommission the parallel
infrastructure.

### Work items

1. **Code removal**
   - Remove the in-process payments module code from the
     monolith
   - Remove the feature flags controlling the path selection
   - Update the monolith's architecture tests: the rules that
     previously enforced the payments boundary are no longer
     needed (the module is gone); the new rules enforce that
     nothing in the monolith attempts to re-implement payments
     or access payments data directly

2. **Database cleanup**
   - Drop the payments tables from the monolith's database
     (after validating that the data has been fully migrated
     and that no read paths remain)
   - Update the monolith's schema documentation
   - Remove the temporary logical replication from the
     monolith's database to the service's database

3. **Event bus cleanup**
   - Disable the in-process event publication for payment
     events
   - Remove the event definitions from the monolith's shared
     event catalog

4. **Compliance validation**
   - Full compliance audit of the extracted posture
   - PCI-DSS level 1 certification confirmed for the service
   - Payment data residency confirmed for the Nigerian region

5. **Documentation updates**
   - Update the domain map (Payments becomes a service rather
     than a module)
   - Update the contract catalog (in-process contract retired;
     REST contract and Kafka event contracts become primary)
   - Update the ownership registry (no change to ownership, but
     the module description updates)
   - Archive the superseded documents

### Exit criteria

- In-process payments code is removed from the monolith
- The monolith builds, deploys, and runs successfully without
  any payments code
- Compliance has certified the final posture
- All documentation is updated
- A retrospective has been held covering what worked and what
  did not

---

## Rollback

Rollback is structured differently at each phase.

**Rollback during Phase 1**: trivial. The in-process path is the
primary path; the service is running in parallel with no
consumers. Stopping Phase 1 leaves the monolith unaffected.

**Rollback during Phase 2**: per-consumer. Each consumer can be
rolled back independently through the feature flag. Full rollback
is possible if multiple consumers need to return to the
in-process path.

**Rollback during Phase 3**: substantially harder. Once the
in-process code is removed, the monolith cannot serve payments
without being reverted. The rollback procedure involves redeploying
the previous monolith version and switching consumers back. This
is practicable but costly, and Phase 3 is deliberately gated on
strong confidence from Phase 2.

**Rollback after Phase 3**: this is effectively consolidation
rather than rollback. If, after full extraction, the decision
proves wrong, the correct response is to apply the
[consolidate outcome](../../../decision-model/outcomes/consolidate.md)
rather than trying to restore the pre-extraction state. The
rollback criteria in ADR-0042 describe when this would apply.

---

## Risks and mitigations

| Risk | Likelihood | Impact | Mitigation |
|------|-----------|--------|------------|
| Compliance does not certify the isolated posture | Low | High | Compliance consulted before the assessment, has indicated readiness to certify either path |
| Latency degradation from network boundary | Medium | Medium | Explicit SLO budget reserved for the extra hop; monitoring on the Orders-to-Payments path |
| Data migration loses records | Low | Very high | Dual-write period validates parity; reconciliation tooling confirms every record transferred |
| Event ordering issues during Kafka migration | Medium | Medium | Consumers designed to tolerate out-of-order events (already the case for events consumed by Reporting and Audit) |
| Consumer migration stalls | Medium | Medium | Phase 2 is structured to tolerate individual consumer delays; the overall timeline has slack |
| Nigerian launch timeline compresses | Low | Medium | Phase 1 compliance work on schedule; contingency plan to delay Nigerian launch rather than compress extraction |

---

## Communication

Weekly status goes to:

- Commerce Platform group (peer group for the Payments Team)
- Consumer team leads (Orders, Refunds UI, Reporting,
  Notifications, Audit)
- Head of Engineering
- Head of Compliance

Broader updates at phase boundaries go to all engineering and to
the relevant business stakeholders.

Significant issues or changes to the plan are communicated
immediately rather than waiting for the weekly update.

---

## Tracking

Progress against this plan is tracked in the team's standard
project management tooling. This document is the source of truth
for the plan's structure; specific task-level tracking lives
with the team's usual tooling.

Phase boundaries are captured as explicit milestones with
written sign-off from the Payments Team lead and the Commerce
Platform architect.

---

## Post-extraction review

Six months after Phase 3 completes (so Q1 2027), the team runs
the scheduled reassessment of the now-extracted Payments
service. The assessment follows the standard evolution workflow
(see [`evolution-workflow.md`](../../../adoption/playbooks/evolution-workflow.md)),
with particular attention to whether the drivers that motivated
the extraction have paid off.

If the answer is yes, the extraction has succeeded and the
decision stands. If the answer is no, consolidation is the
honest response, and the rollback criteria in ADR-0042 apply.
