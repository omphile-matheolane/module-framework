# Ownership Registry

**System**:

**Last updated**:

**Maintained by**:

---

## Purpose

This document is the authoritative record of who owns each module.
When questions or disputes arise about who is responsible for a
given module, this registry is the source of truth.

Ownership means the owning team is responsible for:

- The integrity of the module (internal quality and adherence to
  the public surface)
- Reviewing changes to the module's public interface
- Managing the module's evolution over time
- Communicating with consumers about upcoming changes
- Responding to incidents involving the module

Other teams may contribute to a module, but the owning team carries
decision authority over the module's interface and direction.

---

## Review cadence

Ownership is confirmed at least annually. The `Last confirmed`
date for each entry records when ownership was most recently
validated. Entries not confirmed in over a year should be treated
as stale and reviewed.

Ownership changes are recorded in the change log at the bottom of
this document.

---

## Registry

| Module | Owning team | Primary contact | Escalation contact | Last confirmed |
|--------|-------------|-----------------|--------------------|----------------|
| | | | | |

---

## Ownership responsibilities by module

If specific modules have unusual ownership arrangements (shared
ownership, platform-provided with domain-team customisation,
deprecated with minimal maintenance), document the specifics here.

### Module: [Module Name]

**Standard responsibilities apply**: (yes / no)

**Exceptions or clarifications**:

---

## Decision rights

For cases where ownership requires specific decision rights, record
them explicitly.

| Decision type | Authority | Consulted |
|---------------|-----------|-----------|
| Public interface changes (breaking) | Owning team | Consuming teams |
| Public interface changes (additive) | Owning team | |
| Internal implementation | Owning team | |
| Module split or merge | Owning team with architecture review | Consuming teams |
| Cross-module schema changes | Owning teams jointly | |

---

## Change log

| Date | Module | Change | Reason |
|------|--------|--------|--------|
| | | | |
