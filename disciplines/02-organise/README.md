# 2. Organise Module and Service Internals

## What this discipline involves

The Organise discipline focuses on the internal structure of individual
modules and on the seam between a module's internals and the rest of
the system. Each module should be organised with a clear public
surface and encapsulated internals. Implementation details should be
hidden behind that surface, so that consumers depend only on what the
module exposes and not on how the module happens to be built.

This discipline applies whether the module is a package within a
monolith, a bounded context within a modular monolith, or an
independently deployed service. The scale differs, but the principle
remains constant: separate what is public from what is private, and
enforce that separation.

---

## The boundary between Organise and Design

Organise and Design both concern themselves with module interfaces,
and the line between them is worth drawing carefully because the two
disciplines do different work.

Organise is concerned with the public surface of a module as it exists
in code. It is a structural fact about the codebase: the set of
classes, functions, or endpoints that are reachable from outside the
module. Whether those elements are genuinely hidden from the rest of
the system is enforced by the language, the build, and the governance
tooling. The work in Organise is to make sure the surface reflects
deliberate choice rather than whatever happened to be visible.

Design is concerned with the contract that consumers depend on when
they interact with that surface. It specifies the operations available,
the data structures exchanged, the error conditions that may arise, and
the behavioural guarantees the module offers. A module can have a
well-defined public surface in code and still have an informal,
undocumented, easily-violated contract in practice.

Consider a payments module that exposes a single PaymentProcessor
class. The Organise work is making sure PaymentProcessor is the only
class visible from outside the module, that the rest of the package is
package-private, that no consumer can reach around PaymentProcessor to
touch an internal class, and that this constraint is verified by
automated tests. The [Design work](../03-design/) is specifying what
PaymentProcessor promises, how those promises evolve, and how
consumers rely on them.

Both disciplines touch the same class, but they concern themselves
with different properties of it.

---

## Why encapsulation matters

Encapsulation is what allows a module to evolve independently. When
consumers depend only on the public surface, the internals can change
without affecting anyone outside the module. The owning team can
refactor, optimise, or restructure the implementation without needing
to coordinate with other teams.

Without encapsulation, changes propagate in ways that are difficult to
anticipate. A developer modifies an internal class, not knowing that
another team has taken a dependency on it. The modification breaks
the other team's code, but the breakage may not surface until
integration, or until production. The debugging that follows erodes
trust and tends to push teams toward defensive practices that slow
everyone down over time.

---

## How to practice it

### Define a public surface explicitly

Each module should have a clear entry point, whether that is a facade,
an API layer, a set of use case handlers, or a defined collection of
public classes. Everything behind that entry point is implementation
detail.

A practical pattern for Java modules is to place the public surface in
a single package at the root of the module and keep all implementation
packages one level below.

```
com.example.payments              (public: PaymentProcessor, domain events)
com.example.payments.domain       (package-private)
com.example.payments.persistence  (package-private)
com.example.payments.integration  (package-private)
```

### Hide internals structurally

Use the features the language and platform provide to enforce
encapsulation. Guidelines that rely on discipline alone tend to erode
during busy periods when code review attention is stretched. The goal
is to make violations detectable rather than merely discouraged.

### Keep the public surface small

The smaller the surface, the less there is to maintain and the less
area consumers have to take dependencies on. Resist the temptation to
expose internal capabilities on the grounds that someone might need
them eventually. If a capability is not intended to be part of the
contract, it should not be visible.

### Match internal architecture to internal complexity

Modules with straightforward logic often do not need elaborate internal
structure. Modules with rich business rules may benefit from something
like hexagonal architecture or vertical slices. The internal structure
can and should vary across modules depending on what each one is
doing. What matters is that the choice is deliberate and that the
public surface remains stable regardless of how the internals are
organised.

---

## Enforcement across ecosystems

The mechanics of enforcing encapsulation vary significantly across
languages and platforms, but the underlying pattern is consistent. A
module declares which elements are part of its public surface, the
language or tooling restricts access to anything else, and automated
checks verify the restriction at build time. What differs is which
mechanisms each ecosystem provides and how strong the enforcement is
by default.

### Java

Package-private visibility is the starting point. A class without an
access modifier is reachable only within its own package, which
provides a natural boundary for module internals when packages are
organised along module lines.

For stronger enforcement, the Java Platform Module System lets a
module explicitly declare which packages it exports. Consumers can
only access exported packages regardless of the modifiers within them.
JPMS is powerful but carries real complexity around module path
configuration, and many mature Java codebases have chosen not to adopt
it.

ArchUnit is the pragmatic middle ground. It is a testing library that
verifies architectural rules by analysing compiled bytecode, running
as part of the normal test suite. A typical rule looks like this:

```java
@ArchTest
static final ArchRule payments_internals_are_not_accessed_externally =
    noClasses()
        .that().resideOutsideOfPackage("com.example.payments..")
        .should().dependOnClassesThat()
        .resideInAnyPackage(
            "com.example.payments.domain..",
            "com.example.payments.persistence..",
            "com.example.payments.integration..");
```

Systems built on Spring need an additional consideration. Component
scanning will by default pick up any class annotated with @Component,
@Service, or similar stereotypes, regardless of package visibility
intentions. An internal class annotated as a bean becomes effectively
accessible through autowiring. The practical approach is to scope
component scanning to the public packages of each module, use explicit
@Configuration classes to wire internal beans rather than relying on
blanket scanning, and treat @ComponentScan boundaries as part of the
module definition.

### .NET

The `internal` access modifier plays a similar role to Java's
package-private, with `InternalsVisibleTo` providing controlled
exceptions for test assemblies. NetArchTest and ArchUnitNET offer
equivalent architectural testing capabilities to ArchUnit.

### Go

The `internal` directory convention is built into the language:
packages placed under an `internal` path can only be imported from
within a specific subtree, which provides a strong and well-understood
enforcement mechanism. Custom linters handle the rules that the
language itself does not cover.

### Python

Enforcement leans more heavily on convention and tooling than on
language features. Leading underscores signal intent but do not prevent
access. Tools like `import-linter` are typically used to assert
layering rules in CI.

### TypeScript and Node.js

Package `exports` fields and workspace-level tools like
`dependency-cruiser` or `ts-arch` fill the role that ArchUnit fills in
the Java world, though the enforcement tends to be weaker at the
language level than in statically-typed JVM or CLR ecosystems.

---

## Greenfield versus brownfield

### Greenfield

Encapsulation can be established from the start. The team defines
module boundaries and public surfaces as part of the initial design,
and the codebase grows within those constraints from the beginning.
Architectural rules are added as modules are added, and the build
fails on the first violation rather than accumulating debt.

### Brownfield

The situation is usually messier. Internal classes are accessed from
across the codebase, dependencies run in multiple directions, and
there is no clear boundary between what is intended to be public and
what simply ended up being visible.

Introducing encapsulation in this context means progressive
refactoring: identifying what genuinely belongs in the public surface,
redirecting existing dependencies to go through that surface, and
gradually restricting access to internals one module at a time.

A baseline pattern is useful here. Existing violations are captured as
a known-debt list, new violations fail the build, and the baseline
shrinks over time as the team pays down the debt. Most architectural
testing tools across ecosystems support something along these lines.
This work tends to be done incrementally, prioritised based on where
the lack of encapsulation is causing the most friction.

---

## Failure modes and anti-patterns

### A public surface that is too large

When everything is exposed, nothing is really encapsulated. The surface
should reflect what consumers actually need, not what is convenient
for the implementer to make visible. A useful exercise: list every
public class or function in the module, then justify why each one
needs to be public. The ones that cannot be justified should be
hidden.

### Encapsulation that is documented but not enforced

Guidelines that say not to depend on internal classes are largely
ineffective without something in the build or tooling that catches
violations. They accumulate quietly during periods when nobody has
time to police them carefully. The remedy is to express the
architectural rules as tests that fail the build when violated.

### Overly complex internal structure

Some teams impose elaborate architectural patterns on modules that
simply do not need them. Hexagonal architecture for a module that does
nothing except validate input and forward it to a queue is overhead
without benefit. Internal architecture should be proportionate to
internal complexity.

### Leaking implementation details through the surface

A public surface that exposes internal data structures, or that
requires consumers to understand how the implementation works in order
to use it correctly, is not really a boundary at all. The surface
should be expressed in domain terms that make sense to the people
consuming it.

Common leaks: returning ORM entity objects across the boundary,
exposing database identifiers that consumers end up storing, surfacing
transaction or session objects. Each of these forces consumers to
understand implementation details they should not need.

---

## Connections to other disciplines

Organise depends on [Map](../01-map/) to define what the modules
actually are. You cannot organise the internals of a module whose
boundaries are still unclear.

The public surfaces established through Organise become the code-level
foundation for the contracts that [Design](../03-design/) formalises.
The two disciplines touch the same artefacts from different angles.

Enforcement of encapsulation is automated through
[Unify](../04-unify/). The architecture tests that keep modules
encapsulated are part of the governance infrastructure.

Observability data from [Leverage](../05-leverage/) can surface
situations where encapsulation is being circumvented at runtime
through reflection, dynamic proxies, or other mechanisms that bypass
the structural constraints the team has put in place.

---

## Further reading

For Java specifically, ArchUnit's own documentation is the best
starting point for architectural testing. The examples in the ArchUnit
repository are more comprehensive than most introductions to the
library.

*Clean Architecture* by Robert C. Martin is worth reading for its
treatment of layering, though the specific patterns should be adapted
rather than applied verbatim. The underlying principle that business
logic should not depend on infrastructure is broadly applicable; the
specific arrangement of layers tends to need adjustment to local
context.

Vaughn Vernon's *Implementing Domain-Driven Design* covers internal
module structure for domain-heavy modules in considerable depth.
