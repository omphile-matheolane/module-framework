# MODULE Framework

**M**ap · **O**rganise · **D**esign · **U**nify · **L**everage · **E**valuate

A structured model for designing, governing, and evolving software
architecture over time. Deployment-agnostic: applies equally to
monoliths, modular monoliths, and microservices.

→ [siteName]

---

## The problem this solves

Most architectural guidance addresses one dimension at a time. Domain-driven
design helps with boundaries. Team Topologies addresses team structure.
Fitness functions handle governance automation. Each does its part well.

What has been missing is a model that treats all of these concerns as
continuous disciplines and connects them into a single coherent practice.
That is the gap MODULE fills.

---

## The six disciplines

| Letter | Discipline | What it governs |
|--------|------------|-----------------|
| M | Map Domain and Team Boundaries | Business capabilities, domain seams, team ownership |
| O | Organise Module and Service Internals | Encapsulation, internal coherence, public interfaces |
| D | Design Contracts and Collaboration | APIs, events, commands, communication patterns |
| U | Unify Ownership and Governance | Automated boundary enforcement, CI policies, ADRs |
| L | Leverage Observability | Metrics, traces, logs as architectural signals |
| E | Evaluate Evolution | Evidence-based decisions on when and how to evolve |

These are not sequential phases. They run continuously throughout the
lifecycle of a system, informing each other as teams and requirements change.

---

## Evolution is bidirectional

MODULE does not treat architecture as a one-way journey toward microservices.
Systems evolve in both directions depending on what the signals say:
```
Monolith → Modular Monolith → Microservices
                    ↑                  ↓
                    ←   Consolidation  ←
```

The evolution decision model (see `/decision-model`) provides a structured
way to assess which direction is right for a specific component at a
specific point in time.

---

## The five evolution signals

When evaluating whether and how a component should evolve, MODULE assesses
five observable signals:

1. **Code Isolation** — are the boundaries clear and dependencies minimal?
2. **Data Ownership** — does the component own its data, or share tables?
3. **Contract Stability** — is the public interface stable enough to extract?
4. **Team Autonomy** — can one team develop, test, and deploy independently?
5. **Operational Impact** — what is the component's contribution to incidents and latency?

Each signal points toward one of five outcomes: extract to a service,
consolidate, modularise inside the monolith, harden the module, or
stabilise as-is. See `/decision-model` for the full reference and a
downloadable scoring spreadsheet.

---

## What is in this repository

| Folder | Contents |
|--------|----------|
| `/whitepaper` | Full framework whitepaper in PDF and markdown |
| `/decision-model` | Five-signal reference, scoring guide, outcome files |
| `/adoption` | Templates for domain maps, ownership registries, contract catalogs |
| `/examples` | Worked example: payment processing domain |

---

## Who this is for

MODULE is designed for the people who sit between the whiteboard and
production: engineering leads making boundary decisions, architects
governing systems under pressure, platform teams trying to enforce
standards at scale, and technical leads managing module ownership day
to day.

It is particularly useful in organisations where the architecture has
already diverged from the original intent and the team needs a model
for getting it back under control without starting over.

---

## Status

The framework is at v1.0. The whitepaper, decision model, and adoption
templates are stable. The examples folder is actively being developed.

Feedback, case studies, and worked examples from practitioners are
welcome. See `CONTRIBUTING.md` for how to contribute.

---

## Author

Omphile Matheolane is a Technology Leader with years of
experience building enterprise systems across the financial
sector, working across payment systems, trade finance, and mandate
processing at scale. He speaks at international conferences on software
architecture and writes on engineering leadership.

MODULE grew from repeated encounters with the same architectural
problems in environments where getting them wrong has immediate,
measurable consequences.

→ [siteName]

---

## License

MIT License. See `LICENSE` for details.
