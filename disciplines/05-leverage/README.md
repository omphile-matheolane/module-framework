# 5. Leverage Observability

## What this discipline involves

The Leverage discipline is concerned with instrumenting systems to
reveal how they actually behave at runtime. Observability covers
metrics, logs, and traces that show how the system performs, how
components interact, and where problems tend to concentrate.

From an architectural standpoint, observability is not simply about
operational monitoring. It is about understanding whether the
architecture is functioning as intended. Source code shows how the
system was designed. Observability shows what the system is actually
doing.

---

## Why it matters

Design assumptions do not always survive contact with production. A
module that appears well-isolated in the code may have runtime
dependencies that were never anticipated. A contract that looks
stable may degrade under load. A boundary that seems clear from static
analysis may be violated through dynamic behaviour that no build-time
tool can detect.

Observability provides the empirical grounding needed to validate
architectural assumptions against reality. It shows which modules are
called most frequently, which calls are slowest, where errors
concentrate, and how dependencies actually flow at runtime. That data
informs decisions about where to invest optimisation effort, where
boundaries need strengthening, and when a component has drifted far
enough from its intended behaviour that evolution is warranted.

---

## How to practice it

### Emit structured logs with module identifiers

Logs should carry metadata indicating which module produced them,
which makes it possible to filter and aggregate by module and surface
patterns that would be invisible in undifferentiated log output.

A minimal structured log entry for this purpose might look like:

```json
{
  "timestamp": "2026-04-19T14:32:11.482Z",
  "level": "INFO",
  "module": "payments",
  "trace_id": "a1b2c3d4e5f6",
  "message": "payment authorised",
  "customer_id": "cust_1234",
  "amount_cents": 4500
}
```

The module field is the addition that matters for architectural
purposes. Standardising on it across the codebase enables every
subsequent discipline practice in this section.

### Measure latency across module boundaries

Instrumenting the points where modules interact reveals which
interactions are performing well and which are becoming bottlenecks.
This is often where the most actionable signal lives.

The pattern that tends to work: when module A calls module B, record
the latency of the call on A's side. You now have a distribution of
latencies per caller-callee pair, which you can track over time and
set alerts against.

### Track error rates by module

Aggregating errors at the module level identifies which components
are contributing most to system instability and focuses attention
where it is actually needed rather than spreading it evenly across
the system.

A module that produces an outsized share of errors relative to its
traffic share is a candidate for investigation, and over time the
trend tells you whether the situation is improving or getting worse.

### Implement distributed tracing

Traces that follow requests across module boundaries reveal the
actual runtime dependency structure, showing which modules participate
in serving a given request and how time is distributed among them.
OpenTelemetry has become the de facto standard here, with SDKs and
instrumentation libraries across most major languages, which makes it
a reasonable default regardless of the underlying stack.

Distributed tracing frequently surfaces dependencies that were not
apparent from reading the code. A module that, according to the
source, has no dependency on a particular subsystem may nevertheless
call it at runtime through some indirect path. Traces show this
clearly in ways that static analysis cannot.

### Build architectural dashboards

Observability data should be available in a form that makes
architectural health visible to the team. Dependency patterns, latency
distributions, error concentrations, and throughput by module are the
kinds of signals that belong in a view the team looks at regularly,
not just during incidents.

A useful dashboard structure:

- **Per-module health overview**: error rate, latency percentiles, throughput
- **Module interaction heatmap**: which modules call which, with call volumes
- **Dependency flow**: the actual runtime call graph over a recent time window
- **Top error sources**: modules contributing most to recent errors
- **Latency hotspots**: interactions above defined latency budgets

The dashboard does not need to be elaborate. It needs to be looked at.

---

## What good instrumentation looks like

Not all observability data is equally useful for architectural
purposes. Some characteristics distinguish instrumentation that
supports architectural decisions from instrumentation that supports
only incident response.

**Module context on every span and every log line.** Without this,
data cannot be aggregated by module, and architectural questions
become hard to answer.

**Semantic conventions applied consistently.** OpenTelemetry's
semantic conventions provide a starting point. The specific
conventions matter less than the consistency of their application.

**Sampling that preserves representativeness.** Aggressive sampling
keeps costs down but can hide tail behaviour. The interactions that
matter most for architectural decisions are often in the tail. Adjust
sampling to preserve visibility into the tail where it matters.

**Retention long enough to see trends.** Architectural decisions
operate on timescales of months, not minutes. Data that is retained
only for operational use cases is usually not retained long enough to
support architectural analysis.

---

## Greenfield versus brownfield

### Greenfield

Observability can be built in from the start. Instrumentation is
added as modules are created, and the team develops habits of working
with visibility into system behaviour before the system is large
enough to be opaque.

A reasonable starting set for a new module: structured logs with
module and trace identifiers, basic latency and error rate metrics,
and participation in distributed tracing. That minimal set provides
meaningful signal from day one and scales to more sophisticated
instrumentation as the module grows.

### Brownfield

Observability is often incomplete or absent in the areas that matter
most. Introducing it into existing systems means instrumenting code
that was not designed with it in mind, which requires some care.

The practical approach is to start with the most critical paths and
expand coverage progressively. The return on this investment tends to
be quick: the first dashboards often reveal genuine surprises about
how the system is actually behaving, surprises that no amount of
reading the source code would have uncovered.

A common pattern: instrument the top five most critical user flows
end-to-end, build dashboards that show their health, and use the
insights to prioritise the next round of instrumentation. This tends
to produce better returns than trying to instrument everything
uniformly.

---

## Failure modes and anti-patterns

### Collecting without analysing

Observability infrastructure gets put in place, data flows into
dashboards and log aggregators, and then nobody looks at it
systematically. Collection without interpretation provides no real
value. The remedy is to schedule regular architectural reviews that
consult the data, and to tie dashboard review into existing rituals
such as sprint retrospectives or on-call handovers.

### Too much data

Observability systems can generate overwhelming volumes of signal,
and without aggregation, filtering, and summarisation, the genuinely
useful information gets lost. The goal is insight, not volume. A
dashboard with five carefully chosen metrics is often more useful
than one with fifty.

### Data without architectural context

If logs and traces do not include module identifiers or dependency
information, they can support incident response but they cannot tell
the team whether the architecture is working. The instrumentation
needs to be designed with architectural questions in mind from the
start.

### Reactive use only

Consulting observability data only during incidents is common but
leaves most of the architectural value unclaimed. Proactive analysis,
looking for emerging patterns before they become problems, is where
the discipline earns most of its return. A module whose latency has
been drifting upward for three months is a problem waiting to happen,
and catching it at month two is considerably cheaper than catching it
at month six.

---

## Connections to other disciplines

Observability data informs [Map](../01-map/) by revealing whether
boundaries are functioning as intended in practice. Runtime
dependencies that contradict the documented map are a signal that
either the map or the implementation has drifted.

It feeds [Evaluate](../06-evaluate/) by providing the runtime signals
that evolution decisions should be grounded in. The Data Ownership,
Contract Stability, and Operational Impact signals all benefit from
observability data.

It validates the contracts defined in [Design](../03-design/) by
showing whether they are actually being fulfilled under real
conditions. A contract that specifies a latency budget is only a
contract if that budget is measured.

And it complements the static enforcement that [Unify](../04-unify/)
provides with a layer of runtime visibility that build-time checks
alone cannot supply. Some violations only show up at runtime, and
only observability catches them.

---

## Further reading

*Observability Engineering* by Charity Majors, Liz Fong-Jones, and
George Miranda is the practical reference for modern observability
practice. It treats observability as a discipline rather than a set
of tools, which aligns well with how MODULE frames it.

The OpenTelemetry documentation is the authoritative source for the
specific mechanics of distributed tracing, and its semantic
conventions are worth internalising even if you end up using them
selectively.

For the specific case of tracing in service-oriented systems, *Distributed
Tracing in Practice* by Austin Parker and colleagues is a solid
reference that covers both the concepts and the implementation
concerns.
