# The Five-Signal Model

Architecture changes should be guided by observable signals rather than intuition or trend-following. The five-signal model provides the assessment criteria for the MODULE Framework's evolution decision model.

## The Signals

| Signal | What It Reveals | Evolution Trigger |
|--------|-----------------|-------------------|
| Code Isolation | Whether a component has clear boundaries and minimal inbound dependencies | Low isolation: candidate for modularisation or boundary clarification |
| Data Ownership | Whether a component owns its data or shares tables and schemas with other modules | Shared data is the strongest blocker to extraction. Resolve before separating services. |
| Contract Stability | How frequently the public interface changes and whether consumers break on update | Unstable contracts: stabilise the interface before extracting. |
| Team Autonomy | Whether a single team can develop, test, and deploy the component independently | High coordination overhead: consider extraction or clearer ownership boundaries. |
| Operational Impact | The component's contribution to incidents, latency, and operational burden | Frequent incidents or disproportionate cost: consider consolidation or retirement. |

## Reading the Signals Together

The five signals are not independent checkboxes. They interact, and certain signals carry more weight than others depending on the situation.

**Data Ownership is the hardest constraint.** A component with excellent code isolation but shared data is not ready to extract. Extracting it would create a distributed data problem that is significantly harder to resolve than the original monolith problem. Resolve data boundaries first, then reassess.

**Contract Stability is the second gate.** An unstable interface amplifies the cost of extraction. Every consumer of the component will need to adapt to changes, and if the interface is still evolving, that adaptation becomes a recurring tax. Stabilise the contract before extracting, or accept that you are creating coupling that will persist across service boundaries.

**Code Isolation and Team Autonomy often move together.** A team that cannot deploy independently usually has a boundary problem underneath. Either the module depends on too many other modules, or other modules depend on it in ways that require coordination. Improving code isolation tends to improve team autonomy, and the reverse is also true.

**Operational Impact is the only signal that can recommend consolidation regardless of the other four.** A service that is well-isolated, has clean data ownership, a stable contract, and an autonomous team can still be a candidate for consolidation if its operational burden is disproportionate to its business value. This is the signal that catches services which should not have been extracted in the first place.

## Scoring

Each signal can be assessed on a simple scale:

| Score | Meaning |
|-------|---------|
| 1 | Weak: significant issues that block evolution in this dimension |
| 2 | Developing: work in progress, not yet reliable |
| 3 | Adequate: meets minimum requirements but has room for improvement |
| 4 | Strong: well-established, few concerns |
| 5 | Excellent: a clear strength, no concerns |

The [five-signals.xlsx](five-signals.xlsx) spreadsheet provides a structured format for recording scores and the reasoning behind each assessment.

## Reversibility

Decisions produced by the model are not permanent. Architecture evolves, and what was the right call six months ago may not be the right call today. The model is designed to be rerun periodically as circumstances change.

A component extracted prematurely can be consolidated back. A component modularised inside a monolith can be extracted later when the signals are right. The goal is not to reach a final architectural state but to make each change with evidence behind it.

## Related Resources

- [/outcomes](outcomes/): Detailed guidance for each of the five outcomes
- [Interactive Wizard](https://moduleframework.figma.site): Web-based tool for running assessments
- [Whitepaper](../whitepaper/MODULE_Framework_Whitepaper.pdf): Full framework documentation
