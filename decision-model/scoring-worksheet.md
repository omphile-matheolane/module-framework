# Evolution Assessment Worksheet

Copy this worksheet into wherever you track architectural decisions
and fill it in for the component under assessment. One worksheet
per component. Two people should complete the assessment
independently and then reconcile differences.

Keep evidence concrete. Ratings without evidence are not useful.

---

## Component under assessment

**Component name**:

**Current deployment model** (monolith module / modular monolith /
independent service):

**Owning team**:

**Assessors** (two names recommended):

**Date of assessment**:

**Previous assessment** (if any, link or date):

---

## Signal assessments

For each signal, rate Low, Medium, or High and cite the evidence
supporting that rating. Calibration anchors are in
[`signals.md`](signals.md).

### 1. Code Isolation

**Rating**:

**Evidence**:
- Architecture test status (pass/fail, suppression count):
- Dependency graph observations:
- Any known violations of the public surface:

**Notes**:

---

### 2. Data Ownership

**Rating**:

**Evidence**:
- Tables or schemas owned exclusively:
- Tables or schemas shared with other components:
- Cross-component foreign keys (if any):
- Who reads from the component's tables outside the component itself:

**Notes**:

---

### 3. Contract Stability

**Rating**:

**Evidence**:
- Breaking changes in the last six months:
- Versioning policy (exists / followed):
- Consumer-driven test coverage:
- Recent consumer-reported integration issues:

**Notes**:

---

### 4. Team Autonomy

**Rating**:

**Evidence**:
- Deployment cadence and independence:
- Cross-team coordination frequency for standard work:
- Approvals required outside the owning team:

**Notes**:

---

### 5. Operational Impact

**Rating**:

**Evidence**:
- Incidents attributable to this component in the last quarter:
- Latency contribution (meets / misses targets):
- On-call burden relative to business value:

**Notes**:

---

## Summary

| Signal | Rating |
|--------|--------|
| Code Isolation | |
| Data Ownership | |
| Contract Stability | |
| Team Autonomy | |
| Operational Impact | |

---

## Driver assessment

Signals alone do not produce an outcome. The decision also depends
on whether a genuine driver exists to support evolution. Check any
that apply:

- [ ] Independent scaling requirements that the current structure is working against
- [ ] Technology divergence (different runtime, language, or operational profile justified)
- [ ] Team autonomy needs that the current structure constrains
- [ ] Regulatory or compliance requirements demanding separation
- [ ] Cost reduction achievable through consolidation
- [ ] Operational burden disproportionate to business value
- [ ] Frequent change concentrated in this component
- [ ] Multiple teams working on this component regularly

**Driver summary**:

---

## Outcome

**Recommended outcome** (one of: extract, consolidate, modularise,
harden, stabilise):

**Reasoning**: why this outcome follows from the signal pattern
combined with the drivers above. If Data Ownership is Low, explain
why extraction is or is not being recommended despite that. If
multiple outcomes seem plausible, explain what tipped the choice.

---

## Implementation plan

**Next actions**:
1.
2.
3.

**Estimated effort**:

**Dependencies** (other components or teams):

**Target completion**:

---

## Reassessment trigger

**Scheduled reassessment date**:

**Events that would trigger earlier reassessment**:
- Major incident involving this component
- Team reorganisation affecting ownership
- Significant change in consumer base
- New requirements that materially affect the component's role

---

## Sign-off

**Assessor A**:

**Assessor B**:

**Decision recorded as ADR**: (link to ADR)
