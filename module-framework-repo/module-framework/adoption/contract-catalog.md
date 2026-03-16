# Contract Catalog Template

**Related Discipline:** Design

**Purpose:** Document the contracts between modules, including APIs, events, commands, and queries.

---

## Instructions

Record each contract between modules in the system. A contract is any formal interface through which one module interacts with another: a synchronous API, an asynchronous event, a command, or a query.

Update the catalog when contracts are introduced, changed, or retired. Link to the canonical contract definition (OpenAPI spec, AsyncAPI spec, or interface definition) where available.

---

## System: [System Name]

**Last Updated:** [Date]

**Reviewed By:** [Names or Team]

---

## Contract Index

| Contract | Type | Provider | Consumer(s) | Version | Status | Spec Location |
|----------|------|----------|-------------|---------|--------|---------------|
| [Contract Name] | [API / Event / Command / Query] | [Module Name] | [Module Names] | [e.g., v2.1] | [Stable / Evolving / Deprecated] | [Link or path] |
| | | | | | | |
| | | | | | | |
| | | | | | | |

---

## Contract Types

| Type | Description | Typical Use |
|------|-------------|-------------|
| **API** | Synchronous request-response interface | Queries, commands requiring immediate response |
| **Event** | Asynchronous notification that something happened | Decoupled notifications, eventual consistency |
| **Command** | Asynchronous instruction to perform an action | Fire-and-forget operations, queued work |
| **Query** | Request for data (may be sync or async) | Data retrieval, reporting |

---

## Contract Status Definitions

| Status | Meaning |
|--------|---------|
| **Stable** | The contract has not had breaking changes in the last two release cycles. Consumers can depend on it. |
| **Evolving** | The contract is actively changing. Breaking changes may occur. Consumers should coordinate with the provider. |
| **Deprecated** | The contract is scheduled for removal. Consumers should migrate to the replacement. |

---

## Contract Detail Template

For each significant contract, create a detailed entry using this format:

### [Contract Name]

**Type:** [API / Event / Command / Query]

**Provider:** [Module Name]

**Consumer(s):** [Module Names]

**Version:** [e.g., v2.1]

**Status:** [Stable / Evolving / Deprecated]

**Spec Location:** [Link or path to OpenAPI, AsyncAPI, or interface definition]

**Description:**
[One or two sentences describing what this contract does and why it exists]

**Breaking Change Policy:**
[How breaking changes are communicated and managed. e.g., "Two sprint notice, versioned endpoint, sunset period for old version"]

**SLA (if applicable):**
[Availability, latency, throughput expectations]

**Change History:**

| Date | Version | Change | Breaking? |
|------|---------|--------|-----------|
| [Date] | [Version] | [What changed] | [Yes/No] |

---

## Observations

**Patterns observed:**
- [e.g., "Most contracts are stable, but Notification events are still evolving"]
- [e.g., "Payment Initiation API has three consumers, all internal"]

**Concerns:**
- [e.g., "No formal spec exists for Reconciliation API"]
- [e.g., "Two deprecated contracts have not been migrated yet"]

**Actions:**
- [e.g., "Create OpenAPI spec for Reconciliation API"]
- [e.g., "Complete migration from deprecated Notification v1 events"]

---

## Related

- [domain-map-template.md](domain-map-template.md): Module boundary documentation
- [ownership-registry.md](ownership-registry.md): Who approves contract changes
- [/decision-model/five-signals.md](../decision-model/five-signals.md): Contract stability as an evolution signal
