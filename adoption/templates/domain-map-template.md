# Domain Map

**System**:

**Last updated**:

**Maintained by**:

---

## Purpose

This document is the working map of the system's modules, their
business capabilities, their ownership, and their dependencies. It
should reflect the current state of the system as it actually
exists, not the state that was intended at some previous point.

If any part of this document does not match what is in production,
that is a signal that either the map or the production state needs
to change. The map is valuable because it is accurate.

---

## Review cadence

This map is reviewed quarterly. It is also updated whenever:

- A new module is created
- A module is split, merged, or retired
- Module ownership changes
- A significant dependency is added or removed

---

## Modules

For each module, record the capability it supports, its owning
team, its public surface, and its dependencies on other modules.

### Module: [Module Name]

**Business capability**: What this module does in business terms.
One or two sentences. Avoid technical jargon where plain language
works.

**Owning team**:

**Status**: (greenfield / active / mature / deprecated)

**Public surface**: What consumers of this module actually depend
on. A facade class, an API, a set of events. Be specific.

**Data owned**: Which tables, schemas, or data stores this module
owns authoritatively.

**Dependencies**:

| Depends on | Type (sync/async/event) | Notes |
|-----------|-------------------------|-------|
| [Other module] | | |

**Consumed by**:

| Consumer | Type (sync/async/event) | Notes |
|----------|-------------------------|-------|
| [Other module] | | |

**Known issues** (if any): Boundary violations, contract instability,
data sharing that has not yet been resolved. Be honest about the
current state.

---

### Module: [Next Module Name]

(repeat structure above)

---

## Dependency overview

A visual representation of the module dependencies is often useful.
This can be a Mermaid diagram, a PlantUML diagram, an image, or a
link to an external tool. Keep it simple enough to maintain.

Example in Mermaid:

```
graph LR
    orders --> payments
    orders --> inventory
    orders --> notifications
    payments --> notifications
    inventory --> notifications
```

---

## Changes since last review

| Date | Change | Reason |
|------|--------|--------|
| | | |

---

## Notes

Anything that did not fit elsewhere but is useful context for
readers of this document. Evolving conversations, pending
decisions, known tensions between what is mapped and what the team
wishes were true.
