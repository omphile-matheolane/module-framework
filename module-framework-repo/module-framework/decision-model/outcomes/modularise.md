# Modularise Inside the Monolith

**Trajectory:** Modularise

**When It Applies:** The area changes often but has weak code and data boundaries. Needs internal structure before extraction is viable.

## When Modularisation Makes Sense

Modularisation is the appropriate step when a part of the system needs better structure but is not ready for extraction. Common indicators include:

- **High change rate with weak boundaries:** The area changes frequently, and those changes ripple across the codebase because there is no clear separation
- **Low code isolation (1-2):** Other parts of the system depend directly on internal classes, making independent evolution difficult
- **Unclear data ownership:** The area accesses tables that are also accessed by other parts of the system, with no clear ownership
- **Merge conflicts and coordination friction:** Teams working on this area frequently conflict with teams working elsewhere, even when the work should be independent

Modularisation creates the structure needed to improve these signals. It is often a prerequisite to extraction, but it is also valuable on its own. A well-structured module inside a monolith is easier to understand, test, and maintain than a poorly-structured distributed service.

## What Modularisation Involves

Modularisation means introducing a clear boundary around a set of related capabilities:

- **Define a public API:** The module exposes a set of entry points (a facade, an API layer, use case handlers) that other parts of the system use
- **Hide internals:** Internal classes, repositories, and domain logic are not directly accessible from outside the module
- **Enforce the boundary:** Automated tests (such as ArchUnit rules) verify that the boundary is respected

The goal is to make the module's internals invisible to the rest of the codebase, so that changes inside the module do not force changes outside it.

## Execution Guidance

### 1. Identify the module boundary

Define which capabilities belong to the module. This should align with a business capability or domain concept. If the boundary is unclear, revisit the Map discipline before proceeding.

### 2. Introduce a public API

Create the entry points that other parts of the system will use:

- A facade class that exposes the module's operations
- Use case handlers that accept commands and queries
- An API layer that defines the contract

At this stage, the API may simply delegate to existing code. The goal is to establish the entry point, not to refactor everything at once.

### 3. Redirect consumers

Update existing callers to use the public API instead of accessing internals directly. This is often the most time-consuming step, especially if the area has been accessed in many different ways.

Approach this incrementally:

- Identify the most common access patterns
- Redirect those first
- Use static analysis or boundary tests to find remaining violations

### 4. Hide internals

Once consumers are using the public API, restrict access to internals:

- Move internal classes to internal packages
- Apply access modifiers or package-private visibility
- Add boundary tests that fail if internal classes are accessed from outside

### 5. Add boundary enforcement

Introduce automated tests that verify the boundary is respected. In Java, ArchUnit is a common choice. Other ecosystems have equivalent tools.

These tests should run in CI and block merges on violation.

### 6. Assess data ownership

With the code boundary in place, assess whether the module owns its data. If it shares tables with other modules, decide whether to:

- Accept the sharing and document it
- Gradually migrate to owned tables
- Introduce a data contract between modules

Data ownership is often harder to resolve than code isolation. It may require a separate effort after the code boundary is established.

## Post-Modularisation Review

After modularisation, reassess the five signals:

- Code Isolation should have improved to 4-5
- Data Ownership may still need work
- Contract Stability can now be measured against the new public API

If signals are strong enough, the module may be a candidate for [Extract](extract.md). If data ownership or contract stability remain weak, [Harden](harden.md) is the next step.

## Related

- [Harden](harden.md): If the module exists but needs boundary strengthening
- [Extract](extract.md): If signals become strong enough after modularisation
- [five-signals.md](../five-signals.md): Full signal definitions
