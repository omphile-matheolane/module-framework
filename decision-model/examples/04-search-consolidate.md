# Example 4: Search — Consolidate

A search service at a mid-sized marketplace. It was extracted from
the monolith 18 months ago to support independent scaling during a
period of rapid traffic growth. Traffic patterns have since
normalised, the anticipated scaling requirement did not materialise,
and the team that extracted the service was reorganised out of
existence in the last restructuring. The service now runs with
shared on-call coverage and no clear ownership.

---

## Component under assessment

**Component name**: Search

**Current deployment model**: Independent service

**Owning team**: Platform Team (by default, after the original
Search Team was dissolved)

**Assessors**: G. Director, H. Staff Engineer

**Date of assessment**: 2026-01-28

**Previous assessment**: 2024-08-15 (recommended extract;
extraction completed Q4 2024)

---

## Signal assessments

### 1. Code Isolation: **High**

**Evidence**:
- Clear service boundary with a well-defined API
- Internal code is fully encapsulated (service boundary makes this
  automatic)
- Consumers access search only through the documented API

**Notes**: Extraction enforces isolation automatically.

---

### 2. Data Ownership: **High**

**Evidence**:
- Search owns its own index and cache, stored in a dedicated
  Elasticsearch cluster
- No other service has direct access to the index
- Source data is pushed to search through an event stream; search
  never reaches into the monolith's database

**Notes**: Clean ownership.

---

### 3. Contract Stability: **High**

**Evidence**:
- No breaking changes to the search API in the last 12 months
- All consumers covered by contract tests
- Versioning policy applied consistently

**Notes**: The contract has been stable. This is working well.

---

### 4. Team Autonomy: **Low**

**Evidence**:
- The original Search Team no longer exists
- The Platform Team covers search among many other responsibilities
- Changes to search are made whenever someone on Platform has time
- Deployment cadence has dropped from weekly to roughly monthly

**Notes**: The team autonomy that extraction was meant to enable
has effectively regressed below what the monolith would offer.

---

### 5. Operational Impact: **Low**

**Evidence**:
- Three incidents attributable to search in the last quarter (two
  were Elasticsearch cluster issues, one was a deployment
  regression)
- Latency is within SLO but p99 has drifted upward over the last
  six months
- On-call burden for the search cluster is disproportionate given
  how rarely the service is changed

**Notes**: The cluster itself is a source of ongoing operational
cost, and nobody is paying focused attention to it.

---

## Summary

| Signal | Rating |
|--------|--------|
| Code Isolation | High |
| Data Ownership | High |
| Contract Stability | High |
| Team Autonomy | Low |
| Operational Impact | Low |

---

## Driver assessment

Reassessing the drivers that motivated the original extraction:

- [ ] Independent scaling requirements — original driver; traffic
  growth did not materialise, and search load is now well within
  what shared infrastructure would handle
- [ ] Technology divergence — search used Elasticsearch, but so does
  the rest of the monolith's search within other features
- [ ] Team autonomy needs — original driver; the team that was
  meant to exercise this autonomy no longer exists
- [ ] Regulatory requirements
- [x] Cost reduction — the Elasticsearch cluster is a meaningful
  monthly cost, and the operational burden could be substantially
  reduced
- [x] Operational burden disproportionate to business value — the
  on-call cost exceeds the value the team is currently extracting
  from independent deployment
- [ ] Frequent change concentrated in this component
- [ ] Multiple teams working on this component regularly

**Driver summary**:

The drivers that originally justified the extraction have evaporated.
Traffic growth did not materialise as projected. The team that was
meant to benefit from autonomy no longer exists. What remains is an
independent service with ongoing operational and infrastructure
costs and no meaningful benefits relative to running the
functionality inside the monolith.

---

## Outcome

**Recommended outcome**: Consolidate

**Reasoning**:

The technical signals are healthy: Code Isolation, Data Ownership,
and Contract Stability are all High. Under normal circumstances
those would support continued independent operation or further
evolution. The problem is that the benefits of being a service are
not being realised, and the costs are.

Team Autonomy is Low, which is striking for an extracted service.
The whole point of extraction is to enable autonomous teams to
move faster. Without a team dedicated to search, that autonomy
produces no value. It is purely cost.

Operational Impact is also Low (unhealthy). The service is
contributing to incidents and consuming on-call attention
disproportionate to its business value.

The original drivers no longer apply. Consolidation is not a
failure of the original decision; it is an honest response to
changed circumstances. The extraction made sense in 2024 given
the assumptions at the time. Those assumptions turned out to be
wrong, and consolidation corrects for that.

---

## Implementation plan

**Next actions**:

1. ADR to record the consolidation decision and reference the
   original extraction ADR
2. Identify the absorbing component (proposed: the main
   marketplace module, which is the largest consumer of search)
3. Stand up an in-process search implementation using the
   existing Elasticsearch cluster, with consumers migrating
   gradually
4. Once all consumers have migrated to the in-process path, retire
   the search service's deployment pipeline
5. Evaluate whether the Elasticsearch cluster can be consolidated
   with other search infrastructure or run at reduced scale
6. Remove the service's on-call rotation

**Estimated effort**: Two quarters, because the Elasticsearch
cluster consolidation will take care.

**Dependencies**: All search consumers need to migrate before the
service is retired.

**Target completion**: End of Q3 2026.

---

## Reassessment trigger

**Scheduled reassessment date**: Q2 2027

**Events that would trigger earlier reassessment**:
- Traffic growth materialises unexpectedly
- A dedicated search team is reformed
- Search requirements diverge significantly from the rest of the
  marketplace

---

## Sign-off

**Assessor A**: G. Director

**Assessor B**: H. Staff Engineer

**Decision recorded as ADR**: ADR-0061 (Search consolidation)
