# Contributing to the MODULE Framework

MODULE is a living framework. It improves through the experience of
practitioners who apply it in real systems and bring back what they
learn.

Contributions are welcome in four forms:

---

## 1. Case studies and worked examples

If you have applied MODULE (or parts of it) in a real system, a written
account of that experience is the most valuable contribution you can
make. It does not need to be polished. It needs to be honest.

A useful case study covers:

- The starting point: what the system and team situation looked like
  before applying the framework
- Which disciplines you applied and in what order
- What the signals told you and what decisions you made
- What worked, what did not, and what you would do differently

Submit case studies as a pull request to the `/examples` folder. Use
the existing `payment-processing` example as a structural guide.

---

## 2. Corrections and clarifications

If something in the whitepaper, decision model, or templates is
unclear, imprecise, or wrong, open an issue describing the problem and
where it appears. Include a suggested correction if you have one.

Corrections to the five-signal model and outcome definitions are
particularly important to get right, as these feed directly into the
Evolution Decision Wizard at [SiteName]

---

## 3. Template improvements

The adoption templates in `/adoption` are starting points, not final
answers. If you have used the domain map, ownership registry, or
contract catalog in practice and found that something was missing or
needed restructuring, open a pull request with the change and a brief
note explaining what prompted it.

Changes to templates should be grounded in real usage. Theoretical
improvements are harder to evaluate and less likely to be merged.

---

## 4. Translations

If you would like to translate the whitepaper or decision model
reference into another language, open an issue first to confirm the
language is not already in progress. Translations live in a subfolder
named by ISO 639-1 language code, for example `/whitepaper/fr/`.

---

## What is not in scope

MODULE is intentionally framework and language agnostic. Contributions
that tie the framework to a specific technology stack, programming
language, or vendor tool are out of scope for this repository.

Code implementations of MODULE concepts (enforcement libraries,
tooling, CI integrations) are welcome as separate repositories. If you
build one, open an issue here to have it listed in the README under a
community projects section.

---

## How to submit a contribution

1. Fork the repository
2. Create a branch named for the contribution,
   e.g. `case-study/retail-platform` or `fix/signal-definition-clarity`
3. Open a pull request with a clear description of what you are
   contributing and why
4. Allow up to two weeks for review

For significant contributions, opening an issue before writing anything
is the best way to avoid effort that does not align with where the
framework is heading.

---

## Code of conduct

Contributions are expected to be respectful and grounded in practice.
Criticism of the framework is welcome when it is specific and
constructive. Abstract disagreement without a concrete alternative is
not useful to anyone.

---

*MODULE is maintained by Omphile Matheolane. Questions can be directed
via the[siteName]
site.*
