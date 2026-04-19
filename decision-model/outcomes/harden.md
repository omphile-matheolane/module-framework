# Outcome: Harden This Module

Hardening strengthens the boundaries and contracts of a component
that is already a module but has not yet reached High across the
key isolation signals. The component stays in its current deployment
model. What changes is the quality of its boundary: the public
surface becomes tighter, architecture tests become stricter, the
contract becomes more explicit, and the data ownership becomes
cleaner.

Hardening is often a prerequisite to extraction. A component that
needs to become a service should typically be hardened as a module
first, because extraction amplifies every weakness in the existing
boundary. Hardening is also valuable on its own merits: a better-bounded
module is cheaper to maintain whether or not it ever becomes a
service.

---

## When this outcome applies

Hardening is appropriate when all of the following hold:

- The component is already a recognisable module (not just
  accumulated code inside a larger component)
- At least one of Code Isolation, Data Ownership, or Contract
  Stability is Medium rather than High
- Change impact or coordination pain is Medium or High

Concretely, hardening tends to be the right call when:

**The module mostly works but has rough edges**. Most of the
boundary is clean, but a handful of internal classes are accessed
from outside, a few tables are read by other components directly,
or the contract has some informal elements that are not documented.
The distance to High on each signal is not large, and the benefits
of closing the gap are worth the effort.

**Extraction is a plausible future but not a current decision**.
The team may want to extract this component eventually, but the
drivers are not yet in place. Hardening now reduces the future cost
of extraction while providing immediate benefits in the monolith.

**Coordination pain is climbing but extraction is not justified**.
The module is creating friction, and that friction will get worse
if nothing changes. Hardening addresses the friction without the
operational cost of extraction.

---

## When this outcome does not apply

Hardening is not appropriate when:

**The module does not yet exist as a module**. If Code Isolation
is Low and there is no real boundary, hardening has nothing to
strengthen. [Modularise](modularise.md) first.

**The signals are already High across the board**. If the module
is well-formed, [stabilise](stabilise.md) is the honest outcome.
Further hardening at that point is polishing rather than addressing
a real problem.

**Extraction is actually justified**. If all the extraction
signals are High and a driver applies, hardening delays the right
answer. That said, it is worth distinguishing between components
that are truly ready for extraction and components where the team
is impatient. Hardening as a prelude to extraction is often the
right call even when extraction is the eventual destination.

**The problems are not in the boundary**. If the module's issues
are internal complexity, poor performance, or code quality inside
the module, hardening is the wrong frame. Those are internal
improvements, not boundary strengthening.

---

## How to implement

Hardening is usually scoped smaller than modularisation. The module
already exists; the work is to close the remaining gaps.

### Identify the specific weaknesses

The signal assessment should have surfaced what is not yet High.
Write down each weakness concretely. "Code Isolation is Medium
because three classes in the module are referenced from outside
through reflection." "Contract Stability is Medium because the
interface has had two breaking changes in the past year, handled
through notification emails rather than versioned deprecation."

### Address each weakness

The right action depends on the specific weakness. Some common
patterns:

**For Code Isolation weaknesses**: tighten the architecture tests,
remove the suppressions, close the gaps in the public surface that
are driving the violations. If consumers need capabilities that
are not on the public surface, add them to the surface rather than
leaving the workarounds in place.

**For Data Ownership weaknesses**: route out-of-module reads
through the module's public interface. Remove cross-module foreign
keys. Claim logical ownership of tables that are owned in practice
but not formally.

**For Contract Stability weaknesses**: introduce a versioning
policy and apply it retroactively to document the current state.
Add consumer-driven contract tests for consumers that do not yet
have them. Document the deprecation process and apply it to any
future changes.

### Update the architecture tests

Whatever rules were permissive to accommodate the existing weaknesses
should be tightened. The goal is that a reintroduction of the
weakness would fail the build.

### Reassess

Run the assessment again and confirm that the signals have moved.
Hardening is complete when the signals that motivated it have moved
to High, or when the remaining gap is clearly too small to justify
further investment.

---

## Common pitfalls

**Hardening without reassessing**. If the signals have not actually
moved, the hardening work did not succeed. Either the work was not
sufficient, or it addressed different weaknesses from the ones that
mattered.

**Treating hardening as a one-time project**. Modules drift. The
hardening work needs to be maintained, and the architecture tests
need to catch regressions. A module that was hardened a year ago is
not necessarily well-hardened now.

**Hardening the wrong thing**. If the weaknesses identified in the
signal assessment were imprecise, the hardening work may target
surface problems rather than root causes. Paired assessment tends
to reduce this risk.

**Stopping short of full separation**. Especially with data
ownership, it is tempting to stop when the most visible coupling is
resolved, leaving the subtler coupling intact. The subtler coupling
is what will bite when the module needs to evolve further.

---

## Related outcomes

If hardening completes and extraction drivers are in place,
[extract](extract.md) is the natural next step.

If hardening completes and no further evolution is needed,
[stabilise](stabilise.md) is the honest next state.

If the component turns out not to be a module at all, but a collection
of loosely related code, [modularise](modularise.md) is the right
frame rather than hardening.
