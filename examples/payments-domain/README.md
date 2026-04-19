# Worked Example: Payments Domain

This folder contains a worked example showing all six MODULE
disciplines applied to a single domain: payment processing at a
mid-sized ecommerce company. The example is fictional but
realistic. The decisions, artefacts, and implementation choices are
the kind a real team would produce applying MODULE seriously over
time.

The example is meant to be read end to end the first time through.
Once you are familiar with its shape, individual artefacts can be
consulted as reference when you are producing the equivalent for
your own system.

---

## The fictional context

**Company**: An ecommerce marketplace serving business buyers in
South Africa and the wider SADC region. Roughly 200 engineers
organised into 25 teams. The core platform is a Java modular
monolith with a small number of independent services around it.

**Payments domain**: The portion of the system responsible for
authorising, capturing, and refunding payments, reconciling with
external payment providers, and producing financial records. It
has been in production for three years.

**Owning team**: The Payments Team, five engineers, reporting to
the Commerce Platform group.

**Current state at the time of this example**: The payments
functionality has been modularised within the monolith for roughly
18 months. An evolution assessment has been run and concluded that
extraction is warranted. The example captures the artefacts that
MODULE produces around this situation.

---

## What is in this folder

| File | Discipline | What it demonstrates |
|------|-----------|----------------------|
| [`docs/01-domain-map.md`](docs/01-domain-map.md) | Map | The payments portion of a domain map |
| [`docs/02-module-structure.md`](docs/02-module-structure.md) | Organise | Internal structure of the payments module |
| [`docs/03-contract-catalog.md`](docs/03-contract-catalog.md) | Design | The payments public contracts |
| [`docs/04-governance.md`](docs/04-governance.md) | Unify | Ownership, architecture tests, and ADRs |
| [`docs/05-observability.md`](docs/05-observability.md) | Leverage | How observability is structured for payments |
| [`docs/06-evolution-assessment.md`](docs/06-evolution-assessment.md) | Evaluate | The completed assessment recommending extraction |
| [`docs/07-adr-extract.md`](docs/07-adr-extract.md) | Cross-cutting | The ADR recording the extraction decision |
| [`docs/08-extraction-plan.md`](docs/08-extraction-plan.md) | Cross-cutting | The implementation plan derived from the ADR |

---

## How to read this

The artefacts are numbered in the order the disciplines would
typically be touched for a component of this maturity. In practice,
all six disciplines run continuously, and the artefacts evolve
together rather than being produced sequentially.

The example shows the state of each artefact at a single point in
time: the moment when the extraction decision has just been made
and the implementation is about to begin. Earlier versions of each
artefact (from the greenfield phase, the initial modularisation,
the hardening work that preceded extraction) are not included to
keep the example manageable.

---

## What the example illustrates

**Consistency across artefacts**. The domain map, contract catalog,
ownership registry, architecture tests, and evolution assessment
all refer to the same modules in consistent terms. When MODULE is
practiced properly, the artefacts reinforce each other.

**Honest current state**. The domain map records weaknesses (like
the one remaining read dependency) rather than hiding them. The
evolution assessment cites concrete evidence for every signal
rating. The extraction ADR acknowledges the rollback criteria.

**Evidence-based evolution**. The extraction decision is grounded
in specific signals and specific drivers. Without that grounding,
the decision would be difficult to defend and easy to second-guess.

**Continuity of practice**. The artefacts reference earlier
decisions (the 2025 modularisation, the Q3 2025 hardening) and
plan for future reassessment. MODULE is a continuous practice, and
the example shows what that continuity looks like.

---

## What the example does not cover

**Every possible artefact**. A real system of this scale would have
many more ADRs, a longer contract catalog, and a broader
ownership registry. The example includes enough to show the shape
without being exhaustive.

**Implementation code**. The example is about the MODULE artefacts,
not the payments implementation itself. Code samples appear where
they illustrate a specific point (architecture tests, contract
interfaces), but the payments code is not reproduced in full.

**Every discipline's possible output**. Each discipline produces
more artefacts than what is shown here. The example picks the most
representative outputs for each.

---

## Using this as a template

You can copy the artefacts in this folder as a starting point for
your own system. A few things to keep in mind:

- The business context (an ecommerce marketplace) is fictional. Do
  not just rename the modules; the shape of the payments domain in
  the example is specific to ecommerce and will not fit every
  context.
- The numbers in the evolution assessment are illustrative. Your
  own signal ratings should be backed by evidence from your own
  system, not borrowed from this example.
- The extraction decision here is correct for this fictional
  context. It may not be correct for yours. The decision model is
  the tool; the decision itself depends on your situation.

---

## Related reading

- [Discipline guides](../../disciplines/) for the concepts behind
  each artefact
- [Decision model](../../decision-model/) for the signals and
  outcomes used in the evolution assessment
- [Adoption materials](../../adoption/) for the templates this
  example is built on
