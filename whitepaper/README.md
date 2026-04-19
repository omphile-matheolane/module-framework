# Whitepaper

This folder holds the MODULE Framework whitepaper, which is the
authoritative description of the framework as a whole. The discipline
guides and the decision model in the rest of this repository are
practical extensions of what the whitepaper establishes, and they
assume the reader has worked through the whitepaper at least once.

---

## Files

| File | Description |
|------|-------------|
| `MODULE_WhitePaper_v1_0.docx` | The whitepaper in Word format |
| `MODULE_WhitePaper_v1_0.pdf` | The whitepaper in PDF format (recommended for reading) |

*Note: place the whitepaper files in this folder. They are the source
of truth for the framework; everything else in this repository is
derived from them.*

---

## How to read it

The whitepaper is structured so that you can read it linearly from
start to finish, but it is also designed for selective reading once you
are familiar with the overall shape. The sections are grouped roughly
as follows.

The opening sections establish the problem. The introduction and the
chapter on the cost of architectural drift explain why the framework
exists and what it is trying to address. If you already know why you
need architectural discipline, these sections can be skimmed. If you
are trying to justify the investment to someone else, they are the
sections worth quoting.

The six discipline chapters form the core. Each chapter follows the
same structure: what the discipline involves, why it matters, how to
practice it, greenfield versus brownfield considerations, failure
modes, and its connections to the other disciplines. The discipline
guides in `disciplines/` expand on this material with more detail, more
examples, and more practical patterns.

The evolution decision model and the bidirectional evolution chapter
explain how MODULE handles the question of when and how components
should evolve. These sections are essential reading before any
extraction or consolidation decision. The `decision-model/` folder
extends this material with scoring templates and worked examples.

The later sections cover adoption at different scales, greenfield and
brownfield paths, failure modes, costs and limitations, and how to
measure whether MODULE is working. If you are planning an adoption,
these sections are the ones to read carefully.

---

## Relationship to the rest of the repository

The whitepaper is the reference document. The discipline guides, the
decision model, the adoption materials, and the worked example are all
elaborations of what the whitepaper establishes. Where these other
materials cover something the whitepaper does not, that is usually
because the repository needs more operational detail than a whitepaper
is the right format for. Where they appear to conflict with the
whitepaper, the whitepaper wins, and the conflict is probably a bug.

If you find such a conflict, please open an issue.
