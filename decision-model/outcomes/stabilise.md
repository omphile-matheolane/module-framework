# Outcome: Stabilise As-Is

Stabilisation is the decision to make no structural change. The
component remains in its current deployment model, with its current
boundaries, its current contract, and its current ownership. What
changes is that the decision has been made deliberately rather than
by default.

Stabilisation is a legitimate outcome, not a sign that assessment
has been deferred. Not everything needs to evolve. A component that
is serving the organisation well, is not creating coordination
overhead, and is not blocking other work does not need to be
reshaped to match someone's architectural preferences.

---

## When this outcome applies

Stabilisation is appropriate when all of the following hold:

- The change rate is low
- Operational impact is High (meaning the component is operationally
  healthy, not that it is causing problems)
- No strong driver for evolution exists
- Coordination pain is low

Concretely, stabilisation tends to be the right call when:

**The component is mature and rarely changes**. It was built for a
well-understood purpose, that purpose has not shifted materially, and
the component does its job without requiring much attention. Reshaping
a stable component tends to destabilise it, and the benefits of
reshaping rarely justify that destabilisation.

**The current structure is working**. The boundaries may not be
textbook perfect, but they are not creating friction. Teams can make
the changes they need to make. Consumers can rely on the contract.
Incidents involving the component are rare. "Good enough" is a
legitimate state.

**The component will be replaced rather than evolved**. If the
component is scheduled for replacement or retirement, investing in
its structure is misallocated effort. Keeping it stable until the
replacement is ready is the right call.

**The team does not have capacity for evolution work**. Architectural
improvement competes with feature development for attention. If the
feature work is more valuable, deferring evolution is a legitimate
choice. The key is to make that deferral explicit so it is revisited
when capacity changes, rather than letting it become indefinite by
default.

---

## When this outcome does not apply

Stabilisation is not appropriate when:

**The component is actually causing friction**. If change impact is
High or coordination pain is climbing, stabilisation is avoidance
rather than a deliberate decision. Modularisation or hardening is
likely the right answer.

**Signals are degrading over time**. A component whose signals are
getting worse needs attention, even if the current state is
acceptable. Stabilisation means holding position; it does not mean
ignoring a drift.

**An extraction or consolidation driver has appeared**. If regulatory
requirements, scaling needs, or team changes have materially
changed the context, the earlier decision to stabilise should be
revisited.

**The component is functioning but the broader system is not**.
Sometimes a component is individually fine but is part of a pattern
that is failing at the system level. Stabilising each component
individually can miss the system-level problem.

---

## How to implement

Stabilisation is mostly about what not to do. The implementation is
lightweight, but there are a few things worth doing deliberately.

### Record the decision

Even a decision to do nothing should be recorded. An ADR that says
"we assessed this component and decided no structural change is
warranted" is valuable institutional memory. Without it, the
assessment itself tends to get lost, and the next person who looks
at the component may run the same analysis and reach the same
conclusion without knowing the previous one existed.

The ADR should include:

- The signals as assessed
- The absence of drivers
- The reasoning for stabilisation rather than another outcome
- A scheduled reassessment date

### Set a reassessment date

Stabilisation is not permanent. A stable component may become an
evolution candidate if its context changes. Schedule a reassessment
(annually is usually appropriate) to confirm that the stabilisation
decision still holds.

### Maintain the baseline

A stabilised component still needs routine maintenance. Architecture
tests should continue to run. Contract tests should continue to
pass. Observability should remain in place. Stabilisation is not an
excuse to let the component's quality baseline degrade.

### Resist the urge to improve

Once stabilisation has been decided, small improvements to the
component's structure should be scrutinised carefully. Each "minor"
refactoring risks destabilising something that is working. If an
improvement is genuinely worth the risk, it may be a signal that
the stabilisation decision is outdated and should be revisited.
Otherwise, leave it alone.

---

## Common pitfalls

**Stabilisation as avoidance**. If the signals show real problems
and stabilisation is being chosen because nobody wants to do the
work, the problems will compound. A component that needs hardening
but gets stabilised will continue to create friction, and the
friction will get harder to address over time.

**Stabilisation without a reassessment trigger**. A stabilisation
decision made today may not reflect the situation in a year. Without
a scheduled reassessment, the decision effectively becomes
permanent, even when circumstances have changed.

**Continuous small changes that destabilise over time**. A
stabilised component that keeps receiving "small improvements"
ends up being neither stable nor improved. Either commit to the
stabilisation and leave the component alone, or acknowledge that
evolution is warranted and choose a different outcome.

**Mistaking absence of pain for absence of problem**. Some components
are stable because nobody is asking them to change. When someone
eventually does, the latent problems surface. Stabilisation is
appropriate when the component is genuinely healthy, not when it has
simply not been tested.

---

## Related outcomes

If reassessment later reveals that signals have degraded or drivers
have appeared, consider [harden](harden.md), [modularise](modularise.md),
[extract](extract.md), or [consolidate](consolidate.md) as
appropriate.

If the component is healthy but the boundaries could be marginally
tightened for a clear reason, [harden](harden.md) is a narrower
intervention than stabilisation foregoes.
