# Legal documents

The four documents a TrikRide user agrees to, as issued:

| Document | Effective | Who accepts it |
|:---|:---|:---|
| [Terms and Conditions](terms-and-conditions.md) | 28 July 2026 | Everyone, at sign-up |
| [Privacy Policy](privacy-policy.md) | 16 August 2026 | Everyone, at sign-up |
| [Safety and Community Guidelines](safety-and-community-guidelines.md) | 28 July 2026 | Everyone, at sign-up |
| [Driver Agreement](driver-agreement.md) | 16 August 2026 | Drivers, after choosing a driver account |

These are copies for reading and for printing. The text the app actually shows lives in
`app/src/main/java/com/tpc/trikride/ui/screens/LegalScreen.kt`, and that is the copy of
record — it is what a user reads before ticking the box, so if the two ever disagree, the
one in the app is what was agreed to. The files here are generated from it.

## Changing a document

1. Edit the string in `LegalScreen.kt`.
2. Run `python3 docs/legal/sync.py` to rewrite the Markdown from it.
3. Bump `Constants.LEGAL_VERSION` to the new effective date.
4. Update the effective date in the table above, and in Appendix D of the manuscript.

Step 3 is the one that matters. Acceptance is recorded against `LEGAL_VERSION`, so
changing that string makes every existing user tick the boxes again on their next launch.
Leave it alone and the revised document is shown to nobody who has already agreed.

`sync.py` with no arguments rewrites the files. With `--check` it reports any difference
instead of writing, which is what to run if you want to know whether these copies have
drifted from the app.
