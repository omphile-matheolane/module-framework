# Evolution Workflow Playbook

This playbook describes the end-to-end workflow for running an
evolution assessment and implementing its outcome. The decision
model itself is documented in the
[`decision-model/`](../../decision-model/) folder; this playbook
is about how to run the assessment process as a team activity
and what happens after a decision is made.

An assessment that stops at "we have decided to extract" is not
complete. What follows the decision is usually more expensive than
the decision itself, and the workflow needs to extend beyond the
moment of choice.

---

## When to run an assessment

Assessments should be triggered by one of:

- **Scheduled cadence**: quarterly for actively evolving
  components, annually for stable ones
- **Significant incidents**: where the incident reveals
  architectural concerns worth reassessing
- **Team reorganisation**: where ownership changes or team
  structure shifts in ways that affect the component
- **Substantial new requirements**: where the component's
  purpose or scope is changing materially
- **Proposed changes**: when someone is already proposing to
  extract, consolidate, or otherwise evolve a component, the
  assessment should happen before the decision rather than after

Assessments should not be triggered by:

- Industry trends or architectural fashion
- Discomfort with the current state that is not backed by
  specific evidence
- A desire to ratify a decision that has already been made
  informally

---

## Step 1: Scoping

- Identify the component under assessment. Assessments cover one
  component at a time. Attempting to assess multiple components
  at once tends to blur the analysis.
- Confirm the component is well-defined enough to assess. If the
  "component" is actually a loose collection of code with
  unclear boundaries, the assessment is premature.
  [Map](../../disciplines/01-map/) first.
- Identify the two assessors. Paired assessment is required.
  Different contexts produce better calibration.
- Schedule the assessment with enough time for both assessors to
  prepare independently.

---

## Step 2: Independent preparation

Each assessor independently prepares:

- Pulls the relevant evidence:
  - Architecture test results and suppression list
  - Database schema and access patterns
  - API changelog and contract test coverage
  - Deployment history for the last quarter
  - Incident records for the last quarter
  - Observability data (latency, error rates, traces)
- Rates each signal using the
  [signals reference](../../decision-model/signals.md)
- Records evidence supporting each rating
- Considers which drivers apply and which do not

Crucially, the assessors do not compare ratings before the joint
session. The independent ratings are the input to the
reconciliation.

---

## Step 3: Joint reconciliation

The assessors meet to reconcile their independent ratings.

- Compare ratings signal by signal
- For each signal where the ratings differ, discuss the
  underlying evidence rather than negotiating or averaging
- Resolve disagreements through evidence. If the evidence is
  insufficient to settle the disagreement, the rating defaults
  to the lower level and the missing evidence is recorded as a
  follow-up
- Record the final ratings in the
  [scoring worksheet](../../decision-model/scoring-worksheet.md)
- Review the drivers. Each driver that applies must be backed by
  specific evidence
- Identify the outcome that follows from the signals and drivers
- Consult the [relevant outcome file](../../decision-model/outcomes/)
  to confirm the conditions actually apply

The reconciliation is where the real calibration happens. Rush
this step and the assessment loses most of its value.

---

## Step 4: Record the decision

- Write an ADR capturing the decision. Use the
  [ADR template](../templates/adr-template.md).
- The ADR should reference the completed scoring worksheet and
  capture:
  - The component assessed
  - The signal ratings and their evidence
  - The drivers that apply
  - The outcome recommended
  - The reasoning linking signals, drivers, and outcome
  - Alternative outcomes considered and why they were rejected
  - Rollback criteria (under what conditions would this decision
    be revisited)
- Link the ADR from the domain map and the ownership registry
  where relevant

---

## Step 5: Plan the implementation

An assessment without an implementation plan is a document, not
a decision. The plan turns the outcome into concrete work.

- Break the outcome into specific next actions
- Assign owners to each action
- Estimate effort and identify dependencies
- Set a target completion date
- Identify the observable signals that will indicate whether the
  implementation is succeeding

For outcomes that take a quarter or more, set interim milestones.
The plan should be concrete enough that someone could pick up the
ADR in six months and know exactly what state the implementation
is in.

---

## Step 6: Implement

The specifics of implementation depend on the outcome. The outcome
files provide detailed guidance:

- [Extract](../../decision-model/outcomes/extract.md)
- [Consolidate](../../decision-model/outcomes/consolidate.md)
- [Modularise](../../decision-model/outcomes/modularise.md)
- [Harden](../../decision-model/outcomes/harden.md)
- [Stabilise](../../decision-model/outcomes/stabilise.md)

Each has its own sequence of work and its own common pitfalls.

Throughout implementation:

- Track progress against the plan
- Update the ADR if material aspects of the plan change
- Communicate with affected teams
- Capture any new evidence that might affect the original
  assessment

If the implementation reveals that the original assessment was
wrong, pause and reassess rather than continuing to execute a
plan that no longer fits.

---

## Step 7: Verify

After implementation, verify that the intended outcomes actually
materialised.

For extraction: did the drivers that justified extraction pay
off? Independent scaling, team autonomy, technology divergence,
regulatory separation. If the answer is no, consider whether
consolidation is the honest response.

For consolidation: did the operational savings materialise? Was
the integration clean? If the answer is no, understand why before
moving on.

For modularisation: did the friction that motivated
modularisation decrease? Are boundary violations staying rare?

For hardening: did the signals actually move to the intended
levels? Is the suppression list shorter?

For stabilise: did any new signals appear that would change the
decision?

Verification closes the loop. Without it, the assessment is just
an exercise.

---

## Step 8: Schedule reassessment

Every outcome has a reassessment trigger. Record it explicitly.

- Scheduled reassessment date (typically quarterly or annually
  depending on the component's activity level)
- Events that would trigger earlier reassessment (major
  incidents, team reorganisation, new requirements)

The reassessment trigger should be actively managed, not filed
and forgotten. Someone needs to remember to run the next
assessment when it is due.

---

## Patterns across the workflow

### Evidence over impression

Every step involves specific evidence: observability data,
architecture test results, incident counts, deployment records.
Evidence makes the assessment auditable and makes disagreements
resolvable. Steps that rely on impression tend to drift toward
the assessor's prior beliefs.

### Action over analysis

An assessment that produces no action is effort that produced no
value. Track implementation of outcomes, not just completion of
assessments. If most assessments are not being acted on, the
problem is in the action, not the assessment.

### Honest over flattering

The assessments that add most value are the ones that capture the
situation honestly, including the weaknesses. Assessments that
make everything look cleaner than it is conceal the problems the
framework is meant to address.

### Progressive over perfect

The first few assessments a team runs will be imperfect. The
ratings will be inconsistent, the drivers may be over- or
under-weighted, the implementation plans may be too vague. This
is fine. The discipline improves with practice, and the early
assessments are valuable as learning even when they are not
optimal.

---

## Common failure modes

### Retrofitted justification

A decision is made first, and the assessment is run to support
it. This tends to produce ratings that conveniently match the
desired outcome. Paired assessment and evidence citation are the
main safeguards, but the deeper remedy is organisational: the
framework informs decisions rather than ratifies them.

### Analysis paralysis

Assessments run continuously without ever producing action.
Components sit in assessed-but-not-evolved states for quarters
at a time. The remedy is to put time boundaries around
assessments and to track implementation, not just assessment
completion.

### Assessment without verification

The assessment is run, the decision is made, the implementation
happens, and then nothing is verified. If the outcome did not
match expectations, the team never learns. Schedule the
post-implementation verification as part of the initial plan.

### Ignoring "stabilise"

Stabilise is often the right answer, but teams that have
embraced MODULE sometimes resist it because it feels like not
doing anything. Stabilise is a legitimate outcome. A component
that is serving its purpose, not creating friction, and not
consuming attention does not need to be reshaped.
