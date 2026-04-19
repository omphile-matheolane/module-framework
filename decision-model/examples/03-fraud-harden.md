# Example 3: Fraud Detection — Harden

A fraud detection module inside a modular monolith at a financial
services company. The module was modularised two years ago and has
mostly held its shape. It has a defined public surface, owns most
of its data, and is owned by a single team. A few rough edges
remain: a handful of internal classes are still accessed from
outside, one table is still read directly by another module, and
the public interface has changed in a breaking way twice in the
past year.

---

## Component under assessment

**Component name**: Fraud Detection

**Current deployment model**: Module inside a modular monolith

**Owning team**: Risk Team (4 engineers)

**Assessors**: E. Principal, F. Senior

**Date of assessment**: 2026-03-02

**Previous assessment**: 2025-03-18 (recommended modularise;
modularisation completed Q3 2025)

---

## Signal assessments

### 1. Code Isolation: **Medium**

**Evidence**:
- ArchUnit tests exist and mostly pass
- Current suppression list has six entries
- Four of the six are classes in `fraud.scoring.internal` being
  accessed by the reporting module for monthly reports
- Two are older violations that were suppressed during the
  modularisation and have not been addressed since
- Dependency graph is mostly one-directional

**Notes**: Close to High, but the suppressions have not been paid
down as intended.

---

### 2. Data Ownership: **Medium**

**Evidence**:
- Fraud owns eight tables; no other module writes to them
- The `risk_events` table is still read directly by the reporting
  module for regulatory reporting
- No cross-module foreign keys remain
- Physical separation would be possible except for the reporting
  read dependency

**Notes**: One remaining read dependency. Reporting could be
routed through the public surface to remove it.

---

### 3. Contract Stability: **Medium**

**Evidence**:
- Two breaking changes in the last year, both handled reactively
  (consumers fixed after the fact rather than ahead of deprecation)
- Versioning policy is documented but not consistently followed
- Three of five consumers have Pact tests; two do not

**Notes**: The versioning work has been inconsistent. Closing the
gaps is straightforward but has not been prioritised.

---

### 4. Team Autonomy: **High**

**Evidence**:
- The Risk Team deploys its changes through the monolith's standard
  pipeline without cross-team coordination for standard work
- The most recent cross-team coordination was four months ago, for
  a schema change that affected reporting

**Notes**: Deployment coupling that would typically hold this back
has not been a practical issue for this module.

---

### 5. Operational Impact: **High**

**Evidence**:
- Zero incidents in the last two quarters
- Latency within SLO consistently
- On-call burden from fraud is minimal

**Notes**: Operationally healthy.

---

## Summary

| Signal | Rating |
|--------|--------|
| Code Isolation | Medium |
| Data Ownership | Medium |
| Contract Stability | Medium |
| Team Autonomy | High |
| Operational Impact | High |

---

## Driver assessment

- [ ] Independent scaling requirements
- [ ] Technology divergence
- [ ] Team autonomy needs (already High)
- [ ] Regulatory requirements for separation
- [ ] Cost reduction
- [ ] Operational burden
- [x] Frequent change concentrated in this component (regulatory
  scope continues to evolve)
- [ ] Multiple teams working on this component regularly

**Driver summary**:

The fraud detection module sees continued change because the
regulatory landscape keeps shifting and new fraud patterns keep
emerging. This does not on its own justify extraction, but it does
mean the module's boundaries need to be robust. The current
weaknesses (suppressions, reporting read dependency, inconsistent
versioning) create ongoing low-grade friction that will compound
as the rate of change continues.

---

## Outcome

**Recommended outcome**: Harden this module

**Reasoning**:

Extraction is not justified. There is no scaling, technology,
autonomy, or regulatory driver that would require independent
deployment. Team Autonomy is already High, so extraction would not
add value there.

Consolidation does not apply (the module is not over-extracted).

Modularisation is not the right frame because the module already
exists as a module. The gaps are specific, not structural.

Stabilisation would accept the current rough edges, but those
edges are creating ongoing low-grade friction, and the rate of
change in this area means the friction will continue. Hardening
closes the specific gaps without changing the deployment model,
and positions the module well for continued evolution.

The specific hardening work is:

1. Remove the four suppressions caused by reporting's direct access
   to internal fraud scoring classes (route through the public
   surface)
2. Route the remaining `risk_events` read through the public
   surface
3. Bring the remaining two consumers under Pact test coverage
4. Apply the versioning policy consistently to any future changes

---

## Implementation plan

**Next actions**:

1. ADR to record the hardening decision and list the specific gaps
2. Extend the public `FraudService` surface to expose the reports
   and events that reporting currently reads directly
3. Migrate reporting to the extended surface
4. Remove the suppressions and tighten the ArchUnit rules
5. Add Pact tests for the two uncovered consumers
6. Apply the versioning policy to the next planned interface change

**Estimated effort**: Six weeks of focused work by the Risk Team.

**Dependencies**: Reporting team for the migration to the public
surface.

**Target completion**: End of Q2 2026.

---

## Reassessment trigger

**Scheduled reassessment date**: Q1 2027

**Events that would trigger earlier reassessment**:
- Regulatory change that materially alters the module's scope
- New driver for extraction (scaling, autonomy, regulatory
  separation)

---

## Sign-off

**Assessor A**: E. Principal

**Assessor B**: F. Senior

**Decision recorded as ADR**: ADR-0051 (Fraud Detection hardening)
