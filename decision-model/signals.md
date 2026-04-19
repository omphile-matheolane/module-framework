# The Five Signals

Each signal is assessed at Low, Medium, or High against concrete
evidence. The anchors below are starting points rather than definitive
rubrics. Teams should adapt them to their own context, but they should
do so deliberately, documenting the calibration decisions so that
future assessments apply the same standard.

Ratings without evidence are not useful. Every rating should be
supported by specific, verifiable facts: architecture test results,
incident counts over a named period, deployment frequencies, consumer
counts. This is what makes the assessment auditable and what lets
disagreements be resolved through evidence rather than opinion.

---

## Signal 1: Code Isolation

Code Isolation captures whether the component has clear boundaries,
well-defined and minimal dependencies, and a stable coherent public
interface.

### Low

The component has no clearly defined public interface, or its
internals are accessed directly from other components. Architecture
tests either do not exist for the component or produce substantial
violations. Static analysis reveals dependencies that flow both into
and out of the component in ways that suggest the boundary is more
notional than real.

Typical evidence: no ArchUnit tests for the component, or tests
exist but suppress dozens of known violations. Dependency graph shows
bidirectional edges with multiple other components. Consumers import
classes from deep inside the component's internals.

### Medium

A public interface exists and covers most interactions, but exceptions
remain. Some internals are accessed from other components either
deliberately or through historical accident. Architecture tests exist
but suppress a meaningful number of known violations. Dependencies
are mostly one-directional but with occasional exceptions.

Typical evidence: a defined public package or facade exists, but a
handful of internal classes are still imported from outside. Tests
cover the rule but have a documented suppression list with somewhere
between one and ten entries.

### High

The public interface is the only entry point to the component,
enforced by automated tests that run on every build with zero or
near-zero suppressions. Static analysis confirms that no component
reaches around the interface, and cyclic dependencies with other
components are absent.

Typical evidence: architecture tests pass with no suppressions, or
with a small number of suppressions that are documented and
time-bounded. Dependency graph is cleanly one-directional. New
consumers can be onboarded without needing to understand any
internals.

---

## Signal 2: Data Ownership

Data Ownership captures whether the component owns its data, or
whether it shares databases, tables, or schemas with other components.

Data Ownership is the hardest constraint in the decision model.
Shared data is the strongest practical blocker to extraction,
regardless of how well-isolated the code appears.

### Low

The component shares mutable tables or schemas with one or more other
components. Multiple components can read and write the same data, and
no component has authoritative control over any given table.
Database-level foreign keys cross component boundaries freely.

Typical evidence: the component's ORM models reference tables that
are also referenced from other components' models. Schema migrations
are coordinated across teams. Extracting the component would require
resolving which other components would lose access to which tables.

### Medium

The component has logical ownership of its data, but physical
separation is incomplete. It may share a database instance with
other components while owning distinct schemas, or it may have
separate tables but with cross-component foreign keys that prevent
independent migration. Read access may extend beyond the owning
component even if write access is controlled.

Typical evidence: the component owns its tables by convention but
other components read from them directly for reporting or
cross-functional queries. Foreign keys exist but point inward from
other components' tables.

### High

The component owns its data outright. No other component writes to
its tables, and read access is either absent or mediated through the
component's public interface. Physical separation is sufficient that
the data could move to a separate database without breaking anything
that depends on it.

Typical evidence: no other component has credentials to write to the
tables. Queries from other components go through the component's
API, not directly to the database. Foreign key relationships do not
cross component boundaries.

---

## Signal 3: Contract Stability

Contract Stability captures how frequently the public interface
changes and whether those changes tend to break consumers. A stable
contract can be depended upon with confidence. An unstable one
amplifies the cost of any evolution that crosses the contract
boundary.

### Low

The public interface has seen more than one breaking change in the
last six months, or minor changes have repeatedly caused downstream
failures. No versioning policy exists, or exists but is not
followed. Consumers complain about integration surprises.

Typical evidence: multiple incident postmortems cite unexpected
interface changes. Consumers have adopted defensive practices such
as version pinning or extensive mocking because direct integration
is unreliable. No API changelog exists, or the changelog exists but
does not reflect actual changes.

### Medium

The interface is mostly stable, with occasional breaking changes
that are handled through versioning. A versioning policy exists and
is generally followed, but compliance is inconsistent. Consumer-driven
tests exist for some consumers but not all.

Typical evidence: the interface has one or two breaking changes per
year, handled through explicit versioning. Most consumers are
covered by contract tests but some integrations rely on informal
agreements. Deprecation notices are given but timelines are not
always enforced.

### High

No breaking changes have occurred in the last six to twelve months.
A versioning policy is consistently applied. Consumer-driven contract
tests cover all significant consumers, and new versions are
introduced with documented deprecation timelines rather than by
modification.

Typical evidence: the API changelog shows only additive changes for
the past year. All consumers are covered by Pact or equivalent
contract tests. Deprecations follow a documented timeline and old
versions are retired on schedule.

---

## Signal 4: Team Autonomy

Team Autonomy captures whether a single team can develop, test, and
deploy the component independently, or whether every change requires
coordination across teams.

### Low

Most deployments require coordination with other teams. The owning
team cannot ship changes without synchronisation points with at
least one other team. Deployment windows are shared rather than
independent, or deployment approvals cross team boundaries regularly.

Typical evidence: deployment checklists include handoffs to other
teams. Release calendars coordinate with multiple team schedules.
Changes to the component routinely require pull requests against
other components' repositories.

### Medium

Routine deployments proceed independently, but non-trivial changes
require coordination. The team owns its deployment pipeline but
invokes it together with other teams for changes that touch shared
concerns. Cross-team coordination occurs weekly or monthly rather
than per-deployment.

Typical evidence: standard feature work ships independently, but
schema changes, contract evolution, or infrastructure updates trigger
cross-team planning. A handful of deployments per month involve
cross-team coordination.

### High

The team deploys independently on its own schedule with no
synchronisation with other teams required for standard work.
Coordination is rare and limited to genuinely shared changes such as
protocol upgrades or significant contract evolution.

Typical evidence: the team ships on its own cadence without
referencing other teams' calendars. Cross-team coordination is an
exceptional event, occurring a handful of times per year rather than
per-release.

---

## Signal 5: Operational Impact

Operational Impact captures how the component contributes to
incidents, latency, and general operational burden.

This signal can override the others in either direction. A component
that is structurally sound but operationally burdensome may be a
candidate for consolidation. A component that is poorly isolated but
operationally stable may be a candidate for modularisation rather
than extraction.

Note that the rating conventions here are inverted relative to
intuition: Low Operational Impact is the *problem* state (the
component creates operational burden), and High Operational Impact
is the *healthy* state. This mirrors how the other signals are
rated: High is always the state that favours structural stability
or evolution readiness, Low is always the state that indicates
problems.

### Low

Frequent source of incidents (more than one significant incident
attributable to this component per quarter), latency contribution is
a regular concern, or on-call burden from this component is
disproportionate to its business value.

Typical evidence: incident tracker shows the component appearing in
multiple postmortems per quarter. Latency dashboards consistently
flag the component as a bottleneck. On-call engineers report that
the component accounts for an outsized share of paging volume.

### Medium

Occasional incidents (one or two per quarter), latency is acceptable
but not exemplary, on-call burden is present but manageable.
Component health is adequate but not strong.

Typical evidence: the component appears in incident reports
occasionally but not routinely. Latency is within targets most of
the time but has occasional spikes. On-call engineers acknowledge
the component exists but do not single it out as a problem area.

### High

Rare incidents (less than one per quarter), latency is within
targets consistently, on-call burden is minimal. The component is
operationally quiet.

Typical evidence: the component has not featured in a postmortem in
the last quarter. Latency metrics show consistent adherence to
targets. The component is rarely mentioned in on-call discussions
because it does not require attention.

---

## Practices for reducing assessment variance

Calibration rubrics help, but they do not eliminate variance on
their own. Three practices tend to reduce variance further in teams
that assess regularly.

### Paired assessment

Two people assess the same component independently and then
reconcile differences. Disagreement surfaces early, and the
reconciliation forces explicit discussion of the evidence rather
than leaving interpretation implicit. The resulting assessment tends
to be more defensible than either individual one.

### Explicit evidence citation

Every rating is supported by specific, verifiable facts. Ratings
without evidence tend to reflect the assessor's mood or prior beliefs.
Requiring evidence makes the rating auditable and makes
disagreements easier to resolve, because the disagreement becomes
about what the evidence shows rather than what the component feels
like.

### Periodic recalibration across teams

When multiple teams are applying the model, their interpretations
will drift apart over time. A quarterly session where teams compare
their ratings against a shared component library, or against each
other's components, surfaces calibration drift and corrects it
before the model loses its consistency. The session does not need
to produce identical ratings across teams. It needs to produce a
shared understanding of what the levels mean.
