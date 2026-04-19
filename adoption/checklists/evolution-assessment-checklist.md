# Evolution Assessment Checklist

Use this checklist when running an evolution decision model
assessment. It is a companion to the
[scoring worksheet](../../decision-model/scoring-worksheet.md),
which captures the outputs; this checklist captures the process.

An assessment that skips the process tends to produce ratings
that reflect the assessor's prior beliefs rather than the state
of the component. The checklist is a safeguard against that.

---

## Before the assessment

- [ ] The component under assessment is clearly identified
- [ ] Two assessors are named (paired assessment is required)
- [ ] Evidence sources are accessible:
  - [ ] Architecture test results and suppression list
  - [ ] Database schema and access patterns
  - [ ] API changelog and contract tests
  - [ ] Deployment history for the last quarter
  - [ ] Incident records for the last quarter
  - [ ] Observability data (latency, error rates, traces)
- [ ] The [scoring worksheet](../../decision-model/scoring-worksheet.md)
  has been copied to the team's chosen location
- [ ] The [signals reference](../../decision-model/signals.md) is
  at hand for calibration

---

## During the assessment: independent ratings

- [ ] Each assessor rates each of the five signals independently
  without discussing ratings first
- [ ] Each rating is supported by specific, verifiable evidence
- [ ] Evidence is cited with enough precision that a third party
  could verify it
- [ ] Ratings rely on data from the last quarter where available,
  not impressions from earlier periods

---

## During the assessment: reconciliation

- [ ] The two assessors compare their ratings signal by signal
- [ ] Disagreements are resolved by discussing the underlying
  evidence, not by averaging or deferring to seniority
- [ ] Where evidence is insufficient to settle a disagreement, the
  rating defaults to the lower level and the missing evidence is
  recorded for follow-up
- [ ] The final ratings are recorded in the worksheet

---

## During the assessment: drivers

- [ ] Each potential driver is considered explicitly
- [ ] Drivers that apply are supported by specific evidence, not
  by general sentiment
- [ ] Drivers that do not apply are noted as such (rather than
  omitted), so the reasoning is visible
- [ ] The aggregate driver picture is summarised clearly

---

## During the assessment: outcome

- [ ] The outcome follows from the combination of signals and
  drivers, not from preference
- [ ] The [outcome file](../../decision-model/outcomes/) for the
  chosen outcome has been consulted to confirm the conditions
  actually apply
- [ ] If the outcome is Extract, Data Ownership is High
- [ ] If the outcome is Extract, Contract Stability is High
- [ ] If the outcome is Extract, at least one genuine driver is
  recorded (not "because microservices")
- [ ] If the outcome is Consolidate, the absorbing component is
  identified
- [ ] If the outcome is Stabilise, a reassessment date is set

---

## After the assessment

- [ ] The decision is recorded as an ADR
- [ ] The ADR links to the completed worksheet
- [ ] The implementation plan is broken into concrete next actions
  with owners
- [ ] A reassessment trigger is set (scheduled date plus events)
- [ ] The domain map is updated if the outcome implies a map
  change
- [ ] The ownership registry is updated if ownership changes
- [ ] The relevant teams are informed

---

## Red flags

Treat any of these as reasons to pause and reconsider rather than
to push through:

- Both assessors agree on every rating without any discussion
  (probably means the evidence was not examined closely enough)
- The outcome was already decided before the assessment started
- Ratings are not supported by specific evidence
- Data Ownership is Low but Extract is being recommended anyway
- No driver applies but evolution is being recommended anyway
- The implementation plan is vague ("we will extract the payments
  service" is not an implementation plan)
- No reassessment trigger is set

---

## Paired assessment: getting it right

The paired assessment is the key safeguard against motivated
reasoning, and it is often the most skipped part of the process.
A few things help.

Use assessors from different contexts when possible. One from the
owning team, one from outside. The outside perspective catches
blind spots.

Require both assessors to fill in ratings before any discussion.
Shared rating sessions tend to produce converged ratings that do
not reflect genuine independent judgement.

Treat disagreement as useful signal rather than something to
resolve quickly. If two experienced assessors reach different
ratings from the same evidence, the disagreement itself is worth
understanding.

Record the reconciliation, not just the final rating. Future
readers benefit from knowing why the rating landed where it did.
