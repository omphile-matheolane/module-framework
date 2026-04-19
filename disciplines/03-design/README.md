# 3. Design Contracts and Collaboration

## What this discipline involves

The Design discipline is concerned with how modules interact. It
involves defining explicit contracts between modules, choosing
appropriate communication patterns, and ensuring that those contracts
are documented, versioned, and maintained over time.

A contract specifies what a module offers and what it expects in
return. It covers the operations available, the data structures
exchanged, the error conditions that may arise, and any behavioural
guarantees such as idempotency, ordering, or latency bounds. Depending
on the communication pattern and the technology context, contracts
might be expressed through API specifications, schema definitions,
interface declarations, or event catalogs.

Where [Organise](../02-organise/) is concerned with the structural
public surface of a module, Design is concerned with the semantic
agreement that surface carries. The two come together in practice, and
keeping the ideas distinct helps because they evolve under different
pressures. The structural surface evolves when the team restructures
the module. The contract evolves when consumers need different
behaviour.

---

## Why it matters

Explicit contracts are what make independent development practical.
When module A depends on module B, the contract defines exactly what A
can rely on. As long as B fulfils that contract, A has no need to
understand or care about how B is implemented internally. Teams can
work in parallel, each responsible for their side of the agreement.

Contracts also give teams a principled way to manage change. When a
module needs to evolve, the contract defines what must be preserved
for existing consumers to keep working. Changes that stay within the
contract can proceed without coordination. Changes that modify the
contract require it. By making contracts explicit, teams can draw a
clear line between internal changes and breaking ones, which is a
distinction that matters a great deal in practice.

Without explicit contracts, integration tends to be fragile in ways
that are hard to diagnose. Teams form assumptions about how other
modules behave. Those assumptions may hold today and be quietly
violated tomorrow when something changes upstream. Because the
expectations were never documented, the resulting breakages can be
difficult to trace back to their cause.

---

## Communication patterns

Contracts exist for different types of communication, and the choice
of pattern has real implications for how modules are coupled.

### Synchronous request-response

Module A sends a request to module B and waits for a response before
proceeding. This pattern is appropriate when an immediate answer is
genuinely required. It is straightforward to reason about, but it
creates temporal coupling: if B is slow or unavailable, A is directly
affected.

Use it when the calling operation genuinely cannot proceed without the
answer. A checkout flow that needs to authorise a payment before
confirming the order is a reasonable candidate for synchronous calls.
A notification system that wants to record that a user completed an
action is not.

### Asynchronous messaging

Module A sends a message that module B processes independently,
without A waiting for a response. This reduces temporal coupling but
introduces its own complexity around eventual consistency, error
handling, and message ordering.

Use it when the calling operation can complete without waiting for
downstream work, or when the downstream work might take long enough
that synchronous coupling would become a reliability problem.

### Event-driven communication

Module A publishes an event signalling that something has happened,
and any number of consumers may react to it independently. A has no
knowledge of what consumers exist or what they do with the event. This
pattern provides a high degree of loose coupling, but it can make the
overall system harder to follow because the flow of control is
implicit.

Use it when the producer should not know or care who consumes the
information, or when multiple consumers need to react to the same
occurrence. Order placed events often fit this pattern: the order
service publishes the event, and inventory, fulfilment, and analytics
all react independently.

---

## How to practice it

### Document contracts explicitly

For significant interfaces, maintain contract documentation in a
durable form. OpenAPI specifications for REST APIs, AsyncAPI
specifications for event-driven interfaces, Protocol Buffer or Avro
schemas for structured data exchange. This documentation should be
versioned alongside the code so that it stays in sync with what is
actually deployed.

A [contract catalog template](../../adoption/templates/contract-catalog-template.md)
captures the structure that tends to work well: a list of every
significant interface, its current version, its owner, its consumers,
and a link to its formal specification.

### Version contracts deliberately

When a contract needs to change, introduce a new version rather than
modifying the existing one. Breaking changes should flow through
versioning, not through silent modification. Deprecate old versions
with clear timelines rather than removing them abruptly and leaving
consumers to discover the gap.

A useful pattern: for REST APIs, version in the URL path (`/v1/`,
`/v2/`) or in a header. For events, include a schema version in the
event envelope. For RPC interfaces, use namespaced service definitions
(`PaymentServiceV2`). Whatever mechanism you choose, apply it
consistently.

### Test contracts

Consumer-driven contract testing gives teams a way to verify that
producers are actually fulfilling the contracts their consumers depend
on. Pact is the most widely used tool for this and has bindings across
most major ecosystems (Java, .NET, Go, Python, JavaScript, Ruby).
Spring Cloud Contract is a common alternative in the Spring ecosystem
specifically. Either approach catches breaking changes before they
reach production, where the cost of discovery is considerably higher.

### Choose patterns based on requirements

Do not default to one pattern across the board. The relevant questions
are practical ones: does the caller need an immediate response, can
the interaction tolerate latency, what happens if the callee is
unavailable? Let the answers shape the pattern choice rather than
convenience or familiarity.

---

## Evolving a contract: a worked example

To make the distinction between Organise and Design more concrete,
consider how a contract might evolve over time. A payments module
originally exposes a single authorise operation that takes an amount
and a card token, and returns an authorisation identifier.

```java
public interface PaymentProcessor {
    AuthResult authorise(Money amount, CardToken cardToken);
}
```

Six months later, the business adds support for stored customer
profiles, and callers want to authorise against a saved profile rather
than passing a card token each time. The first instinct might be to
add a second operation to the interface:

```java
public interface PaymentProcessor {
    AuthResult authorise(Money amount, CardToken cardToken);
    AuthResult authorise(Money amount, CustomerId customerId);
}
```

In code-surface terms this is a purely additive change. No existing
consumer is affected, and the [Organise](../02-organise/) work is
trivial: the operation is added to the public surface and implemented
behind it. In contract terms, though, the change is more significant
than it looks. The interface now has two authorise operations with
overlapping purpose, and consumers need to understand which one to
call in which situation. The contract has grown in ways that will be
harder to undo than to introduce.

A contract-first approach would ask different questions. Is this
actually a new operation, or is it a variation of the existing one?
Should the authorisation source be part of the request rather than
part of the operation signature? What happens when a third
authorisation source is added next year? Thinking through these
questions produces a different design:

```java
public interface PaymentProcessor {
    AuthResult authorise(AuthRequest request);
}

public sealed interface AuthRequest {
    record WithCard(Money amount, CardToken cardToken) implements AuthRequest {}
    record WithProfile(Money amount, CustomerId customerId) implements AuthRequest {}
}
```

This shape is a breaking change relative to the original, so it cannot
simply replace the old interface. The realistic path is to introduce
it as version 2 of the contract while continuing to support version 1
for existing consumers, deprecate version 1 with a clear timeline, and
retire it once consumers have migrated.

The Organise work here is modest. The Design work is significant, and
most of it happens before any code is written.

The point is not that one approach is always right. There are
situations where adding a second operation is genuinely the correct
call, particularly when the two operations are meaningfully distinct
and the interface is unlikely to grow further. The point is that
these are different questions from the ones Organise asks, and
answering them well requires thinking about the contract as a separate
concern from the code surface that carries it.

---

## Greenfield versus brownfield

### Greenfield

Contracts can be designed ahead of implementation. The team defines
the interfaces between modules as part of the initial architecture,
and the implementations are built to satisfy those interfaces from
the start. This interface-first approach tends to produce cleaner
boundaries than inferring contracts from implementations after the
fact.

### Brownfield

Contracts are often implicit or inconsistent. They exist in the code
and in the runtime behaviour of the system, but they have never been
written down in any formal sense. The work here involves discovering
what contracts actually exist, formalising them through documentation
and testing, and gradually improving consistency across the system.

A reasonable starting point is the interfaces that break most often.
Those are the ones where explicit contracts would provide the most
immediate value, and they are usually the ones where consumers will
appreciate the formalisation most.

---

## Failure modes and anti-patterns

### Implicit contracts

When contracts exist only in the code, without documentation or tests,
they are easily violated. A change that looks safe to the team making
it turns out to break consumers in ways nobody anticipated, and there
is nothing in place to catch it early. The remedy is consumer-driven
contract testing and documented specifications.

### Chatty interactions

When modules make many small calls to each other rather than fewer
coarser-grained ones, performance degrades and coupling increases.
Across network boundaries this becomes particularly costly. The remedy
is to design operations at a meaningful granularity from the
consumer's perspective, not to mirror internal method calls.

### Wrong pattern for the interaction

Synchronous calls for interactions that would be better handled
asynchronously introduce unnecessary fragility. Events for
interactions that actually require immediate consistency introduce
complexity that the use case does not warrant. Pattern choice should
follow requirements rather than habit.

A common misuse: publishing an event and then immediately querying
the consumer for the result. That is a synchronous call wearing the
costume of an event. If the caller needs the answer now, a synchronous
call is more honest.

### Contract sprawl

Without some governance, contracts multiply inconsistently, with
different styles, different tooling, and varying levels of
documentation. At a certain point, some consolidation and
standardisation becomes necessary just to keep the overall picture
navigable.

---

## Connections to other disciplines

Design depends on [Map](../01-map/) to establish what modules exist
and how they relate to one another. The public surfaces defined
through [Organise](../02-organise/) form the structural foundation
that contracts in Design are built on.

Contract stability is one of the signals that [Evaluate](../06-evaluate/)
uses when assessing how components should evolve. A component with an
unstable contract is not a good candidate for extraction.

Governance of contracts is automated through [Unify](../04-unify/).
Schema validation, consumer-driven contract tests, and version
compatibility checks all belong in the governance infrastructure.

The runtime behaviour of contract interactions, how they actually
perform and fail under real conditions, is what [Leverage](../05-leverage/)
makes visible. A contract may be well-defined on paper and still prove
fragile under load.

---

## Further reading

Sam Newman's *Building Microservices* and *Monolith to Microservices*
cover contract design for service-oriented systems in considerable
depth, including the mechanics of versioning, backwards compatibility,
and consumer-driven contracts. Both are essential reading for teams
working across service boundaries.

Chris Richardson's *Microservices Patterns* covers the patterns that
recur in contract design across distributed systems, with particular
attention to the operational concerns that emerge at service
boundaries.

For the specific mechanics of contract testing, the Pact documentation
is the most thorough and the least polemical source. The underlying
concepts apply whether you use Pact specifically or an equivalent tool
in your ecosystem.
