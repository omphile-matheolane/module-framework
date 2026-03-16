# Payment Processing Example: Domain Map

**System:** Payment Processing Platform

**Last Updated:** March 2026

**Reviewed By:** Payments Team, Finance Team, Platform Team

---

## Domain Map

| Module | Business Capability | Owning Team | Boundary Status | Key Dependencies | Notes |
|--------|---------------------|-------------|-----------------|------------------|-------|
| Payment Initiation | Receives and validates payment requests from channels | Payments | Clear | Payment Authorisation (sync), Notification (async) | Boundary enforced in CI via ArchUnit |
| Payment Authorisation | Handles authentication, fraud checks, approval workflows | Payments | Clear | Payment Initiation (called by) | Co-owned with Payment Initiation, same team |
| Notification | Sends payment status updates to customers | Customer Experience | Drifting | Payment Initiation (shared schema) | Schema sharing is legacy; migration planned |
| Reconciliation | Matches payments against settlement records | Finance | Clear | None (batch integration with core banking) | Operates independently, minimal coupling |
| Reporting | Provides payment data for regulatory and BI reporting | Unclear (Finance / Platform) | Unclear | All modules (reads from multiple sources) | Multiple teams access internals directly |

---

## Boundary Status Summary

| Status | Count | Modules |
|--------|-------|---------|
| Clear | 3 | Payment Initiation, Payment Authorisation, Reconciliation |
| Drifting | 1 | Notification |
| Unclear | 1 | Reporting |

---

## Observations

The domain map reveals three distinct situations:

**Payment Initiation and Payment Authorisation** are in good shape. Both have clear boundaries enforced in CI, and the Payments team owns both. These modules are candidates for evolution assessment when the team is ready to consider extraction.

**Notification** has a recognisable boundary, but it is drifting because of the shared schema with Payment Initiation. The schema sharing originated from a shortcut during initial development: both modules needed access to payment status, and sharing a table seemed simpler than designing a proper contract. That shortcut now blocks independent evolution. Any change to the schema requires coordination between the Payments team and the Customer Experience team.

**Reporting** is the most problematic. There is no clear boundary: multiple teams access its internals to build reports. Ownership is contested between Finance (who consumes the reports) and Platform (who built the infrastructure). The module is not a module in any meaningful sense; it is a collection of tables and queries that have accumulated over time.

---

## Recommended Actions

**Immediate (this quarter):**

1. **Clarify Reporting ownership.** Assign a single owning team, even if that ownership is temporary while the module is restructured. The current ambiguity is creating friction.

2. **Document the shared schema between Notification and Payment Initiation.** Even before migration, make the dependency explicit so both teams understand the constraint.

**Next quarter:**

3. **Migrate the shared schema.** Either Notification owns its copy of payment status (populated via events), or a clear data contract is established between the modules.

4. **Introduce a boundary for Reporting.** Define a public API for report data access. Redirect direct table access through the API. This will take time, but the first step is defining what the API should be.

---

## Change History

| Date | Change | Author |
|------|--------|--------|
| March 2026 | Initial domain map created | Payments Team |

---

## Related

- [ownership-registry.md](ownership-registry.md): Detailed ownership and governance tracking
- [decision-model-walkthrough.md](decision-model-walkthrough.md): Evolution assessment for Payment Initiation
- [/adoption/domain-map-template.md](../../adoption/domain-map-template.md): Template used for this map
