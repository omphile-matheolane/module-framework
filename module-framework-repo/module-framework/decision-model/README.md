# Evolution Decision Model

The evolution decision model is the core assessment tool within the MODULE Framework's Evaluate discipline. It provides a structured approach for determining whether a component should evolve and in which direction.

## The Five Signals

| Signal | Description |
|--------|-------------|
| Code Isolation | Whether a component has clear boundaries and minimal inbound dependencies |
| Data Ownership | Whether a component owns its data or shares tables and schemas with other modules |
| Contract Stability | How frequently the public interface changes and whether consumers break on update |
| Team Autonomy | Whether a single team can develop, test, and deploy the component independently |
| Operational Impact | The component's contribution to incidents, latency, and operational burden |

## The Five Outcomes

| Outcome | Description |
|---------|-------------|
| Extract | Move to an independent service when isolation is strong and autonomy is a genuine driver |
| Consolidate | Merge with another service or module when separation creates overhead without sufficient value |
| Modularise | Introduce internal structure within the monolith before extraction becomes viable |
| Harden | Strengthen boundaries and contracts of an existing module before extraction |
| Stabilise | Maintain current state when change rate and operational impact are both low |

## Resources

- [five-signals.md](five-signals.md): Full reference for the five-signal assessment model
- [five-signals.xlsx](five-signals.xlsx): Offline scoring spreadsheet for team assessments
- [/outcomes](outcomes/): Detailed guidance for each of the five outcomes
- [Interactive Wizard](https://moduleframework.figma.site): Web-based tool for running assessments

## How to Use This Folder

Teams typically start by reading [five-signals.md](five-signals.md) to understand how to assess each signal. The [spreadsheet](five-signals.xlsx) provides a structured format for scoring components and recording the reasoning behind each assessment. Each outcome has its own file in the [/outcomes](outcomes/) folder with specific guidance on what to do once a decision is reached.
