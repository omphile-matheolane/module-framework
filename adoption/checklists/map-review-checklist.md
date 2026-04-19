# Map Review Checklist

Use this checklist when running a quarterly review of the domain
map. The goal is to confirm that the map still reflects the actual
system, to surface any emerging boundary problems, and to record
any changes since the last review.

A review that finds nothing to change is still valuable, because
it is evidence that the map is being maintained rather than
ignored.

---

## Before the review

- [ ] The current domain map is accessible to all reviewers
- [ ] Changes to the system since the last review are summarised
  (new modules, retired modules, ownership changes, significant
  refactors)
- [ ] Observability data from the last quarter is available for
  reference (latency trends, error patterns, dependency heatmaps)
- [ ] The suppression list from architecture tests is available

---

## During the review: boundaries

- [ ] Every module in the current codebase is represented on the map
- [ ] Every module on the map corresponds to an actual module in
  the codebase
- [ ] Module boundaries on the map match the actual boundaries
  enforced by architecture tests
- [ ] Any emerging hotspots (modules with climbing change rates or
  growing dependencies) are identified
- [ ] Any modules that have grown beyond what their team can own
  coherently are flagged for possible split
- [ ] Any modules that have become too small to justify dedicated
  ownership are flagged for possible consolidation

---

## During the review: ownership

- [ ] Every module has a designated owning team on the map
- [ ] Ownership matches the ownership registry
- [ ] Any modules that have drifted into shared ownership are
  identified
- [ ] Any ownership changes since the last review are documented
  in the change log
- [ ] Any team reorganisations that affect ownership are
  reflected

---

## During the review: dependencies

- [ ] The dependency structure on the map matches what
  observability data shows at runtime
- [ ] Any new dependencies since the last review are documented
- [ ] Any runtime dependencies that do not appear on the map are
  investigated
- [ ] Any modules with an unusual number of inbound or outbound
  dependencies are flagged for discussion
- [ ] Cycles in the dependency graph are identified and tracked

---

## During the review: honesty check

- [ ] The map reflects the current state rather than the desired
  state
- [ ] Boundary violations are documented as known issues rather
  than omitted
- [ ] Contested or unclear ownership is marked as such
- [ ] The known-issues section matches what the team actually
  knows

The honesty check matters most. An aspirational map that shows the
architecture as it should be rather than as it is conceals
problems rather than surfacing them.

---

## After the review

- [ ] The map is updated to reflect the outcomes of the review
- [ ] The change log records what changed and why
- [ ] Any modules flagged for evolution assessment are scheduled
- [ ] The next review date is set
- [ ] Significant findings are communicated to the broader
  engineering organisation

---

## Red flags

If any of these show up during a review, treat them as signals
worth investigating rather than items to close:

- The map has not been updated between reviews, but the system
  has changed
- Multiple modules have "contested" or "unclear" ownership
- Architecture test suppressions have grown rather than shrunk
- Runtime dependency data shows dependencies that no team
  acknowledges
- The same modules appear in the hotspot list review after review
  without action
