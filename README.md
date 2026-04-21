# MODULE Framework

**M**ap · **O**rganise · **D**esign · **U**nify · **L**everage · **E**valuate

A structured model for practicing, governing, and evolving software
architecture over time. MODULE treats architecture as an ongoing
discipline rather than a fixed blueprint, and is deployment-agnostic:
it applies equally to monoliths, modular monoliths, and microservices.

---

## Start here

If you are new to MODULE, the recommended reading order is:

1. Read the [whitepaper](whitepaper/) for the full framework.
2. Work through the [six discipline guides](disciplines/) for practical detail on each.
3. Read the [evolution decision model](decision-model/) before making any extraction or consolidation call.
4. Use the [adoption materials](adoption/) to put the disciplines into practice in your own context.
5. Explore the [worked example](examples/payments-domain/) to see what the disciplines look like together.

If you are evaluating whether MODULE is worth adopting at all, the costs
and limitations section of the whitepaper is the honest starting point.

---

## The problem this solves

Most architectural guidance addresses one dimension at a time.
Strategic Domain-Driven Design helps with boundaries. Team Topologies
addresses team structure. Fitness functions handle governance
automation. Each does its part well.

What has been missing is a model that treats all of these concerns as
continuous disciplines and connects them into a single integrated
practice that can be sustained across the full lifecycle of a system.
That is the synthesis gap MODULE fills.

---

## The six disciplines

| Letter | Discipline | What it governs | Guide |
|--------|------------|-----------------|-------|
| M | Map Domain and Team Boundaries | Business capabilities, domain seams, team ownership | [→](disciplines/01-map/) |
| O | Organise Module and Service Internals | Encapsulation, internal coherence, public surfaces | [→](disciplines/02-organise/) |
| D | Design Contracts and Collaboration | APIs, events, commands, communication patterns | [→](disciplines/03-design/) |
| U | Unify Ownership and Governance | Ownership registries, automated boundary enforcement, ADRs | [→](disciplines/04-unify/) |
| L | Leverage Observability | Metrics, traces, logs as architectural signals | [→](disciplines/05-leverage/) |
| E | Evaluate Evolution | Evidence-based decisions across the full range of evolution outcomes | [→](disciplines/06-evaluate/) |

These are not sequential phases. They run continuously throughout the
lifecycle of a system, informing each other as teams and requirements
change. Their relative intensity shifts over time, but none of them
reaches a point where it can simply be set aside.

---

## Evolution is bidirectional

MODULE does not treat architecture as a one-way journey toward
microservices. Systems evolve in both directions depending on what
the signals say:

```
Monolith → Modular Monolith → Microservices
                    ↑                  ↓
                    ←   Consolidation  ←
```

Extraction and consolidation are both valid architectural moves when
supported by evidence. The [evolution decision model](decision-model/)
provides a structured way to assess which direction is right for a
specific component at a specific point in time.

---

## Repository layout

| Folder | Contents |
|--------|----------|
| [`whitepaper/`](whitepaper/) | The full framework whitepaper |
| [`disciplines/`](disciplines/) | One guide per discipline with practices, anti-patterns, and examples |
| [`decision-model/`](decision-model/) | Five-signal reference, scoring worksheet, outcome descriptions, and worked examples |
| [`adoption/`](adoption/) | Playbooks, templates, and checklists for greenfield and brownfield adoption |
| [`examples/`](examples/) | Worked example: a payment processing domain applying all six disciplines |

---

## Who this is for

MODULE is written for practitioners who sit between the whiteboard and
production, and who are responsible for keeping the architecture
coherent as systems grow and teams change. It applies across contexts,
though the emphasis shifts with scale. Startups benefit most from
establishing habits around Map and Organise early. Scale-ups typically
find Unify becoming critical as teams multiply. Enterprises often begin
with Map and Unify to introduce discipline into systems that have
evolved for years without it.

It is particularly useful when an architecture has already diverged
from its original intent and a team needs a model for bringing it back
under control progressively, without starting over.

---

## Status

The framework is at v1.0. Feedback, case studies, and worked examples
from practitioners are welcome. See [CONTRIBUTING.md](CONTRIBUTING.md)
for how to contribute.

---

## Author

Omphile Matheolane is a technology leader with years of experience
building enterprise systems. He speaks at international conferences on
software architecture and writes on engineering leadership.

MODULE grew from repeated encounters with the same architectural
problems in environments where getting them wrong has immediate,
measurable consequences.

https://doi.org/10.5281/zenodo.19676156

---

## License

MIT License. See [LICENSE](LICENSE) for details.
