# Incident Architectural Review Checklist

Use this checklist after an incident that has architectural
implications. Not every incident needs an architectural review;
the ones that do are those where the failure crossed module
boundaries, where unclear ownership slowed the response, or
where the incident revealed a gap between the documented
architecture and the actual runtime behaviour.

This is not a replacement for the normal incident postmortem
process. It is a supplementary lens that asks specifically
architectural questions.

---

## Before the review

- [ ] The incident's primary postmortem is complete
- [ ] The timeline is documented
- [ ] The modules involved in the incident are identified
- [ ] Relevant observability data (traces, logs, metrics from the
  incident window) is accessible
- [ ] The domain map and ownership registry are at hand for
  reference

---

## Boundaries

- [ ] Did the incident cross module boundaries?
- [ ] If yes, did the failure propagate in ways the boundaries
  were meant to prevent?
- [ ] Did any module experience a failure because of a dependency
  it did not know it had?
- [ ] Did runtime traces show dependencies that do not appear on
  the domain map?

---

## Ownership

- [ ] Was it clear which team owned each component involved in
  the incident?
- [ ] Did the response involve coordination across teams that
  slowed the resolution?
- [ ] Did the ownership registry match who actually responded?
- [ ] Were there gaps in ownership that the incident exposed?

---

## Contracts

- [ ] Did any contract violation contribute to the incident?
- [ ] If a module produced unexpected output, was the contract
  explicit enough to have caught it?
- [ ] Did a contract change since the last deployment contribute
  to the incident?
- [ ] Were consumer-driven tests in place for the affected
  interfaces?

---

## Data

- [ ] Did shared data or cross-module database access contribute
  to the incident?
- [ ] Did a schema change in one module affect another
  unexpectedly?
- [ ] Were the data ownership boundaries actually respected during
  the incident response?

---

## Observability

- [ ] Did the observability tooling provide the signal needed to
  diagnose the incident quickly?
- [ ] Were traces complete enough to follow the request across
  modules?
- [ ] Did module identifiers on logs make filtering practical?
- [ ] Were there gaps in instrumentation that delayed the
  response?

---

## Governance

- [ ] Would any existing architecture test have caught the
  underlying cause if it had been in place?
- [ ] Were there known suppressions in architecture tests that
  turned out to matter?
- [ ] Were there architectural rules that should have existed but
  did not?

---

## Evolution implications

- [ ] Does the incident change the signal assessment for any
  involved component?
- [ ] Does it surface a need to reassess a component earlier than
  scheduled?
- [ ] Does it suggest a specific outcome (harden, modularise,
  extract, consolidate) that was not previously on the table?

---

## Findings and actions

For each finding from the review above, record:

| Finding | Module or boundary | Action | Owner | Target date |
|---------|--------------------|--------|-------|-------------|
| | | | | |

Actions should be specific and have owners. Vague improvements
tend not to happen. Specific ones ("add ArchUnit rule preventing
external reads of the notifications events table by end of Q3")
tend to.

---

## Escalation

- [ ] Do any findings require an ADR to record the architectural
  implications?
- [ ] Does the domain map need to be updated based on what the
  incident revealed?
- [ ] Does the ownership registry need to be updated?
- [ ] Do any of the regular reviews (map review, evolution
  assessment) need to be scheduled earlier than planned?

---

## Red flags

If any of these apply, the incident probably warrants a broader
conversation beyond this review:

- The same module has been involved in multiple architectural
  incidents in recent months
- Runtime dependencies do not match the documented architecture
- Ownership confusion delayed the response by hours
- The incident was caused by a contract change that nobody
  noticed was breaking
- Existing architecture tests were suppressed for the exact
  violation that caused the incident
