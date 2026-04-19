# Greenfield Adoption Playbook

This playbook describes how to adopt MODULE when starting a new
system or a new major component. Greenfield adoption is simpler
than brownfield in most respects: there is no accumulated debt to
work around, no existing violations to suppress, and no
established habits to unlearn. The challenge is different: doing
the discipline work before the system needs it, because the cost
of starting well is much lower than the cost of retrofitting.

---

## When this playbook applies

- A new system is being built from scratch
- A new service or major component is being introduced into an
  existing organisation that is adopting MODULE
- A rewrite is underway where the existing system is being
  replaced rather than evolved

For introducing MODULE into an existing system that will continue
to run, see the [brownfield adoption playbook](brownfield-adoption.md).

---

## Phase 1: Before any code is written

The work in this phase is primarily about Map and Unify. The goal
is to establish the shape of the system and the governance
mechanisms before the codebase exists.

### Domain discovery

- Run a discovery session with engineers, product managers, and
  domain experts. EventStorming, domain storytelling, and
  capability mapping are all appropriate techniques.
- Produce an initial domain map that captures the candidate
  modules, their capabilities, and their rough boundaries.
- Treat the map as a hypothesis rather than a commitment. The
  team will learn more as they build, and the map should be
  expected to shift.

### Team alignment

- Identify the owning team for each candidate module.
- If the team structure does not yet exist, note which modules
  each future team is intended to own.
- If the team structure contradicts the domain structure,
  resolve that before proceeding. Conway's Law will assert
  itself regardless; better to address it deliberately.

### Governance foundations

- Decide on the architecture testing tool for the chosen
  ecosystem (ArchUnit for Java, NetArchTest for .NET, and so on).
- Draft the initial set of architecture rules (see
  [starter architecture tests](../templates/starter-arch-tests.md)).
- Decide on the ADR format and where ADRs will live.
- Set up the repository structure with ADRs, domain map,
  ownership registry, and contract catalog in place from the
  first commit.

### Deliverables from Phase 1

- Initial domain map
- Ownership registry populated with current and intended owners
- First ADR documenting the initial architecture
- Architecture testing tool selected and configured
- Repository scaffolded with all MODULE artefacts

---

## Phase 2: First modules

As the first modules are built, apply the Organise and Design
disciplines alongside the code. The [new module checklist](../checklists/new-module-checklist.md)
is the operational companion for this phase.

### Per-module setup

For each new module:

- Define the public surface before writing implementation code.
  Interface-first design tends to produce cleaner boundaries than
  inferring contracts from implementations after the fact.
- Set up package structure with the public surface at the root
  and internals one level down, marked package-private or
  equivalent.
- Add the module's architecture tests, including:
  - No external access to internals
  - No cyclic dependencies with other modules
  - Explicit dependency declaration
- Document the module's initial contract in the contract catalog.
- Add observability instrumentation from the first code: module
  identifiers on logs, basic metrics, participation in distributed
  tracing.

### Avoiding the sequential trap

It is tempting to "do Map and Organise now, worry about the
others later." This tends to produce systems that are cleanly
bounded but poorly observed, or well-observed but without clear
contracts. The disciplines reinforce each other, and introducing
them out of sequence creates gaps.

The better pattern is to do the minimum viable version of each
discipline for each new module. The minimum for Leverage is
structured logs with module identifiers, which is not a large
commitment. The minimum for Design is a documented contract,
which takes an hour. Doing a little of each is cheaper than
doing all of one and then retrofitting the others.

### Deliverables from Phase 2

- First modules built with MODULE disciplines applied from the
  start
- Architecture tests running in CI and failing the build on
  violations
- Domain map reflects the current set of modules accurately
- Contract catalog reflects the current interfaces
- Observability instrumentation in place across all modules

---

## Phase 3: Team habits

The disciplines become sustainable only when they are part of how
the team works, not a separate activity that competes with the
"real work". Establishing those habits is the focus of the third
phase.

### Rhythms to establish

- Quarterly domain map review, using the
  [map review checklist](../checklists/map-review-checklist.md).
- Annual ownership confirmation.
- ADRs consulted when related changes are being made (and linked
  from PR descriptions).
- Architecture test failures treated with the same seriousness
  as any other build failure.
- Contract changes handled through the versioning policy, not
  through silent modification.
- Incident reviews using the [incident review checklist](../checklists/incident-review-checklist.md)
  for incidents with architectural implications.

### The first year

In a greenfield system, evolution assessment is not typically
relevant in the first year. The signals are not stable enough to
be meaningful, and the modules have not accumulated the kind of
context that evolution decisions require.

The first evolution assessments should usually happen between 12
and 18 months after launch, once there is enough runtime data to
support them. Schedule the first assessments for this window so
they are not forgotten.

### Deliverables from Phase 3

- Regular review cadences in place
- The team can articulate which disciplines apply to a given
  decision without consulting the framework
- Architecture tests run routinely and stay green
- ADRs accumulate at a reasonable rate (too few suggests
  decisions are not being recorded; too many suggests the bar is
  too low)

---

## Common pitfalls in greenfield adoption

### Over-engineering the initial structure

Greenfield adopters often produce elaborate initial structures
that do not survive contact with the actual domain. The first
domain map should be simple enough that being wrong about it is
cheap. Two or three modules with clear responsibilities are
easier to refine than fifteen modules with overlapping scope.

### Deferring the disciplines until "later"

The argument that "we will add architecture tests once the
codebase is large enough to need them" is almost always wrong.
The cost of adding them before the codebase is large is minimal.
The cost of retrofitting them to a large codebase is substantial.
The discipline that prevents problems costs less than the
discipline that resolves them.

### Treating the first ADR as the last ADR

Some teams record an initial architecture decision and then never
write another ADR. The ADR stream should be ongoing, with each
significant architectural choice captured as it is made. A
repository with one ADR from two years ago is indistinguishable
from a repository with none.

### Ignoring the cost side

Even in greenfield, MODULE has costs. The upfront investment in
domain discovery, governance setup, and observability
infrastructure takes time that could go into feature development.
Teams that adopt without acknowledging this tend to resent the
framework when the costs become visible. The
[whitepaper section on costs and limitations](../../whitepaper/)
is worth reading explicitly at the start of adoption.

---

## Signs adoption is going well

After six months, the following should be true:

- New engineers can understand the system's structure by reading
  the domain map rather than by spelunking through code
- Architecture test violations are rare, and when they occur they
  are resolved quickly
- Contracts between modules are documented and followed
- Observability data reveals surprises occasionally, which the
  team investigates rather than ignores
- ADRs get consulted when related changes are being made
- The team's language includes "the map", "the public surface",
  "the contract", without those terms needing to be explained

---

## Signs adoption is struggling

- The domain map has not been updated in months
- Architecture tests exist but are routinely bypassed or disabled
- Contracts are implicit; breaking changes happen without
  versioning
- Observability is "turned on" but nobody looks at it
- ADRs exist but nobody reads them
- The disciplines feel like bureaucracy rather than help

If these show up, pause and figure out why before adding more
process. Usually the problem is that one of the disciplines was
skipped or under-invested in early, and the absence is now making
the others less effective.
