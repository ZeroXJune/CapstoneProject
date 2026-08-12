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

# [[PB]] in the Markdown becomes a Word page break.
python3 - "$SRC" "$TMP/manuscript.md" <<'PY'
import sys
src, dst = sys.argv[1], sys.argv[2]
pb = '```{=openxml}\n<w:p><w:r><w:br w:type="page"/></w:r></w:p>\n```'
open(dst, 'w').write(open(src).read().replace('[[PB]]', pb))
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
