# Example 1: Payments — Extract

A well-formed payments module inside a modular monolith at a
mid-sized ecommerce company. The module has been in production for
three years. The company is expanding into a new geography with
different regulatory requirements, and the payments team has grown
to five engineers working full-time on the module.

---

## Component under assessment

**Component name**: Payments

**Current deployment model**: Module inside a modular monolith

**Owning team**: Payments Team (5 engineers)

**Assessors**: A. Engineer, B. Architect

**Date of assessment**: 2026-03-15

**Previous assessment**: 2025-06-10 (recommended harden; hardening
completed Q3 2025)

---

## Signal assessments

### 1. Code Isolation: **High**

**Evidence**:
- ArchUnit tests pass with zero suppressions (confirmed on CI build 4821)
- Public surface is the `PaymentService` facade and three domain events
- Dependency graph shows all cross-module traffic routed through the facade
- No module imports from `com.example.payments.internal.*`

**Notes**: Hardening work in Q3 2025 closed the remaining gaps. The
public surface is small and has been stable since hardening completed.

---

### 2. Data Ownership: **High**

**Evidence**:
- Payments owns 12 tables; no other module writes to any of them
- One read dependency remained (from reporting) and was routed
  through the facade in February 2026
- No cross-module foreign keys remain
- Confirmed that the data could move to a separate database cluster
  without breaking any other module

**Notes**: Data separation was the main focus of hardening. The work
is complete.

---

### 3. Contract Stability: **High**

**Evidence**:
- No breaking changes to the public interface in the last 14 months
- All five consumers covered by Pact tests
- Versioning policy is documented and followed
- Two non-breaking additions in the last year, both flagged in the
  changelog

**Notes**: The contract has been genuinely stable.

---

### 4. Team Autonomy: **Medium**

**Evidence**:
- The Payments Team ships its own changes, but deployments are still
  coupled to the monolith's weekly release train
- Emergency patches require release coordination with three other
  teams
- The team's deployment cadence is constrained by the shared pipeline

**Notes**: The boundary problem here is deployment coupling rather
than code coupling. Extraction would address this.

---

### 5. Operational Impact: **High**

**Evidence**:
- One incident attributable to payments in the last quarter (a
  third-party provider outage, not a module issue)
- Latency within SLO consistently (p99 < 400ms)
- On-call load from payments is modest relative to other modules

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

The expansion into the new geography requires payments to meet
regulatory requirements (specifically PCI-DSS level 1 and local
payment data residency rules) that are easier to satisfy with
infrastructure separation. This driver on its own would be enough
to justify extraction even if the signals were weaker. On top of
that, the payments team's deployment cadence is genuinely
constrained by the monolith's release train, and the team would
ship more quickly with independent deployment.

---

## Outcome

**Recommended outcome**: Extract to a service

**Reasoning**:

Code Isolation, Data Ownership, and Contract Stability are all
High, which is the necessary precondition for extraction. Team
Autonomy is Medium, which is part of what would improve through
extraction rather than a blocker. Operational Impact is healthy,
which means extraction will not inherit operational problems from
the current state.

Two drivers apply: the regulatory requirement from the new
geography, and the deployment coupling that constrains team
velocity. The regulatory driver alone is sufficient; the team
autonomy benefit is additional.

The component is genuinely ready. This is what the model
recommends extraction for.

---

## Implementation plan

**Next actions**:

1. ADR to record the extraction decision and its drivers
2. Set up independent deployment pipeline for payments service
3. Migrate the existing module to the service behind a feature flag
4. Route consumers through the network path one at a time
5. Decommission the in-process module
6. Confirm regulatory compliance independently

**Estimated effort**: One quarter of focused work by the Payments
Team, with platform support for the deployment pipeline.

**Dependencies**: Platform team for deployment infrastructure;
Security team for compliance sign-off.

**Target completion**: End of Q3 2026.

---

## Reassessment trigger

**Scheduled reassessment date**: Q1 2027 (six months after target
completion)

**Events that would trigger earlier reassessment**:
- Extraction does not deliver the regulatory or autonomy benefits
- Operational cost of the separate service exceeds expectations
- Significant change in consumer patterns during the migration

---

## Sign-off

**Assessor A**: A. Engineer

**Assessor B**: B. Architect

**Decision recorded as ADR**: ADR-0042 (Payments extraction)
