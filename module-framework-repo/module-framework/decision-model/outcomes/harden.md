# Harden This Module

**Trajectory:** Harden

**When It Applies:** Already a module, but boundaries or contracts need strengthening before extraction becomes viable.

## When Hardening Makes Sense

Hardening is the appropriate step when a module exists and has recognisable structure, but one or more signals are not yet strong enough for extraction. Common indicators include:

- **Code Isolation is adequate but not strong (3):** The module has a boundary, but there are still some dependencies that should be removed or some internal classes that are accessed from outside
- **Data Ownership is developing (2-3):** The module shares some tables with other modules, and that sharing needs to be resolved
- **Contract Stability is developing (2-3):** The public API exists but has changed recently, and consumers have been affected by those changes
- **Governance is manual or incomplete:** Boundary rules are documented but not enforced in CI

Hardening is different from modularisation. Modularisation creates a module where none existed. Hardening improves a module that already exists but has weaknesses that would create problems if the module were extracted.

## What Hardening Involves

Hardening focuses on specific improvements to move signals from adequate (3) to strong (4-5):

- **Strengthening boundaries:** Removing remaining dependencies on internals, tightening access control
- **Resolving data sharing:** Separating shared schemas, introducing clear data ownership
- **Stabilising contracts:** Freezing the public API for a release cycle, versioning contracts
- **Automating governance:** Adding boundary tests to CI, blocking violations before merge

## Execution Guidance

### 1. Identify the weak signals

Review the five-signal assessment. Hardening should focus on the signals that are blocking extraction:

- If Code Isolation is weak, focus on removing inappropriate dependencies
- If Data Ownership is weak, focus on schema separation
- If Contract Stability is weak, focus on freezing and versioning the API
- If governance is manual, focus on automation

### 2. Strengthen code isolation

Find and remove dependencies on module internals:

- Use static analysis or boundary tests to identify violations
- Update callers to use the public API
- Tighten access modifiers to prevent future violations

If there are legitimate reasons for certain dependencies, document them as exceptions and ensure they are reviewed when the module evolves.

### 3. Resolve data sharing

If the module shares tables with other modules:

- Identify which tables are shared and why
- Determine ownership: which module should own each table?
- Plan the migration: introduce owned tables, migrate data, update queries
- Consider a synchronisation period for high-risk migrations

Data ownership work is often incremental. Start with the tables that cause the most friction and work outward.

### 4. Stabilise contracts

If the public API has been changing frequently:

- Declare a contract freeze for one or two release cycles
- Version the API so that breaking changes can be introduced without affecting existing consumers
- Document the contract clearly so consumers know what to depend on

Contract stability is as much about discipline as it is about technical measures. The team needs to commit to not changing the interface unless absolutely necessary.

### 5. Automate governance

If boundary rules are documented but not enforced:

- Add boundary tests (ArchUnit or equivalent) that verify the module structure
- Add these tests to the CI pipeline so they run on every merge request
- Configure the pipeline to block merges on violation

Manual governance does not scale. If the team grows from five to twenty engineers, documented rules will be forgotten. Automated rules survive.

### 6. Reassess

After hardening, rerun the five-signal assessment:

- Code Isolation should now be 4-5
- Data Ownership should now be 4-5, or at least have a clear plan
- Contract Stability should now be 4-5, measured against at least one stable release cycle

If signals are strong enough, the module may be a candidate for [Extract](extract.md). If signals remain weak, continue hardening or accept that the module should [Stabilise](stabilise.md) as-is.

## Timeline

Hardening is typically a focused effort lasting two to four months, depending on the severity of the issues. It should have a clear end point: the signals are strong enough for extraction, or the team decides to stabilise as-is.

## Related

- [Extract](extract.md): If signals become strong enough after hardening
- [Modularise](modularise.md): If the module does not yet exist and needs to be created
- [Stabilise](stabilise.md): If hardening reveals that extraction is not justified
- [five-signals.md](../five-signals.md): Full signal definitions
