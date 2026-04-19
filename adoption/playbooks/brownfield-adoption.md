# Brownfield Adoption Playbook

This playbook describes how to adopt MODULE in an existing system.
Brownfield adoption is where the framework most often earns its
keep, because the systems that most need architectural discipline
are usually the ones that have been running long enough to need
it. The challenge is introducing the disciplines without a
disruptive rewrite, and without demanding perfection before value
can be delivered.

The short version: start with discovery, introduce governance
incrementally, pay down debt progressively, and accept that this
is work measured in quarters rather than sprints.

---

## When this playbook applies

- An existing system where MODULE has not been practiced
- A system that has drifted from its original design over time
- An organisation adopting MODULE across existing services and
  modules

For new systems, see the [greenfield adoption playbook](greenfield-adoption.md).

---

## Phase 1: Honest discovery

The first phase is Map as discovery rather than design. The goal
is to document what actually exists, not to propose what should
exist. Nothing in this phase should change the codebase; the
output is a shared understanding of the current state.

### Map what exists

- Identify the modules (or would-be modules) as they actually
  exist in the codebase. This may not match the original intent.
  Document what is there.
- For each module, note the business capability it supports,
  which team is currently responsible for it (which may be
  nobody, or several teams), and what dependencies it has on
  other modules.
- Be honest about boundaries. If a "module" is actually three
  subsystems intermingled, say so. If two "modules" share all
  their data, say so.

### Assess governance

- Are there any architecture tests? If so, what do they cover?
- Are contracts between modules documented anywhere? If so,
  where, and how current are they?
- Are there ADRs? Are they consulted?
- Is there structured observability? Does it carry module
  context?

### Identify the friction

- Where are merge conflicts concentrated?
- Which boundaries are most frequently violated?
- Which contracts break most often?
- Which modules take the most cross-team coordination?
- Where does onboarding take longest?

This friction map is the prioritisation input for the next phase.
The places where pain is most acute are where improvement will
have the most immediate impact.

### Deliverables from Phase 1

- A realistic domain map documenting current state
- An assessment of current governance posture
- A list of high-friction areas with concrete evidence
- An honest summary of the gap between intended and actual
  architecture

---

## Phase 2: Prioritise and commit

The second phase takes the discovery outputs and turns them into
a concrete plan. Not everything can be improved at once.

### Choose a starting scope

Pick one or two modules to focus on initially. The right choice
is usually:

- A module with real friction (so the improvement will be felt)
- A module whose team has capacity to engage (so the work gets
  done)
- A module whose improvement is tractable (not the most
  entangled thing in the system)

Avoid starting with the most painful module. The most painful
module is usually the most difficult to fix, and an early failed
effort is expensive both practically and politically.

### Commit to a pace

Brownfield adoption is work measured in quarters. A realistic
pace is one to three modules brought to acceptable state per
quarter for the initial period, scaling up as the team builds
experience. Attempting to fix everything in a single initiative
tends to produce nothing.

### Get executive alignment

Brownfield adoption involves cost: engineering time that could go
to features, occasional slowdowns while boundaries are clarified,
and explicit tracking of technical debt that had previously been
invisible. This cost needs to be acknowledged and budgeted rather
than absorbed silently.

The argument worth making: the alternative (continuing to
accumulate architectural drift) is more expensive over time, but
the cost is less visible because it shows up as slow delivery
rather than line items. Explicit investment in architectural
discipline converts the invisible cost into visible investment.

### Deliverables from Phase 2

- Target modules selected for the first round of improvement
- Pace and capacity agreed
- Executive alignment on the cost and expected benefits
- An ADR recording the adoption decision and its scope

---

## Phase 3: Introduce governance

Governance is introduced before the code is improved, not after.
Architecture tests for the target modules catch regressions and
make the existing debt explicit.

### Start with the boundary rule

For each target module, add an architecture test that enforces
the rule "nothing outside this module depends on its internals".
Run the test. It will fail, often with a substantial number of
violations.

Do not fix all the violations immediately. Suppress them with a
documented list (a "baseline"), configure the test to fail on
new violations, and let the baseline shrink over time.

This pattern converts hidden debt into visible debt. The
suppression list is the catalogue of what needs to be cleaned up,
and its size is the measure of progress.

### Add cycle detection

Cyclic dependencies between modules are a strong signal of
boundary problems. Add a test that detects cycles. As with the
boundary rule, suppress existing cycles and prevent new ones.

### Establish ownership

Update the ownership registry to record current ownership
honestly. If ownership is shared or unclear for some modules,
document that. Do not pretend ownership is clean when it is not;
the acknowledgement is the first step toward fixing it.

### Deliverables from Phase 3

- Architecture tests in place for target modules, with
  documented suppression lists
- Ownership registry reflects current reality
- Build pipeline fails on new violations while tolerating known
  ones

---

## Phase 4: Pay down debt

With governance in place, the team can make incremental progress
on the debt without fear of regression.

### Shrink the suppression list

Each sprint, pay down a small number of entries on the
suppression list. Which ones depends on the friction they are
causing and the effort required to address them. Low-effort,
high-impact items go first.

When a suppression is removed, the rule tightens automatically.
The improvement is permanent because the test enforces it.

### Formalise contracts

For target modules, take the existing implicit contracts and make
them explicit. Document the public interface. Add consumer-driven
contract tests for the most important consumers. Apply a
versioning policy to future changes.

This is often where brownfield teams discover that "the contract"
is actually several contradictory things consumed by different
parts of the system. That discovery is useful: making the
contradictions explicit is the prerequisite to resolving them.

### Separate shared data

Where modules share data, start routing cross-module access
through the public surface rather than directly against the
database. This is usually the most involved part of brownfield
adoption, because data coupling tends to be more entangled than
code coupling.

A realistic approach: tackle one table or schema at a time,
starting with the ones where the coupling causes the most
friction. Expect this work to span several quarters for any
non-trivial system.

### Add observability

As target modules are improved, add the instrumentation that
should have been there all along: module identifiers on logs,
module-level metrics, participation in distributed tracing.

The first dashboards in a brownfield system often reveal
surprises. A module that was thought to be rarely called turns
out to be on the hot path. A dependency that was thought to be
async turns out to be synchronous. These surprises are useful;
they inform the next round of prioritisation.

### Deliverables from Phase 4

- Shrinking suppression lists for target modules
- Explicit contracts in place for target modules
- Cross-module data access progressively routed through public
  surfaces
- Observability in place for target modules

---

## Phase 5: Expand and sustain

Once the initial target modules are in acceptable state, expand
the approach to the next set, and establish the rhythms that
sustain the disciplines over time.

### Expand the scope

Apply the pattern to the next set of modules. Each cycle tends to
be cheaper than the first, because the team has built experience
and the shared infrastructure (architecture tests, contract
testing, observability dashboards) is already in place.

### Begin evaluation

Once a module has stable boundaries and enough observability data
to ground an assessment, it becomes a candidate for evolution
evaluation. Use the [decision model](../../decision-model/) to
consider whether the module should evolve further, consolidate
with something else, or stabilise as-is.

In brownfield systems, many evolution assessments end in
stabilise or harden. That is not a failure of the model; it is
the honest response to components that are serving their purpose
and do not need to change.

### Establish sustaining rhythms

- Quarterly domain map reviews
- Annual ownership confirmation
- ADRs captured for significant decisions as they happen
- Incident reviews with architectural implications using the
  [incident review checklist](../checklists/incident-review-checklist.md)
- Suppression list review as part of routine team rhythms

The goal is to reach a state where the disciplines are part of how
the team works, not a special initiative that requires dedicated
attention.

### Deliverables from Phase 5

- Expanded coverage across the system
- Evolution assessments producing actionable outcomes
- Sustaining rhythms integrated into team routines
- Observability data informing architectural decisions

---

## Common pitfalls in brownfield adoption

### Attempting perfection before value

Teams sometimes try to reach a clean state before delivering any
benefit. Brownfield adoption is progressive by nature. Partial
improvement beats paralysis.

### Fixing everything at once

Broad, sweeping refactors tend to fail. Each attempt consumes
capacity without delivering outcomes, and the team becomes
reluctant to try again. Narrow scope, commit to completion,
expand from there.

### Framing debt as failure

Engineers can be reluctant to document the suppression list
because it feels like admitting the codebase is bad. The
documentation is not the problem; the hidden debt is the
problem. Documenting it is the first step toward resolving it.

### Letting the suppression list grow

If the suppression list grows rather than shrinks, the governance
is not actually constraining the debt; it is just making it
visible while letting it continue. This is often a sign that
capacity has not been allocated to the cleanup work, and no
amount of tooling will compensate.

### Under-investing in observability

Brownfield teams sometimes treat observability as a nice-to-have
that can come after the code is cleaned up. That tends to be
wrong. Observability surfaces surprises that inform the code
cleanup, and the surprises tend to be more useful than the
team's prior assumptions about the system. Invest in
observability early in the adoption.

### Executive impatience

Senior leaders often expect faster results than are realistic.
Set expectations explicitly: "we will see visible improvement in
X quarters, and the full benefit will take Y". Over-promising
leads to disengagement when the benefits do not arrive as fast as
expected.

---

## Signs adoption is working

After six to twelve months:

- The domain map reflects the current state honestly
- At least one or two modules have tightened boundaries that
  hold up under architecture tests
- Suppression lists are shrinking rather than growing
- Observability is in place for the target modules
- The team can name the modules, their owners, and their public
  surfaces without checking
- At least one evolution assessment has produced an actionable
  outcome

---

## Signs adoption is struggling

- Discovery was done but never led to action
- Architecture tests were added but are routinely suppressed or
  disabled
- Suppression lists are growing
- Observability data is collected but not consulted
- Meetings about MODULE happen more often than work on MODULE
- The team has lost energy for the effort

When these show up, it is usually better to narrow the scope and
reset expectations than to push harder on the existing approach.
A smaller, more sustainable effort that actually delivers is
better than an ambitious one that collapses.
