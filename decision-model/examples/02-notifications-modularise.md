# Example 2: Notifications — Modularise

A notifications component inside a monolith at a growing SaaS
company. The component has accumulated over four years. What
started as an email-sending utility now handles email, SMS, push
notifications, in-app messages, and Slack integrations. No clear
boundary separates notifications code from the rest of the
application, and three teams routinely touch it.

---

## Component under assessment

**Component name**: Notifications

**Current deployment model**: Code inside a monolith, no clear
module boundary

**Owning team**: Platform Team (nominal ownership; in practice
everyone touches it)

**Assessors**: C. Lead, D. SRE

**Date of assessment**: 2026-02-20

**Previous assessment**: None

---

## Signal assessments

### 1. Code Isolation: **Low**

**Evidence**:
- No ArchUnit tests exist for notifications
- `NotificationSender` is imported directly from 47 classes across
  the codebase, many of them reaching into internal helpers
- No defined public surface; the "notifications package" is a
  collection of classes with no consistent access pattern
- Dependency graph shows bidirectional edges with at least five
  other components (user management, billing, ordering, support,
  admin)

**Notes**: This is not yet a module in any meaningful sense. It is
a grab-bag of functionality that has grown organically.

---

### 2. Data Ownership: **Low**

**Evidence**:
- The `notification_events` table is written to by at least three
  different components directly (outside any notifications code path)
- The `notification_preferences` table is shared with user
  management; both modules read and write it
- Foreign keys link notification records to user, account, and
  order tables bidirectionally

**Notes**: Data ownership is effectively shared. Resolving this is
a significant prerequisite to any future evolution.

---

### 3. Contract Stability: **Low**

**Evidence**:
- No versioning policy exists
- The `NotificationSender.send()` signature has changed four times
  in the past six months
- Each change required follow-up fixes in other components
- No consumer-driven tests exist for the notifications interfaces

**Notes**: There is no real contract; there are implementations
that other components are reaching into directly.

---

### 4. Team Autonomy: **Low**

**Evidence**:
- The Platform Team owns notifications in theory
- In practice, the Ordering Team, Billing Team, and Support Team
  all push changes to notification code
- No review process requires Platform Team approval on those
  changes
- Deployments involving notification changes routinely require
  coordination across multiple teams

**Notes**: Ownership is nominal, not real. Multiple teams share
the component without clear decision authority.

---

### 5. Operational Impact: **Medium**

**Evidence**:
- Two incidents attributable to notifications in the last quarter
  (one SMS provider outage, one bug where a refactoring changed
  delivery order)
- Latency from notification sends is not currently a system-level
  concern
- On-call burden from notifications is moderate

**Notes**: The component is not the worst contributor to
operational pain, but it is also not quiet. The bug incident
originated from the multi-team ownership pattern.

---

## Summary

| Signal | Rating |
|--------|--------|
| Code Isolation | Low |
| Data Ownership | Low |
| Contract Stability | Low |
| Team Autonomy | Low |
| Operational Impact | Medium |

---

## Driver assessment

- [ ] Independent scaling requirements
- [ ] Technology divergence
- [x] Team autonomy needs that the current structure constrains
- [ ] Regulatory or compliance requirements
- [ ] Cost reduction achievable through consolidation
- [ ] Operational burden disproportionate to business value
- [x] Frequent change concentrated in this component
- [x] Multiple teams working on this component regularly

**Driver summary**:

The component is being touched by multiple teams without clear
ownership, which is producing coordination overhead and the
occasional operational incident. Changes to notification behaviour
happen frequently, and the lack of a contract means each change
risks breaking consumers in ways that only surface in production.

---

## Outcome

**Recommended outcome**: Modularise inside the monolith

**Reasoning**:

Extraction is not viable yet. Data Ownership is Low, which is the
hardest blocker, and Code Isolation and Contract Stability are
also Low. Extracting in this state would convert a monolithic
coupling into a distributed one and make the existing problems
considerably worse.

The drivers do point toward evolution: frequent change, multiple
teams, and real coordination pain. But the right evolution here is
to create a genuine module, not to carve out a service. Once the
module is established, with clear ownership, a defined public
surface, owned data, and an explicit contract, the component can
be reassessed. It may turn out that modularisation is sufficient
and no further evolution is needed. Or extraction may become
viable in a year or two.

The work order matters. Attempting modularisation and extraction
at once would be expensive and risky. Modularise first, then
revisit.

---

## Implementation plan

**Next actions**:

1. ADR to record the modularisation decision and define the
   intended module
2. Assign clear ownership to the Platform Team with explicit
   decision rights
3. Define the public surface (proposed: `NotificationService`
   facade plus a small number of domain events)
4. Write architecture tests that enforce the new boundary, with
   suppressions for existing violations
5. Redirect external writes to shared tables through the facade
6. Document the contract and add consumer-driven tests for the
   top consumers
7. Pay down the suppression list over subsequent quarters

**Estimated effort**: Initial modularisation in one quarter;
continued suppression paydown over two or three quarters.

**Dependencies**: Alignment with the Ordering, Billing, and
Support teams on the new access pattern.

**Target completion**: Initial boundary by end of Q2 2026;
full modularisation by end of Q4 2026.

---

## Reassessment trigger

**Scheduled reassessment date**: Q1 2027

**Events that would trigger earlier reassessment**:
- Modularisation stalls (suppression list not shrinking)
- New driver appears for extraction (regulatory, scaling)
- Significant incident traceable to the notifications boundary

---

## Sign-off

**Assessor A**: C. Lead

**Assessor B**: D. SRE

**Decision recorded as ADR**: ADR-0038 (Notifications modularisation)
