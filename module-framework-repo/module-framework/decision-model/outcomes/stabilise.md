# Stabilise As-Is

**Trajectory:** Stabilise

**When It Applies:** Low change rate, low operational impact, no strong drivers for evolution in either direction.

## When Stabilisation Makes Sense

Stabilisation is the recognition that a component does not need to evolve right now. This is a valid outcome, not a lack of decision. Common indicators include:

- **Low change rate:** The component changes rarely, perhaps a few times a year, and those changes are typically small
- **Low operational impact:** The component does not contribute disproportionately to incidents, latency problems, or operational burden
- **No autonomy pressure:** The owning team is not blocked by the current structure; they can deliver without coordination friction
- **No scaling pressure:** The component does not need to scale independently of the rest of the system

Stabilisation does not mean the component is perfect. It means the cost of evolution exceeds the benefit given current circumstances. That calculation may change as the business evolves, which is why stabilisation includes a review cadence.

## What Stabilisation Involves

Stabilisation is the least disruptive outcome. It involves:

- **Documenting the current state:** Ensure the component's purpose, ownership, and boundaries are clear
- **Setting a review cadence:** Schedule periodic reassessment to check whether circumstances have changed
- **Monitoring for signal changes:** Keep an eye on the signals that would trigger a different outcome

## Execution Guidance

### 1. Document the current state

Ensure the component is well-documented:

- **Purpose:** What business capability does this component support?
- **Ownership:** Which team owns this component?
- **Boundaries:** What is the module boundary, even if informal?
- **Dependencies:** What does this component depend on, and what depends on it?
- **Data:** What data does this component own or access?

This documentation serves two purposes: it helps anyone who needs to work on the component, and it provides baseline information for future reassessments.

### 2. Set a review cadence

Schedule a periodic review to reassess the five signals. The cadence depends on how stable the area is:

- **Quarterly:** For components in areas of the system that change moderately
- **Biannually:** For truly stable components with minimal change

The review does not need to be extensive. A quick check of the signals and a conversation with the owning team is usually sufficient.

### 3. Define trigger conditions

Document the conditions that would prompt a reassessment outside the regular cadence:

- Significant increase in change rate (e.g., multiple changes per month instead of per quarter)
- New scaling requirements that cannot be met with current structure
- Team reorganisation that changes ownership or coordination patterns
- Operational incidents that suggest the component is becoming a burden

These triggers ensure the team responds to changing circumstances rather than waiting for the next scheduled review.

### 4. Ensure minimal governance

Even stable components benefit from basic governance:

- Boundary tests (if the component is a module) to prevent accidental coupling
- Code ownership rules to ensure changes are reviewed by the right people
- Basic monitoring to detect if the component starts contributing to incidents

This governance does not need to be extensive. The goal is to prevent drift, not to impose process for its own sake.

## When Stabilisation Is Not Enough

Stabilisation is a holding pattern. If any of the following occur, reassess and consider a different outcome:

- **Change rate increases significantly:** If the component starts changing frequently, consider [Modularise](modularise.md) to introduce better structure
- **Operational impact increases:** If the component starts contributing to incidents, consider [Consolidate](consolidate.md) or investigate root causes
- **Team autonomy becomes blocked:** If coordination friction increases, consider [Harden](harden.md) or [Extract](extract.md)

The signals guide the decision. Stabilisation is appropriate when the signals are quiet. When they are not, it is time to evolve.

## Related

- [Modularise](modularise.md): If change rate increases and structure is weak
- [Harden](harden.md): If boundaries need strengthening
- [Consolidate](consolidate.md): If operational burden becomes disproportionate
- [five-signals.md](../five-signals.md): Full signal definitions
