# Collections Rationale — Objective 3

## Price list
A switch on produce code inside the pricing logic, rather than a separate structure — there are only four fixed codes, so a dedicated collection would add indirection with no real benefit over a direct lookup.

## Weekly volume grid
A `double[21][4]` 2D array — weeks and produce codes are both small, fixed-size dimensions known in advance, so a plain array gives O(1) access by index. A `Map` keyed by week number was considered but rejected: it would need boxing every key and offers no advantage when the valid range is already known and contiguous.

## Deliveries (ArrayList)
An `ArrayList<Delivery>` holds every delivery in the order it was recorded. Insertion order matters here (it's what "top five by value" and any chronological view rely on), and the size isn't known ahead of time, which is exactly what `ArrayList` is for over a fixed array.

## Total payment per member (HashMap)
A `HashMap<String, Double>` keyed by member id. Every delivery needs to update one specific member's running total in O(1), and the ordering of members doesn't matter for this structure's job — only fast accumulate-and-look-up does.

## Deliveries per member (Map of Lists)
A `Map<String, List<Delivery>>` groups each member's full delivery history together, which the eventual season report needs (one table per member). Storing this separately from the plain `ArrayList` avoids scanning the whole season's deliveries every time one member's data is needed.

## Distinct member identifiers (HashSet)
A `HashSet<String>` — this only ever needs to answer "how many distinct members, and which ids", automatically collapsing duplicates. A `List` was rejected because it would require manually checking for duplicates before every insert.

## Sorting — Comparator vs Comparable
`Comparator` is used for "sort deliveries by value" because that's one specific, situational ordering, not `Delivery`'s only sensible order. `Comparable` is used on `Member`, sorted by id, because that genuinely is the one natural ordering for a member — the season report needs a consistent, canonical order for its sections.

## Search by identifier (absent case)
`findMemberById` checks `containsKey` before fetching, so a missing id returns `null` deliberately and predictably rather than the caller risking a `NullPointerException` from an unchecked `.get()`.

## Removal via Iterator
Filtering out REJECT deliveries for reporting purposes requires removing items while traversing a list — a plain for-each loop would throw `ConcurrentModificationException` if mutated mid-iteration, so an `Iterator` with `.remove()` is used instead, on a copy, since REJECT deliveries must still remain in the season's real records for volume statistics.