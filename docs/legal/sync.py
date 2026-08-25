#!/usr/bin/env python3
"""Rewrites the Markdown copies of the legal documents from the app's own text.

The strings in LegalScreen.kt are what a user actually reads before agreeing, so
they are the copy of record and this only ever reads them. Run with --check to
find out whether the copies here have drifted without rewriting anything.

    python3 docs/legal/sync.py
    python3 docs/legal/sync.py --check
"""
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
SOURCE = ROOT / "app/src/main/java/com/tpc/trikride/ui/screens/LegalScreen.kt"
HERE = Path(__file__).parent

FILES = {
    "TERMS_TEXT": "terms-and-conditions.md",
    "PRIVACY_TEXT": "privacy-policy.md",
    "COMMUNITY_TEXT": "safety-and-community-guidelines.md",
    "DRIVER_AGREEMENT_TEXT": "driver-agreement.md",
}

# Section titles that carry no number. Listed by name rather than guessed at
# from length, so that a short sentence is never promoted to a heading.
NAMED_HEADINGS = {
    "Our Commitment", "Respect Everyone", "Safe Riding", "Driver Responsibilities",
    "Passenger Responsibilities", "Prohibited Conduct", "Reporting Safety Concerns",
    "Account Enforcement", "Driver Eligibility", "Your License Photograph",
    "Professional Conduct", "Safety Requirements",
    "Account Suspension or Termination", "Limitation of Responsibility", "Agreement",
}


def extract() -> dict[str, str]:
    src = SOURCE.read_text(encoding="utf-8")
    found = dict(
        re.findall(r'private val (\w+_TEXT) = """\n(.*?)\n""".trimIndent\(\)', src, re.S)
    )
    missing = set(FILES) - set(found)
    if missing:
        raise SystemExit(f"not found in {SOURCE.name}: {', '.join(sorted(missing))}")
    return found


def to_markdown(raw: str) -> str:
    lines = raw.split("\n")
    out = [f"# {lines[0].strip()}", ""]
    for line in lines[1:]:
        s = line.rstrip()
        if not s:
            out.append("")
        elif s.startswith("Effective Date:"):
            out.append(f"*{s}*")
        elif s.startswith("•  "):
            out.append(f"- {s[3:]}")
        elif re.match(r"^\d+\. \S", s) and len(s) < 60:
            out.append(f"## {s}")
        elif s in NAMED_HEADINGS:
            out.append(f"## {s}")
        else:
            out.append(s)

    tidied: list[str] = []
    for line in out:
        # A heading with a paragraph pressed against it does not render.
        if line.startswith("## ") and tidied and tidied[-1] != "":
            tidied.append("")
        tidied.append(line)
    return "\n".join(tidied).rstrip() + "\n"


def main() -> int:
    check = "--check" in sys.argv[1:]
    texts = extract()
    stale = []
    for key, name in FILES.items():
        path = HERE / name
        rendered = to_markdown(texts[key])
        if check:
            current = path.read_text(encoding="utf-8") if path.exists() else ""
            if current != rendered:
                stale.append(name)
        else:
            path.write_text(rendered, encoding="utf-8")
            print(f"wrote {name}")

    if check:
        if stale:
            print("out of date with LegalScreen.kt: " + ", ".join(stale))
            print("run docs/legal/sync.py to rewrite them")
            return 1
        print("all four match LegalScreen.kt")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
