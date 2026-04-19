# 1. Map Domain and Team Boundaries

## What this discipline involves

The Map discipline is concerned with understanding where natural seams
exist within a system and ensuring that technical boundaries align with
those seams. In practice this means identifying business capabilities,
defining domain boundaries, establishing team ownership, and
maintaining explicit documentation of how these three dimensions relate
to one another.

A domain map captures the current understanding of how the system is
divided. It shows which modules exist, what business capabilities each
module supports, which team owns each module, and what dependencies
exist between modules. It is not an artefact produced once during
initial design and then filed away. It is a working document that
evolves as understanding deepens and as the business itself changes.

---

## Why it matters

When technical boundaries align with domain boundaries, changes tend to
stay localised. A change to how payments are processed affects the
payments module. A change to how notifications are sent affects the
notifications module. Teams can work with a reasonable degree of
independence because their domains are genuinely distinct.

When technical boundaries cut across domain boundaries, changes become
distributed in ways that are hard to manage. A feature that should
require modifications to one module instead touches three, and the
teams responsible for those modules must coordinate even though their
actual domains have nothing entangled about them. That coordination
overhead compounds over time.

The alignment between team boundaries and domain boundaries matters for
similar reasons. When a single team owns a coherent domain, that team
carries the full context and can make decisions without external input
at every turn. When ownership is fragmented across teams with different
priorities and partial understanding, even straightforward decisions
require negotiation.

---

## How to practice it

### Run collaborative discovery sessions

Bring engineers, product managers, and domain experts together to map
the business capabilities of the system. EventStorming, domain
storytelling, and capability mapping are all effective techniques. The
goal is shared understanding across disciplines, not a formally perfect
model.

A reasonable first session might look like this. Gather the group for
two to three hours with a large wall or a shared digital canvas. Work
backwards from the business outcomes the system supports. List the
domain events that happen in the course of producing those outcomes.
Group the events into coherent clusters. Those clusters are the
starting point for candidate modules.

Nothing produced in this session is final. It is a shared hypothesis
that will be refined as the team encounters the real complexity of
each area.

### Document the current state honestly

The domain map should reflect reality rather than aspiration. If
boundaries are unclear, mark them as unclear. If ownership is
contested, mark it as contested. A map that makes the current state
look tidier than it actually is hides problems rather than surfacing
them, which defeats most of the purpose.

A useful template for the map is included at
[`adoption/templates/domain-map-template.md`](../../adoption/templates/domain-map-template.md).
It is deliberately spare, because elaborate templates tend to
discourage honest updates.

### Include dependencies explicitly

For each module, document what it depends on and what depends on it.
Distinguish between synchronous dependencies, where one module calls
another and waits for a response, and asynchronous dependencies, where
a module publishes an event that another consumes. The dependency
structure often reveals boundary problems that are not obvious from
examining modules individually.

A module that has synchronous dependencies on four other modules is
rarely healthy. A module that publishes events consumed by a dozen
others is not necessarily unhealthy, but it is worth understanding why.

### Align ownership with boundaries

Each module should have a single owning team. If a module has grown too
large for a single team to own coherently, that may be a signal it
should be split. If a module is too small to justify dedicated
ownership, consolidation may be worth considering.

Ownership is not the same as exclusive access. Other teams may
contribute to a module, but the owning team carries responsibility for
its integrity, its interface, and its evolution. The ownership registry
at [`adoption/templates/ownership-registry-template.md`](../../adoption/templates/ownership-registry-template.md)
captures this relationship explicitly.

### Review the map periodically

A quarterly cadence works well for most teams. Review the map whenever
significant changes occur: team reorganisations, new business
capabilities, or major architectural decisions. Each review should ask
three questions. Do the boundaries still make sense? Does the
dependency structure look healthy? Are there emerging hotspots that
warrant attention?

The [Map review checklist](../../adoption/checklists/map-review-checklist.md)
provides a structured starting point for these sessions.

---

## Greenfield versus brownfield

### Greenfield

Map receives attention early, often before much code has been written.
The team develops a hypothesis about how the domain should be divided,
and that hypothesis guides the initial structure of the codebase. Treat
those initial boundaries as hypotheses rather than commitments. The
team will learn a great deal as they build, and the boundaries should
shift to reflect that learning.

A common failure in greenfield projects is treating the first domain
map as a commitment. Teams that lock in their boundaries before they
understand the domain often spend considerable effort later undoing
decisions they should have been free to revise.

### Brownfield

Map begins with discovery rather than design. Examine the existing
system to understand how it is actually structured, which may differ
considerably from how it was originally intended to be structured. The
domain map documents this reality, revealing where boundaries are
reasonably clear, where they have drifted, and where they have
effectively disappeared.

This assessment is the foundation for improvement, not a source of
embarrassment. Every sufficiently old system looks worse than its
original design intended. The map is what makes the gap visible so it
can be worked on.

The [brownfield adoption playbook](../../adoption/playbooks/brownfield-adoption.md)
covers the discovery process in more detail.

---

## Failure modes and anti-patterns

### The one-time map

A domain map produced during initial design and never revisited
provides little ongoing value. The map needs to evolve alongside the
system. Signs that this is happening: the map has not been updated in
six months, key details no longer match what is in production, or new
team members consult the map and then ignore it because it does not
reflect what they see in the code.

The remedy is to integrate map updates into routine work. When a
module is created, split, or merged, updating the map is part of the
change rather than an optional follow-up.

### Aspirational mapping

A map that shows the desired architecture rather than the actual
architecture conceals problems rather than making them visible. This
tends to happen when the map is treated as a document that needs to
look good, rather than as an honest assessment.

The remedy is to write the map for your team rather than for an
external audience. If the current state is messy, the map should show
that it is messy. Cleaning up the reality is a separate project from
documenting it.

### Ignoring team boundaries

A domain map that only addresses technical structure without
considering team ownership misses a critical dimension. Conway's Law
means that team structure and system structure will influence each
other whether or not that relationship is made explicit. The map
should surface it.

### Over-detailed mapping

At the other end, some teams invest excessive effort in producing maps
that are far more detailed than is actually useful for decision-making.
The map should be detailed enough to guide real decisions, but not so
detailed that maintaining it becomes a burden in its own right.

A useful test: if the map is too painful to update, it is too
detailed. Strip it back until updates are easy enough to happen.

---

## Connections to other disciplines

Map establishes the modules that every other discipline operates on.

The boundaries identified through Map are enforced through
[Unify](../04-unify/). Governance rules need something to govern, and
the map is where those somethings are defined.

The contracts between modules identified in Map are formalised through
[Design](../03-design/). The dependency structure in the map tells you
where contracts need to exist.

Observability data from [Leverage](../05-leverage/) provides the
operational signal needed to assess whether the boundaries in the map
are working as intended. Runtime behaviour sometimes reveals coupling
that the map did not capture.

Evolution decisions from [Evaluate](../06-evaluate/) may trigger
boundary changes that require the map to be updated. This is one of
the more concrete ways the disciplines stay connected in practice.

---

## Further reading

For the organisational side of domain mapping, *Team Topologies* by
Matthew Skelton and Manuel Pais is the essential reference. It provides
the vocabulary for discussing team types and interaction modes, and it
treats cognitive load as a first-class constraint on what boundaries
are practical.

For the domain-modelling side, Eric Evans's *Domain-Driven Design* and
Vaughn Vernon's *Implementing Domain-Driven Design* remain the
foundational sources on bounded contexts and context mapping. MODULE's
Map discipline draws on their patterns and treats them as continuous
rather than upfront activities.

For practical techniques, Alberto Brandolini's material on
EventStorming and Nick Tune's writing on domain modelling are both
worth time.
