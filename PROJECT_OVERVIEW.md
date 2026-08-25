# Project status

Where TrikRide stands, what is finished, and what is still open. Written for whoever
picks this up next, including us in a month.

## Finished and on the branch

**Accounts.** Registration with full name, birthdate through a date picker, email, mobile
number, and a password checked against a live rule list as it is typed. The Terms,
Privacy Policy and Community Guidelines must be ticked before the account is created. Sign-in remembers the email if asked.
Firebase keeps the session, so the app opens straight to the dashboard on the next
launch. Password reset by email, from the sign-in screen or from Settings.

**First run.** A five-slide carousel on first launch, shown once and never again.
Returning users get the welcome artwork while the session restores.

**Booking.** Pickup and destination both come from the same searchable, zone-filtered
list of the 240 posted FeTODAT stops plus the two flat rates, which sort to the top
because the table has no row for them. Pickup can also be pinned anywhere on the map. Prices show against destinations only, since that is what
the ordinance fixes. A rate column switch for regular versus senior, PWD, and student.
One to five passengers, luggage chips, and a free-text note. The fare appears itemised as
soon as a destination is chosen.

**Matching.** The request goes to every online, verified driver with a countdown. The
first to accept gets it and it disappears from everyone else. Unaccepted requests expire
after five minutes.

**The ride.** A shared status timeline through arriving, arrived, in progress, and
completed, driven by the driver and reflected on the passenger's screen without either
refreshing. A completion summary with a rating. History on both sides.

**Driver onboarding.** Licence number, expiry, and tricycle number submitted at
registration, then a photograph of the licence from the driver's own profile, with a
consent step naming what the image is for and when it is destroyed. An administrator opens
the photograph, checks it against the typed details, and approves or rejects; a refusal
deletes the image with it. An unapproved driver cannot accept anyone, and the decision
arrives as a notification.

**Admin.** Verification queue with a badge. Concerns with a badge, a status, and a note
field. A live monitor of drivers, verification states, and rides. The fare table with
search, zone filters, a review queue, per-entry editing, activation, addition, deletion,
and a dialog for the minimums and flat rates. Exports for rides, drivers, and concerns
over a month, a year, all time, or two dates picked from a calendar — as a printable PDF
whose first page carries the headline figures and four charts, or as a spreadsheet.

**Everywhere.** Profile editing with a photo from camera or gallery, dark mode, terms and
privacy text, an in-app notification centre with unread counts, skeleton loading,
pull-to-refresh, and a concern form on the Support tab for passengers and drivers
alike, with the reporter's own past reports and the administrator's reply beneath it.

**Maps and live tracking.** Real maps on the booking and tracking screens, from Google
Maps when `MAPS_API_KEY` is set and OpenStreetMap through osmdroid when it is not. Drivers publish their
position while online with the app open, and passengers watch it move. Pickup can be
pinned anywhere on the map and is reverse-geocoded to a readable label. Destinations stay
on the fare table, since the ordinance fixes the price per named stop, but the map can be
used to find one: the passenger moves it to where they are going and the app names the
nearest posted stop with its fare. Fare stops can carry coordinates, editable in the admin
fare screen, and appear on the map — and become findable that way — once they do.

**Agreements.** The Terms and Conditions, Privacy Policy, Safety and Community
Guidelines, and Driver Agreement are all in the app and all readable from Settings. The
first three are ticked at sign-up; drivers accept the fourth after choosing their account
type. A consent gate between sign-in and the dashboard enforces it — every document
ticked separately, or sign out. Acceptance is recorded against
`Constants.LEGAL_VERSION`, so revising the documents means changing that string and
everyone is asked again on their next launch.

**Release signing.** `assembleRelease` produces a properly signed APK once four values
are set in `.env`. Without them it falls back to the debug key and Gradle prints a warning
that the output must not be distributed, so a debug-signed build cannot be handed out by
accident. The keystore itself is gitignored and has to be generated once and backed up —
see the Deployment section of SETUP_GUIDE.md.

**Runs on the free tier.** Auth, Realtime Database, Cloud Messaging, and App Distribution
are all free and none of them asks for a card. Cloud Storage is the one Firebase service
that needs the paid Blaze plan, so it is not used: profile photos are squared off at 256
pixels, compressed to a few kilobytes, and written to `profilePhotos/{uid}` in the
database. They are kept out of the user record on purpose, so the admin screens can list
every user without dragging every avatar across the network. Licence photographs use the
same trick at a legible size in `driverDocuments/{uid}`, and are the one node the rules
keep from ordinary users.

## What is not built, and why

**Push to a sleeping phone.** FCM is wired into the app, but delivering a notification
when the app is not running needs a server-side sender. That is a small set of Cloud
Functions, deferred.

**Google Sign-In.** Needs a Firebase console step and a SHA-1 fingerprint. Deferred.

**Online payment.** Out of scope by the study's own limitations. Cash only.

## What still needs a person, not a commit

Nothing on this list is code. It is either something somebody has to do or data that does
not exist yet. The order matters: steps 1 to 5 make the app installable, 6 and 7 have to
happen before it reaches anyone real, and everything after that is waiting on the
evaluation, which cannot start until the first seven are done.

### Before it can be deployed at all

1. **Compile and smoke-test it.** `./gradlew assembleDebug`, then click through every
   screen on a real phone. Recent work was written without a build to check it against.
2. **Generate the keystore.** Back it up in two places and put the four `RELEASE_*` values
   in `.env`. Nothing can be distributed until this exists, and nothing can be updated if
   it is lost.
3. **Fill `.env`.** Copy `.env.example`. The build reads it.
4. **Publish the security rules,** including `driverDocuments` and `driverRatings`.
   Without the first, licence uploads are refused; without the second, ratings are.
5. **Load the fare table.** Sign in as an administrator and use Fares → Load official
   rates. The app cannot price a ride until this is done once. Then open the minimums
   and flat rates dialog and check the four numbers read ₱25, ₱20, ₱25, ₱25. Loading
   the rates deliberately leaves those alone, so a database seeded before they were
   corrected still holds the old ones and no amount of re-loading will change them.

Steps 1 to 5 are an afternoon. Full instructions are in the Deployment section of
SETUP_GUIDE.md.

### Before real people use it

6. **Correct the flagged fares.** Forty-six of the 240 entries are flagged. Open Fares,
   tap the "Needs review" chip, and work through them against the physical posted sheet.
   Four price the discount above the regular rate, which is backwards. Three are switched
   off because no rate could be read and need a number or removal; one of those, San Roque
   "Dina/Gabril", looks like a duplicate of "Gabriel & Dina", already in the table at
   ₱36/₱40. While you are there, check the twenty-seven rows priced at ₱20: they sit below
   the ₱25 minimum and are charged at ₱25, so either the sheet they came from is out of
   date or they were read wrong.
7. **Get the two permission letters signed** — one to the College President, one to the
   FeTODAT President. Appendix B needs them as signed copies, and the evaluation cannot
   begin without them, so this is worth starting first even though it is listed second.

### The evaluation, which is the long pole

No survey data has been collected, and the manuscript no longer pretends otherwise —
results, collected data and statistical treatment were removed rather than left as empty
tables. The chain is: letters signed, respondents recruited, app distributed, respondents
use it, responses collected, statistician sets the treatment, results written up.

What has to be written once that is done:

| Where | What is missing |
|:---|:---|
| § 1.9 | Population figures, the sampling method and sample size, respondents who took part |
| § 1.11 | The statistical treatment, once the statistician has specified it |
| Chapter 4 | A section presenting the evaluation results and their interpretation |
| Chapter 5 | A summary of findings, ahead of the conclusions already written |
| § 4.5 | User acceptance completion rates, performance measurements, the defect log |
| Appendices | Raw responses and the statistical computation, as the statistician requires |

Three appendices need nothing but capturing while the evaluation is running:
**Appendix G**, screenshots covering every module; **Appendix M**, printed sample reports,
one of each in both formats; and **Appendix N**, photographs of the data gathering, with
the consent of those pictured.

Then rebuild the .docx and it is submittable.

### Worth doing, not blocking

**Stop coordinates.** None of the 240 fare stops has a position yet. They book and price
fine without one; they just do not appear on the map, and cannot be found through it —
"Find it on the map" has nothing to offer until at least a few are filled in. Doing the
dozen or so common destinations as you verify their fares would cover a demo.

**The onboarding artwork.** See the rough edges at the end of this file.

## Documentation

`docs/capstone_manuscript.md` is the written documentation in Markdown, and
`docs/TrikRide_Capstone_Documentation.docx` is the same thing built for submission,
following the department's required format: preliminaries, five chapters, references in
APA 7, and appendices A through P.

Chapters 1 through 4 are complete. Nothing about results, collected data, or statistical
treatment is in the document: the evaluation has not been run, and the treatment is the
statistician's to specify, so those sections were removed rather than left as empty
tables. What remains to be added once the evaluation happens is the results, their
interpretation, Chapter 5's summary and findings, and the appendices that depend on them.

Appendix D now carries all four user agreements in full rather than a summary and a
placeholder, and Appendix H the user manual rather than an outline of one. The manual is
also `docs/user-manual.md`, so it can be handed out on its own. They are also in `docs/legal/` as Markdown, generated from the strings in
`LegalScreen.kt` by `docs/legal/sync.py`, which with `--check` reports whether the two
have drifted.

Appendix I does not print the source. Twelve thousand seven hundred lines across 55 files
would add roughly three hundred pages to every bound copy, which the budget will not carry.
It gives the clone URL, a walk-through for retrieving the code with Git or as a ZIP, a note
on the two credential files that are not in the repository, and the fare engine as a
representative extract.

Fourteen figures are generated from text in `docs/figures/`: research flow, conceptual
framework, system architecture, context diagram, DFD level 1, ERD, use case, activity,
sequence, class, database schema, waterfall, screen flow, and the Gantt chart. Changing
one means editing a script, not redrawing a picture.

## Rebuilding the .docx

Edit `docs/capstone_manuscript.md`, then run `bash docs/build_docx.sh`. It needs pandoc.

The script expands the `[[PB]]` markers into Word page breaks, renders against
`docs/reference.docx` (which carries the page setup: Letter, Times New Roman 12, double
spaced, 1.5-inch left margin), and patches the image content types back into the result —
pandoc omits them, and without them neither Word nor LibreOffice will open the file.

## Known rough edges

The onboarding slides are around 316 pixels wide and will look soft stretched to a
1080-pixel screen. Slides four and five have a strip of a neighbouring image along the
top edge, cropped from a contact sheet a few pixels low. Replacements can be dropped over
the same filenames in `app/src/main/res/drawable-nodpi/` with no code change.

No automated tests exist. The test tables in the manuscript document tests that were run
by hand. Converting the `FareEngine` and `ReportBuilder` cases into JUnit tests would be
straightforward and worth doing; they are pure functions with no Android dependency.
