# Contributing to MODULE

Thank you for considering a contribution. MODULE is a practitioners'
framework, and the contributions that tend to be most valuable come from
people who have actually applied it in real systems and can share what
worked, what did not, and what the framework got wrong.

---

## What makes a useful contribution

The framework benefits most from three kinds of contribution.

The first is case studies. If you have applied MODULE to a real system,
a write-up of how it went is genuinely valuable. Honest case studies,
including the ones where adoption struggled, tend to be more useful than
polished success stories. What mattered, what did not, and what you
would do differently next time are the questions worth answering.

The second is worked examples. The discipline guides benefit from
concrete examples across different languages, deployment models, and
domains. If you can contribute an example in a context not already
covered (a Go service, a .NET modular monolith, a Python data pipeline,
an event-driven architecture), that broadens the framework's practical
reach.

The third is corrections and refinements. If something in the whitepaper
or a discipline guide is wrong, unclear, or at odds with your experience,
open an issue. Counter-examples are particularly welcome, because the
framework is more useful when its boundaries are well understood.

---

## What does not need to change

MODULE is v1.0 and deliberately opinionated. The six disciplines, the
five signals, and the five outcomes are the core of the framework, and
changes to any of them need to be well justified by evidence from real
systems. Renaming, reordering, or adding disciplines is a high bar, and
pull requests proposing those kinds of changes will generally be
declined unless they come with substantial supporting case studies.

Surface-level edits to improve clarity, fix errors, or add examples are
always welcome. Structural changes to the framework itself should start
as an issue with discussion before any code is written.

---

## How to contribute

Case studies and examples can be submitted as pull requests directly.
See [`examples/`](examples/) for the structure to follow, and mirror the
conventions used in the existing worked example where practical.

For corrections, clarifications, or additions to a discipline guide,
pull requests against the relevant file in [`disciplines/`](disciplines/)
are the right starting point. Please include a short note in the PR
description explaining what problem the change addresses.

For larger changes, or for anything that affects the framework itself
rather than just the supporting material, open an issue first. That
saves time on both sides if the change is not going to land, and gives
space to work through the reasoning before committing to an
implementation.

---

## Writing style

The existing material is written in a plain, practitioner-oriented
style. Contributions that match that style are easier to integrate than
ones that do not.

A few things to keep in mind. The framework is not a marketing document.
Grounded, concrete language works better than abstract or aspirational
phrasing. Trade-offs should be acknowledged rather than glossed over.
The reader is assumed to be an experienced engineer who will respond
better to honest explanation than to promotional language.

If in doubt, mirror the tone of the whitepaper.

---

## Reporting issues

If you find something wrong, unclear, or missing, open an issue. The
[issue templates](.github/ISSUE_TEMPLATE/) provide a starting structure
for the three most common types: framework questions, content
corrections, and requests for additional examples.

---

## Code of conduct

Contributions are expected to be constructive and respectful. Technical
disagreement is welcome, personal attacks are not. The framework
benefits from rigorous criticism; it does not benefit from hostility.
