# Payment Processing Example: Overview

This example shows the MODULE Framework applied to a digital banking payment processing system. The system is realistic but anonymised, based on patterns common across financial services.

## System Description

The payment processing system handles the lifecycle of payments within a digital banking platform. It supports multiple payment types (internal transfers, external payments, scheduled payments) and integrates with downstream banking infrastructure for clearing and settlement.

The system is organised into five modules:

| Module | Responsibility |
|--------|---------------|
| **Payment Initiation** | Receives payment requests from channels (mobile app, web, API), validates inputs, and creates payment records |
| **Payment Authorisation** | Handles authentication, fraud checks, and approval workflows for high-value or flagged payments |
| **Notification** | Sends payment status updates to customers via SMS, email, and push notifications |
| **Reconciliation** | Matches processed payments against external settlement records and flags discrepancies |
| **Reporting** | Provides payment data for regulatory reporting, business intelligence, and customer statements |

## Starting State

The system was originally built as a monolith and has been gradually modularised over the past two years. The current state reflects that history:

- **Payment Initiation** and **Payment Authorisation** have clear boundaries enforced in CI. The Payments team owns both modules and can deploy them independently.
- **Notification** has a recognisable boundary but shares a database schema with Payment Initiation. This shared schema was a shortcut taken during initial development that has not yet been resolved.
- **Reconciliation** has a clear boundary and is owned by the Finance team. It operates largely independently.
- **Reporting** has an unclear boundary. Multiple teams access its internals directly to build reports, and ownership is contested between Finance and a central Platform team.

## What This Example Demonstrates

The example walks through several MODULE practices:

### Domain Mapping

The [domain-map.md](domain-map.md) shows how to document the five modules, their boundaries, and their ownership. The map reveals that Notification has a drifting boundary and Reporting has an unclear boundary, both of which need attention.

### Ownership Registry

The [ownership-registry.md](ownership-registry.md) tracks governance status for each module. Notification shows manual governance as a realistic starting point before the team upgrades to enforced-in-CI. Reporting shows no governance, reflecting the unclear boundary.

### Evolution Decision Model

The [decision-model-walkthrough.md](decision-model-walkthrough.md) applies the five-signal model to the Payment Initiation module. The team is considering extraction because the Payments team is growing and wants more autonomy.

The walkthrough shows:

1. **Initial assessment:** Code Isolation is high (4), but Data Ownership is low (2) because of the shared schema with Notification. Contract Stability is medium (3). The outcome is **Harden**, not Extract.

2. **Six months later:** After resolving the shared schema and stabilising the contract, the reassessment produces a different result. Data Ownership is now high (4), Contract Stability is now high (4). The outcome is **Extract**.

This demonstrates how the decision model prevents premature extraction and how reassessment works when circumstances change.

### Evolution Timeline

The [evolution-timeline.md](evolution-timeline.md) shows the Payment Initiation module's journey over twelve months, from initial assessment through hardening to extraction. It also includes a consolidation decision for Reporting, demonstrating the bidirectional nature of MODULE's evolution path.

## Technology Stack

The example is deliberately technology-agnostic. The patterns apply whether the system is built in Java with Spring, in .NET, or in another stack. Where technology choices are mentioned, they are illustrative, not prescriptive.

## Related Files

- [domain-map.md](domain-map.md)
- [ownership-registry.md](ownership-registry.md)
- [decision-model-walkthrough.md](decision-model-walkthrough.md)
- [evolution-timeline.md](evolution-timeline.md)
