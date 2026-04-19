# Example 5: Reporting — Stabilise

A reporting module inside a modular monolith at a B2B software
company. The module was built five years ago, was modularised
three years ago, and has received minimal structural change since.
It produces scheduled reports and ad-hoc exports for a stable set
of customers. Nobody has complained about it in over a year.

---

## Component under assessment

**Component name**: Reporting

**Current deployment model**: Module inside a modular monolith

**Owning team**: Data Team (2 engineers, with the module as one of
several responsibilities)

**Assessors**: I. Manager, J. Architect

**Date of assessment**: 2026-02-10

**Previous assessment**: 2024-11-05 (recommended stabilise;
scheduled reassessment today)

---

## Signal assessments

### 1. Code Isolation: **High**

**Evidence**:
- ArchUnit tests pass with zero suppressions
- Public surface is small: a `ReportingService` facade and a set
  of scheduled job definitions
- No consumer imports anything from `com.example.reporting.internal.*`
- Dependency graph is cleanly one-directional

**Notes**: The boundary has held up well over three years.

---

### 2. Data Ownership: **High**

**Evidence**:
- Reporting has read-only access to operational tables, mediated
  through the public surfaces of the owning modules
- It owns its own cache tables and report archive tables
- No other module writes to reporting-owned tables
- No cross-module foreign keys involve reporting

**Notes**: Clean ownership.

---

### 3. Contract Stability: **High**

**Evidence**:
- No breaking changes in the last 24 months
- Versioning policy in place but rarely exercised because changes
  are rare
- Consumer-driven tests cover the two internal consumers
- External report formats have not changed; customers rely on the
  existing schedule

**Notes**: The contract has been genuinely stable. This is
unsurprising; reporting requirements are slow-changing.

---

### 4. Team Autonomy: **High**

**Evidence**:
- The Data Team ships changes to reporting through the standard
  monolith pipeline without cross-team coordination
- Routine updates happen in the course of normal sprint work
- Cross-team coordination has occurred twice in the last year,
  both for minor schema dependencies

**Notes**: Full autonomy, modest pace.

---

### 5. Operational Impact: **High**

**Evidence**:
- Zero incidents attributable to reporting in the last two
  quarters
- The module runs on the same pipeline as the monolith; its
  latency contribution is negligible
- On-call burden is effectively zero

**Notes**: Operationally quiet.

---

## Summary

| Signal | Rating |
|--------|--------|
| Code Isolation | High |
| Data Ownership | High |
| Contract Stability | High |
| Team Autonomy | High |
| Operational Impact | High |

---

## Driver assessment

- [ ] Independent scaling requirements
- [ ] Technology divergence
- [ ] Team autonomy needs
- [ ] Regulatory requirements
- [ ] Cost reduction
- [ ] Operational burden
- [ ] Frequent change concentrated in this component
- [ ] Multiple teams working on this component regularly

**Driver summary**:

No drivers for evolution are present. The module is serving its
purpose, not creating friction, and not consuming attention that
could be better spent elsewhere.

---

## Outcome

**Recommended outcome**: Stabilise as-is

**Reasoning**:

All five signals are High. None of the evolution drivers apply.
The module has been stable for several years and continues to
serve its purpose well. The right decision is to leave it alone.

Extraction could be argued on the strength of the signals, but
there is no driver that would justify the operational cost. Team
autonomy is already High. Scaling is not a concern. No regulatory
requirement mandates separation. Extracting this module would
convert a quietly well-functioning part of the system into an
independently deployed service that does not need to be
independent.

Hardening is not warranted because the signals are already at the
level hardening is meant to achieve.

Modularisation is not warranted because the module is already
modularised.

Consolidation is not warranted because the module is not extracted.

Stabilisation is the honest answer. The current state is the
correct state.

---

## Implementation plan

**Next actions**:

1. ADR to record the stabilisation decision
2. Confirm that architecture tests, contract tests, and
   observability instrumentation remain in place
3. Schedule the next reassessment

**Estimated effort**: Minimal. The decision itself is the work.

**Dependencies**: None.

**Target completion**: End of Q1 2026 (essentially, now).

---

## Reassessment trigger

**Scheduled reassessment date**: Q1 2027

**Events that would trigger earlier reassessment**:
- Material change in reporting requirements (new customer tier,
  regulatory reporting obligations, significant volume growth)
- Team reorganisation affecting ownership
- Observability data showing latency or error trends that depart
  from the current pattern

---

## Sign-off

**Assessor A**: I. Manager

**Assessor B**: J. Architect

**Decision recorded as ADR**: ADR-0067 (Reporting stabilisation,
confirmed)
