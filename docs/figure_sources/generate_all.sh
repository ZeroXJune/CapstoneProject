#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")"

for f in 01_core_diagrams.py 02_dfd.py 03_uml_and_schema.py 04_flow_diagrams.py 05_sequence_and_gantt.py; do
  echo "--- $f"
  python3 "$f"
done

# Word does not want transparency, and an oversized image overflows the page.
python3 - <<'PY'
from PIL import Image
import glob, os, re
FIG = os.path.abspath('../figures')
MD  = os.path.abspath('../capstone_manuscript.md')
MAXW, MAXH = 6.0, 7.4          # usable inches on a Letter page with these margins
sizes = {}
for f in sorted(glob.glob(f'{FIG}/*.png')):
    im = Image.open(f)
    if im.mode in ('RGBA', 'LA', 'P'):
        im = im.convert('RGBA')
        bg = Image.new('RGB', im.size, 'white')
        bg.paste(im, mask=im.split()[-1])
        im = bg
    else:
        im = im.convert('RGB')
    im.thumbnail((2000, 2600), Image.LANCZOS)
    im.save(f, 'PNG', optimize=True)
    w, h = im.size
    sizes[os.path.basename(f)] = round(min(MAXW, MAXH * w / h), 2)

md = open(MD).read()
md = re.sub(r'!\[(.*?)\]\((figures/[^)]+)\)(?:\{[^}]*\})?',
            lambda m: f'![{m.group(1)}]({m.group(2)})'
                      f'{{width={sizes[os.path.basename(m.group(2))]}in}}', md)
open(MD, 'w').write(md)
for k, v in sizes.items():
    print(f'  {k:<34}{v}in')
PY
echo "figures regenerated"
