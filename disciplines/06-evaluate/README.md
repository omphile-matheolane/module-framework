# 6. Evaluate Evolution

## What this discipline involves

The Evaluate discipline is concerned with making structured decisions
about how architecture should evolve over time. Rather than following
industry trends or reacting to whatever pain is most acute at a given
moment, teams examine observable signals to determine whether a
component needs to change and, if so, in what direction.

Evolution can take many forms. A component might be extracted into an
independent service. It might be consolidated with another component.
It might be modularised to introduce better internal structure. It
might be hardened to strengthen its existing boundaries. Or it might
be left as it is, because the evidence suggests no change is actually
warranted. The Evaluate discipline provides a framework for deciding
which of these trajectories makes sense for a given component at a
given point in time.

The operational detail of the decision model lives in
[`decision-model/`](../../decision-model/). This guide covers the
discipline as a practice: when to assess, how often, who is involved,
and what to do with the results.

---

## Why it matters

Architectural evolution is frequently driven by instinct, fashion, or
the preferences of whoever argues most persuasively in a given
meeting. Teams extract services because microservices are the
prevailing trend, not because extraction addresses a real problem.
Teams resist consolidation because it feels like admitting something
went wrong, not because maintaining the separation is genuinely
justified. Neither of these is a reliable basis for decisions that
will shape the system for years.

Structured evaluation replaces instinct with evidence. It gives teams
a shared vocabulary for discussing evolution decisions and a
consistent framework for making them. It reduces the influence of
individual preference and current fashion. And it ensures that
whatever direction is chosen, the decision is grounded in the
observable characteristics of the actual system rather than in
assumptions about what good architecture looks like in the abstract.

---

## The five signals

The Evaluate discipline centres on an assessment model that examines
five signals. Detailed calibration for each signal is in the
[decision model reference](../../decision-model/).

1. **Code Isolation** asks whether the component has clear boundaries,
   well-defined and minimal dependencies, and a stable coherent public
   interface.
2. **Data Ownership** asks whether the component owns its data, or
   whether it shares databases, tables, or schemas with other
   components.
3. **Contract Stability** looks at how frequently the public interface
   changes and whether those changes tend to break consumers.
4. **Team Autonomy** considers whether a single team can develop,
   test, and deploy the component independently.
5. **Operational Impact** examines how the component contributes to
   incidents, latency, and general operational burden.

Each signal is assessed as Low, Medium, or High. Those levels are
inputs to judgement rather than inputs to a formula, which preserves
the discretion that architectural decisions genuinely require.

---

## The five outcomes

Based on the signal assessment, one of five outcomes is recommended.
Each is documented in detail in [`decision-model/outcomes/`](../../decision-model/outcomes/).

1. **Extract to a service**: move the component from a monolith or
   modular monolith to an independent service.
2. **Consolidate**: merge an existing independent service back into a
   modular structure.
3. **Modularise inside the monolith**: introduce better internal
   structure within the existing monolith boundary.
4. **Harden this module**: strengthen the boundaries and contracts of
   a component that is already a module but has not yet reached High
   across the key isolation signals.
5. **Stabilise as-is**: maintain the current state.

The signals and outcomes are connected but not mechanical. The
interpretation of a signal pattern requires judgement informed by
context: the current deployment architecture, the drivers that would
motivate evolution, and the broader priorities of the organisation.

---

## How to practice it

### Schedule assessments deliberately

Reassessment should be scheduled rather than left to occur whenever
someone remembers to do it. A reasonable cadence is quarterly review
for components that are actively evolving, and annual review for
components that are stable. Reassessment should also be triggered by
significant events: major incidents, team reorganisations, or
substantial new requirements that change the context the original
assessment was made in.

### Assess in pairs

Having two people assess the same component independently and then
reconcile differences surfaces disagreement early and forces explicit
discussion of the evidence rather than leaving interpretation
implicit. The reconciliation conversation is where the real
calibration happens, and the resulting assessment tends to be more
defensible than either individual one.

### Cite evidence for every rating

Every rating should be supported by specific, verifiable facts:
architecture test results, incident counts over a named period,
deployment frequencies, consumer counts. Ratings without evidence
tend to reflect the assessor's mood or prior beliefs rather than the
state of the component. Requiring evidence makes the rating auditable
and makes disagreements easier to resolve.

The [scoring worksheet](../../decision-model/scoring-worksheet.md)
captures this structure: for each signal, a rating and the evidence
supporting it.

### Track outcomes to implementation

An assessment that produces a decision and then sits on a shelf is
worse than no assessment, because it consumes effort without producing
value. Track which assessments have been run, what outcomes they
recommended, and whether those outcomes have actually been
implemented. If most outcomes are not being acted on, the problem is
in the action, not in the assessment.

### Recalibrate across teams periodically

When multiple teams are applying the model, their interpretations
will drift apart over time. A quarterly session where teams compare
their ratings against a shared component library, or against each
other's components, surfaces calibration drift and corrects it before
the model loses its consistency.

---

## When the model does not apply

The evolution decision model is a tool for structured thinking, not a
universal rule. There are situations where it provides less guidance
than usual.

Strategic imperatives sometimes override technical signals. A
component may need to be extracted for regulatory reasons regardless
of its data ownership score. A component may need to be consolidated
for cost reasons regardless of how well-isolated it is. The model
informs these decisions but does not make them.

In domains that are not yet well understood, the signals themselves
may be unstable because the domain is still being discovered.
Applying the model rigorously to something that is changing shape
every few weeks can produce misleading results. Investing in
understanding the domain is the more productive use of time.

During incidents or periods of acute business pressure, the careful
assessment the model requires may simply not be feasible. The model
is designed for deliberate evolution, not emergency response.

---

## Failure modes and anti-patterns

### Assessment without implementation

Teams can spend considerable effort on assessments that never produce
action. Components sit in assessed-but-not-evolved states for quarters
at a time while the team continues to refine its analysis. The
remedy is to put time boundaries around assessments and to track
implementation rather than just assessment completion.

### Overweighting a single signal

Teams sometimes fixate on one signal at the expense of the others. A
component with strong code isolation gets tagged for extraction even
though its data ownership is Low. The five signals exist because each
captures something the others miss, and assessments that rely heavily
on one signal tend to miss constraints that the others would have
surfaced.

### Treating signals as a formula

The levels are inputs to judgement, not inputs to a formula. A
component scoring High on three signals and Low on two is not
three-fifths of the way to extraction. The pattern matters, and some
signals (particularly Data Ownership) act more as gates than as
inputs. Assessments that try to sum or average the signals tend to
miss this.

### Retrofit justification

The opposite of structured assessment is a decision made first and
justified afterward. Teams that have already decided what they want
to do will find ways to rate the signals that support the decision.
Paired assessment and evidence citation both help guard against this,
but the deeper remedy is organisational: the framework needs to be
used to inform decisions, not to ratify them.

---

## Connections to other disciplines

Evaluate draws on inputs from all of the other disciplines.

[Map](../01-map/) provides the understanding of domain boundaries.
Without clear modules, there is nothing to assess.

[Organise](../02-organise/) provides the structural foundation.
Code isolation cannot be assessed if there is no defined public
surface.

[Design](../03-design/) provides awareness of contract health.
Contract stability draws directly on the versioning and testing
practices in Design.

[Unify](../04-unify/) provides the governance infrastructure and the
record of architectural decisions. ADRs are the institutional memory
that contextualises new assessments.

[Leverage](../05-leverage/) provides the observability data that
grounds the signal assessment in what is actually happening at
runtime. Operational Impact, in particular, can only be assessed with
observability data.

Evaluate brings these inputs together into decisions about evolution
direction, which in turn create new inputs for all the other
disciplines to work with.

---

## Further reading

For the specific mechanics of service extraction and consolidation,
Sam Newman's *Monolith to Microservices* is the most thorough
practical reference. MODULE's evolution decision model assumes
familiarity with the patterns Newman covers.

Chris Richardson's *Microservices Patterns* covers the broader
catalogue of patterns relevant to evolution decisions, including the
data management patterns that often dominate the Data Ownership
signal.

For the evaluation practice itself, *Infrastructure as Code* by Kief
Morris (particularly the later editions) has useful material on
evidence-based decision-making for infrastructure changes that
translates well to architectural evolution more broadly.

The full decision model reference, with scoring worksheets and worked
examples, is in [`decision-model/`](../../decision-model/).
