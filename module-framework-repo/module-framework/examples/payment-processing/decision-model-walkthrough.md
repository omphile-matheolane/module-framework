# Payment Processing Example: Decision Model Walkthrough

This walkthrough shows the evolution decision model applied to the Payment Initiation module. The Payments team wanted to know whether extraction made sense, and the five-signal assessment gave them a clear answer: not yet, but with a path to get there.

---

## Context

The Payments team has grown from five engineers to twelve over the past year, and the roadmap suggests continued growth. The team lead raised the question of extracting Payment Initiation to a separate service. The reasoning was straightforward: the team wants to deploy more frequently than the monolith release cycle allows, scaling requirements for payment processing differ from other parts of the system, and the team would benefit from independent deployability.

These are reasonable drivers. The question is whether the module is ready for extraction, or whether work needs to happen first.

---

## Initial Assessment (Month 0)

The team ran the five-signal assessment on Payment Initiation. Here is what they found.

### Signal 1: Code Isolation

**Score: 4 (Strong)**

Payment Initiation has clear boundaries enforced in CI. The module exposes a public API through a facade, and internal classes are not accessible from outside. ArchUnit tests verify the boundary on every merge request, and violations block the build.

A few legacy dependencies predate the boundary enforcement. These are documented, understood, and small enough that they would not block extraction. The team rated this signal as strong.

### Signal 2: Data Ownership

**Score: 2 (Developing)**

This is where the assessment revealed a problem. Payment Initiation shares a database schema with the Notification module. Both modules read and write to a `payment_status` table, and neither has clear ownership of that table.

The sharing originated from a shortcut during initial development. Payment Initiation needed to record status changes, and Notification needed to read those changes to send updates to customers. Rather than designing an event-based contract, the teams shared a table.

That shortcut now creates a hard constraint. If Payment Initiation were extracted to a separate service, the shared table would become a distributed data problem. Either both services would need to access the same database (defeating much of the isolation benefit), or the data would need to be replicated between services with all the consistency challenges that entails.

The team rated data ownership as developing, which is generous. Shared data is the strongest blocker to extraction in the five-signal model, and this module has it.

### Signal 3: Contract Stability

**Score: 3 (Adequate)**

The public API exists and is documented, but it changed twice in the last quarter. Both changes were additive rather than breaking, but they still required coordination with consumers. The team is still learning what the stable interface should look like.

An adequate score here is not a blocker on its own, but combined with the data ownership problem, it reinforces the case for hardening before extraction. Extracting a service with an unstable contract means the instability crosses a network boundary, which makes every change more expensive.

### Signal 4: Team Autonomy

**Score: 4 (Strong)**

The Payments team can develop and test Payment Initiation independently. The module has its own test suite, and the team does not need to coordinate with other teams for most changes. The main coordination friction comes from the shared schema with Notification, which shows up here as well as in the data ownership signal.

### Signal 5: Operational Impact

**Score: 3 (Adequate)**

Payment Initiation contributes to some incidents but not disproportionately. Latency is acceptable. The module is not a significant operational burden. This signal is neutral: it does not push toward extraction or away from it.

---

## Initial Outcome: Harden

The assessment produced a clear outcome. Code isolation and team autonomy are strong, but data ownership is weak and contract stability is adequate. The module is a candidate for extraction in principle, but extracting it now would create problems that are easier to solve inside the monolith than across service boundaries.

The outcome is **Harden**: strengthen boundaries and resolve the data sharing before reconsidering extraction.

The team defined specific work to address the weak signals:

1. **Resolve the shared schema.** Payment Initiation will own the `payment_status` table. Notification will receive status updates via events rather than reading the table directly. This requires introducing an event contract between the modules.

2. **Stabilise the contract.** The team committed to a contract freeze for two release cycles. Any changes to the public API would be additive and backward-compatible. Breaking changes would wait until the freeze period ended.

3. **Reassess in six months.** The team scheduled a follow-up assessment to determine whether extraction had become viable.

---

## Six Months Later (Month 6)

The team completed the hardening work and ran the assessment again.

### Signal 1: Code Isolation

**Score: 4 (Strong)**

No change. The boundary was already strong and remains so.

### Signal 2: Data Ownership

**Score: 4 (Strong)**

The shared schema is resolved. Payment Initiation now owns the `payment_status` table exclusively. Notification receives status updates through a `PaymentStatusChanged` event published by Payment Initiation. The event contract is documented and versioned.

This was the most significant work. Resolving the data sharing required changes to both modules, migration of existing queries in Notification, and introduction of event infrastructure. The work took about three months of focused effort across both teams.

### Signal 3: Contract Stability

**Score: 4 (Strong)**

The public API has been stable for two release cycles. No breaking changes, no consumer coordination required. The team now has confidence in what the stable interface looks like.

### Signal 4: Team Autonomy

**Score: 5 (Excellent)**

With the shared schema resolved, the Payments team can deploy Payment Initiation completely independently. There is no remaining coordination friction with other teams for day-to-day work.

### Signal 5: Operational Impact

**Score: 3 (Adequate)**

No significant change. The module continues to operate within acceptable parameters.

---

## Reassessment Outcome: Extract

The signals have shifted. Data ownership moved from 2 to 4. Contract stability moved from 3 to 4. Team autonomy moved from 4 to 5. The blockers that prevented extraction six months ago are resolved.

The outcome is now **Extract**: the module is ready to become an independent service.

The team proceeded with extraction planning:

1. Define the service boundary (aligned with the existing module boundary)
2. Set up independent deployment infrastructure
3. Migrate consumers to call the new service
4. Retire the module from the monolith

---

## What the Model Prevented

The five-signal assessment prevented a premature extraction that would have created significant problems.

If the team had extracted Payment Initiation at Month 0, they would have extracted a service with a shared database dependency. That dependency would have been harder to resolve across service boundaries than inside the monolith. The team would have faced a choice between maintaining the shared database (which negates isolation benefits) or migrating data between services (which introduces consistency challenges and operational complexity).

By hardening first, the team resolved the data ownership problem in a simpler context, then extracted with confidence.

---

## What the Model Enabled

The model also prevented excessive caution. A different team might have looked at the shared schema and concluded that extraction was too risky to consider. The five-signal assessment gave the team a structured path: here is what needs to change, here is how to measure progress, and here is when to reassess.

Six months later, the team had evidence that extraction was viable. The decision was based on observable signals rather than intuition or trend-following.

---

## Related

- [domain-map.md](domain-map.md): Module boundary documentation
- [ownership-registry.md](ownership-registry.md): Governance status for each module
- [evolution-timeline.md](evolution-timeline.md): Full twelve-month timeline
- [/decision-model/five-signals.md](../../decision-model/five-signals.md): Signal definitions
- [/decision-model/outcomes/harden.md](../../decision-model/outcomes/harden.md): Harden outcome guidance
- [/decision-model/outcomes/extract.md](../../decision-model/outcomes/extract.md): Extract outcome guidance
