# New Module Checklist

Use this checklist when creating a new module. The goal is to
start the module with the disciplines in place from day one,
rather than retrofitting them later when the module has already
accumulated shortcuts.

A module that starts with clear boundaries, explicit ownership, a
defined contract, and working architecture tests is substantially
cheaper to evolve than one that acquires those properties after
the fact.

---

## Definition

- [ ] The business capability the module supports is stated in
  plain language
- [ ] The module has a name that reflects the domain rather than
  the technology
- [ ] The decision to create the module (rather than extending an
  existing one) is recorded as an ADR

---

## Map

- [ ] The module is added to the domain map with its capability,
  owner, and initial dependencies
- [ ] The module's relationship to neighbouring modules is
  documented
- [ ] Any expected future dependencies are noted

---

## Organise

- [ ] The public surface of the module is defined explicitly
  (which classes, interfaces, or endpoints consumers may use)
- [ ] Internal packages are set up and marked as package-private
  or equivalent
- [ ] The internal architecture style (layered, hexagonal,
  vertical slices, flat) is chosen deliberately and documented
- [ ] Component scanning, dependency injection, or equivalent
  wiring is scoped to the module's public surface

---

## Design

- [ ] The module's public contract is documented (API spec,
  interface documentation, event schema, whatever format fits
  the communication pattern)
- [ ] The contract is added to the contract catalog
- [ ] A versioning policy applies (inheriting the system default
  unless the module needs something different)
- [ ] Consumer-driven contract tests are set up for the first
  consumer, even if there is only one

---

## Unify

- [ ] The owning team is recorded in the ownership registry
- [ ] Primary and escalation contacts are identified
- [ ] Architecture tests are in place enforcing:
  - [ ] No external access to the module's internals
  - [ ] No cyclic dependencies with other modules
  - [ ] Dependencies match the declared set
- [ ] Architecture tests run on every build
- [ ] The creation of the module is recorded as an ADR

---

## Leverage

- [ ] Structured logging is configured with the module identifier
  on every log line
- [ ] Basic metrics are in place (at minimum: latency, error
  rate, throughput)
- [ ] The module participates in distributed tracing with the
  correct module context on every span
- [ ] The module is represented on the architectural dashboard

---

## Evaluate

Evaluation is not typically relevant for a new module, because
the signals are not yet meaningful. The first assessment should
be scheduled for roughly six to twelve months after the module
has been in production, once there is enough evidence to support
it.

- [ ] Initial evolution assessment scheduled (typically six to
  twelve months out)

---

## Data

- [ ] The module's data is defined (which tables, schemas, or
  stores it will own)
- [ ] Data ownership is documented in the domain map
- [ ] Cross-module reads and writes are avoided by design (or
  explicitly justified and documented)
- [ ] Migration tooling is in place for the module's schema
  evolution

---

## Operational readiness

- [ ] Deployment pipeline is set up (shared with the monolith or
  independent as appropriate)
- [ ] Alerts are configured for the module's key metrics
- [ ] Runbook exists for the most common incident types
- [ ] On-call coverage is clear

---

## Red flags

If any of these are true when the checklist is complete, pause
and address them before the module goes into production:

- The public surface is not clearly defined
- Architecture tests do not exist or do not enforce the boundary
- Ownership is shared or unclear
- The contract is not documented
- The module reads or writes data outside its own store without
  going through another module's public surface
- Observability is not in place
