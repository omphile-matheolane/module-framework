# Consolidate with Another Service or Module

**Trajectory:** Consolidate

**When It Applies:** The service creates coordination overhead without enough independent value to justify the separation.

## When Consolidation Makes Sense

Consolidation is the recognition that a service should not have been extracted, or that circumstances have changed enough that separation is no longer justified. Common indicators include:

- **High coordination overhead:** Changes to the service regularly require coordinated releases with other services, negating the autonomy benefit of separation
- **Low independent change rate:** The service rarely changes independently; most changes are driven by requirements in other parts of the system
- **Disproportionate operational burden:** The service requires monitoring, alerting, on-call coverage, and infrastructure management that exceeds its business value
- **Unclear ownership:** No team has clear ownership of the service, and it has become a shared burden
- **Data coupling that was never resolved:** The service still shares data with other services through synchronisation, replication, or shared databases, creating distributed data problems without the isolation benefits

Consolidation is not a failure. It is a legitimate architectural direction when the evidence supports it. The industry narrative of one-way evolution toward microservices does not match what happens in practice.

## Prerequisites for Consolidation

Before consolidation, ensure:

- **Clear target:** Identify which module or service will absorb the consolidating service. This should be the component with the strongest domain relationship.
- **Consumer inventory:** Document all consumers of the service and their usage patterns
- **Data inventory:** Understand what data the service owns and how it will be migrated or merged

## Execution Guidance

### 1. Choose the consolidation target

The target should be the module or service that has the strongest domain relationship with the consolidating service. If the service was originally extracted from a monolith module, that module is often the natural target.

### 2. Plan data consolidation

If the service owns data, plan how that data will be merged with the target:

- If the target already has related data, design the schema merge carefully to avoid duplication
- If the data is unique to the consolidating service, it can often be moved directly
- Consider a synchronisation period where both locations have the data, allowing for rollback

### 3. Migrate consumers

Update consumers to call the target instead of the consolidating service. This is typically simpler than extraction migration because the target already exists and has established patterns.

Options:

- Direct migration: update all consumers at once
- Proxy period: the consolidating service proxies calls to the target, allowing gradual consumer migration
- Feature flag: route traffic based on configuration, enabling controlled rollout

### 4. Retire the service

Once all consumers have migrated and the target is stable:

- Remove the service from deployment
- Archive the repository or merge it into the target's codebase
- Decommission the infrastructure

### 5. Update governance

Ensure the target module's ownership, contracts, and governance automation reflect the new responsibilities.

## Post-Consolidation Review

After consolidation, review at 30 days:

- Has coordination overhead decreased as expected?
- Is the target module's complexity manageable, or has it become overloaded?
- Are the consumers stable?

If the target becomes too large or complex, consider [Modularise](modularise.md) to introduce internal structure.

## Related

- [Modularise](modularise.md): If the consolidated module needs internal structure
- [Harden](harden.md): If the consolidated module needs boundary strengthening
- [five-signals.md](../five-signals.md): Full signal definitions
