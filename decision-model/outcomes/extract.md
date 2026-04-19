# Outcome: Extract to a Service

Extraction moves a component from within a monolith or modular
monolith to an independent service with its own deployment lifecycle,
its own data store, and its own operational footprint.

Extraction is a significant investment. The operational overhead of
running an additional service has to be justified by the benefits,
and if those benefits do not materialise, the extraction has added
cost without adding value. The model is conservative about
recommending extraction for exactly this reason.

---

## When this outcome applies

Extraction is appropriate when all of the following hold:

- The component currently lives in a monolith or modular monolith
- Code Isolation is High
- Data Ownership is High
- Contract Stability is High
- At least one genuine driver exists for extraction

The first three conditions are necessary but not sufficient. A
component with strong signals but no driver does not need extraction,
and extracting it anyway produces operational overhead without
corresponding benefit. The driver is what distinguishes a component
that is *ready* for extraction from a component that *should* be
extracted.

### Genuine drivers

**Independent scaling requirements**. The component has a load
profile meaningfully different from the rest of the system. Peak
demand for the component does not coincide with peak demand for the
monolith, or the resource characteristics (CPU, memory, I/O) differ
enough that sharing infrastructure is wasteful.

**Technology divergence**. The component would benefit from a
different runtime, language, or framework, and that divergence is
genuinely justified rather than aesthetic. A component that would
genuinely benefit from being written in Go when the monolith is
Java is a candidate. A component where someone on the team would
prefer to use a different language is not.

**Team autonomy needs**. Multiple teams are working on the
component, or one team's development velocity is materially
constrained by being coupled to others in the deployment pipeline.
This is harder to assess than it looks: teams often believe they
need autonomy when what they actually need is better internal
modularity.

**Organisational separation**. The component belongs to a part of
the business that is genuinely separate from the rest, to the point
that even operational concerns should not cross the boundary.
Acquired subsidiaries and regulated business units sometimes fit
this pattern.

**Regulatory or compliance requirements**. Separation is mandated
by external requirements that the current structure cannot satisfy.
This driver can justify extraction even when signals are not yet
High, because the requirement is not negotiable.

---

## When this outcome does not apply

Extraction is not appropriate in any of these situations, even when
other signals suggest readiness:

**Data Ownership is Low or Medium**. Extracting a component that
shares data with other components transforms a monolithic data
coupling into a distributed one, which is significantly harder to
resolve. Address the data coupling first.

**Contract Stability is Low**. Extracting a component with an
unstable interface amplifies the cost of every future change,
because every change now crosses a network boundary and affects
consumers in ways that are harder to coordinate.

**No genuine driver**. Extraction without a concrete benefit is
almost always the wrong call. The operational overhead is real
whether or not the benefits materialise. If the only reason for
extraction is that it seems like the natural next step, the answer
is to harden or modularise instead.

**Acute pressure**. Extraction is substantial work, and attempting
it during a period of acute business pressure or operational
distress tends to produce rushed extractions that inherit the
problems the team was trying to escape.

---

## How to implement

Extraction is not a single-step operation. A reasonable sequence
looks like this.

### Preparation

Confirm the preconditions. Run the full signal assessment, have it
reviewed by someone outside the owning team, and document the
drivers. If any of the necessary signals are not actually High,
stop and address them first.

Record the decision as an ADR. The ADR should capture what is being
extracted, why, the drivers supporting the decision, and the
rollback criteria if the extraction does not deliver the expected
benefits.

### Data separation

If the component does not already own its data outright, separate
it first. This usually means:

1. Identify every read and write touching the component's tables
   from outside the component
2. Route those accesses through the component's public interface
   rather than directly to the database
3. Verify that no cross-boundary foreign keys remain
4. Confirm that the data could move to a separate database without
   breaking anything

Only proceed once the data is genuinely owned.

### Interface hardening

The interface that will become a network boundary needs to be
genuinely stable. Add consumer-driven contract tests for every
consumer. Version the interface explicitly. Apply backwards
compatibility rules that assume changes will be harder to make once
consumers are across a network.

### Deployment separation

Create the independent deployment pipeline, including CI, release
automation, monitoring, alerting, and on-call arrangements. The
component should be running independently in production before any
consumer starts calling it across the network.

### Traffic migration

Migrate consumers one at a time. Keep the in-process path working
until all consumers have migrated successfully. Feature flags or
routing layers are useful for this.

### Retirement of the in-process path

Once all consumers are on the network path and stable, retire the
in-process path. Remove the code, remove the build configuration,
and update the architecture tests to enforce that the network path
is the only path.

### Post-extraction review

Six months after extraction, reassess. If the drivers that
motivated the extraction are being realised, the extraction is a
success. If they are not, consider whether consolidation is the
honest response. The commitment is to the right architecture, not
to a particular decision.

---

## Common pitfalls

**Extracting before the data is separated**. The most common
failure, and the one that converts a manageable problem into a
much harder one. Distributed data coupling is significantly harder
to unwind than monolithic data coupling.

**Extracting a component that is still evolving its contract**.
Every contract change now crosses a network boundary. Consumer
coordination costs rise, and the overhead of extraction is felt
continuously rather than as a one-time cost.

**Underestimating operational cost**. An independent service
requires independent monitoring, independent incident response,
independent deployment infrastructure, and independent on-call
coverage. These costs are real and ongoing.

**Extracting for the wrong reason**. "Because microservices" is not
a reason. The drivers above are reasons. If the team cannot articulate
which driver applies, the extraction is unlikely to deliver.

---

## Related outcomes

If signals are not yet ready for extraction, consider
[harden](harden.md) instead to prepare the component for potential
future extraction.

If the component is already extracted and the drivers did not
materialise, [consolidate](consolidate.md) is the honest response.
