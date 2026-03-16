# Domain Map Template

**Related Discipline:** Map

**Purpose:** Document the domain boundaries, module responsibilities, and team ownership across the system.

---

## Instructions

Fill in the table below for each module or bounded context in your system. The goal is to capture the current state, not an idealised future state. Be honest about unclear boundaries and contested ownership.

Review the domain map quarterly or when significant changes occur (team reorganisations, new capabilities, architectural decisions).

---

## System: [System Name]

**Last Updated:** [Date]

**Reviewed By:** [Names or Team]

---

## Domain Map

| Module | Business Capability | Owning Team | Boundary Status | Key Dependencies | Notes |
|--------|---------------------|-------------|-----------------|------------------|-------|
| [Module Name] | [What business capability does this module support?] | [Team Name] | [Clear / Drifting / Unclear] | [Which modules does this depend on?] | [Any relevant context] |
| | | | | | |
| | | | | | |
| | | | | | |
| | | | | | |

---

## Boundary Status Definitions

| Status | Meaning |
|--------|---------|
| **Clear** | The module has a well-defined boundary. Internal classes are hidden behind a public API. Dependency rules are documented and ideally enforced. |
| **Drifting** | The module had a clear boundary at some point, but it has eroded. There may be violations of the original boundary rules, shared schemas, or unclear ownership of certain areas. |
| **Unclear** | The module does not have a recognisable boundary. Other parts of the system access internals directly. Ownership may be contested or unknown. |

---

## Observations

Use this section to record what the domain map reveals about the current state of the system.

**Patterns observed:**
- [e.g., "Three modules are marked as Drifting, all in the payments area"]
- [e.g., "Ownership of the Reporting module is unclear, accessed by multiple teams"]

**Immediate concerns:**
- [e.g., "Notification and Payment Initiation share a schema, blocking independent evolution"]

**Recommended actions:**
- [e.g., "Clarify ownership of Reporting before Q3 planning"]
- [e.g., "Prioritise schema separation between Notification and Payment Initiation"]

---

## Change History

| Date | Change | Author |
|------|--------|--------|
| [Date] | [What changed] | [Name] |
| | | |

---

## Related

- [ownership-registry.md](ownership-registry.md): Detailed ownership and governance tracking
- [/decision-model](../decision-model/): For assessing whether modules should evolve
- [/examples/payment-processing/domain-map.md](../examples/payment-processing/domain-map.md): Worked example
