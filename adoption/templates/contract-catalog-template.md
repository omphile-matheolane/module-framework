# Contract Catalog

**System**:

**Last updated**:

**Maintained by**:

---

## Purpose

This catalog lists the significant interfaces between modules in
the system. For each interface, it records the current version,
the owning module, the consumers, the communication pattern, and
where the formal specification lives.

An interface counts as significant if it is depended on by more
than one team, if changes to it have coordination implications, or
if it carries behavioural guarantees that consumers rely on.
Private module internals do not belong here.

---

## Review cadence

This catalog is reviewed quarterly, and updated whenever:

- A new significant interface is introduced
- A contract version changes
- A consumer is added or removed from an existing interface
- A deprecation timeline is agreed

---

## Catalog

For each contract, record the details below. Use a separate entry
per contract; do not fold multiple contracts into one entry just
because they share an owner.

### Contract: [Contract Name]

**Owning module**:

**Current version**:

**Communication pattern**: (synchronous REST / asynchronous
messaging / event / in-process interface)

**Specification location**: Link to the OpenAPI spec, AsyncAPI
spec, Protobuf definition, Java interface, or wherever the formal
contract lives.

**Consumers**:

| Consumer | Version consumed | Contract test coverage |
|----------|------------------|------------------------|
| | | |

**Previous versions still supported**:

| Version | Deprecation date | Retirement date |
|---------|------------------|-----------------|
| | | |

**Versioning notes**: How breaking changes are handled for this
contract specifically, if different from the system-wide policy.

**Stability**: (stable / evolving / unstable) and the evidence
supporting that rating (from Contract Stability in the decision
model).

---

### Contract: [Next Contract]

(repeat structure above)

---

## System-wide policies

Conventions that apply across all contracts in this catalog unless
noted otherwise in a specific entry.

**Versioning**: How versions are assigned and expressed (semantic
versioning, URL-based, header-based, schema evolution rules for
events, etc.).

**Breaking change handling**: The standard process for introducing
a breaking change, including deprecation period, consumer
notification, and the criteria for retiring old versions.

**Contract testing**: The required coverage and tooling (Pact,
Spring Cloud Contract, or equivalent for your ecosystem).

**Documentation**: Where contracts are documented, how they are
kept in sync with code, and how changes are reviewed.

---

## Change log

| Date | Contract | Change | Reason |
|------|----------|--------|--------|
| | | | |
