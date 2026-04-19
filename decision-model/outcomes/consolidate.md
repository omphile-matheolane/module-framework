# Outcome: Consolidate

Consolidation moves a component from an independent service back into
a modular structure within another service or the main application.
It reverses an earlier extraction decision when the circumstances
that justified the extraction no longer apply, or when the
extraction did not deliver the benefits that motivated it.

Consolidation is often resisted because it feels like admitting that
a previous decision was wrong. That framing is not particularly
useful. Extracting a service was a hypothesis: that extraction would
provide benefits exceeding its costs. If the hypothesis proved
incorrect, consolidation is the appropriate response. Maintaining a
service that consistently costs more than it provides is the more
problematic position.

---

## When this outcome applies

Consolidation is appropriate when all of the following hold:

- The component is currently an independent service
- The operational overhead of running it separately is real
- No genuine driver for independent deployment currently applies
- The service can be absorbed into another service or the main
  application without creating new problems

Concretely, consolidation tends to be the right call in these
situations:

**The service creates coordination overhead without independent
value**. Teams spend time coordinating releases across the service
and its consumers, but the service does not actually scale
differently, use different technology, or support meaningful team
autonomy. The separation is costing coordination effort without
returning anything.

**The drivers that motivated extraction no longer apply**. The
service was extracted because of a scaling requirement that has
since been addressed through other means. Or the team autonomy need
that motivated extraction has disappeared because the teams
reorganised. The original rationale is no longer valid, and the
service is still paying the operational cost.

**The service was extracted prematurely**. The boundaries were not
ready, the data was not separated, and the extraction created more
problems than it solved. Consolidating and reworking internally is
often cleaner than continuing to paper over the original mistake.

**The operational burden exceeds the business value**. The service
is expensive to run, frequently appears in incidents, and consumes
on-call attention disproportionate to what it contributes. Even if
the technical signals look reasonable, the ongoing cost does not
justify the separation.

---

## When this outcome does not apply

Consolidation is not appropriate in these situations:

**A genuine driver still applies**. The service meets a regulatory
requirement, serves a materially different scaling profile, or
supports a team that would be constrained without it. Removing the
separation would recreate the problems that motivated the extraction
in the first place.

**The component has grown substantially since extraction**.
Consolidating a service that has doubled or tripled in scope since
extraction means absorbing a large codebase into another one. That
absorption can create more problems than it solves.

**The consuming components are themselves unstable**. Consolidating
into a shaky target makes the consolidated whole worse. Stabilise
the destination first.

**The consolidation would violate clear domain boundaries**. If the
service is genuinely a separate domain, consolidating it into an
unrelated component creates a module whose domain definition is
incoherent. The right answer in this case may be to consolidate into
a different target, or to accept that the service deserves to stay
independent even if its signals look marginal.

---

## How to implement

Consolidation is roughly the reverse of extraction, but it tends to
encounter organisational resistance that extraction does not. Teams
may have formed around the service. Infrastructure may have been
provisioned specifically for it. These factors need to be worked
through alongside the technical work.

### Preparation

Record the decision as an ADR. The ADR should reference the original
extraction decision, explain what has changed, and describe the
expected benefits of consolidation. Being specific about the
expected benefits is important, because consolidation is often
politically contested and a vague rationale will not hold up under
scrutiny.

Identify the absorbing component. This is usually the consumer that
uses the service most heavily, or a component in the same domain.
The choice matters: the absorbing component inherits the code and
the data.

### Consumer migration

If the service has multiple consumers, the ones that are not being
absorbed need an alternative. Usually this means exposing the
consolidated functionality through the absorbing component's
interface, or providing a transitional API during migration. Running
the old and new paths in parallel during migration is standard.

### Data merger

Merge the data back into the absorbing component's store. This is
often the most involved part of the work. Foreign key relationships
need to be restored where they were broken by extraction. Schema
migrations need to preserve existing data. Downtime should be
minimised, but some careful coordination is usually unavoidable.

### Code absorption

Move the service's code into the absorbing component as an internal
module. Apply the [Organise](../../disciplines/02-organise/) and
[Design](../../disciplines/03-design/) disciplines to make the
absorbed module a genuine module rather than a dumping ground.
Architecture tests should be updated to enforce the new internal
boundary.

### Infrastructure retirement

Tear down the service's deployment infrastructure: CI pipelines,
monitoring, alerting, on-call rotations, environments. This is
often what makes the benefits of consolidation tangible, so it
should not be deferred indefinitely.

### Post-consolidation review

Three to six months after consolidation, reassess. The operational
savings should be evident. If they are not, something went wrong,
and it may be worth understanding what.

---

## Common pitfalls

**Consolidating without resolving why extraction failed**. If the
original extraction was premature because of data coupling, and the
consolidation simply restores that coupling without resolving it,
the underlying problem is still there. Use consolidation as an
opportunity to fix the root cause, not just to reverse the
deployment change.

**Absorbing into the wrong target**. Consolidating a service into a
component that does not share its domain creates a module that is
hard to reason about and harder to evolve. Think carefully about
where the consolidated functionality genuinely belongs.

**Preserving the service's internal structure wholesale**. The
service's structure was shaped by being a service: defensive
coding, extensive serialisation, retries and circuit breakers at
every call site. Most of that is unnecessary once the functionality
is in-process. Consolidation is a good opportunity to simplify.

**Political resistance that delays the actual work**. Teams may
fight consolidation even when the evidence is clear. The remedy is
usually to be explicit about the costs of the status quo and the
expected benefits of consolidation, and to track those benefits
after the work completes. The framing that matters: consolidation
is not an admission of failure, it is a response to changed
evidence.

---

## Related outcomes

If the service signals are actually healthy and a driver exists,
the right outcome may be [stabilise as-is](stabilise.md) rather
than consolidation.

If the service could be consolidated but the target component is
not ready to absorb it, consider [harden](harden.md) the target
first, then revisit.
