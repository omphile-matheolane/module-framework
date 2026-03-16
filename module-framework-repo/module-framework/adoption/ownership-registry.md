# Ownership Registry Template

**Related Discipline:** Unify

**Purpose:** Track module ownership, governance status, and accountability across the system.

---

## Instructions

Fill in the table below for each module in your system. The registry should reflect actual ownership, not aspirational ownership. If ownership is contested or unclear, record that honestly.

Update the registry when ownership changes, when governance automation is added, or when new modules are introduced.

---

## System: [System Name]

**Last Updated:** [Date]

**Reviewed By:** [Names or Team]

---

## Ownership Registry

| Module | Owning Team | Tech Lead / Contact | Governance Status | Contract Approval | Last Review |
|--------|-------------|---------------------|-------------------|-------------------|-------------|
| [Module Name] | [Team Name] | [Name and contact] | [Enforced in CI / Manual / None] | [Who approves contract changes?] | [Date] |
| | | | | | |
| | | | | | |
| | | | | | |
| | | | | | |

---

## Governance Status Definitions

| Status | Meaning |
|--------|---------|
| **Enforced in CI** | Boundary rules are encoded as automated tests that run in CI and block merges on violation. This is the target state for mature modules. |
| **Manual** | Boundary rules exist and are documented, but enforcement relies on code review. Violations may slip through during busy periods. |
| **None** | No boundary rules are defined or enforced. The module boundary exists only as a convention. |

---

## Ownership Responsibilities

When a team owns a module, they are responsible for:

- **Boundary integrity:** Ensuring the module's boundary is respected and enforcing violations
- **Contract maintenance:** Maintaining the public API and managing breaking changes
- **Consumer communication:** Notifying consumers of upcoming changes and deprecations
- **Evolution decisions:** Running the five-signal assessment when evolution is considered
- **Documentation:** Keeping the module's documentation current

Ownership does not mean the team does all the work. Other teams may contribute code, but the owning team is accountable for the module's architectural integrity.

---

## Escalation Path

| Situation | Escalate To |
|-----------|-------------|
| Ownership is contested between teams | [Architecture lead or governance body] |
| Contract change affects multiple consumers | [Owning team + affected consumers] |
| Boundary violation is intentional (exception requested) | [Owning team + architecture lead] |
| Module has no owner | [Engineering leadership] |

---

## Observations

Use this section to record what the registry reveals.

**Gaps identified:**
- [e.g., "Two modules have no governance automation"]
- [e.g., "Reporting module has no clear owner"]

**Actions:**
- [e.g., "Add ArchUnit tests to Notification module by end of sprint"]
- [e.g., "Assign Reporting ownership to Finance team pending discussion"]

---

## Change History

| Date | Change | Author |
|------|--------|--------|
| [Date] | [What changed] | [Name] |
| | | |

---

## Related

- [domain-map-template.md](domain-map-template.md): Broader domain boundary documentation
- [governance-checklist.md](governance-checklist.md): Verify governance practices
- [/examples/payment-processing/ownership-registry.md](../examples/payment-processing/ownership-registry.md): Worked example
