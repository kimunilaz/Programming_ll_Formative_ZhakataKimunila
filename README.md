# REKOLT Planters' Cooperative Produce Tracker

Programming II formative project — ALCHE, Feb 2026 cohort.
Individual project, Java, console application.

## What this is

A console application that records produce deliveries from REKOLT's
smallholder members, applies a fixed payment rule set consistently,
answers the season-figure questions the treasurer used to work out by
hand, and generates a single Word document (`output/season-report.docx`)
containing one payment statement per member.

Delivery data lives only in memory for the duration of a run — no file
persistence between runs is in scope for this assessment.

## Prerequisites

- JDK 17 or later
- Maven
- Git

## How to build

```
mvn clean package
```

This compiles the project and resolves the Apache POI dependency used
for Word document generation.

## How to run

```
mvn exec:java -Dexec.mainClass="mu.rekolt.app.ConsoleApp"
```

Or, after building a jar with `mvn clean package`:

```
java -cp target/classes;target/dependency/* mu.rekolt.app.ConsoleApp
```

(On macOS/Linux, replace the `;` classpath separator with `:`.)

## Design decisions

### Numeric types

- **`massKg` is `double`**, not `int` — the sample run shows fractional
  masses (e.g. `412.5` kg), so a whole-number type would silently lose
  precision.
- **`qualityScore` and `week` are `int`** — both are explicitly required
  to be whole numbers by the spec (a decimal quality score or week is
  rejected as invalid input), so `int` enforces that at the type level
  rather than relying only on validation.
- **All monetary values (`baseValue`, `netPayable`, commission, levy,
  member totals) are `double`, kept unrounded through every intermediate
  step.** Rounding to two decimal places happens only at the point of
  display, via `String.format("%.2f", ...)` — never mid-calculation —
  so repeated arithmetic on a delivery's value never accumulates
  rounding error.
- **No explicit casts were needed** in the final design: `Produce`,
  `Grade`, and `Delivery` are typed consistently enough (e.g.
  `qualityScore` is read as `int` everywhere it's produced and consumed)
  that no implicit widening/narrowing conversion was required.

### Package structure

- `app` — `ConsoleApp`: entry point and the menu loop only. Delegates
  all input validation to `util` and all business logic to `service`.
- `model` — `Produce` (abstract) and its three subclasses
  (`CerealProduce`, `PerishableProduce`, `CashCropProduce`), `Grade`
  (enum), `Member`, `Delivery`, and the `Payable`/`Reportable`
  capability interfaces.
- `service` — `SeasonService` (owns the season's collections and
  aggregation logic: totals, weekly grid, sorting, search, REJECT
  filtering) and `ReportService` (builds the `.docx` season report).
- `util` — `InputValidator`: every validated console-input prompt
  (member id, name, produce code, mass, quality score, week).

### Why this shape

Payment arithmetic lives on `Delivery`/`Produce`/`Grade`, not in the
console layer — this is what let the Objective 5 refactor move logic
out of `ConsoleApp` without rewriting the arithmetic itself, and it's
what makes the polymorphic reporting pass (`Delivery`/`Member` both
implementing `Reportable`) possible with zero `instanceof` or
downcasting.

## Repository conventions

- One feature branch per objective, merged into `main` with
  `git merge --no-ff`.
- Tags `v0.1` through `v1.0` mark each merged objective.
- `docs/` holds design PDFs, screenshots, and rationale write-ups.
- `output/` holds the generated report and run log — never hand-edited.

## Status

- [x] Objective 1 — Environment and arithmetic
- [x] Objective 2 — Control flow and version control
- [x] Objective 3 — Collections
- [x] Objective 4 — On-paper design
- [x] Objective 5 — Abstraction and inheritance
- [x] Objective 6 — Writing Word documents