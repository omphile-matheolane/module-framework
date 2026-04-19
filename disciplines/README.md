# The Six Disciplines

This folder contains a detailed guide for each of the six disciplines
that make up the MODULE framework. The whitepaper introduces each
discipline in outline. These guides go further: they cover the patterns
that tend to work, the anti-patterns that show up repeatedly, and
enough practical detail to apply the discipline in your own context.

---

## How these guides are organised

Each guide follows the same structure, which mirrors the structure used
in the whitepaper itself:

1. **What the discipline involves** and why it exists as a separate
   concern
2. **Why it matters** in concrete terms, with specific problems the
   discipline addresses
3. **How to practice it**, including patterns, techniques, and worked
   examples
4. **Greenfield versus brownfield** considerations, because the path
   is meaningfully different in each
5. **Failure modes and anti-patterns** that tend to emerge when the
   discipline is practiced poorly or inconsistently
6. **Connections to other disciplines**, because the disciplines
   reinforce each other and applying any one in isolation tends to
   produce incomplete results
7. **Further reading**, pointing to deeper sources on specific aspects

The guides are meant to be read in order the first time through, because
later disciplines build on earlier ones. Once you are familiar with the
shape of the framework, they can be consulted independently as reference
material when specific questions come up.

---

## The guides

| # | Discipline | Guide |
|---|------------|-------|
| 1 | Map Domain and Team Boundaries | [01-map/](01-map/) |
| 2 | Organise Module and Service Internals | [02-organise/](02-organise/) |
| 3 | Design Contracts and Collaboration | [03-design/](03-design/) |
| 4 | Unify Ownership and Governance | [04-unify/](04-unify/) |
| 5 | Leverage Observability | [05-leverage/](05-leverage/) |
| 6 | Evaluate Evolution | [06-evaluate/](06-evaluate/) |

---

## A note on examples

The examples in the guides lean on Java, because the Java ecosystem has
particularly mature tooling for architectural enforcement and the
patterns translate cleanly to other languages. Equivalents for .NET,
Go, Python, and TypeScript are called out where they matter. If you
work in one of those ecosystems and want to contribute additional
worked examples, see [CONTRIBUTING.md](../CONTRIBUTING.md).

The broader worked example in [`examples/payments-domain/`](../examples/payments-domain/)
shows all six disciplines applied together to a single system.
