# Outcome: Modularise Inside the Monolith

Modularisation introduces better internal structure within the
existing monolith boundary. The component stays in-process and keeps
its existing deployment model, but its boundaries become genuine
rather than notional. The public surface is defined, internals are
hidden, architecture tests enforce the separation, and data ownership
within the monolith becomes explicit.

Modularisation is often the right outcome even in situations where
extraction was the instinctive first thought. It addresses most of
the coordination and cognitive-load problems that drive teams toward
extraction, and it does so without the operational cost of running
an additional service. For components that are not yet ready for
extraction, and that may never need to be, modularisation is often
the best sustainable outcome.

---

## When this outcome applies

Modularisation is appropriate when all of the following hold:

- The component lives in a monolith
- Code Isolation or Data Ownership is Low or Medium
- At least one pressure signal is present: change impact is Medium
  or High, coordination pain is Medium or High, or two or more
  teams are regularly working on the component

Concretely, modularisation tends to be the right call when:

**The component changes frequently**. A component that sees regular
changes needs clear boundaries more than a stable one does. The
friction of working without boundaries compounds with every change.

**Multiple teams touch the component**. Shared ownership without
clear boundaries produces coordination overhead and inconsistent
evolution. Modularisation makes the shared surface explicit and
gives each team a clearer understanding of what they can and cannot
change.

**The component is growing**. A module that is accumulating
functionality will eventually need clear boundaries. Establishing
them while the module is manageable is considerably cheaper than
retrofitting them when it has grown unmanageable.

**Extraction is not yet justified**. The signals or drivers for
extraction are not all in place. Modularisation improves the
component's structure without committing to the operational cost
of extraction, and it positions the component for potential
extraction later if the drivers do emerge.

---

## When this outcome does not apply

Modularisation is not the right answer when:

**The component is already a well-formed module**. If Code Isolation
is already High, modularisation has little to add. Consider
[harden](harden.md) or [stabilise](stabilise.md) instead.

**There is no pressure**. If the component is stable, rarely
changes, and is touched by only one team, the overhead of
establishing formal module boundaries may exceed the benefit.
Modularisation is an investment, and investments need returns.

**The component is actually a separate domain that should be a
service**. If all the extraction signals are High and a driver
applies, modularisation delays the right answer.

**The component is a candidate for consolidation with a
neighbouring module**. If two modules within the monolith would be
better as one, modularisation of each in isolation does not address
the underlying problem.

---

## How to implement

Modularisation is progressive work rather than a single project. A
realistic sequence looks like this.

### Define the module

Write down what the module is: which business capability it
supports, which classes comprise its public surface, which classes
are internal. This definition is a commitment to a particular shape
and should be recorded as an ADR.

### Establish the public surface

Identify or create the facade, API, or public package that will be
the module's only entry point. If consumers currently reach into
internals, redirect them through the public surface. Some of this
work will expose gaps in the public surface: capabilities consumers
need that the surface does not currently provide. Fill those gaps
deliberately rather than by leaving the internal access in place.

### Add architecture tests

Write tests that enforce the boundary. Start with the core rule:
nothing outside the module should depend on anything inside the
module except through the public surface. If there are existing
violations, suppress them with a documented list and a plan to
address each.

### Separate data

If the module shares data with other modules, separate it. Within a
monolith this means:

1. Define which tables the module owns
2. Route access from other modules through the module's public
   surface, not directly to its tables
3. Remove cross-module foreign keys where practical

Full physical separation is not required for modularisation (that
is an extraction concern). Logical ownership is.

### Establish ownership

If ownership is unclear or shared, resolve it. A single team should
own the module. Contributors from other teams can still contribute,
but the owning team has decision authority over the module's
interface and direction.

### Document the contract

Capture the module's public interface in a form consumers can rely
on. For in-process modules this might be interface documentation,
Javadoc, or an OpenAPI spec for internal endpoints. What matters is
that the contract is explicit rather than implicit.

### Pay down the suppression list

The suppression list of known violations should shrink over time.
Track it, set quarterly targets, and resist the temptation to add
new suppressions in response to new work.

---

## Common pitfalls

**Declaring modules without enforcing them**. A module that exists
in documentation but not in architecture tests is a module that
will drift back to the pre-modularised state. Enforcement is what
makes modularisation stick.

**Modularising the code without modularising the data**. Shared
data across modules is the most common reason modularisation fails
to deliver benefits. The code looks cleaner, but the coupling is
still there, and it will surface at the next significant change.

**Defining the public surface too broadly**. If everything is
public, nothing is really encapsulated. Every class exposed on the
surface is something consumers can depend on and something that
constrains future changes. Expose only what is necessary.

**Treating modularisation as a one-time project**. Modules drift
unless they are maintained. The architecture tests, the public
surface definition, and the ownership record all need to stay
current. A module that was well-formed a year ago is not
necessarily well-formed now.

**Over-engineering the internal structure**. Modularisation is
about the boundary, not about imposing elaborate internal patterns.
Hexagonal architecture, CQRS, and similar patterns can be useful
inside a module when the complexity warrants them, but they are not
part of modularisation itself. Keep them separate decisions.

---

## Related outcomes

If the module is already well-structured but has minor boundary
weaknesses, [harden](harden.md) is a smaller-scope alternative.

If modularisation succeeds and genuine extraction drivers later
emerge, [extract](extract.md) becomes viable. The work done in
modularisation is prerequisite to extraction, and teams that
modularise carefully often find they can extract cheaply when the
time comes.
