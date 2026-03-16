# Payment Processing Example: Ownership Registry

**System:** Payment Processing Platform

**Last Updated:** March 2026

**Reviewed By:** Engineering Leadership

---

## Ownership Registry

| Module | Owning Team | Tech Lead / Contact | Governance Status | Contract Approval | Last Review |
|--------|-------------|---------------------|-------------------|-------------------|-------------|
| Payment Initiation | Payments | Sarah Chen | Enforced in CI | Sarah Chen + API Guild | March 2026 |
| Payment Authorisation | Payments | Sarah Chen | Enforced in CI | Sarah Chen | March 2026 |
| Notification | Customer Experience | James Okonkwo | Manual | James Okonkwo | January 2026 |
| Reconciliation | Finance | Priya Sharma | Enforced in CI | Priya Sharma | February 2026 |
| Reporting | Unassigned | — | None | — | Never |

---

## Governance Status Details

### Payment Initiation

**Status:** Enforced in CI

The module has comprehensive boundary tests using ArchUnit. Tests verify:
- No external access to internal packages
- Dependency rules are respected (only allowed modules can depend on Payment Initiation)
- Public API is accessed only through defined entry points

The CI pipeline blocks merges on any boundary violation. Contract changes to the public API require approval from the Tech Lead and the API Guild.

### Payment Authorisation

**Status:** Enforced in CI

Same governance model as Payment Initiation. Both modules are owned by the same team and share the same governance automation. The modules are tested together as part of the Payments domain.

### Notification

**Status:** Manual

Boundary rules exist and are documented in the module's README, but enforcement relies on code review. The Customer Experience team reviews all changes, but violations occasionally slip through during busy periods.

The team has a backlog item to add ArchUnit tests and integrate them into CI. This is blocked on resolving the shared schema with Payment Initiation, as the boundary tests would immediately fail on the current structure.

### Reconciliation

**Status:** Enforced in CI

The Finance team added boundary enforcement six months ago after a production incident caused by an unexpected dependency. The module now has strict boundary tests and runs independently of the other modules.

### Reporting

**Status:** None

No boundary rules exist. The module does not have a defined public API. Multiple teams access tables directly to build reports. There is no formal ownership, though Finance consumes most of the output and Platform maintains the infrastructure.

This is the highest-risk module in the system from a governance perspective.

---

## Escalation Path

| Situation | Escalate To |
|-----------|-------------|
| Ownership is contested between teams | Head of Engineering |
| Contract change affects multiple consumers | Owning Tech Lead + affected Tech Leads |
| Boundary violation is intentional (exception requested) | Owning Tech Lead + Architecture Guild |
| Module has no owner | Head of Engineering |

---

## Observations

**Governance is inconsistent across modules.** Three modules have CI-enforced governance, one has manual governance, and one has none. This inconsistency creates risk: changes to Reporting or Notification can have unintended effects that are not caught until production.

**Reporting ownership must be resolved.** The lack of ownership is not sustainable. Every quarter, the question of who owns Reporting comes up, and every quarter it is deferred. This example demonstrates what happens when ownership is not assigned: governance does not exist, boundaries do not form, and the module becomes a liability.

**Notification governance is blocked by technical debt.** The team wants to add boundary enforcement but cannot do so until the shared schema is resolved. This is a common pattern: governance improvements are blocked by structural issues that require investment to fix.

---

## Recommended Actions

| Priority | Action | Owner | Target |
|----------|--------|-------|--------|
| 1 | Assign Reporting ownership to Finance team | Head of Engineering | This month |
| 2 | Resolve shared schema between Notification and Payment Initiation | Payments + CX teams | Q2 |
| 3 | Add boundary tests to Notification after schema resolution | Customer Experience | Q2 |
| 4 | Define Reporting public API and introduce boundary | Finance (new owner) | Q3 |

---

## Change History

| Date | Change | Author |
|------|--------|--------|
| March 2026 | Initial registry created | Engineering Leadership |

---

## Related

- [domain-map.md](domain-map.md): Module boundary documentation
- [decision-model-walkthrough.md](decision-model-walkthrough.md): Evolution assessment for Payment Initiation
- [/adoption/ownership-registry.md](../../adoption/ownership-registry.md): Template used for this registry
