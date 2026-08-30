# Design Change Log — deviations from design-v1

Log every place the built system (design-v2 / Objective 5) diverges from
the original paper design (design-v1 / Objective 4), and why.

| Change | Reason |
|---|---|
| PaymentCalculator dropped as a separate class | Its logic fit naturally as Delivery.netPayable(), avoiding an extra class with only one caller |
| SeasonService owns collections directly as fields | Simpler than passing five collections through every method signature |