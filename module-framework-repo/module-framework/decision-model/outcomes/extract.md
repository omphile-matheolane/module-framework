# Extract to a Service

**Trajectory:** Extract

**When It Applies:** Strong code, data, and contract isolation already exist. Independent scaling or team autonomy is a genuine driver.

## Prerequisites

Before extraction is viable, the component should demonstrate:

- **Code Isolation (4-5):** Clear boundaries, minimal inbound dependencies, no direct access to internals from outside the module
- **Data Ownership (4-5):** Owns its data entirely, no shared tables or schemas with other modules
- **Contract Stability (4-5):** Public interface has been stable for at least one or two release cycles, consumers are not breaking on updates
- **Team Autonomy (3-5):** A single team can develop and test the component with minimal coordination
- **Operational Impact (3-5):** The component is not a disproportionate source of incidents

If any of these signals score below 3, consider [Harden](harden.md) or [Modularise](modularise.md) first.

## When Extraction Makes Sense

Extraction is warranted when there is a genuine driver beyond the technical capability to extract. Common drivers include:

- **Independent scaling requirements:** The component needs to scale differently from the rest of the system, and that scaling cannot be achieved within the current deployment model
- **Team autonomy:** A team needs to deploy independently to maintain velocity, and the current monolith structure creates coordination bottlenecks
- **Technology divergence:** The component would benefit from a different technology stack, and that benefit outweighs the operational cost of running a separate service
- **Organisational boundaries:** The component is owned by a different part of the organisation and co-location creates friction

Extraction without a clear driver tends to create operational overhead without corresponding benefit.

## Execution Guidance

### 1. Define the service boundary

Document exactly what the service will own: which capabilities, which data, which contracts. This boundary should align with the module boundary that already exists.

### 2. Plan data migration

If the component has its own schema within a shared database, plan how that schema will be extracted. Options include:

- Schema-per-service in the same database cluster (simpler operationally, less isolated)
- Separate database instance (more isolated, more operational overhead)
- Gradual migration with a synchronisation period

### 3. Establish contracts

Formalise the contracts between the new service and its consumers. If the module already has a clean public API, this may be a matter of documenting what exists. If not, this is the moment to stabilise the interface.

### 4. Set up independent deployment

The service needs its own CI/CD pipeline, deployment configuration, and monitoring. This is where the operational cost of extraction becomes real.

### 5. Migrate consumers

Update consumers to call the new service instead of the module. Consider a phased rollout: route a percentage of traffic to the new service, monitor, and increase gradually.

### 6. Retire the module

Once all consumers have migrated and the new service is stable, remove the module from the monolith.

## Post-Extraction Review

After extraction, schedule a review at 30, 60, and 90 days to assess:

- Is latency acceptable? Inter-service calls are slower than in-process calls.
- Is the operational burden manageable? The team now has a service to run, not just code to maintain.
- Are the contracts holding? Unexpected coupling may surface once the service is live.

If the extraction is not delivering the expected benefits, [Consolidate](consolidate.md) is a valid next step.

## Related

- [Harden](harden.md): If boundaries or contracts need strengthening first
- [Consolidate](consolidate.md): If extraction creates more overhead than value
- [five-signals.md](../five-signals.md): Full signal definitions
