# Payment Processing Example: Evolution Timeline

This timeline shows the Payment Initiation module's journey over twelve months, from initial assessment through hardening to extraction. It also includes a consolidation decision for Reporting, which demonstrates the bidirectional nature of MODULE's evolution path.

---

## Timeline

| Month | Event | Module | Outcome |
|-------|-------|--------|---------|
| 0 | Initial five-signal assessment | Payment Initiation | Harden |
| 0 | Domain map created, shared schema identified | All | — |
| 1 | Schema separation work begins | Payment Initiation, Notification | — |
| 2 | Event contract designed for payment status updates | Payment Initiation, Notification | — |
| 3 | Schema separation complete, events live | Payment Initiation, Notification | — |
| 3 | Governance rules added to CI for Notification | Notification | — |
| 3 | Contract freeze begins for Payment Initiation API | Payment Initiation | — |
| 4 | First stable release cycle completes | Payment Initiation | — |
| 5 | Second stable release cycle completes | Payment Initiation | — |
| 6 | Five-signal reassessment | Payment Initiation | Extract |
| 6 | Reporting ownership assigned to Finance | Reporting | — |
| 7 | Extraction planning begins | Payment Initiation | — |
| 7 | Five-signal assessment for Reporting | Reporting | Consolidate |
| 8 | Payment Initiation service deployed to production | Payment Initiation | — |
| 9 | Post-extraction observability review | Payment Initiation | — |
| 9 | Reporting consolidation into Reconciliation begins | Reporting | — |
| 12 | Reporting consolidation complete | Reporting (retired) | — |

---

## Month 0: Initial Assessment

The Payments team ran the first five-signal assessment on Payment Initiation after questions arose about whether the module should become an independent service. The assessment revealed strong code isolation and team autonomy, but weak data ownership due to a shared schema with Notification.

The outcome was Harden. The team created a plan to resolve the data sharing and stabilise the contract before reconsidering extraction.

At the same time, the domain map was created for the entire payment processing system. The mapping exercise surfaced the Reporting module's unclear boundary and contested ownership, issues that would be addressed later in the timeline.

---

## Months 1-3: Hardening Payment Initiation

The schema separation work took three months of focused effort across the Payments and Customer Experience teams.

In Month 1, the teams mapped all queries that touched the shared `payment_status` table and designed the target state: Payment Initiation would own the table, and Notification would receive updates via events.

In Month 2, the event contract was designed and implemented. The `PaymentStatusChanged` event carries the information Notification needs without requiring direct table access. The event infrastructure was already in place for other purposes, so the team could focus on the contract design rather than plumbing.

In Month 3, the migration completed. Notification's queries were rewritten to consume events, and the shared table access was removed. With the data coupling resolved, the Customer Experience team added boundary tests to Notification and integrated them into CI.

Also in Month 3, the Payments team began a contract freeze on the Payment Initiation public API. The goal was two stable release cycles with no breaking changes, which would move the contract stability signal from adequate to strong.

---

## Months 4-5: Contract Stabilisation

These months were relatively quiet for Payment Initiation. The team continued normal development, but all changes to the public API were additive and backward-compatible. The discipline required some adjustment: features that would previously have changed the API were designed differently to maintain compatibility.

By the end of Month 5, the team had completed two full release cycles without breaking changes. Contract stability was now demonstrably strong.

---

## Month 6: Reassessment and New Decisions

Two significant things happened in Month 6.

First, the Payments team ran the five-signal reassessment on Payment Initiation. Data ownership had moved from 2 to 4 with the schema separation complete. Contract stability had moved from 3 to 4 with two stable release cycles. The outcome changed from Harden to Extract.

Second, engineering leadership finally assigned Reporting ownership to the Finance team. This had been deferred for quarters, but the domain mapping exercise in Month 0 made the problem visible and kept it on the agenda.

---

## Month 7: Extraction Planning and Reporting Assessment

The Payments team began extraction planning for Payment Initiation. The work involved defining the service boundary (which aligned closely with the existing module boundary), setting up independent deployment infrastructure, and planning the consumer migration.

Meanwhile, the Finance team ran a five-signal assessment on their newly-owned Reporting module. The results were sobering:

- Code Isolation: 1 (Weak). No boundary exists. Multiple teams access tables directly.
- Data Ownership: 2 (Developing). Reporting reads from tables owned by other modules with no clear contracts.
- Contract Stability: 1 (Weak). There is no defined contract to stabilise.
- Team Autonomy: 2 (Developing). The Finance team cannot make changes without coordinating with teams that access the tables.
- Operational Impact: 3 (Adequate). Reporting queries occasionally cause performance issues, but nothing severe.

The outcome was Consolidate. Reporting in its current form provides little independent value and creates coordination overhead. The Finance team decided to consolidate Reporting into Reconciliation, which is a well-structured module they already own. The reporting capabilities would become part of Reconciliation rather than a separate, poorly-bounded module.

---

## Months 8-9: Extraction and Consolidation in Parallel

In Month 8, Payment Initiation went live as an independent service. The extraction went smoothly because the hardening work had resolved the structural issues that would have complicated a distributed deployment. Consumers migrated over two weeks, with traffic gradually shifting from the monolith module to the new service.

In Month 9, the team conducted a post-extraction observability review. Latency was slightly higher than in-process calls, as expected, but well within acceptable bounds. The service was stable, and the Payments team was deploying independently as planned.

Also in Month 9, the Finance team began consolidating Reporting into Reconciliation. The work involved migrating the useful reporting queries into Reconciliation's codebase, establishing proper contracts for data access, and redirecting consumers away from the old Reporting tables.

---

## Month 12: Consolidation Complete

By Month 12, the Reporting consolidation was complete. The useful reporting capabilities now live inside Reconciliation, behind a proper public API with boundary enforcement. The old Reporting tables have been retired, and the teams that previously accessed them directly now use the Reconciliation API.

---

## What This Timeline Demonstrates

The twelve-month period shows evolution in both directions.

Payment Initiation followed the path from modular monolith to independent service. The module was assessed, found to need hardening, and then reassessed after the hardening work was complete. Extraction happened with confidence because the signals supported it.

Reporting followed the opposite path. A poorly-structured module was assessed, found to provide insufficient independent value, and consolidated into a better-structured module. The system became simpler as a result.

Both directions are valid. The evolution decision model does not assume that extraction is always the goal. It assesses each component on its own terms and recommends the appropriate trajectory based on observable signals.

---

## Current State

At the end of the twelve-month period, the payment processing system looks different from where it started:

| Component | Month 0 State | Month 12 State |
|-----------|---------------|----------------|
| Payment Initiation | Module (shared schema) | Independent service |
| Payment Authorisation | Module | Module (unchanged) |
| Notification | Module (shared schema) | Module (clean boundary) |
| Reconciliation | Module | Module (absorbed Reporting) |
| Reporting | Unclear boundary, no owner | Retired (consolidated into Reconciliation) |

The system has four well-structured components instead of five poorly-structured ones. One module was extracted because the signals supported extraction. One module was retired because the signals supported consolidation. The decisions were made with evidence rather than intuition.

---

## Related

- [decision-model-walkthrough.md](decision-model-walkthrough.md): Detailed assessment of Payment Initiation
- [domain-map.md](domain-map.md): Module boundary documentation
- [ownership-registry.md](ownership-registry.md): Governance status for each module
- [/decision-model](../../decision-model/): The evolution decision model
