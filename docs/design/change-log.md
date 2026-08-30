# Design Change Log — deviations from design-v1

Comparing the rough pre-implementation sketch (design-v1) against what was
actually built (design-v2 / the real codebase), now that design-v1 exists
as a genuinely separate, earlier document rather than one written after
the code.

| Change | Reason |
|---|---|
| `CropItem` (abstract) → `Produce` | Renamed once real coding started — "Produce" matches the brief's own vocabulary more directly than the working label used in the rough sketch. |
| `GrainCrop` / `PerishableCrop` / `ExportCrop` → `CerealProduce` / `PerishableProduce` / `CashCropProduce` | Renamed to match the three category names actually used in the brief's payment rule table, rather than the informal names used before the rule table was studied closely. |
| `QualityGrade` (enum) → `Grade` | Simplified naming; no functional change — still carries A/B/C/REJECT and each grade's multiplier exactly as sketched. |
| `Batch` → `Delivery` | Renamed to match the brief's own terminology once it was clear "delivery" was the term used throughout the spec, not "batch". |
| `Farmer` → `Member` | Renamed to match the cooperative's own terminology ("member"), used consistently in the brief and the sample run. |
| `SeasonRecords` → `SeasonService` | Renamed during the Objective 5 packaging pass to match the `service` package's naming convention. |
| `DocumentBuilder` → `ReportService` | Same reasoning — aligned with the `service` package convention rather than the earlier working name. |
| `MenuController` → `ConsoleApp` | Renamed; functionally identical — still just the menu loop and console I/O, nothing else. |
| `PaymentRulesEngine` dropped as a separate class | Design-v1's rationale (§11.1) explicitly left this undecided ("might get folded in"). Once `Produce` and `Grade` existed and already carried everything the calculation needed, the pricing logic fit naturally as `Delivery.netPayable()` instead, avoiding a class with only one caller. |
| `Farmer`/`Member` does not hold its own list of deliveries | Design-v1's rationale (§11.3) left this as an open question. Resolved in favour of `SeasonService` owning a `Map<String, List<Delivery>>` — keeping all season-level bookkeeping in one place rather than spreading delivery-tracking across individual `Member` objects. |
| A `Produce.create(...)` factory method was added | Not present anywhere in design-v1's rough class diagram. Became necessary once building the abstraction properly — something has to decide which concrete subclass to construct from a produce code, and this is the one deliberate place that decision is made, keeping every other part of the code working with `Produce` polymorphically. |
| `PriceList` as its own class — resolved as "no" | Design-v1 marked this undecided. Built as a plain lookup inside `Produce.create()` instead — the four prices are fixed reference data with no behaviour of their own, so a dedicated class would have added a type with nothing to do. |
| REJECT handling confirmed as an explicit early return | Design-v1's pseudocode (§9) flagged this as needing confirmation, specifically calling out that the transport levy "looks like it could trip this up". Confirmed against the brief: since the levy is mass-based, not value-based, `Delivery.netPayable()` returns `0.00` before commission or levy are computed at all for a REJECT grade, rather than relying on the grade multiplier alone to zero things out. |