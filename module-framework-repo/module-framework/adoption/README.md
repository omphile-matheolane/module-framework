# Adoption Templates

This folder contains templates for teams adopting the MODULE Framework. Each template corresponds to a practice within one of the six disciplines.

## Templates

| Template | Related Discipline | Purpose |
|----------|-------------------|---------|
| [domain-map-template.md](domain-map-template.md) | Map | Document domain boundaries, module responsibilities, and team ownership |
| [ownership-registry.md](ownership-registry.md) | Unify | Track module ownership, governance status, and accountability |
| [contract-catalog.md](contract-catalog.md) | Design | Document contracts between modules including APIs, events, and commands |
| [governance-checklist.md](governance-checklist.md) | Unify | Verify governance practices are in place for boundary enforcement and ownership |

## How to Use These Templates

These templates are starting points, not rigid formats. Adapt them to fit your organisation's conventions and tooling.

**Start small.** You do not need all four templates on day one. Most teams begin with the domain map and ownership registry to establish clarity about what exists and who owns it. The contract catalog and governance checklist come later as the system matures.

**Keep them alive.** Templates that are filled in once and forgotten provide little value. Link them from your team's documentation, review them during architectural discussions, and update them when the system changes.

**Make them accessible.** Store these documents where the team will actually use them: in the repository, in a wiki, or in whatever system your organisation uses for architectural documentation.

## Starting Point Recommendations

If you are adopting MODULE for the first time:

1. **Week 1:** Fill in the domain map. Get clarity on what modules exist (even informally) and which teams own them.
2. **Week 2:** Create the ownership registry. Formalise ownership and note where governance is manual or missing.
3. **Week 3-4:** Run the governance checklist. Identify the most critical gaps and prioritise addressing them.
4. **Ongoing:** Add to the contract catalog as you formalise contracts between modules.

## Related

- [/decision-model](../decision-model/): The evolution decision model for assessing components
- [/examples](../examples/): Worked examples showing these templates in use
- [Whitepaper](../whitepaper/MODULE_Framework_Whitepaper.pdf): Full framework documentation
