# 4. Unify Ownership and Governance

## What this discipline involves

The Unify discipline is concerned with two things that need to work
together: clear ownership and automated enforcement of architectural
rules. Ownership means that every module has a designated team
responsible for its integrity and evolution. Governance means that
architectural rules are encoded as automated checks running
continuously, rather than existing as guidelines that people try to
remember to follow.

These concerns are bundled because they depend on each other in
practice. Governance without ownership has no team to enforce against
when a rule needs judgement. Ownership without governance has no
mechanism to act through beyond informal persuasion.

---

## Why it matters

Ownership creates accountability in a concrete and practical sense.
When a module has a clear owner, there is a team to make decisions
about its direction, to review changes to its interface, and to
respond when something goes wrong. Without that, modules tend to
become communally maintained by whoever needs them at a given moment,
with no consistent vision and no long-term stewardship. The quality
of those modules reflects exactly that situation over time.

Automated governance is what makes architectural integrity sustainable
at scale. Rules enforced only through code review cannot hold as teams
grow and pressure increases. Reviewers get busy, violations slip
through, and the violations accumulate quietly until the architecture
has degraded in ways that are expensive to reverse. Automated
governance catches violations immediately, regardless of workload or
the attention available at any given moment.

---

## How to practice it

### Assign ownership explicitly

Every module should have a designated owning team in an ownership
registry, and that registry should be the source of truth when
questions or disputes arise about who is responsible for a given
module.

The [ownership registry template](../../adoption/templates/ownership-registry-template.md)
provides a starting structure. At minimum it should list the module,
the owning team, a primary contact, and the date ownership was last
confirmed. Ownership that has not been confirmed in a year is usually
stale.

### Make ownership responsibilities concrete

Owning a module means:

- Maintaining its integrity, including internal quality and adherence to the public surface
- Reviewing changes to its public interface
- Managing its evolution over time, including contract versioning and deprecation
- Communicating with consumers about upcoming changes that might affect them
- Responding to incidents involving the module

These responsibilities need to be stated clearly, not left to
inference. Teams that do not know what ownership means tend to exercise
it inconsistently.

### Automate boundary enforcement

Most modern ecosystems have at least one mature architectural testing
tool:

- Java: ArchUnit
- .NET: NetArchTest or ArchUnitNET
- Python: import-linter
- TypeScript: dependency-cruiser or ts-arch
- Go: custom linters built around the internal package mechanism

These tools let teams define rules such as classes in one module not
being permitted to reach into the internals of another, and those
rules are verified on every build. The goal is to make boundary
violations visible immediately rather than letting them accumulate
undetected.

A starter set of rules that tends to be useful across contexts:

1. No module depends on the internals of another module
2. No cyclic dependencies between modules
3. Modules depend only on modules they explicitly declare
4. Shared types live in a designated shared module and nowhere else

The [starter architecture tests](../../adoption/templates/starter-arch-tests.md)
provide concrete implementations in ArchUnit that can be adapted to
other ecosystems.

### Integrate checks into CI

Governance checks should run on every pull request and block merges
when violations are detected. This ensures violations surface while
the developer who introduced them still has the context to fix them
quickly, rather than weeks later when the change has been forgotten.

A failed architecture test is not a suggestion. It is a build failure,
and it should be treated with the same seriousness as any other build
failure.

### Record significant architectural decisions

When choices are made about module structure, communication patterns,
or the direction of a module's evolution, capture the context, the
decision, and the reasoning behind it in an Architecture Decision
Record. This provides institutional memory that survives team
turnover.

The [ADR template](../../adoption/templates/adr-template.md) follows
the Michael Nygard format, which has become the de facto standard.
ADRs should be short, concrete, and focused on the decision rather
than the broader context.

Good ADRs answer three questions: What was the situation? What
alternatives were considered? Why was this option chosen? Without
those elements, an ADR tends to be a historical curiosity rather than
useful documentation.

---

## Governance without bureaucracy

A reasonable concern about governance is that it will slow teams down,
and heavy approval processes and review gates certainly can become
bottlenecks. The way to avoid that is to shift structural governance
away from human processes and toward automated checks.

Automated checks run in seconds rather than days. They do not depend
on someone being available to review. They catch violations when the
fix is straightforward rather than after the code has been merged and
the context has been lost. They also enforce rules consistently,
without the variability that comes with human attention spread across
a large codebase.

This does not make code review unnecessary. Human review remains
valuable for the things automated tools cannot assess: design quality,
domain correctness, maintainability concerns, and the kinds of
judgement calls that require understanding the broader context.
Structural governance, the enforcement of module boundaries and
dependency rules, should be handled by tooling. That frees reviewers
to focus on the problems that actually require a human to look at
them.

---

## Greenfield versus brownfield

### Greenfield

Governance can be put in place from early on. Boundary rules are
defined as modules are created, and the codebase grows within those
constraints rather than having them retrofitted later. The first
architecture test typically runs before the first feature is
delivered.

### Brownfield

Introducing governance typically surfaces existing violations
immediately. The first run of architecture tests may produce a
significant number of failures, which can feel discouraging.

The practical path forward is to prioritise which violations matter
most, suppress the ones that will be addressed later rather than
ignoring them, and gradually work down the suppression list as the
codebase is cleaned up. Most architectural testing tools support
suppression mechanisms for exactly this purpose. The baseline shrinks
over time as the team pays down the debt, and the list of suppressed
violations is itself a useful artefact: it makes the accumulated debt
visible.

This is work measured in quarters rather than sprints, but it is the
kind of work that makes long-term maintainability possible.

---

## Failure modes and anti-patterns

### Documented but unenacted ownership

An ownership registry that nobody consults when decisions are being
made provides little real value. Ownership needs to be operationalised
through code review assignments, clear escalation paths, and explicit
decision rights, otherwise it exists only on paper.

A useful test: when a question comes up about a module, does anyone
actually check the registry? If not, the registry is not functioning
as a source of truth, and the first task is to make consulting it a
habit.

### Rigid governance

Rules that allow no exceptions tend to generate frustration and
workarounds rather than compliance. Some flexibility is necessary, but
exceptions should be explicit, documented, and revisited periodically
rather than quietly accumulated.

The baseline mechanism most architectural testing tools provide is a
reasonable way to handle this. Known exceptions are listed explicitly,
and each exception has a documented reason and, ideally, an expected
resolution date.

### Manual enforcement

Governance that relies on manual enforcement rather than automation
will not hold at scale. This is probably the most reliable failure
mode of all: the rules exist, people broadly agree with them, but
enforcement depends on human attention that is always in short
supply.

### Write-once ADRs

Decision records that are written once and never referenced again
drift into being historical curiosities rather than useful
documentation. ADRs are most valuable when they are consulted when
related changes are being considered, which requires a habit of
actually going back to them.

A practice that tends to help: when a pull request touches an area
covered by an ADR, the PR description should link to the relevant
ADR. This keeps the record alive in ongoing work.

---

## Connections to other disciplines

Unify depends on [Map](../01-map/) to establish what modules exist and
who owns them. You cannot enforce boundaries that are not defined, and
you cannot assign ownership to modules that do not exist as distinct
things.

The boundaries Unify enforces are the boundaries that
[Organise](../02-organise/) defines. The architecture tests are what
make the intended structural separation stick over time.

Contract governance connects directly to [Design](../03-design/).
Schema compatibility checks, contract tests, and version deprecation
tracking are all governance concerns even when they apply to
contracts rather than structural boundaries.

Observability data from [Leverage](../05-leverage/) can surface
governance violations that occur at runtime rather than at compile
time. Reflection, dynamic proxies, and similar mechanisms can bypass
build-time checks, and only runtime observation catches them.

The governance constraints themselves may need to be adjusted as
evolution decisions from [Evaluate](../06-evaluate/) change the shape
of the system. When a module is extracted or consolidated, the rules
that govern it typically need to change as well.

---

## Further reading

*Building Evolutionary Architectures* by Neal Ford, Rebecca Parsons,
and Patrick Kua is the foundational text on fitness functions and
automated architectural governance. It goes considerably deeper on
governance mechanics than MODULE needs to, and is essential reading
for teams implementing Unify in any depth.

The ArchUnit user guide is a pragmatic starting point for teams in
the Java ecosystem. The examples repository is often more useful than
the prose documentation.

For ADRs specifically, Michael Nygard's original blog post on
"Documenting Architecture Decisions" is still the best short
introduction. Nat Pryce and others have written useful expansions on
the practice.
