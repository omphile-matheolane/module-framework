# The Evolution Decision Model

This folder contains the operational detail needed to apply MODULE's
evolution decision model in practice. The model itself is introduced
in the whitepaper and summarised in the [Evaluate discipline guide](../disciplines/06-evaluate/).
What lives here is the reference material: the signals and how to
rate them, the outcomes and when each applies, a scoring worksheet
teams can use directly, and worked examples showing the model applied
to realistic situations.

---

## What is in this folder

| File | Purpose |
|------|---------|
| [`signals.md`](signals.md) | The five signals with calibration anchors for Low, Medium, and High |
| [`scoring-worksheet.md`](scoring-worksheet.md) | A blank worksheet for assessing a component |
| [`outcomes/`](outcomes/) | One file per outcome, explaining when it applies and how to implement it |
| [`examples/`](examples/) | Worked examples of the model applied to concrete components |

---

## How the model works

The model examines five observable signals and maps the pattern of
ratings to one of five outcomes. Neither the signals nor the outcomes
are mechanical: the levels are inputs to judgement, and the mapping
requires interpretation of the specific context.

### The five signals

1. **Code Isolation**: clear boundaries, minimal and well-defined
   dependencies, a stable public interface
2. **Data Ownership**: exclusive write access to data, absence of
   shared schemas or cross-component foreign keys
3. **Contract Stability**: low rate of breaking changes, versioning
   policy in place, consumer-driven tests
4. **Team Autonomy**: independent deployment, absence of cross-team
   coordination requirements for routine work
5. **Operational Impact**: incident rate, latency contribution,
   on-call burden

Each signal is rated Low, Medium, or High. The full calibration is
in [`signals.md`](signals.md).

### The five outcomes

1. **Extract to a service**: appropriate when the relevant signals
   are High and a genuine driver exists
2. **Consolidate**: appropriate when an existing service creates
   overhead without sufficient independent value
3. **Modularise inside the monolith**: appropriate when a component
   in a monolith needs better internal structure
4. **Harden this module**: appropriate when a module needs
   strengthening before any other evolution becomes viable
5. **Stabilise as-is**: appropriate when no driver for evolution
   exists

Each outcome has its own file in [`outcomes/`](outcomes/).

---

## How to use the model

The recommended workflow for a single component:

1. Copy [`scoring-worksheet.md`](scoring-worksheet.md) into wherever
   you track architectural decisions (an ADR, a wiki page, a
   spreadsheet).
2. Have two people assess the component independently, citing
   evidence for each signal rating.
3. Reconcile differences through discussion of the evidence rather
   than through averaging or deferring to seniority.
4. Interpret the signal pattern to identify the likely outcome.
5. Confirm the outcome by considering whether a genuine driver exists
   to support it.
6. Record the decision, the evidence, and the rationale in an ADR.
7. Plan the implementation work and track it through to completion.

Assessment alone is not the point. An assessment that produces no
action consumed effort without producing value.

---

## How the signals interact

The five signals are not independent checkboxes to be assessed in
isolation. They interact, and certain signals carry more weight than
others depending on the situation.

Data Ownership is the hardest constraint. A component with excellent
code isolation but shared data is not ready for extraction. Extracting
it would transform a monolithic data coupling into a distributed one,
which tends to be significantly harder to resolve. Data boundaries
need to be addressed before anything else.

Contract Stability is the next gate. An unstable interface amplifies
the cost of extraction because every consumer must adapt to changes,
and if the interface is still actively evolving, that adaptation
becomes a recurring cost rather than a one-time effort.

Code Isolation and Team Autonomy tend to move together in practice.
A team that cannot deploy independently usually has an underlying
boundary problem in the code. Improving code isolation tends to
improve team autonomy over time, and the reverse is also generally
true.

Operational Impact can override the other signals in either direction.
A component that is structurally sound but operationally burdensome
may be a candidate for consolidation. A component that is poorly
isolated but operationally stable may be a candidate for
modularisation rather than extraction. Operational Impact is the
signal that surfaces components which look reasonable on paper but
are proving unsustainable in practice.

---

## When the model does not apply

The evolution decision model is a tool for structured thinking, not
a universal rule. Strategic imperatives sometimes override technical
signals. Regulatory requirements can force extraction regardless of
Data Ownership scores. Cost pressures can force consolidation
regardless of how well-isolated a service is.

In domains that are not yet well understood, the signals themselves
may be unstable because the domain is still being discovered.
Applying the model rigorously to something that is changing shape
every few weeks can produce misleading results.

During incidents or periods of acute business pressure, the careful
assessment the model requires may simply not be feasible. The model
is designed for deliberate evolution, not emergency response.

---

## A note on the worked examples

The examples in [`examples/`](examples/) are illustrative. They show
the model being applied to realistic but fictional situations, with
full signal assessments, evidence, and outcomes. They do not cover
every possible combination of signals, because the number of
combinations is too large to enumerate usefully. They do cover the
patterns that come up most often in practice.

If you have applied the model to a real component and would like to
contribute a case study, see [CONTRIBUTING.md](../CONTRIBUTING.md).
