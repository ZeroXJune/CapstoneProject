# Statistical computation — source files

## Contents

| File | What it is |
|:---|:---|
| `TrikRide_Survey_Tally.xlsx` | The raw returns. One row per respondent, one column per item, transcribed from the completed questionnaires. Every figure in this study derives from this file. |
| `as-supplied/WEIGTHEDMEAN_*.xlsx` | The computation workbooks exactly as supplied by the study's statistician. Unmodified. |
| `WEIGTHEDMEAN_*_corrected.xlsx` | The same workbooks with six verbal labels corrected. These are the versions Appendix G reproduces. |
| `apply_label_corrections.py` | The script that produces the corrected copies from the as-supplied originals. Run from this directory. |

## The correction

Every number in the supplied workbooks is correct. All 61 frequency
distributions, 61 item weighted means, 13 category means, 3 overall means and
every rank were independently recomputed from `TrikRide_Survey_Tally.xlsx` and
reconcile exactly.

Six **verbal labels** did not follow the interpretation legend printed at the
head of each of the three sheets:

```
4.21 – 5.0  = Strongly Agree      2.61 – 3.40 = Neutral
3.41 – 4.20 = Agree               1.81 – 2.60 = Disagree
                                  1.00 – 1.80 = Strongly Disagree
```

| Workbook | Cell | Figure | As supplied | Corrected to |
|:---|:---|---:|:---|:---|
| Passenger | W25 | 4.35 — *The system is easy to learn.* | Agree | **Strongly Agree** |
| Passenger | W26 | 4.40 — *The menus are easy to understand.* | Agree | **Strongly Agree** |
| Passenger | W28 | 4.30 — *The interface is visually appealing.* | Agree | **Strongly Agree** |
| Driver | W26 | 4.15 — *I can comfortably use the system without any technical assistance.* | Strongly Agree | **Agree** |
| Driver | G22 | 4.20 — **Usability, category mean** | Strongly Agree | **Agree** |
| Administrator | G14 | 4.00 — **Reliability, category mean** | Strongly Agree | **Agree** |

The last two change what the study reports: driver Usability and administrator
Reliability fall in *Agree*, not *Strongly Agree*. The other four are item-level
and alter no category mean, no overall mean and no rank.

## Why these are errors and not a different rubric

1. **The legend is the statistician's own**, printed identically on all three
   sheets, and it accounts for 71 of her 77 labels. The common alternative
   rubric (4.50–5.00 = Strongly Agree) accounts for only 26.
2. **The duplicated passenger item settles it.** *"The system is easy to
   learn."* appears twice on the passenger sheet with the same weighted mean of
   4.35 and the same frequencies 7/13/0/0/0, labelled `AGREE` on one row and
   `STRONGLY AGREE` on the other. No threshold rule can produce two labels for
   one value.
3. **The labelling is non-monotonic** — 36 distinct pairs where a lower
   weighted mean carries a stronger label than a higher one (4.15 is labelled
   *Strongly Agree* while several 4.20 figures are labelled *Agree*). Threshold
   rules are monotonic by construction, so no legend of any kind reproduces the
   supplied label set.
4. **Every `DESCRIPTION` cell is typed text, not a formula** (35, 21 and 8 cells
   across the three workbooks), which is the mechanism that produces scattered
   keying slips.
5. **No figure falls in a gap between bands.** The legend leaves hairline gaps
   at 4.20–4.21 and 3.40–3.41; none of the 77 figures lands in one. Driver
   Usability is exactly 4.2000 and administrator Reliability exactly 4.0000,
   both unambiguously inside *Agree*. No rounding judgement is involved.

## What the correction changes

Only the six cells above. Verified after generation: in each workbook the only
differing part is `xl/worksheets/sheet1.xml`; styles, theme, shared strings and
every other part are byte-identical to the original; all 944 numeric cells are
unchanged; and all 77 labels now agree with the printed legend.

The correction is recorded in the manuscript at Appendix G.4.
