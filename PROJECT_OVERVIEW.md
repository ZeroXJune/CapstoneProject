# Project status

Where TrikRide stands, what is finished, and what is still open. Written for whoever
picks this up next, including us in a month.

## Finished and on the branch

**Accounts.** Registration with full name, birthdate through a date picker, email, mobile
number, and a password checked against a live rule list as it is typed. The Terms,
Privacy Policy and Community Guidelines must be ticked before the account is created. Sign-in remembers the email if asked.
Firebase keeps the session, so the app opens straight to the dashboard on the next
launch. Password reset by email.

**First run.** A five-slide carousel on first launch, shown once and never again.
Returning users get the welcome artwork while the session restores.

**Booking.** Pickup from a fixed list of campus points. Destination from a searchable,
zone-filtered list of the 240 posted FeTODAT stops plus the two flat rates. A rate column
switch for regular versus senior, PWD, and student. One to five passengers, luggage
chips, and a free-text note. The fare appears itemised as soon as a destination is
chosen.

**Matching.** The request goes to every online, verified driver with a countdown. The
first to accept gets it and it disappears from everyone else. Unaccepted requests expire
after five minutes.

**The ride.** A shared status timeline through arriving, arrived, in progress, and
completed, driven by the driver and reflected on the passenger's screen without either
refreshing. A completion summary with a rating. History on both sides.

**Driver onboarding.** Licence number, expiry, and tricycle number submitted at
registration. An administrator approves or rejects. An unapproved driver cannot accept
anyone, and the decision arrives as a notification.

**Admin.** Verification queue with a badge. Concerns with a badge, a status, and a note
field. A live monitor of drivers, verification states, and rides. The fare table with
search, zone filters, a review queue, per-entry editing, activation, addition, deletion,
and a dialog for the minimums and flat rates. Monthly, yearly, and all-time exports for
rides, drivers, and concerns — as a printable PDF whose first page carries the headline
figures and four charts, or as a spreadsheet.

**Everywhere.** Profile editing with a photo from camera or gallery, dark mode, terms and
privacy text, an in-app notification centre with unread counts, skeleton loading,
pull-to-refresh, and a concern form for passengers and drivers.

**Maps and live tracking.** Real maps on the booking and tracking screens, from Google
Maps when `MAPS_API_KEY` is set and OpenStreetMap through osmdroid when it is not. Drivers publish their
position while online with the app open, and passengers watch it move. Pickup can be
pinned anywhere on the map and is reverse-geocoded to a readable label. Destinations stay
on the fare table, since the ordinance fixes the price per named stop. Fare stops can
carry coordinates, editable in the admin fare screen, and appear on the map once they do.

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
every user without dragging every avatar across the network.

## What is not built, and why

**Push to a sleeping phone.** FCM is wired into the app, but delivering a notification
when the app is not running needs a server-side sender. That is a small set of Cloud
Functions, deferred.

**Google Sign-In.** Needs a Firebase console step and a SHA-1 fingerprint. Deferred.

**Online payment.** Out of scope by the study's own limitations. Cash only.

## What still needs a person, not a commit

**The fare data.** Forty-six of the 240 entries are flagged. Open Fares, tap the "Needs
review" chip, and work through them against the physical posted sheet. Four of them price
the discount above the regular rate, which is backwards and should be fixed before anyone
real uses the app. Three entries are switched off because no rate could be read; they
need a number or removal. One of those, San Roque "Dina/Gabril", looks like a duplicate of
"Gabriel & Dina", which is already in the table at ₱36/₱40.

**`.env`.** Copy `.env.example` and fill it in. The build reads it.

**Stop coordinates.** None of the 240 fare stops has a position yet. They book and price
fine without one; they just do not appear on the map. Filling in the dozen or so common
destinations as you verify their fares would cover a demo.

**The keystore.** Generate it, back it up in two places, and put the four
`RELEASE_*` values in `.env`. Nothing can be distributed until this exists, and nothing
can be updated if it is lost.

**Evaluation.** No survey data has been collected. The manuscript's section 4.9 and
Chapter 5 carry the tables and the interpretation scale, with the numbers left blank and
marked as such. Nothing there is invented and nothing should be.

## Documentation

`docs/capstone_manuscript.md` is the written documentation in Markdown, and
`docs/TrikRide_Capstone_Documentation.docx` is the same thing built for submission,
following the department's required format: preliminaries, five chapters, references in
APA 7, and appendices A through R. The format discussion listed A through Q; Appendix D
was added for the legal documents carried in the app, which pushed the rest down one.

Chapters 1 through 4 are complete. Section 4.9's results tables, Chapter 5's summary and
conclusions, and several appendices (raw data, statistical computation, screenshots,
signed permission letters, photographs of the data gathering) are marked as placeholders
because they depend on work that has not happened yet.

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
