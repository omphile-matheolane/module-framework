# Examples

This folder contains worked examples showing the MODULE Framework applied to realistic systems. The examples demonstrate how the adoption templates, decision model, and disciplines work together in practice.

## Current Examples

### Payment Processing

A digital banking payment processing system with five modules: Payment Initiation, Payment Authorisation, Notification, Reconciliation, and Reporting.

The example covers:

| File | What It Shows |
|------|---------------|
| [overview.md](payment-processing/overview.md) | System description, starting state, and what the example demonstrates |
| [domain-map.md](payment-processing/domain-map.md) | Domain map populated for the five modules |
| [ownership-registry.md](payment-processing/ownership-registry.md) | Ownership and governance status for each module |
| [decision-model-walkthrough.md](payment-processing/decision-model-walkthrough.md) | Five-signal assessment of Payment Initiation, including reassessment after hardening |
| [evolution-timeline.md](payment-processing/evolution-timeline.md) | Twelve-month evolution timeline showing bidirectional evolution |

## How to Use These Examples

These examples are meant to be pattern-matched against, not copied verbatim. Your system will have different modules, different teams, and different constraints. The examples show how the templates and decision model are applied, not what your specific answers should be.

When working through the examples:

1. **Read the overview first** to understand the system context
2. **Compare the domain map** to how you would document your own system
3. **Follow the decision model walkthrough** to see how signals are assessed and how reassessment works
4. **Study the evolution timeline** to understand how decisions play out over time

## Contributing Examples

Additional examples are welcome. If you have applied MODULE to a system and are willing to share the experience (anonymised as needed), see [CONTRIBUTING.md](../CONTRIBUTING.md) for guidance.

Good examples:

- Show MODULE applied to a real (or realistic) system
- Demonstrate at least one full cycle of the decision model
- Include enough context for readers to understand the reasoning
- Are technology-agnostic or clearly state the technology context

## Related

- [/adoption](../adoption/): Templates used in the examples
- [/decision-model](../decision-model/): The evolution decision model
- [Whitepaper](../whitepaper/MODULE_Framework_Whitepaper.pdf): Full framework documentation
