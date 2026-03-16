# Governance Checklist

**Related Discipline:** Unify

**Purpose:** Verify that governance practices are in place for boundary enforcement, ownership, decision records, and observability readiness.

---

## Instructions

Work through this checklist for your system or for a specific module. Check each item that is in place. For items that are not in place, note whether they are planned, in progress, or not applicable.

This checklist is not a one-time exercise. Review it quarterly or after significant architectural changes.

---

## System / Module: [Name]

**Last Reviewed:** [Date]

**Reviewed By:** [Names or Team]

---

## Boundary Enforcement

| Status | Practice |
|--------|----------|
| [ ] | Module boundary rules are defined (which modules can import from which) |
| [ ] | Boundary rules are encoded as automated tests (e.g., ArchUnit, NetArchTest, or equivalent) |
| [ ] | Boundary tests run in CI and block merges on violation |
| [ ] | No direct access to internal module types from outside the module |
| [ ] | Exceptions to boundary rules are documented and reviewed periodically |

**Notes:**
[Record any gaps, planned work, or context]

---

## Ownership

| Status | Practice |
|--------|----------|
| [ ] | Every module has a named owning team in the ownership registry |
| [ ] | Ownership is current (reviewed in the last quarter) |
| [ ] | Changes to shared contracts require explicit approval from the owning team |
| [ ] | CODEOWNERS file (or equivalent) reflects current module ownership |
| [ ] | Escalation path is defined for contested ownership or cross-cutting changes |

**Notes:**
[Record any gaps, planned work, or context]

---

## Decision Records

| Status | Practice |
|--------|----------|
| [ ] | Architectural decisions that affect module boundaries are documented as ADRs |
| [ ] | ADRs are stored in the repository and linked from the relevant module |
| [ ] | Superseded ADRs are marked as such rather than deleted |
| [ ] | ADRs follow a consistent format (context, decision, consequences) |
| [ ] | ADRs are reviewed as part of architectural discussions |

**Notes:**
[Record any gaps, planned work, or context]

---

## Observability Readiness

| Status | Practice |
|--------|----------|
| [ ] | Each module emits structured logs with a module identifier |
| [ ] | Inter-module call latency is measured and visible in monitoring |
| [ ] | Error rates per module are tracked and alertable |
| [ ] | Distributed tracing is in place for cross-module requests |
| [ ] | Dashboards exist showing module-level health and performance |

**Notes:**
[Record any gaps, planned work, or context]

---

## Summary

| Area | Items Complete | Items Outstanding |
|------|----------------|-------------------|
| Boundary Enforcement | [ ] / 5 | |
| Ownership | [ ] / 5 | |
| Decision Records | [ ] / 5 | |
| Observability Readiness | [ ] / 5 | |
| **Total** | [ ] / 20 | |

---

## Starting Small

Teams do not need all twenty items on day one. A practical starting point:

**Minimum viable governance (Week 1-2):**
- Every module has a named owner
- Boundary rules are documented (even if not yet automated)

**Next priority (Month 1):**
- Boundary tests run in CI for high-risk modules
- CODEOWNERS reflects current ownership
- Structured logging includes module identifiers

**Full governance (Quarter 1):**
- All boundary enforcement items complete
- All ownership items complete
- Observability basics in place

Decision records can be introduced gradually as architectural decisions arise.

---

## Observations and Actions

**Gaps identified:**
- [e.g., "Boundary tests exist but do not run in CI"]
- [e.g., "No distributed tracing for cross-module calls"]

**Priority actions:**
- [e.g., "Add boundary tests to CI pipeline this sprint"]
- [e.g., "Evaluate tracing options for Q3"]

---

## Related

- [ownership-registry.md](ownership-registry.md): Track ownership status
- [domain-map-template.md](domain-map-template.md): Document boundaries
- [/decision-model](../decision-model/): Evolution decision model
