# Figure sources

Every diagram in `../figures/` is generated from these scripts, so changing a diagram
means editing text rather than redrawing a picture.

```bash
bash generate_all.sh
```

Requires Graphviz (`dot`) and, for the last script, Matplotlib and Pillow.

| Script | Figures |
|:---|:---|
| `01_core_diagrams.py` | 2 conceptual framework, 3 system architecture, 4 context diagram, 6 ERD |
| `02_dfd.py` | 5 data flow diagram level 1 |
| `03_uml_and_schema.py` | 7 use case, 10 class, 11 database schema, 12 waterfall, 13 screen flow |
| `04_flow_diagrams.py` | 1 research flow, 8 activity diagram |
| `05_sequence_and_gantt.py` | 9 sequence diagram, 14 Gantt chart |

`generate_all.sh` runs all five and then flattens transparency to white, caps the pixel
dimensions, and rewrites the `{width=…in}` attribute on each figure in
`../capstone_manuscript.md` so nothing overflows the page.
