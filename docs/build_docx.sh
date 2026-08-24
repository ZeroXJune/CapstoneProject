#!/usr/bin/env bash
#
# Rebuilds TrikRide_Capstone_Documentation.docx from capstone_manuscript.md.
#
# Requires pandoc. Run from anywhere:  bash docs/build_docx.sh
#
set -euo pipefail

DOCS="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC="$DOCS/capstone_manuscript.md"
REF="$DOCS/reference.docx"
OUT="$DOCS/TrikRide_Capstone_Documentation.docx"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT

# [[PB]] becomes a Word page break; [[SOURCE_CODE]] becomes the whole listing,
# read from the repository so the appendix cannot drift from the code.
python3 - "$SRC" "$TMP/manuscript.md" "$DOCS/.." <<'PY'
import sys, os
src, dst, root = sys.argv[1], sys.argv[2], sys.argv[3]
pb = '```{=openxml}\n<w:p><w:r><w:br w:type="page"/></w:r></w:p>\n```'
text = open(src, encoding='utf-8').read().replace('[[PB]]', pb)

if '[[SOURCE_CODE]]' in text:
    base = os.path.join(root, 'app', 'src', 'main', 'java')
    files = sorted(
        os.path.join(d, f)
        for d, _, fs in os.walk(base) for f in fs if f.endswith('.kt')
    )
    parts = []
    for path in files:
        code = open(path, encoding='utf-8').read()
        rel = os.path.relpath(path, root).replace(os.sep, '/')
        lines = code.count('\n') + (0 if code.endswith('\n') else 1)
        parts.append(f'### {rel}\n\n*{lines} lines*\n\n```\n{code.rstrip()}\n```\n')
    text = text.replace('[[SOURCE_CODE]]', '\n'.join(parts))
    print(f'  source appendix: {len(files)} files', file=sys.stderr)

open(dst, 'w', encoding='utf-8').write(text)
PY

pandoc "$TMP/manuscript.md" \
  --reference-doc="$REF" \
  --resource-path="$DOCS" \
  -f markdown+pipe_tables+tex_math_dollars+raw_attribute \
  -o "$OUT"

# pandoc writes its own [Content_Types].xml without the image defaults, and Word and
# LibreOffice both refuse to open the file when they are missing.
python3 - "$OUT" <<'PY'
import sys, zipfile, re, shutil
path = sys.argv[1]
tmp = path + '.tmp'
zin = zipfile.ZipFile(path)
ct = zin.read('[Content_Types].xml').decode('utf-8')
for ext, mime in (('png', 'image/png'), ('jpeg', 'image/jpeg'),
                  ('jpg', 'image/jpeg'), ('gif', 'image/gif')):
    if f'Extension="{ext}"' not in ct:
        ct = re.sub(r'(<Types[^>]*>)',
                    r'\1<Default Extension="%s" ContentType="%s"/>' % (ext, mime),
                    ct, count=1)
zout = zipfile.ZipFile(tmp, 'w', zipfile.ZIP_DEFLATED)
for n in zin.namelist():
    zout.writestr(n, ct.encode('utf-8') if n == '[Content_Types].xml' else zin.read(n))
zout.close(); zin.close()
shutil.move(tmp, path)
PY

echo "built $OUT"
