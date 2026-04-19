# Adopting MODULE

This folder contains the practical material for adopting MODULE in
your own context: templates you can copy, checklists for recurring
activities, and playbooks for the two main adoption paths
(greenfield and brownfield).

The discipline guides in [`disciplines/`](../disciplines/) explain
what each discipline involves and why. The material here is about
the mechanics: the documents, the recurring activities, and the
sequence of work.

---

## How to use this folder

Adoption is not a single project. It is a set of habits that get
introduced progressively and then maintained over time. The
templates and checklists here support those habits; they are not
deliverables to be produced once and filed away.

Pick the templates you need, copy them into your own repositories,
and adapt them to your context. The specific wording matters less
than the fact of having a consistent template that your team
actually uses.

The playbooks are longer-form guides for working through adoption
end to end. Read the one that matches your situation.

---

## Templates

Reusable documents for the artefacts MODULE produces. Copy these
into your own repositories and adapt as needed.

| Template | Purpose |
|----------|---------|
| [Domain map](templates/domain-map-template.md) | Documents the modules, their capabilities, ownership, and dependencies |
| [Ownership registry](templates/ownership-registry-template.md) | Authoritative record of who owns each module |
| [Contract catalog](templates/contract-catalog-template.md) | Central listing of significant interfaces between modules |
| [ADR template](templates/adr-template.md) | Structured format for architecture decision records |
| [Starter architecture tests](templates/starter-arch-tests.md) | ArchUnit rules that make a reasonable starting set |

---

## Checklists

Structured prompts for recurring activities. Use these when running
reviews and assessments to reduce the chance of missing something
obvious.

| Checklist | When to use |
|-----------|-------------|
| [Map review checklist](checklists/map-review-checklist.md) | Quarterly review of the domain map |
| [New module checklist](checklists/new-module-checklist.md) | When creating a new module |
| [Evolution assessment checklist](checklists/evolution-assessment-checklist.md) | When running an evolution decision model assessment |
| [Incident architectural review checklist](checklists/incident-review-checklist.md) | After an incident with architectural implications |

---

## Playbooks

Longer-form guides for the two main adoption paths. Each covers the
sequence of work, the decisions involved, and common pitfalls.

| Playbook | Audience |
|----------|----------|
| [Greenfield adoption](playbooks/greenfield-adoption.md) | New systems where MODULE can be applied from the start |
| [Brownfield adoption](playbooks/brownfield-adoption.md) | Existing systems where MODULE is being introduced progressively |
| [Evolution workflow](playbooks/evolution-workflow.md) | End-to-end sequence for running an evolution assessment and implementing the outcome |

---

## What is not here

Framework-specific tooling. MODULE is deployment-agnostic and
language-agnostic, and the choice of tooling depends on your
ecosystem. Specific references are in the discipline guides
(ArchUnit for Java, NetArchTest for .NET, import-linter for Python,
and so on), but this folder does not package those tools.

Also not here: case studies from specific organisations. Those
would be valuable contributions to the repository, and we would
welcome them. See [CONTRIBUTING.md](../CONTRIBUTING.md).
